package org.acme.projectjobschedule.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class  ImportExecutionModesTable {

    public ImportExecutionModesTable() {
        String jdbcUrl = "jdbc:sqlite:src/main/resources/executionModes.db";
        String username = "username";
        String password = "password";

        // Connect to the database
 try (var conn = DriverManager.getConnection(jdbcUrl)) {
            System.out.println("Connection to sqlite has been established.");

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    }


