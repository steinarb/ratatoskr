/*
 * Copyright 2023 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.ratatoskr.db.liquibase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

class RatatoskrTestDbLiquibaseRunnerTest {

    @Test
    void testCreateAndVerifySomeDataInSomeTables() throws Exception {
        DataSource datasource = createDataSource("ratatoskr");

        RatatoskrTestDbLiquibaseRunner runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        runner.prepare(datasource);
        assertAccounts(datasource);
    }

    @Test
    void testFailInGettingConnectionWhenCreatingInitialSchema() throws Exception {
        DataSource datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenThrow(new SQLException("Failed to get connection"));

        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to get connection");
    }

    @Test
    void testFailWhenCreatingInitialSchema() throws Exception {
        Connection connection = spy(createDataSource("ratatoskr1").getConnection());
        DataSource datasource = mock(DataSource.class);
        when(datasource.getConnection()).thenReturn(connection);

        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Error creating ratatoskr test database schema");
    }

    @Test
    void testFailWhenAddingMockData() throws Exception {
        var connection = spy(createDataSource("ratatoskr1").getConnection());
        DataSource datasource = spy(createDataSource("ratatoskr2"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenReturn(connection);

        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Error inserting ratatoskr test database mock data");
    }

    @Test
    void testFailWhenGettingConnectionForUpdatingSchema() throws Exception {
        DataSource datasource = spy(createDataSource("ratatoskr3"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenCallRealMethod()
            .thenThrow(new SQLException("Failed to get connection"));

        RatatoskrTestDbLiquibaseRunner runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Failed to get connection");
    }

    @Test
    void testFailWhenUpdatingSchema() throws Exception {
        var connection = spy(createDataSource("ratatoskr4").getConnection());
        DataSource datasource = spy(createDataSource("ratatoskr4"));
        when(datasource.getConnection())
            .thenCallRealMethod()
            .thenCallRealMethod()
            .thenReturn(connection);

        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        var e = assertThrows(
            SQLException.class,
            () -> runner.prepare(datasource));
        assertThat(e.getMessage()).startsWith("Error updating ratatoskr test database schema");
    }

    private void assertAccounts(DataSource datasource) throws Exception {
        int resultcount = 0;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from ratatoskr_accounts")) {
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        ++resultcount;
                    }
                }
            }
        }
        assertEquals(0, resultcount);
    }

    private DataSource createDataSource(String dbname) throws SQLException {
        DataSourceFactory dataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:" + dbname + ";create=true");
        DataSource datasource = dataSourceFactory.createDataSource(properties);
        return datasource;
    }

}
