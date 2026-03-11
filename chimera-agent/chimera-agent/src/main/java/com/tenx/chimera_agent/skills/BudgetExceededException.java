package com.tenx.chimera_agent.skills;

public class BudgetExceededException extends Exception {
	public BudgetExceededException(String message) {
		super(message);
	}

	public BudgetExceededException(String message, Throwable cause) {
		super(message, cause);
	}
}
