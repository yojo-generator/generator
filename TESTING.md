# Тестирование генерации кода

## Обзор

Проект содержит тесты для сравнения сгенерированного Java кода с ожидаемым. Тесты покрывают два сценария:
- Генерация без Lombok (`without-lombok`)
- Генерация с Lombok (`with-lombok`)

## Структура тестов

### Основные классы

1. **`GenerationComparisonTestBase`** - базовый класс для сравнения сгенерированного кода с ожидаемым
   - Расположен: `src/test/java/ru/yojo/codegen/generator/base/`
   - Предоставляет методы для генерации и сравнения файлов

2. **Тестовые классы для каждого контракта:**
   - `TestContractWithoutLombokTest` / `TestContractWithLombokTest` - для `test.yaml`
   - `AsyncApiV3WithoutLombokTest` / `AsyncApiV3WithLombokTest` - для `async-api-official-v3.0.yaml`
   - `GitterWithoutLombokTest` / `GitterWithLombokTest` - для `gitter-streaming-async-api-v3.0.yaml`
   - `SlackWithoutLombokTest` / `SlackWithLombokTest` - для `slack-real-time-async-api-v3.0.yaml`
   - `SpecFromIssueWithoutLombokTest` / `SpecFromIssueWithLombokTest` - для `spec-from-issue.yaml`
   - `OneMoreWithoutLombokTest` / `OneMoreWithLombokTest` - для `one-more.yaml`
   - `DiscriminatorWithoutLombokTest` / `DiscriminatorWithLombokTest` - для `discriminator.yaml`
   - `TestCreateAppWithoutLombokTest` / `TestCreateAppWithLombokTest` - для `test-create-app.yaml`

### Ожидаемые файлы

Ожидаемые файлы расположены в:
- `src/test/resources/example/expected/without-lombok/` - для генерации без Lombok
- `src/test/resources/example/expected/with-lombok/` - для генерации с Lombok

Структура внутри этих директорий соответствует package location:
- `test/` - для контракта test.yaml (package: `example.testGenerate.test`)
- `asyncapi/` - для AsyncAPI v3 контрактов
- `gitter/` - для Gitter контрактов
- `slack/` - для Slack контрактов
- `specFromIssue/` - для spec-from-issue
- `oneMore/` - для one-more.yaml
- `discriminator/` - для discriminator.yaml
- `testCreateApp/` - для test-create-app.yaml

## Запуск тестов

### Запуск всех тестов
```bash
./gradlew test
```

### Запуск тестов для конкретного контракта
```bash
# Без Lombok
./gradlew test --tests "ru.yojo.codegen.generator.TestContractWithoutLombokTest"

# С Lombok
./gradlew test --tests "ru.yojo.codegen.generator.TestContractWithLombokTest"
```

### Запуск всех тестов без Lombok
```bash
./gradlew test --tests "ru.yojo.codegen.generator.*WithoutLombokTest"
```

### Запуск всех тестов с Lombok
```bash
./gradlew test --tests "ru.yojo.codegen.generator.*WithLombokTest"
```

## Обновление ожидаемых файлов

Если структура генерируемого кода изменилась, нужно обновить ожидаемые файлы:

### Автоматическое обновление

1. Запустите тест `GenerateExpectedFilesTest`:
   ```bash
   # Обновить файлы без Lombok
   ./gradlew test --tests "ru.yojo.codegen.generator.GenerateExpectedFilesTest.generateWithoutLombok"
   
   # Обновить файлы с Lombok
   ./gradlew test --tests "ru.yojo.codegen.generator.GenerateExpectedFilesTest.generateWithLombok"
   ```

2. Тест сгенерирует файлы в директории `src/test/resources/example/expected/`

### Ручное обновление

Если нужно обновить только конкретный контракт:

1. Измените `packageLocation` и `specName` в методе `generateForSpec()` класса `GenerateExpectedFilesTest`
2. Запустите тест
3. Проверьте сгенерированные файлы

## Добавление новых тестов

Для добавления тестов для нового контракта:

1. Создайте новый YAML файл в `src/test/resources/example/contract/`
2. Создайте два тестовых класса (без Lombok и с Lombok):
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
3. Сгенерируйте ожидаемые файлы с помощью `GenerateExpectedFilesTest`
4. Запустите тесты для проверки

## Особенности реализации

### Базовый класс `GenerationComparisonTestBase`

- Использует JUnit 5 `@TempDir` для создания временной директории
- Сравнивает содержимое файлов, а не только их наличие
- Нормализует содержимое (убирает лишние пробелы, приводит переносы строк к единому виду)
- Если ожидаемая директория не существует, просто проверяет, что генерация проходит успешно

### Обработка ошибок

- Тесты падают с подробным сообщением о различиях между ожидаемым и сгенерированным кодом
- В сообщении об ошибке выводятся как ожидаемое, так и фактическое содержимое файлов

## Тесты для separated контрактов

Для контрактов, разделенных на несколько файлов (в директории `contract/separated/`), созданы отдельные тесты:
- `SeparatedCollectionTypesTest`
- `SeparatedNumericValuesTest`
- `SeparatedObjectTypesTest`
- `SeparatedStringValuesTest`

Эти тесты пока только проверяют, что генерация проходит успешно, без сравнения с ожидаемым.

## Полезные советы

1. **Проверка сгенерированного кода**: Если тест падает, посмотрите на различия в отчете: `build/reports/tests/test/index.html`
2. **Отладка**: Включите подробный вывод: `./gradlew test --info`
3. **Очистка**: Для очистки сгенерированных файлов: `./gradlew clean`
