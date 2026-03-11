package com.tenx.chimera_agent.skills;

// File purpose: Defines SkillFetchTrends behavior for Project Chimera.

import java.util.Map;

public interface SkillFetchTrends {
	Map<String, Object> execute(Map<String, Object> input) throws BudgetExceededException;
}


