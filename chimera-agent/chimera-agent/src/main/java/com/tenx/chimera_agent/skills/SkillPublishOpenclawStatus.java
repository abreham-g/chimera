package com.tenx.chimera_agent.skills;

// File purpose: Defines SkillPublishOpenclawStatus behavior for Project Chimera.

import java.util.Map;

public interface SkillPublishOpenclawStatus {
	Map<String, Object> execute(Map<String, Object> input) throws BudgetExceededException;
}


