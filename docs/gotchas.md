# Известные проблемы и особенности

## 2026-06-15 — BlockData больше не принимает Terra SNBT

Контекст: pinned `OVERWORLD` pack содержит TerraScript block strings с хвостом
SNBT, например `minecraft:suspicious_gravel{LootTable:'...'}`.

Проблема: Paper 1.21.11 отклоняет такую строку в `Bukkit.createBlockData`.

Решение/обход: отделять Bukkit BlockData от SNBT, разбирать SNBT через
Adventure NBT и применять поддерживаемые block entity fields после установки
блока. Нельзя молча отбрасывать неизвестные поля. Архитектурное решение:
`/docs/adr/0004-parse-legacy-block-entity-data.md`.

## 2026-06-15 — NMS bindings привязаны к точной версии

Контекст: Paper world generation использует CraftBukkit/NMS signatures и
reflection mappings.

Проблема: успешная компиляция не доказывает совместимость hooks с другой
версией Minecraft; несовпадение может проявиться только при генерации чанков.

Решение/обход: поддерживать только `v1.21.11`, проверять signatures тестом
`NMSChunkGeneratorContractTest` и подтверждать порт запуском
`:platforms:bukkit:runCleanServer`. См.
`/docs/adr/0001-paper-1-21-11-only.md`.

## 2026-06-15 — Clean server должен запускать production JAR

Контекст: обычный dev server может содержать дополнительные плагины и старые
артефакты, скрывающие ошибки упаковки.

Проблема: такой запуск не подтверждает, что самостоятельный production JAR
работает без оригинальной Terra и compatibility plugins.

Решение/обход: использовать `:platforms:bukkit:runCleanServer`; задача удаляет
`platforms/bukkit/run-clean`, создаёт сервер заново и устанавливает только
актуальный shaded JAR.

## 2026-06-15 — Остановка test server через консоль Paper

Контекст: smoke test выполнялся на зафиксированном Paper 1.21.11 test server.

Проблема: после успешного старта консольная команда `/stop` может завершиться
внутренним исключением Paper вместо штатной остановки процесса.

Решение/обход: считать smoke test успешным только после появления признаков
полного старта, включения плагина и загрузки pack; зависший test process затем
завершать отдельно. Это не должно скрывать ошибки старта плагина.
