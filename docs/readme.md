# Отчет о разработке планирования производства глазированных сырков в Березе

## Общие сведения о техпроцессе

### Производительность линий

| № линии | Производительность в минуту | Вид сырка          |
|---------|-----------------------------|--------------------|
| 1       | 200—208                     | Классика, плюш     |
| 2       | 196                         | Классика           |
| 3       | 206                         | Классика           |
| 3       | 170                         | Корумс             |
| 4,5,6   | 220                         | Классика           |
| 4,5,6   | 198                         | Стержень           |
| 5       | 170                         | Корумс             |

### Мойка линий

1. Продолжительность – 4–5 часов
2. Необходимо выполнить мойку после аллергенов (сырков с орехами)
3. Без мойки можно выполнять переход от более светлого к более темному творогу (например: ваниль – бисквиты – шоколад), потом в конце обязательно выполнить мойку

### Варка глазури

Три мельницы:

| № мельницы | Продолжительность варки | Вес в тоннах |
|------------|-------------------------|--------------|
| 1          | 7 часов                 | 1            |
| 2          | 3 часа                  | 2,5          |
| 3          | 3 часа                  | 2,5          |

Итого 6 тонн в сутки

4 вида своей глазури, которую сами варят:

| Код глазури | В каком продукте используется |
|-------------|-------------------------------|
| C6          | Топ, творобушки               |
| С4          | Ваниль, сгущенка, шоколад     |
| С65(47)     | Плюш, фисташка                |
| Аленка      | Аленка                        |

#### Покупные глазури

1. Апельсин
2. Малина и другие

### Дополнительная информация

1. Часто чередуют один день – фисташка, второй – кокос-миндаль.
2. Обычно максимальный заказ – до 42 тонн готового продукта в сутки.

### Актуальный ассортимент сырков

#### РЕСПУБЛИКА БЕЛАРУСЬ

**Классическая линейка:**

+ Сырок творожный глазированный «Савушкин» 23% ваниль, 40г

**Конфетная линейка:**

+ Сырок творожный глазированный «Любимая Коровка» 23%, 40г
+ Сырок творожный глазированный «Моя Алёнка» 20%, 40г
+ Сырок творожный глазированный «Кокос-миндаль» 20%, 40г
+ Сырок творожный глазированный «Бискотти» 20%, 40г
+ Сырок творожный глазированный «Картошка» 20%, 40г
+ Сырок творожный глазированный «Маковка» 20%, 40г
+ Сырок творожный глазированный «Кофе-карамель» 20%, 40г

**ТОП. С начинкой:**

+ Сырок творожный глазированный «ТОП» 20% с начинкой клубника, 40г
+ Сырок творожный глазированный «ТОП» 20% с начинкой манго, 40г
+ Сырок творожный глазированный «ТОП» 20% с начинкой вареная сгущёнка, 40г
+ Сырок творожный глазированный «ТОП» 20% какао с начинкой шоколад, 40г
+ Сырок творожный глазированный «ТОП» 20% с начинкой малина, 40г
+ Сырок творожный глазированный «ТОП» 20% с начинкой фундук, 40г

**ТОП. С кусочками:**

+ Сырок творожный глазированный «ТОП» 20% с кусочками карамельной глазури, 35г
+ Сырок творожный глазированный «ТОП» 20% с кусочками малиновой глазури, 35г
+ Сырок творожный глазированный «ТОП» 20% с кусочками апельсиновой глазури, 35г

#### РОССИЙСКАЯ ФЕДЕРАЦИЯ

**Классическая линейка:**

+ Сырок творожный глазированный «Савушкин» 23% ваниль, 40г
+ Сырок творожный глазированный «Савушкин» 20% вареная сгущёнка, 40г

**Конфетная линейка:**

+ Сырок творожный глазированный «Любимая Коровка» 23%, 40г
+ Сырок творожный глазированный «Кокос-миндаль» 20%, 40г
+ Сырок творожный глазированный «Фисташка» 20%, 40г

**ТВОРОБУШКИ. С начинкой:**

+ Сырок творожный глазированный «Творобушки» 20% с начинкой клубника, 40г
+ Сырок творожный глазированный «Творобушки» 20% с начинкой манго, 40г
+ Сырок творожный глазированный «Творобушки» 20% с начинкой вареная сгущёнка, 40г
+ Сырок творожный глазированный «Творобушки» 20% какао с начинкой шоколад, 40г

**ТВОРОБУШКИ. Классическая линейка:**

+ Сырок творожный глазированный «Творобушки» 20% ваниль, 40г

## Что нужно оптимизировать

1. Минимизировать смену глазури
2. Минимизировать смену продукта (GTIN)

На входе планировщика – разбивка производственного заказа по партиям.
На выходе – разбивка партий по линиям.

## Программная реализация

Поля ProjectJobSchedule состоят из списков объектов следующих классов:

1. `Project` – партия
2. `Resource` – оборудование и материалы
3. `Job` – операции технологического процесса
4. `Allocation` – результат планирования, т. е. какая операция определенной партии на каком оборудовании в какое время будет выполняться.
5. `ExecutionMode` – вариант распределения выполнения технологической операции по оборудованию, плюс время выполнения. Например, на одном оборудовании операция займет одно время, а на другом оборудование - другое.
6. `ResourceRequirement` – требования к ресурсам, какие ресурсы нужны для выполнения операции по выбранному `ExecutionMode`.

Следующий этап после завершения импорта/экспорта данных – настройка правил оптимизации для планирования расчета, а также разработка модульных тестов.

### Настраиваемые Ограничения в Timefold solver

В `Timefold Solver` ограничения могут быть классифицированы на три основных типа: `Hard`, `Medium` и `Soft`. Эти типы определяют важность ограничений и то, как они влияют на общую оценку решения.

Типы ограничений:

+ `Hard Constraints` (Жесткие ограничения):

  + Описание: Жесткие ограничения представляют собой обязательные правила, которые должны быть выполнены для любого допустимого решения. Если хотя бы одно жесткое ограничение нарушено, решение считается недопустимым.
  + Пример: В контексте планирования ресурсов, жесткое ограничение может быть связано с тем, что потребление ресурсов не должно превышать их доступную емкость.

+ `Medium Constraints` (Средние ограничения):
  + Описание: Средние ограничения представляют собой правила, которые важны, но не обязательны для выполнения. Нарушение средних ограничений штрафуется, но решение остается допустимым.
  + Пример: В контексте планирования проектов, среднее ограничение может быть связано с задержкой в завершении проекта. Чем больше задержка, тем больше штраф, но проект все еще может быть завершен.

+ `Soft Constraints` (Мягкие ограничения):
  + Описание: Мягкие ограничения представляют собой правила, которые желательны, но не критичны. Нарушение мягких ограничений также штрафуется, но в меньшей степени, чем средние и жесткие.
  + Пример: В контексте планирования проектов, мягкое ограничение может быть связано с общей продолжительностью проекта (`makespan`). Чем короче продолжительность, тем лучше, но это не обязательное требование.

Ограничения описываются в файле `ProjectJobSchedulingConstraintProvider.Java` в папке `solver`. Код в  данном фалйе представляет собой реализацию интерфейса ConstraintProvider из библиотеки `Timefold Solver` для `Java`.

#### Основные компоненты и их назначение

+ Класс `ProjectJobSchedulingConstraintProvider`:
  + Этот класс реализует интерфейс ConstraintProvider, который требует переопределения метода `defineConstraints`.
  + Метод `defineConstraints` возвращает массив объектов Constraint, которые определяют правила (ограничения) для оптимизации.
+ Метод `defineConstraints`:
  + В этом методе определяются все ограничения, которые будут использоваться для оптимизации.
  + Каждое ограничение представлено отдельным методом, который возвращает объект `Constraint`.

#### Ограничения

+ `nonRenewableResourceCapacity`:
  + Это ограничение проверяет, что потребление невозобновляемых ресурсов не превышает их доступную емкость.
  + Используется метод `filter` для выбора только невозобновляемых ресурсов, `join` для соединения с объектами `Allocation`, `groupBy` для группировки по ресурсам и суммирования потребностей, и penalize для наложения штрафа, если потребность превышает емкость.
+ `renewableResourceCapacity`:
  + Это ограничение проверяет, что потребление возобновляемых ресурсов не превышает их доступную емкость.
  + Аналогично предыдущему, но учитывает даты использования ресурсов.
+ `totalProjectDelay`:
  + Это ограничение штрафует за задержку в завершении проекта.
  + Используется метод `filter` для выбора только конечных задач (`JobType.SINK`) и `penalize` для наложения штрафа на задержку.
+ `totalMakespan`:
  + Это ограничение штрафует за общую продолжительность проекта (`makespan`).
  + Используется метод `groupBy` для нахождения максимальной даты окончания и `penalize` для наложения штрафа на эту дату.

#### Как это работает

+ `ConstraintFactory`:
  + Это фабрика, которая предоставляет методы для           создания потоков данных и применения операций к ним.
  + Она используется для создания и настройки ограничений.
+ `Joiners`, `ConstraintCollectors`, и другие методы:
  + Эти методы позволяют создавать сложные запросы к данным, группировать их, фильтровать и применять различные операции.
+ `Penalize` и `Reward`:
  + Эти методы используются для наложения штрафов или вознаграждений на основе выполнения ограничений.
  + `Penalize` увеличивает оценку, если ограничение нарушено, а `Reward`` уменьшает оценку, если ограничение выполнено.

Этот код определяет набор ограничений для задачи планирования проектов и ресурсов. Он использует `Timefold Solver` для автоматического поиска оптимального решения, учитывая заданные ограничения. Каждое ограничение представляет собой правило, которое должно быть выполнено для достижения оптимального расписания.