package com.tenx.chimera_agent.api;

// File purpose: Defines RunsControllerTest behavior for Project Chimera.

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RunsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void collectRunShouldQueueAndReturnSpecFields() throws Exception {
		mockMvc.perform(
						post("/api/v1/runs/collect")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "platforms": ["youtube_shorts"],
										  "window_minutes": 60,
										  "limit_per_platform": 100
										}
										""")
				)
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.run_id").exists())
				.andExpect(jsonPath("$.status").value("QUEUED"));
	}

	@Test
	void collectRunShouldReturnValidationErrorWhenLimitIsOutOfRange() throws Exception {
		mockMvc.perform(
						post("/api/v1/runs/collect")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "platforms": ["youtube_shorts"],
										  "window_minutes": 60,
										  "limit_per_platform": 500
										}
										""")
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.trace_id").exists());
	}

	@Test
	void rerunShouldReturnNotFoundForUnknownRunId() throws Exception {
		mockMvc.perform(
						post("/api/v1/runs/run_unknown/rerun")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "reason": "retry_after_partial_failure"
										}
										""")
				)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.error.details.field").value("run_id"))
				.andExpect(jsonPath("$.error.details.value").value("run_unknown"));
	}

	@Test
	void rerunShouldQueueWhenSourceRunExists() throws Exception {
		MvcResult collectResult = mockMvc.perform(
						post("/api/v1/runs/collect")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "platforms": ["tiktok"],
										  "window_minutes": 30,
										  "limit_per_platform": 20
										}
										""")
				)
				.andExpect(status().isAccepted())
				.andReturn();

		JsonNode collectJson = objectMapper.readTree(collectResult.getResponse().getContentAsString());
		String sourceRunId = collectJson.get("run_id").asText();

		mockMvc.perform(
						post("/api/v1/runs/{runId}/rerun", sourceRunId)
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "reason": "retry_after_partial_failure"
										}
										""")
				)
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.source_run_id").value(sourceRunId))
				.andExpect(jsonPath("$.new_run_id").exists())
				.andExpect(jsonPath("$.status").value("QUEUED"));
	}
}

