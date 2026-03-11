import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class skillsInterfaceTest {

    @Test
    void fetchTrendsSkillShouldAcceptContractInputsAndDeclareBudgetException() throws Exception {
        Class<?> budgetExceededException = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.skills.BudgetExceededException"),
                "Expected BudgetExceededException to be defined for skill budget guardrails.");

        Class<?> fetchTrendsSkill = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.skills.SkillFetchTrends"),
                "Expected SkillFetchTrends interface to exist.");

        assertTrue(fetchTrendsSkill.isInterface(), "SkillFetchTrends must be an interface.");

        Method execute = fetchTrendsSkill.getMethod("execute", Map.class);
        assertEquals(Map.class, execute.getReturnType(), "SkillFetchTrends.execute must return a contract map.");

        assertTrue(
                Arrays.stream(execute.getExceptionTypes()).anyMatch(type -> type.equals(budgetExceededException)),
                "SkillFetchTrends.execute must declare BudgetExceededException.");
    }

    @Test
    void publishStatusSkillShouldAcceptContractInputsAndDeclareBudgetException() throws Exception {
        Class<?> budgetExceededException = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.skills.BudgetExceededException"),
                "Expected BudgetExceededException to be defined for skill budget guardrails.");

        Class<?> publishStatusSkill = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.skills.SkillPublishOpenclawStatus"),
                "Expected SkillPublishOpenclawStatus interface to exist.");

        assertTrue(publishStatusSkill.isInterface(), "SkillPublishOpenclawStatus must be an interface.");

        Method execute = publishStatusSkill.getMethod("execute", Map.class);
        assertEquals(Map.class, execute.getReturnType(), "SkillPublishOpenclawStatus.execute must return a contract map.");

        assertTrue(
                Arrays.stream(execute.getExceptionTypes()).anyMatch(type -> type.equals(budgetExceededException)),
                "SkillPublishOpenclawStatus.execute must declare BudgetExceededException.");
    }
}
