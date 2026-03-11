package com.tenx.chimera_agent.api;

// File purpose: Defines TrendsControllerTest behavior for Project Chimera.

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrendsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void trendsShouldReturnEmptyContractPayload() throws Exception {
		mockMvc.perform(get("/api/v1/trends"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.window_hours").value(24))
				.andExpect(jsonPath("$.items").isArray());
	}

	@Test
	void trendsShouldValidateLimitRange() throws Exception {
		mockMvc.perform(get("/api/v1/trends?limit=500"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.trace_id").exists());
	}
}

