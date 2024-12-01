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
            var actorId = addActor(
                connection,
                "https://kenzoishii.example.com",
                "kenzoishii",
                "石井健蔵",
                "この方はただの例です",
                "https://kenzoishii.example.com/inbox.json",
                "https://kenzoishii.example.com/following.json",
                "https://kenzoishii.example.com/followers.json",
                "https://kenzoishii.example.com/liked.json",
                "https://kenzoishii.example.com/image/165987aklre4");
            assertActor(
                connection,
                actorId,
                "https://kenzoishii.example.com",
                "kenzoishii",
                "石井健蔵",
                "この方はただの例です",
                "https://kenzoishii.example.com/inbox.json",
                "https://kenzoishii.example.com/following.json",
                "https://kenzoishii.example.com/followers.json",
                "https://kenzoishii.example.com/liked.json",
                "https://kenzoishii.example.com/image/165987aklre4");
            var anotherActorId = addActor(
                connection,
                "https://sally.example.com",
                "sally",
                "Sally Smith",
                "Someone important",
                "https://sally.example.com/inbox.json",
                "https://sally.example.com/following.json",
                "https://sally.example.com/followers.json",
                "https://sally.example.com/liked.json",
                "http://localhost:8181/ratatoskr/image/165987aklre6");
            addFollower(connection, actorId, anotherActorId);
            assertFollower(connection, actorId, anotherActorId);
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

    private int addActor(Connection connection, String id, String preferredUsername, String name, String summary, String inbox, String following, String followers, String liked, String icon) throws Exception {
        var sql = "insert into actors (id, preferred_username, name, summary, inbox, following, followers, liked, icon) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, preferredUsername);
            statement.setString(3, name);
            statement.setString(4, summary);
            statement.setString(5, inbox);
            statement.setString(6, following);
            statement.setString(7, followers);
            statement.setString(8, liked);
            statement.setString(9, icon);
            statement.executeUpdate();
        }

        return findActorId(connection, id);
    }

    private void assertActor(Connection connection, int actorId, String id, String preferredUsername, String name, String summary, String inbox, String following, String followers, String liked, String icon) throws Exception {
        var sql = "select * from actors where actor_id=?";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, actorId);
            try(var results = statement.executeQuery()) {
                while(results.next()) {
                    assertThat(results.getString("id")).isEqualTo(id);
                    assertThat(results.getString("preferred_username")).isEqualTo(preferredUsername);
                    assertThat(results.getString("name")).isEqualTo(name);
                    assertThat(results.getString("summary")).isEqualTo(summary);
                    assertThat(results.getString("inbox")).isEqualTo(inbox);
                    assertThat(results.getString("following")).isEqualTo(following);
                    assertThat(results.getString("followers")).isEqualTo(followers);
                    assertThat(results.getString("liked")).isEqualTo(liked);
                    assertThat(results.getString("icon")).isEqualTo(icon);
                }
            }
        }
    }

    private void addFollower(Connection connection, int actorId, int anotherActorId) throws Exception {
        var sql = "insert into followers (followed, follower) values (?, ?)";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, actorId);
            statement.setInt(2, anotherActorId);
            statement.executeUpdate();
        }
    }

    private void assertFollower(Connection connection, int actorId, int anotherActorId) throws Exception {
        var sql = "select * from followers where followed=? and follower=?";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, actorId);
            statement.setInt(2, anotherActorId);
            try(var results = statement.executeQuery()) {
                assertTrue(results.next());
            }
        }
    }

    private int findActorId(Connection connection, String id) throws Exception {
        var sql = "select actor_id from actors where id=?";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try(var results = statement.executeQuery()) {
                while(results.next()) {
                    return results.getInt("actor_id");
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
