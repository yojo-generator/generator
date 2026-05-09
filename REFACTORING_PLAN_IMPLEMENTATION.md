# План выполнения рефакторинга (фаза 2)

> После полного отката к `ca7db83`. Начинаем заново, но **безопасно**.

## Текущий статус
- ✅ Откат к `ca7db83`
- ✅ `.gradle/` lock-файлы удалены из индекса
- ⚠️ Тесты показывают `BUILD SUCCESSFUL` (ожидаем 14 падающих, как в исходном состоянии)
- 🔴 Path Traversal в JavaFileWriter (CVE-2024-45373) — фикс откачен
- 🔴 `.gradle/` файлы были закоммичены
- 🔴 Logger фейковый (System.out/err, нет setMinimumLevel)
- 🔴 NamingStrategy не интегрирован (попытка сломала 63 теста)
- 🔴 PropertyTypeHandler 8 параметров
- 🔴 7/10 шагов рефакторинга не выполнены

---

## Фаза 1: Стабилизация (критические фиксы)

### Шаг 1.1: Принудительный перезапуск тестов
```powershell
.\gradlew.bat --stop
Remove-Item -Recurse -Force build, .gradle -ErrorAction SilentlyContinue
.\gradlew.bat test --no-build-cache --rerun-tasks 2>&1 | Tee-Object -FilePath "test_output.txt"
Select-String -Path "test_output.txt" -Pattern "tests completed"
```
**Цель:** Увидеть `83 tests completed, 14 failed` (исходный уровень).

### Шаг 1.2: Навсегда исправить `.gradle/` проблему
```powershell
Add-Content .gitignore "`n*.lock"
git rm -r --cached .gradle/ 2>$null
git add .gitignore
git commit -m "chore: ignore gradle lock files, remove .gradle/ from index"
```

### Шаг 1.3: Пере-применить исправление Path Traversal в JavaFileWriter
- Прочитать текущий `JavaFileWriter.java`
- Применить фикс: Path API, валидация имён, `resolveSafePath()`
- **Запустить тесты** — должно остаться 14 падающих

### Шаг 1.4: Коммит и пуш
```powershell
git add -A
git commit -m "fix: CVE-2024-45373 Path Traversal в JavaFileWriter"
git push origin develop
```

---

## Фаза 2: Безопасная интеграция NamingStrategy

**ВАЖНО:** Не трогать `MapperUtil`. Добавить `NamingStrategy` как **дополнительный** инструмент, а не замену.

```powershell
git checkout develop
git checkout -b feature/naming-strategy-safe
```

### Шаг 2.1: Добавить поле в `AbstractMapper`
```java
// AbstractMapper.java
protected static final NamingStrategy namingStrategy = new NamingStrategy();
public NamingStrategy getNamingStrategy() {
    return namingStrategy;
}
```

### Шаг 2.2: Использовать ТОЛЬКО в НОВЫХ местах
- НЕ заменять `MapperUtil.toValidJavaFieldName()` и др.
- Использовать `namingStrategy` только для новых операций.

### Шаг 2.3: Тесты после КАЖДОГО изменения
- Если тесты падают → немедленный откат.
- Цель: 0 новых падений.

### Шаг 2.4: Коммит и мерж
```powershell
git add -A
git commit -m "feat: integrate NamingStrategy as supplementary tool in mappers"
git checkout develop
git merge feature/naming-strategy-safe
git push origin develop
```

---

## Фаза 3: Исправление Logger

```powershell
git checkout develop
git checkout -b fix/logger-implementation
```

### Шаг 3.1: Добавить `setMinimumLevel()` в `Logger.java`
### Шаг 3.2: Исправить `e.printStackTrace(System.err)` → `java.util.logging`
### Шаг 3.3: Тесты и коммит

---

## Фаза 4: Рефакторинг PropertyTypeHandler

```powershell
git checkout develop
git checkout -b refactor/property-type-handler
```

### Шаг 4.1: Создать `PropertyResolutionContext` (record)
### Шаг 4.2: Обновить интерфейс и реализации
### Шаг 4.3: Тесты и коммит

---

## Фаза 5: Оставшиеся 7 шагов рефакторинга

Каждый шаг — отдельная ветка, отдельный коммит, обязательный прогон тестов:
1. **Step 1:** Удалить неиспользуемые импорты и методы
2. **Step 3:** Внедрить зависимости через конструктор
3. **Step 4:** Разделить большие методы
4. **Step 5:** Унифицировать обработку ошибок
5. **Step 7:** Оптимизировать работу с коллекциями
6. **Step 9:** Добавить документацию
7. **Step 10:** Финальная очистка
