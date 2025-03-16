/*
 * Copyright 2023-2025 Steinar Bang
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.assertj.db.type.AssertDbConnectionFactory;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import liquibase.exception.LiquibaseException;

class RatatoskrLiquibaseTest {
    DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();

    @Test
    void testCreateSchema() throws Exception {
        var ratatoskrLiquibase = new RatatoskrLiquibase();
        var datasource = createDataSource("ratatoskr");
        var assertjConnection = AssertDbConnectionFactory.of(datasource).create();

        ratatoskrLiquibase.createInitialSchema(datasource.getConnection());

        var accounts1 = assertjConnection.table("ratatoskr_accounts").build();
        assertThat(accounts1).exists().isEmpty();

        try(var connection = datasource.getConnection()) {
            addAccounts(connection);
        }

        var accounts2 = assertjConnection.table("ratatoskr_accounts").build();
        assertThat(accounts2).hasNumberOfRows(1);

        var actors1 = assertjConnection.table("actors").build();
        assertThat(actors1).exists().isEmpty();

        var actorId = 0;
        try(var connection = datasource.getConnection()) {
            actorId = addActor(
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
        }

        var actors2 = assertjConnection.table("actors").build();
        assertThat(actors2).exists().hasNumberOfRows(1).row(0)
            .value("actor_id").isEqualTo(actorId)
            .value("id").isEqualTo("https://kenzoishii.example.com")
            .value("preferred_username").isEqualTo("kenzoishii")
            .value("name").isEqualTo("石井健蔵")
            .value("summary").isEqualTo("この方はただの例です")
            .value("inbox").isEqualTo("https://kenzoishii.example.com/inbox.json")
            .value("following").isEqualTo("https://kenzoishii.example.com/following.json")
            .value("followers").isEqualTo("https://kenzoishii.example.com/followers.json")
            .value("liked").isEqualTo("https://kenzoishii.example.com/liked.json")
            .value("icon").isEqualTo("https://kenzoishii.example.com/image/165987aklre4");

        var anotherActorId = 0;
        try(var connection = datasource.getConnection()) {
            anotherActorId = addActor(
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
        }

        var followers1 = assertjConnection
            .request("select * from followers where followed=? and follower=?")
            .parameters(actorId, anotherActorId)
            .build();
        assertThat(followers1).hasNumberOfRows(1).row(0)
            .value("followed").isEqualTo(actorId)
            .value("follower").isEqualTo(anotherActorId);

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

    private void addFollower(Connection connection, int actorId, int anotherActorId) throws Exception {
        var sql = "insert into followers (followed, follower) values (?, ?)";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, actorId);
            statement.setInt(2, anotherActorId);
            statement.executeUpdate();
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
        var dataSource = createDataSource(dbname);
        return dataSource.getConnection();
    }

    private DataSource createDataSource(String dbname) throws SQLException {
        var properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:" + dbname + ";create=true");
        return derbyDataSourceFactory.createDataSource(properties);
    }

}
