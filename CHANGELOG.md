# Change Log

## [3.0.0]
### Feature
- **Discriminator support** — добавлена генерация `@JsonTypeInfo` и `@JsonSubTypes` для полиморфной десериализации в Jackson
  - `@JsonTypeInfo` на базовом классе с указанием поля-дискриминатора
  - `@JsonSubTypes` со списком всех подтипов
  - Пример использования в `discriminator.yaml`

### Changes
- **Version bump** — версия поднята до 3.0.0 в связи с новым функционалом
- **Test coverage** — добавлен интеграционный тест `discriminator()`
- **Documentation** — обновлён README.md с секциями 6.1 и 10.1

### Future (see JSON_TYPE_ID_PLAN.md)
- `@JsonTypeId` на поле дискриминатора в подтипах

## [2.0.1]
### Fix
- Исправлен баг с @NoArgsConstructor когда в build.gradle он отключен, но аннотация все равно добавляется к классу.