package com.healthverse.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthverse.auth.dto.LoginRequest;
import com.healthverse.auth.dto.RefreshTokenRequest;
import com.healthverse.auth.dto.RegisterRequest;
import com.healthverse.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Pranathi Test")
                .email("pranathi@healthverse.ai")
                .password("securePassword123")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.user.email", is("pranathi@healthverse.ai")))
                .andExpect(jsonPath("$.user.name", is("Pranathi Test")));
    }

    @Test
    void testRegisterDuplicateEmailReturnsConflict() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Pranathi Test")
                .email("pranathi@healthverse.ai")
                .password("securePassword123")
                .build();

        // First registration
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Duplicate registration
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("User Already Exists")));
    }

    @Test
    void testLoginSuccessfully() throws Exception {
        RegisterRequest regRequest = RegisterRequest.builder()
                .name("Doctor Strange")
                .email("strange@healthverse.ai")
                .password("magicPass123")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = LoginRequest.builder()
                .email("strange@healthverse.ai")
                .password("magicPass123")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.user.email", is("strange@healthverse.ai")));
    }

    @Test
    void testLoginWithInvalidPasswordReturnsUnauthorized() throws Exception {
        RegisterRequest regRequest = RegisterRequest.builder()
                .name("Doctor Strange")
                .email("strange@healthverse.ai")
                .password("magicPass123")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = LoginRequest.builder()
                .email("strange@healthverse.ai")
                .password("wrongPassword")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Unauthorized")));
    }

    @Test
    void testGetMeProtectedEndpoint() throws Exception {
        RegisterRequest regRequest = RegisterRequest.builder()
                .name("Jane Doe")
                .email("jane@healthverse.ai")
                .password("securePassword123")
                .build();

        MvcResult regResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = regResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseJson).get("accessToken").asText();

        // Access /auth/me with valid Bearer token
        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("jane@healthverse.ai")))
                .andExpect(jsonPath("$.name", is("Jane Doe")));

        // Access /auth/me without token -> 403 / 401
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRefreshToken() throws Exception {
        RegisterRequest regRequest = RegisterRequest.builder()
                .name("Tony Stark")
                .email("tony@healthverse.ai")
                .password("ironman123")
                .build();

        MvcResult regResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String refreshToken = objectMapper.readTree(regResult.getResponse().getContentAsString()).get("refreshToken").asText();

        RefreshTokenRequest refreshReq = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.user.email", is("tony@healthverse.ai")));
    }
}
