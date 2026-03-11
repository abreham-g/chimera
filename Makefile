PROJECT_DIR := chimera-agent/chimera-agent

ifeq ($(OS),Windows_NT)
MVN_CMD := cmd /c mvnw.cmd
else
MVN_CMD := ./mvnw
endif

.PHONY: setup test lint
.PHONY: docker-test spec-check

setup:
	cd $(PROJECT_DIR) && $(MVN_CMD) clean install -DskipTests

test:
	cd $(PROJECT_DIR) && $(MVN_CMD) test

lint:
	cd $(PROJECT_DIR) && $(MVN_CMD) checkstyle:check

docker-test:
	docker build --target tester -t chimera-agent-tests .
	docker run --rm chimera-agent-tests

spec-check:
	python scripts/spec_check.py
