package com.tenx.chimera_agent.skills;

import java.util.Map;

public interface SkillFetchTrends {
	Map<String, Object> execute(Map<String, Object> input) throws BudgetExceededException;
}

