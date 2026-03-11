package com.tenx.chimera_agent.concurrency;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class VirtualThreadTaskRunnerTest {

	@Test
	void runAllExecutesTasksConcurrentlyAndCollectsResults() {
		List<Callable<Integer>> tasks = List.of(
				() -> {
					Thread.sleep(200);
					return 1;
				},
				() -> {
					Thread.sleep(200);
					return 2;
				},
				() -> {
					Thread.sleep(200);
					return 3;
				}
		);

		List<Integer> results = assertTimeoutPreemptively(Duration.ofMillis(450),
				() -> VirtualThreadTaskRunner.runAll(tasks));

		assertEquals(List.of(1, 2, 3), results);
	}
}

