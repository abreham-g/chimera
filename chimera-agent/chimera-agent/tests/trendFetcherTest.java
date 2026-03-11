// File purpose: Defines trendFetcherTest behavior for Project Chimera.

import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class trendFetcherTest {

    @Test
    void trendsResponseShouldMatchTechnicalSpecContract() {
        Class<?> trendsResponse = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.api.contract.TrendsResponse"),
                "Expected TrendsResponse contract type to exist.");

        assertTrue(trendsResponse.isRecord(), "TrendsResponse must be a Java record.");

        Set<String> fields = Arrays.stream(trendsResponse.getRecordComponents())
                .map(RecordComponent::getName)
                .collect(Collectors.toSet());

        assertTrue(fields.contains("windowHours"), "TrendsResponse must include windowHours.");
        assertTrue(fields.contains("platform"), "TrendsResponse must include platform.");
        assertTrue(fields.contains("items"), "TrendsResponse must include items.");
    }

    @Test
    void trendItemShouldContainRequiredFieldsFromApiSpec() {
        Class<?> trendItem = assertDoesNotThrow(
                () -> Class.forName("com.tenx.chimera_agent.api.contract.TrendItem"),
                "Expected TrendItem contract type to exist.");

        assertTrue(trendItem.isRecord(), "TrendItem must be a Java record.");

        Set<String> fields = Arrays.stream(trendItem.getRecordComponents())
                .map(RecordComponent::getName)
                .collect(Collectors.toSet());

        assertTrue(fields.contains("videoId"), "TrendItem must include videoId.");
        assertTrue(fields.contains("title"), "TrendItem must include title.");
        assertTrue(fields.contains("creatorHandle"), "TrendItem must include creatorHandle.");
        assertTrue(fields.contains("trendScore"), "TrendItem must include trendScore.");
        assertTrue(fields.contains("views"), "TrendItem must include views.");
        assertTrue(fields.contains("capturedAt"), "TrendItem must include capturedAt.");
    }
}

