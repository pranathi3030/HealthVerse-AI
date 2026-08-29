package com.healthverse.analytics;

import com.healthverse.analytics.controller.AnalyticsController;
import com.healthverse.analytics.dto.HealthScoreResponse;
import com.healthverse.analytics.service.AnalyticsService;
import com.healthverse.analytics.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simple unit test
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @MockBean
    private JwtService jwtService; // Need this mock because of SecurityConfig

    @Test
    void testGetHealthScore() throws Exception {
        HealthScoreResponse mockResponse = HealthScoreResponse.builder()
                .score(85)
                .category("Good")
                .disclaimer("Test disclaimer")
                .build();

        when(analyticsService.getHealthScore(1L)).thenReturn(mockResponse);

        // Manually set authentication since we disabled filters
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user@test.com", 1L, null);

        mockMvc.perform(get("/api/analytics/health-score").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(85))
                .andExpect(jsonPath("$.category").value("Good"));
    }
}
