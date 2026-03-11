package com.tenx.chimera_agent.skills;

import java.util.Map;

public interface SkillPublishOpenclawStatus {
	Map<String, Object> execute(Map<String, Object> input) throws BudgetExceededException;
}

