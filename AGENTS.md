## ОБЯЗАТЕЛЬНЫЙ ПРОТОКОЛ ПЕРЕД НАЧАЛОМ РАБОТЫ

Перед выполнением ЛЮБОЙ задачи в этом проекте ты ОБЯЗАН:

1. Прочитать `/AGENTS.md` в корне репозитория — там стек, команды, соглашения и запреты.
2. Прочитать `/docs/gotchas.md` — известные проблемы, особенности API, костыли.
   Если решение конфликтует с записью оттуда — остановись и спроси пользователя,
   а не игнорируй файл.
3. Прочитать последние 3-5 файлов в `/docs/adr/` (по дате) — это архитектурные
   решения и почему они приняты. Не предлагай отменить или переделать решение
   из ADR без явного запроса пользователя.
4. Если работаешь по конкретной задаче — прочитать соответствующий файл в
   `/tasks/` или issue, там полный контекст и acceptance criteria.

## ПОСЛЕ ЗАВЕРШЕНИЯ ЗАДАЧИ

1. Если принял НЕТРИВИАЛЬНОЕ архитектурное/технологическое решение — создай
   новый файл `/docs/adr/NNNN-short-title.md` по шаблону (см. /docs/adr/template.md).
   Не редактируй старые ADR.
2. Если наткнулся на грабли/особенность API/неочевидное поведение — добавь запись
   в `/docs/gotchas.md` с датой и кратким описанием.
3. В commit message / PR-описании укажи: что сделано, ПОЧЕМУ так, что было
   рассмотрено и отклонено (если применимо), на что обратить внимание при ревью.

## ЗОЛОТЫЕ ПРАВИЛА

- Если информация в файлах противоречит твоим предположениям — файлы важнее.
- Если файла, который должен существовать, нет (например /docs/gotchas.md) —
  сообщи пользователю, не придумывай содержимое.
- Не дублируй информацию между файлами — указывай ссылку на источник.
- При неуверенности — лучше спросить пользователя, чем угадать и сломать что-то,
  что зависело от необъяснённого тебе контекста.

---

# AGENTS.md

## Проект

NullNomadsWorldgen — самостоятельный Paper-плагин ванильноподобной генерации
Overworld, основанный на зафиксированных снимках Terra и TerraOverworldConfig.
Полный продуктовый контекст и acceptance criteria находятся в
[issue NullNomads/.github#4](https://github.com/NullNomads/.github/issues/4).

## Стек

- Java 21 и Kotlin DSL для Gradle.
- Gradle Wrapper из форка Terra.
- Paper API, Paper dev bundle и test server для Minecraft `1.21.11`.
- Terra `7.0.0` pre-release как исходная архитектура.
- Зафиксированный pack `OVERWORLD` `2.0.0` в Git submodule.

Точные upstream commits и политика обновления: `/UPSTREAM.md`.

## Команды

- Build: `gradlew.bat clean build`
- Test: `gradlew.bat test`
- License check: `gradlew.bat verifyLicenseFiles`
- Paper module: `gradlew.bat :platforms:bukkit:build`
- Clean Paper smoke server: `gradlew.bat :platforms:bukkit:runCleanServer`

Для Gradle и Paper dev bundle требуется JDK 21.

## Структура

- `/common/api` — публичные Terra API-модули.
- `/common/implementation` — общая GPL-реализация.
- `/common/addons` — только addons, нужные встроенному `OVERWORLD` pack.
- `/platforms/bukkit/common` — Paper/Bukkit интеграция.
- `/platforms/bukkit/nms` — NMS bindings строго для Minecraft `1.21.11`.
- `/packs/overworld` — pinned Git submodule TerraOverworldConfig.
- `/buildSrc` — общая Gradle-конфигурация и версии зависимостей.
- `/docs/adr` — неизменяемая история архитектурных решений.

Актуальный состав Gradle modules определяется `/settings.gradle.kts`.

## Соглашения

- Сохранять upstream-классы в пакетах `com.dfsek.terra`.
- Новые классы форка размещать в `org.nullnomads.worldgen`.
- Сохранять исходные copyright headers и ближайшие license files.
- Делать небольшие тематические commits и не смешивать разные стадии задачи.
- Отмечать checkbox задачи только после реализации и проверки пункта.
- Карточку проекта не переводить в `Done`, пока не выполнена вся issue.

Полные правила участия и package ownership: `/CONTRIBUTING.md`.

## ЗАПРЕЩЕНО

- Добавлять поддержку версий Minecraft, кроме `1.21.11`, без явного решения.
- Возвращать исключённые платформы или произвольные Terra packs/addons в build.
- Загружать `latest` pack или другой изменяемый артефакт во время сборки/старта.
- Обновлять pinned upstream commits или Paper build без отдельного commit и тестов.
- Массово переименовывать upstream packages ради брендинга форка.
- Обходить NMS version guard JVM-флагом или молча переходить на vanilla generator.
- Перезаписывать пользовательские изменения в рабочей копии pack.
- Переводить GitHub project item в `Done` до выполнения всей issue.

## Дальше читай

- `/docs/gotchas.md`
- последние 3-5 записей в `/docs/adr/`
- `/UPSTREAM.md`
- `/THIRD_PARTY_NOTICES.md`
- конкретную задачу в `/tasks/` или GitHub issue
