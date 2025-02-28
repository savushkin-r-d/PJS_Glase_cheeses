package org.acme.projectjobschedule.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.sql.*;
import java.util.List;

public class ImportFileCreator {

    private final List<ExecutionModeRecord> executionModes = List.of(
            new ExecutionModeRecord("481026804372"	,200,196, 206, 220,220,220,
                    "Сырок тв.г.Кок-мин мдж20%40г"
            ),
            new ExecutionModeRecord("4810268043710",200,196,206,220, 220,220,
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
            new ExecutionModeRecord("44810268045066"	,200,196, 206, 220,220,220,
                    "Сырок тв.  глаз.  с наполнителем \"Молоко сгущенное вареное\" и ароматом ванили мдж 20 % ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268053870"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. \"Картошка\" мдж 20,0 %,  ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268054228"	,200,196, 206, 220,220,220,
                    "Сырок тв. глаз. с печеньем \"Бискотти\" мдж 20,0 %,  ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268044984"	,200,0, 0, 0,0,0,
                    "Сырок тв. глаз.  \"Плюш\" с ароматом ванили мдж 26,0 %, фольга 45 г"
            ),
            new ExecutionModeRecord("4810268044977"	,200,0, 0, 0,0,0,
                    "Сырок тв. глаз. \"Плюш\" с какао и ароматом ванили мдж 18,0 %, фольга 45 г"
            ),
            new ExecutionModeRecord("4810268049859"	,200,0, 0, 0,0,0,
                    "Сырок тв. глаз. с какао и ароматом ванили мдж 23,0 % ТМ \"Мишка на полюсе\", фольга 45 г"
            ),
            new ExecutionModeRecord("4810268049866"	,200,0, 0, 0,0,0,
                    "Сырок тв.гл. Вар.сгущ. мдж 23,0 % ТМ \"Мишка на полюсе\", фольга 45 г"
            ),
            new ExecutionModeRecord("4810268050121"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз.\"ТОП\" с начинкой \"Клубника\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050114"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Манго\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268053153"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Малина\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050138"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с какао и начинкой \"Шоколад\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050107"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Молоко сгущенное вареное\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268054969"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Фундук\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268056826"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТОП\" с начинкой \"Карамель\" и вкусом арахиса мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050664"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз.\"ТВОРОБУШКИ\" с начинкой \"Манго\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050657"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с начинкой \"Клубника\" мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050282"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с ароматом ванили в темной глазури мдж 20,0 %, флоупак 40г"
            ),
            new ExecutionModeRecord("4810268050640"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с какао и начинкой \"Шоколад\"  мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050671"	,0,0, 0, 198,198,198,
                    "Сырок тв. глаз. \"ТВОРОБУШКИ\" с начинкой \"Молоко сгущенное вареное\"  мдж 20,0 %, ФЛОУПАК 40 г"
            ),
            new ExecutionModeRecord("4810268050329"	,0,0, 170, 0,170,0,
                    "Сырок тв. глаз. \"ТОП КАРАМЕЛЬ\" со вкусом и кусочками глазури \"Соленая карамель\" мдж 20 % ФЛОУПАК 35 г"
            ),
            new ExecutionModeRecord("4810268053801"	,0,0, 170, 0,170,0,
                    "Сырок тв. глаз. \"ТОП МАЛИНА\" с кусочками цветной глазури с ароматом малины мдж 20 % ФЛОУПАК 35 г"
            ),
            new ExecutionModeRecord("4810268053818"	,0,0, 170, 0,170,0,
                    "Сырок тв. глаз. \"ТОП АПЕЛЬСИН\" с кусочками цветной глазури с ароматом апельсина мдж 20 % ФЛОУПАК 35 г"
            )
            ) ;

public ImportFileCreator() {

   String url = "jdbc:sqlserver://10.164.30.246;databaseName=MES;,encrypt=true;trustServerCertificate=true";
   // String url = "jdbc:sqlserver://10.164.30.246;databaseName=MES;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    String sqlQuery = "SELECT v.KSK, v.SNPZ, v.DTI, v.DTM, v.KMC, v.EMK, v.KOLMV, v.MASSA, v.KOLEV, v.NP, v.UX, "
            + "m.MASSA, m.EAN13, m.SNM, m.NAME "
            + "FROM [MES].[dbo].[BD_VZPMC] as v, NS_MC as m "
            + "WHERE (v.KMC = m.KMC) AND (v.DTI = ?) AND (v.KSK = ?) AND (m.MASSA < ?) "
            + "ORDER BY v.SNPZ";

    try {
        // Установка соединения
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            // Установка параметров для SQL-запроса
            preparedStatement.setString(1, "2025-02-08T00:00:00"); // Параметр для v.DTI
            preparedStatement.setString(2, "0119030000");          // Параметр для v.KSK
            preparedStatement.setDouble(3, 0.1);                  // Параметр для m.MASSA

            // Выполнение запроса
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

        // Обработка результата
        while (resultSet.next()) {
            String ksk = resultSet.getString("KSK");
            String snpz = resultSet.getString("SNPZ");
            String dti = resultSet.getString("DTI");
            String dtm = resultSet.getString("DTM");
            String kmc = resultSet.getString("KMC");
            String emk = resultSet.getString("EMK");
            int kolmv = resultSet.getInt("KOLMV");
            double massaV = resultSet.getDouble("MASSA"); // MASSA из таблицы BD_VZPMC
            int kolev = resultSet.getInt("KOLEV");
            String np = resultSet.getString("NP");
            String ux = resultSet.getString("UX");
            double massaM = resultSet.getDouble("MASSA"); // MASSA из таблицы NS_MC
            String ean13 = resultSet.getString("EAN13");
            String snm = resultSet.getString("SNM");
            String name = resultSet.getString("NAME");

        }
            }
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage()); // Выводим стек ошибок для отладки
    }
    }

    @JsonPropertyOrder({"PID", "Priority", "VB", "Line3", "GTIN", "NP", "Line6", "ProductName"})
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    static class ProjectListCreator {

        private int pid;
        private String priority;
        private String vb;
        private String gtin;
        private int np;
        private List<ExecutionModeList> executionModeList;

        public ProjectListCreator(String priority,String vb, String gtin, int np, List<ExecutionModeList> executionModeList){
            this.pid = np;
            this.priority = priority;
            this.vb = vb;
            this.gtin = gtin;
            this.np = np;
            this.executionModeList = executionModeList;
        }

    }

    static class ExecutionModeList{
        private String jid;
        private int duration;
        private List<ResourceRequirementList> resourceRequirementListList;
    }

    static class ResourceRequirementList{
        private String rid;
        private String requirement;
    }

    @JsonPropertyOrder({"EAN13", "Line1", "Line2", "Line3", "Line4", "Line5", "Line6", "ProductName"})
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
