# 0004. Разбирать legacy block entity data структурно

Дата: 2026-06-15
Статус: принято

## Контекст

Pinned `OVERWORLD` pack использует TerraScript block strings с SNBT после
BlockData. Paper 1.21.11 больше не принимает всю строку через
`Bukkit.createBlockData`, из-за чего pack не мог загрузиться на clean server.

## Решение

Разделять BlockData и SNBT, разбирать SNBT библиотекой Adventure NBT и хранить
block entity data рядом с Bukkit block state. На первом этапе поддерживать
`LootTable` и необязательный `LootTableSeed`; неизвестные поля отклонять явно.
Adventure NBT включать в shaded JAR с relocation.

## Рассмотренные альтернативы

- Удалять SNBT перед вызовом Bukkit: отклонено, потому что теряются loot tables.
- Разбирать SNBT строковыми операциями: отклонено из-за escaping, вложенных tags
  и риска некорректного молчаливого разбора.
- Изменить pinned pack: отклонено, потому что стадия 1 должна сохранить исходный
  pack и исправить совместимость платформенного слоя.

## Последствия

Новые block entity fields добавляются только с явной валидацией и tests.
Shading обязан сохранять relocated Adventure NBT classes в production JAR.
