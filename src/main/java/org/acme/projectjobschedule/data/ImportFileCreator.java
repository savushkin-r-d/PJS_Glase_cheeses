package org.acme.projectjobschedule.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

public class ImportFileCreator {

    private final List<ExecutionModeRecord> executionModes = List.of(
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв.г.Кок-мин мдж20%40г"
            ),
            new ExecutionModeRecord("481026804371",200,196,206,220, 220,220,
                    "Сырок тв. глаз. \"Моя Аленка\" мдж20% Флоупак 40г"
            ),
            new ExecutionModeRecord("4810268045042",200,196,206,220,220,220,
                    "Сырок тв. глаз. с ароматом ванили мдж 23 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268055492"	,200,196,206,220,220,220
                    ,	"Сырок тв. глаз. с наполнителем \"Кофе-карамель\" мдж 20,0 % ,  ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268040450"	,200,196, 206, 220,220,220,
                    "Сырок тв.  глаз.  \"Любимая Коровка\" с наполнителем мдж 23,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268043475"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. с наполнителем \"Фисташка\" мдж 23,0 % ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268045066"	,200,196, 206, 220,220,220,
                    "Сырок тв.  глаз.  с наполнителем \"Молоко сгущенное вареное\" и ароматом ванили мдж 20 % ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268053870"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"Картошка\" мдж 20,0 %,  ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. с печеньем \"Бискотти\" мдж 20,0 %,  ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз.  \"Плюш\" с ароматом ванили мдж 26,0 %, фольга 45 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"Плюш\" с какао и ароматом ванили мдж 18,0 %, фольга 45 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. с какао и ароматом ванили мдж 23,0 % ТМ \"Мишка на полюсе\", фольга 45 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв.гл. Вар.сгущ. мдж 23,0 % ТМ \"Мишка на полюсе\", фольга 45 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз.\"ТОП\" с начинкой \"Клубника\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Манго\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Малина\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с какао и начинкой \"Шоколад\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Молоко сгущенное вареное\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Фундук\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Карамель\" и вкусом арахиса мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз.\"ТВОРОБУШКИ\" с начинкой \"Манго\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с начинкой \"Клубника\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с ароматом ванили в темной глазури мдж 20,0 %, флоупак 40г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с какао и начинкой \"Шоколад\"  мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с начинкой \"Молоко сгущенное вареное\"  мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП КАРАМЕЛЬ\" со вкусом и кусочками глазури \"Соленая карамель\" мдж 20 % ФЛОУПАК 35 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП МАЛИНА\" с кусочками цветной глазури с ароматом малины мдж 20 % ФЛОУПАК 35 г"
            ),
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"ТОП АПЕЛЬСИН\" с кусочками цветной глазури с ароматом апельсина мдж 20 % ФЛОУПАК 35 г"
            )
            ) ;

    @JsonPropertyOrder({"Allocation", "MatchCount", "Constraint", "Penalty"})
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    record ExecutionModeRecord(String ean13, int line1, int line2, int line3,int line4, int line5, int line6, String productName) {

        @Override
        @JsonProperty("EAN13")
        public String ean13() {
            return ean13;
        }

        @Override
        @JsonProperty("Line1")
        public int line1() {
            return line1;
        }

        @Override
        @JsonProperty("Line2")
        public int line2() {
            return line2;
        }

        @Override
        @JsonProperty("Line3")
        public int line3() {
            return 3;
        }

        @Override
        @JsonProperty("Line4")
        public int line4() {
            return line4;
        }
        @Override
        @JsonProperty("Line5")
        public int line5() {
            return line5;
        }

        @Override
        @JsonProperty("Line6")
        public int line6() {
            return line6;
        }

        @Override
        @JsonProperty("ProductName")
        public String productName() {
            return productName;
        }
    }

}
