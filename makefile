build:
	@chmod +x gradlew
	@./gradlew build

report:
	@./gradlew jacocoTestReport

.PHONY: build