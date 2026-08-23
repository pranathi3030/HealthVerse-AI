package com.healthverse.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthverse.user.dto.UpdateHealthProfileRequest;
import com.healthverse.user.dto.UpdateUserProfileRequest;
import com.healthverse.user.entity.User;
import com.healthverse.user.repository.HealthProfileRepository;
import com.healthverse.user.repository.UserRepository;
import com.healthverse.user.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateTestToken(Long userId, String email, String role) {
        String secret = "HealthVerseAISecretKeyForJWTAuthentication2026MustBeAtLeast256BitsLong!";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    @BeforeEach
    void setUp() {
        healthProfileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetUserProfileSuccessfully() throws Exception {
        User user = User.builder()
                .name("Bruce Wayne")
                .email("bruce@wayne-enterprises.com")
                .password("encryptedPass")
                .role("USER")
                .build();
        User savedUser = userRepository.save(user);

        String token = generateTestToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        mockMvc.perform(get("/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedUser.getId().intValue())))
                .andExpect(jsonPath("$.email", is("bruce@wayne-enterprises.com")))
                .andExpect(jsonPath("$.name", is("Bruce Wayne")))
                .andExpect(jsonPath("$.password").doesNotExist()); // Ensure password is never exposed
    }

    @Test
    void testUpdateUserProfileSuccessfully() throws Exception {
        User user = User.builder()
                .name("Bruce Wayne")
                .email("bruce@wayne-enterprises.com")
                .password("encryptedPass")
                .role("USER")
                .build();
        User savedUser = userRepository.save(user);

        String token = generateTestToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        UpdateUserProfileRequest updateReq = UpdateUserProfileRequest.builder()
                .name("The Dark Knight")
                .build();

        mockMvc.perform(put("/users/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("The Dark Knight")));
    }

    @Test
    void testCreateAndUpdateHealthProfileWithBmiCalculation() throws Exception {
        User user = User.builder()
                .name("Clark Kent")
                .email("clark@dailyplanet.com")
                .password("encryptedPass")
                .role("USER")
                .build();
        User savedUser = userRepository.save(user);

        String token = generateTestToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

        // Height: 180 cm (1.8m), Weight: 75 kg -> BMI = 75 / (1.8 * 1.8) = 23.15 (Normal weight)
        UpdateHealthProfileRequest profileReq = UpdateHealthProfileRequest.builder()
                .age(32)
                .gender("Male")
                .height(180.0)
                .weight(75.0)
                .lifestyle("Very Active")
                .goals("Maintain fitness")
                .allergies("Kryptonite")
                .conditions("None")
                .build();

        mockMvc.perform(put("/health/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age", is(32)))
                .andExpect(jsonPath("$.height", is(180.0)))
                .andExpect(jsonPath("$.weight", is(75.0)))
                .andExpect(jsonPath("$.bmi", is(23.15)))
                .andExpect(jsonPath("$.bmiCategory", is("Normal weight")))
                .andExpect(jsonPath("$.allergies", is("Kryptonite")));

        // Fetch the saved profile via GET
        mockMvc.perform(get("/health/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bmi", is(23.15)))
                .andExpect(jsonPath("$.bmiCategory", is("Normal weight")))
                .andExpect(jsonPath("$.goals", is("Maintain fitness")));
    }

    @Test
    void testUnauthenticatedAccessReturnsForbidden() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/health/profile"))
                .andExpect(status().isForbidden());
    }
}
