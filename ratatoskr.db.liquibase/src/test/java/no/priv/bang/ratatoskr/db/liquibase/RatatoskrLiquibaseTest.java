/*
 * Copyright 2023-2024 Steinar Bang
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
package no.priv.bang.ratatoskr.db.liquibase;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import liquibase.exception.LiquibaseException;

class RatatoskrLiquibaseTest {
    DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();

    @Test
    void testCreateSchema() throws Exception {
        var ratatoskrLiquibase = new RatatoskrLiquibase();

        ratatoskrLiquibase.createInitialSchema(createConnection("ratatoskr"));

        try(var connection = createConnection("ratatoskr")) {
            addAccounts(connection);
            assertAccounts(connection);
        }

        ratatoskrLiquibase.updateSchema(createConnection("ratatoskr"));
    }

    @Test
    void testCreateSchemaAndFail() throws Exception {
        var connection = spy(createConnection("ratatoskr1"));
        // A Derby JDBC connection wrapped in a Mockito spy() fails om Connection.setAutoClosable()

        var ratatoskrLiquibase = new RatatoskrLiquibase();

        var ex = assertThrows(
            LiquibaseException.class,
            () -> ratatoskrLiquibase.createInitialSchema(connection));
        assertThat(ex.getMessage()).startsWith("java.sql.SQLException: Cannot set Autocommit On when in a nested connection");
    }

    @Test
    void testCreateSchemaAndFailOnConnectionClose() throws Exception {
        var connection = spy(createConnection("ratatoskr2"));
        doNothing().when(connection).setAutoCommit(anyBoolean());
        doThrow(Exception.class).when(connection).close();

        var ratatoskrLiquibase = new RatatoskrLiquibase();

        var ex = assertThrows(
            LiquibaseException.class,
            () -> ratatoskrLiquibase.createInitialSchema(connection));
        assertThat(ex.getMessage()).startsWith("java.lang.Exception");
    }

    private void addAccounts(Connection connection) throws Exception {
        addAccount(connection, "admin");
    }

    private void assertAccounts(Connection connection) throws Exception {
        var sql = "select count(*) from ratatoskr_accounts";
        try(var statement = connection.prepareStatement(sql)) {
            try(var results = statement.executeQuery()) {
                if (results.next()) {
                    var count = results.getInt(1);
                    assertEquals(1, count);
                }
            }
        }
    }

    private int addAccount(Connection connection, String username) throws Exception {
        var sql = "insert into ratatoskr_accounts (username) values (?)";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        return findAccountId(connection, username);
    }

    private int findAccountId(Connection connection, String username) throws Exception {
        var sql = "select account_id from ratatoskr_accounts where username=?";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try(var results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private Connection createConnection(String dbname) throws Exception {
        var properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:" + dbname + ";create=true");
        var dataSource = derbyDataSourceFactory.createDataSource(properties);
        return dataSource.getConnection();
    }

}
