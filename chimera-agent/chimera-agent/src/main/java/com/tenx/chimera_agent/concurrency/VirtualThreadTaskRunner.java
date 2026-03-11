package com.tenx.chimera_agent.concurrency;

// File purpose: Defines VirtualThreadTaskRunner behavior for Project Chimera.

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class VirtualThreadTaskRunner {

	private VirtualThreadTaskRunner() {
	}

	public static <T> List<T> runAll(List<? extends Callable<T>> tasks) {
		Objects.requireNonNull(tasks, "tasks");
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			List<Future<T>> futures = new ArrayList<>(tasks.size());
			for (var task : tasks) {
				futures.add(executor.submit(Objects.requireNonNull(task, "task")));
			}

			List<T> results = new ArrayList<>(tasks.size());
			for (var future : futures) {
				try {
					results.add(future.get());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new VirtualThreadExecutionException("Interrupted while waiting for task results.", e);
				} catch (ExecutionException e) {
					throw new VirtualThreadExecutionException("Task execution failed.", e.getCause());
				}
			}
			return results;
		}
	}
}


