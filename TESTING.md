# Code Generation Testing

## Overview

The project contains tests that compare generated Java code against expected output. The tests cover two scenarios:
- Generation without Lombok (`without-lombok`)
- Generation with Lombok (`with-lombok`)

## Test Structure

### Main Classes

1. **`GenerationComparisonTestBase`** — base class for comparing generated code against expected output
   - Located at: `src/test/java/ru/yojo/codegen/generator/base/`
   - Provides methods for file generation and comparison

2. **Test classes for each contract:**
   - `TestContractWithoutLombokTest` / `TestContractWithLombokTest` — for `test.yaml`
   - `AsyncApiV3WithoutLombokTest` / `AsyncApiV3WithLombokTest` — for `async-api-official-v3.0.yaml`
   - `GitterWithoutLombokTest` / `GitterWithLombokTest` — for `gitter-streaming-async-api-v3.0.yaml`
   - `SlackWithoutLombokTest` / `SlackWithLombokTest` — for `slack-real-time-async-api-v3.0.yaml`
   - `SpecFromIssueWithoutLombokTest` / `SpecFromIssueWithLombokTest` — for `spec-from-issue.yaml`
   - `OneMoreWithoutLombokTest` / `OneMoreWithLombokTest` — for `one-more.yaml`
   - `DiscriminatorWithoutLombokTest` / `DiscriminatorWithLombokTest` — for `discriminator.yaml`
   - `TestCreateAppWithoutLombokTest` / `TestCreateAppWithLombokTest` — for `test-create-app.yaml`

### Expected Files

Expected files are located at:
- `src/test/resources/example/expected/without-lombok/` — for generation without Lombok
- `src/test/resources/example/expected/with-lombok/` — for generation with Lombok

The structure inside these directories follows the package location:
- `test/` — for `test.yaml` contract (package: `example.testGenerate.test`)
- `asyncapi/` — for AsyncAPI v3 contracts
- `gitter/` — for Gitter contracts
- `slack/` — for Slack contracts
- `specFromIssue/` — for `spec-from-issue`
- `oneMore/` — for `one-more.yaml`
- `discriminator/` — for `discriminator.yaml`
- `testCreateApp/` — for `test-create-app.yaml`

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Tests for a Specific Contract
```bash
# Without Lombok
./gradlew test --tests "ru.yojo.codegen.generator.TestContractWithoutLombokTest"

# With Lombok
./gradlew test --tests "ru.yojo.codegen.generator.TestContractWithLombokTest"
```

### Run All Tests Without Lombok
```bash
./gradlew test --tests "ru.yojo.codegen.generator.*WithoutLombokTest"
```

### Run All Tests With Lombok
```bash
./gradlew test --tests "ru.yojo.codegen.generator.*WithLombokTest"
```

## Updating Expected Files

If the generated code structure changes, the expected files must be updated:

### Automatic Update

1. Run the `GenerateExpectedFilesTest`:
   ```bash
   # Update files without Lombok
   ./gradlew test --tests "ru.yojo.codegen.generator.GenerateExpectedFilesTest.generateWithoutLombok"
   
   # Update files with Lombok
   ./gradlew test --tests "ru.yojo.codegen.generator.GenerateExpectedFilesTest.generateWithLombok"
   ```

2. The test generates files in the `src/test/resources/example/expected/` directory

### Manual Update

If you need to update only a specific contract:

1. Change `packageLocation` and `specName` in the `generateForSpec()` method of the `GenerateExpectedFilesTest` class
2. Run the test
3. Verify the generated files

## Adding New Tests

To add tests for a new contract:

1. Create a new YAML file in `src/test/resources/example/contract/`
2. Create two test classes (without Lombok and with Lombok):
   ```java
   public class NewContractWithoutLombokTest extends GenerationComparisonTestBase {
       @Override
       protected boolean useLombok() { return false; }
       
       @Override
       protected String getSpecName() { return "new-contract.yaml"; }
       
       @Override
       protected String getPackageLocation() { return "newContract"; }
       
       @Test
       void testNewContractWithoutLombok() throws IOException {
           generateAndCompare();
       }
   }
   ```
3. Generate expected files using `GenerateExpectedFilesTest`
4. Run the tests to verify

## Implementation Details

### Base Class `GenerationComparisonTestBase`

- Uses JUnit 5 `@TempDir` to create a temporary directory
- Compares file content, not just file existence
- Normalizes content (removes extra whitespace, normalizes line endings)
- If the expected directory does not exist, it simply verifies that generation succeeds

### Error Handling

- Tests fail with a detailed message about differences between expected and generated code
- The error message displays both the expected and actual file contents

## Tests for Separated Contracts

For contracts split across multiple files (in the `contract/separated/` directory), separate tests have been created:
- `SeparatedCollectionTypesTest`
- `SeparatedNumericValuesTest`
- `SeparatedObjectTypesTest`
- `SeparatedStringValuesTest`

These tests currently only verify that generation succeeds, without comparing against expected output.

## Tips

1. **Checking generated code**: If a test fails, review the differences in the report: `build/reports/tests/test/index.html`
2. **Debugging**: Enable verbose output: `./gradlew test --info`
3. **Cleanup**: To clean generated files: `./gradlew clean`
