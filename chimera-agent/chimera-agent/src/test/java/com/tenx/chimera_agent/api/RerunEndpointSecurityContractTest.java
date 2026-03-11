package com.tenx.chimera_agent.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RerunEndpointSecurityContractTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void rerunEndpointShouldRequireAuthenticationPerTechnicalSpec() throws Exception {
		MvcResult collectResult = mockMvc.perform(
						post("/api/v1/runs/collect")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "platforms": ["youtube_shorts"],
										  "window_minutes": 60,
										  "limit_per_platform": 20
										}
										""")
				)
				.andExpect(status().isAccepted())
				.andReturn();

		JsonNode collectJson = objectMapper.readTree(collectResult.getResponse().getContentAsString());
		String runId = collectJson.get("run_id").asText();

		mockMvc.perform(
						post("/api/v1/runs/{runId}/rerun", runId)
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "reason": "security_contract_check"
										}
										""")
				)
				// Spec 7.4 states admin rerun endpoints require authn/authz.
				.andExpect(status().isUnauthorized());
	}
}
