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
package no.priv.bang.ratatoskr.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import no.priv.bang.ratatoskr.asvocabulary.Article;
import no.priv.bang.ratatoskr.asvocabulary.Group;
import no.priv.bang.ratatoskr.asvocabulary.Like;
import no.priv.bang.ratatoskr.asvocabulary.Link;
import no.priv.bang.ratatoskr.asvocabulary.LinkOrObjectList;
import no.priv.bang.ratatoskr.asvocabulary.Person;
import no.priv.bang.ratatoskr.db.liquibase.test.RatatoskrTestDbLiquibaseRunner;
import no.priv.bang.ratatoskr.services.RatatoskrException;
import no.priv.bang.ratatoskr.services.beans.CounterIncrementStepBean;
import no.priv.bang.ratatoskr.services.beans.LocaleBean;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.UserManagementService;
import static no.priv.bang.ratatoskr.services.RatatoskrConstants.*;

class RatatoskrServiceProviderTest {
    private static final Locale NB_NO = Locale.forLanguageTag("nb-no");

    private static DataSource datasource;

    @BeforeAll
    static void commonSetupForAllTests() throws Exception {
        var derbyDataSourceFactory = new DerbyDataSourceFactory();
        var properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:ratatoskr;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        var runner = new RatatoskrTestDbLiquibaseRunner();
        runner.activate();
        runner.prepare(datasource);
        // Pre-populate the database with some data
        var ratatoskr = new RatatoskrServiceProvider();
        ratatoskr.setDatasource(datasource);
        ratatoskr.setUseradmin(mock(UserManagementService.class));
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var johnd = Person.with()
            .id("http://localhost:8181/ratatoskr/as/actor/johnd")
            .preferredUsername("johnd")
            .name("John Doe")
            .summary("The common man")
            .icon("http://localhost:8181/ratatoskr/image/165987aklre4")
            .build();
        ratatoskr.addActor(johnd);
        var sally = Person.with()
            .id("https://sally.example.com")
            .preferredUsername("sally")
            .name("Sally Smith")
            .summary("Someone important")
            .inbox("https://sally.example.com/inbox.json")
            .following("https://sally.example.com/following.json")
            .followers("https://sally.example.com/followers.json")
            .liked("https://sally.example.com/liked.json")
            .icon("http://localhost:8181/ratatoskr/image/165987aklre6")
            .build();
        ratatoskr.addActor(sally);
    }

    @Test
    void testGetAccounts() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var accountsBefore = provider.getAccounts();
        assertThat(accountsBefore).isEmpty();
        assertThat(provider.getCounterIncrementStep("jad")).isEmpty();
        assertThat(provider.getCounter("jad")).isEmpty();
        var accountCreated = provider.lazilyCreateAccount("jad");
        assertTrue(accountCreated);
        var accountsAfter = provider.getAccounts();
        assertThat(accountsAfter).isNotEmpty();
        var defaultInitialCounterIncrementStepValue = 1;
        var counterIncrementStep = provider.getCounterIncrementStep("jad");
        assertThat(counterIncrementStep).isNotEmpty();
        assertEquals(defaultInitialCounterIncrementStepValue, counterIncrementStep.get().counterIncrementStep());
        var defaultInitialCounterValue = 0;
        var counter = provider.getCounter("jad");
        assertThat(counter).isNotEmpty();
        assertEquals(defaultInitialCounterValue, counter.get().counter());
        var secondAccountCreate = provider.lazilyCreateAccount("jad");
        assertFalse(secondAccountCreate);
        var accountsAfterSecondCreate = provider.getAccounts();
        assertThat(accountsAfterSecondCreate).isEqualTo(accountsAfter);
    }

    @Test
    void testGetAccountsWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var accounts = provider.getAccounts();
        assertThat(accounts).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testLazilyCreateAccountWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var accountCreated = provider.lazilyCreateAccount("jad");
        assertFalse(accountCreated);
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testThatRolesAreAddedIfMissing() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        verify(useradmin, times(2)).addRole(any());
    }

    @Test
    void testThatRolesAreNotAddedIfPresent() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var existingroles = Arrays.asList(
            Role.with().rolename(RATATOSKRUSER_ROLE).build(),
            Role.with().rolename(RATATOSKRADMIN_ROLE).build());
        when(useradmin.getRoles()).thenReturn(existingroles);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        verify(useradmin, never()).addRole(any());
    }

    @Test
    void testThatSomeRolesAreNotAddedIfNotAllRolesArePresent() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var existingroles = Arrays.asList(
            Role.with().rolename(RATATOSKRADMIN_ROLE).build());
        when(useradmin.getRoles()).thenReturn(existingroles);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        verify(useradmin, times(1)).addRole(any());
    }

    @Test
    void testAddActor() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var person = Person.with()
            .id("https://kenzoishii.example.com")
            .preferredUsername("kenzoishii")
            .name("石井健蔵")
            .summary("この方はただの例です")
            .inbox("https://kenzoishii.example.com/inbox.json")
            .following("https://kenzoishii.example.com/following.json")
            .followers("https://kenzoishii.example.com/followers.json")
            .liked("https://kenzoishii.example.com/liked.json")
            .icon("https://kenzoishii.example.com/image/165987aklre4")
            .build();

        var actor = provider.addActor(person);
        assertThat(actor).hasValue(person);
    }

    @Test
    void testAddActorWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var person = Person.with()
            .id("https://kenzoishii.example.com")
            .preferredUsername("kenzoishii")
            .name("石井健蔵")
            .summary("この方はただの例です")
            .inbox("https://kenzoishii.example.com/inbox.json")
            .following("https://kenzoishii.example.com/following.json")
            .followers("https://kenzoishii.example.com/followers.json")
            .liked("https://kenzoishii.example.com/liked.json")
            .icon("https://kenzoishii.example.com/image/165987aklre4")
            .build();

        var actor = provider.addActor(person);
        assertThat(actor).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindActorWithIdNotFound() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = provider.findActor("https://notfound.example.com");
        assertThat(actor).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindActorWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThrows(RatatoskrException.class, () -> provider.findActor("https://kenzoishii.example.com"));
    }

    @Test
    void testFindActorWithUsernameNotFound() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = provider.findActorWithUsername("notfound");
        assertThat(actor).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindActorWithUsernameSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThrows(RatatoskrException.class, () -> provider.findActorWithUsername("kenzoishii"));
    }

    @Test
    void testFindActorIdWithSQLExceptionThrow() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThrows(RatatoskrException.class, () -> provider.findActorId("kenzoishii"));
    }

    @Test
    void testFindActorIdWithNotFound() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        int actorId = provider.findActorId("notfound");
        assertThat(actorId).isEqualTo(-1);
    }

    @Test
    void testAddGroupWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var group = Group.with().name("Sample group").build();

        var addedGroup = provider.addGroup(group);
        assertThat(addedGroup).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindGroupWithNameNotFound() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var group = provider.findGroup("Not found");
        assertThat(group).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindGroupWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThrows(RatatoskrException.class, () -> provider.findGroup("Doesn't matter"));
    }

    @Test
    void testAddArticle() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var sally = provider.findActorWithUsername("sally").get();
        var article = Article.with()
            .id("https://sally.example.com/posts/123")
            .name("What a Crazy Day I Had")
            .content("<div>... you will never believe ...</div>")
            .attributedTo(Link.with().href(sally.id()).build())
            .build();
        var createdArticle = provider.addArticle(article);
        assertThat(createdArticle).hasValue(article);
    }

    @Test
    void testAddArticleWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = spy(datasource);
        when(mockDatasource.getConnection()).thenCallRealMethod().thenCallRealMethod().thenCallRealMethod().thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var sally = provider.findActorWithUsername("sally").get();
        var article = Article.with().attributedTo(Link.with().href(sally.id()).build()).build();

        assertThrows(RatatoskrException.class, () -> provider.addArticle(article));
    }

    @Test
    void testFindArticleWithNameNotFound() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var article = provider.findArticle("Not found");
        assertThat(article).isEmpty();
        assertThat(logservice.getLogmessages()).isEmpty();
    }

    @Test
    void testFindArticleWithSqlExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThrows(RatatoskrException.class, () -> provider.findArticle("Doesn't matter"));
    }

    @Test
    void testAddFollower() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var followers = provider.findFollowersWithUsername(username);
        assertThat(followers).isEmpty();
        var sally = provider.findActorWithUsername("sally").get();
        var updatedfollowers = provider.addFollowerToUsername(username, sally.id());
        assertThat(updatedfollowers).isNotEmpty().contains(sally);
    }

    @Test
    void testAddFollowerWithSQLExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockedDatasource = spy(datasource);
        when(mockedDatasource.getConnection()).thenCallRealMethod().thenCallRealMethod().thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockedDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var followers = provider.findFollowersWithUsername(username);
        assertThat(followers).isEmpty();
        var sallyId = provider.findActorWithUsername("sally").get().id();
        assertThrows(RatatoskrException.class, () -> provider.addFollowerToUsername(username, sallyId));
    }

    @Test
    void testFindFollowersWithSQLExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockedDatasource = mock(DataSource.class);
        when(mockedDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockedDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        assertThrows(RatatoskrException.class, () -> provider.findFollowersWithUsername(username));
    }

    @Test
    void testAddFollowed() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var following = provider.findFollowingWithUsername(username);
        assertThat(following).isEmpty();
        var sally = provider.findActorWithUsername("sally").get();
        var updatedfollowing = provider.addFollowedToUsername(username, sally.id());
        assertThat(updatedfollowing).isNotEmpty().contains(sally);
    }

    @Test
    void testAddFollowedWithSQLExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = spy(datasource);
        when(mockDatasource.getConnection()).thenCallRealMethod().thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var sallyId = provider.findActorWithUsername("sally").get().id();
        assertThrows(RatatoskrException.class, () -> provider.addFollowedToUsername(username, sallyId));
    }

    @Test
    void testfindFollowingWithUsernameWithSQLExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        assertThrows(RatatoskrException.class, () -> provider.findFollowingWithUsername(username));
    }

    @Test
    void testFindLikedWithUsernameWithDbError() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        assertThrows(RatatoskrException.class, () -> provider.findLikedWithUsername(username));
    }

    @Test
    void testAddLikes() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var sally = provider.findActorWithUsername("sally").get();
        var docId = "https://sally.example.com/posts/124";
        var article = Article.with()
            .id(docId)
            .name("What a Crazy Day I Had")
            .content("<div>... you will never believe ...</div>")
            .attributedTo(Link.with().href(sally.id()).build())
            .build();
        provider.addArticle(article);
        var group = provider.addGroup(Group.with().name("Project ZYX Working Group").build()).get();
        var like = Like.with()
            .summary("John liked Sally's note")
            .audience(group)
            .actor(Link.with().href("http://localhost:8181/ratatoskr/as/actor/johnd").build())
            .target(Link.with().href(docId).build())
            .build();
        var updatedLike = provider.addLikeToUsername(username, like);
        assertThat(updatedLike).isNotEmpty().contains(like);
    }

    @Test
    void testUserLikeArticle() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var sally = provider.findActor("https://sally.example.com").get();
        var audience = provider.addGroup(Group.with().name("Project XYZ Working Group").build()).get();
        var article = provider.addArticle(Article.with()
            .id("https://sally.example.com/posts/125")
            .name("What a Crazy Day I Had")
            .content("<div>... you will never believe ...</div>")
            .attributedTo(Link.with().href(sally.id()).build())
            .build()).get();
        var likes = provider.userLikeArticle(username, article, audience, null); // Duplicate code: remove later
        assertThat(likes).isNotEmpty();

        // Moved here from its own test to ensure that a like is present
        var likes2 = provider.findLikedWithUsername(username);
        assertThat(likes2).isNotEmpty();
    }

    @Test
    void testUserLikeArticleWithDbError() throws Exception {
        var logservice = new MockLogService();
        var mockDatasource = mock(DataSource.class);
        when(mockDatasource.getConnection()).thenThrow(SQLException.class);
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var article = Article.with().build();
        var group = Group.with().build();
        assertThrows(RatatoskrException.class, () -> provider.userLikeArticle("dummy", article, group, null));
    }

    @Test
    void testAddLikesWithNoLikeFound() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockedDatasource = mock(DataSource.class);
        var preparedStatement = mock(PreparedStatement.class);
        var results = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(results);
        var connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(mockedDatasource.getConnection()).thenReturn(connection);
        provider.setLogservice(logservice);
        provider.setDatasource(mockedDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var like = Like.with()
            .summary("John liked Sally's note")
            .actor(Link.with().href("http://localhost:8181/ratatoskr/as/actor/johnd").build())
            .build();
        var likes = provider.addLikeToUsername(username, like);
        assertThat(likes).isEmpty();
    }

    @Test
    void testAddLikesWithSQLExceptionThrown() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var mockedDatasource = mock(DataSource.class);
        when(mockedDatasource.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(mockedDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var username = "johnd";
        var like = Like.with()
            .summary("John liked Sally's note")
            .actor(Link.with().href("http://localhost:8181/ratatoskr/as/actor/johnd").build())
            .build();
        assertThrows(RatatoskrException.class, () -> provider.addLikeToUsername(username, like));
    }

    @Test
    void testListInbox() {
        var logservice = new MockLogService();
        var mockDatasource = mock(DataSource.class);
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = Person.with().build();
        assertThat(provider.listInbox(actor)).isEmpty();
    }

    @Test
    void testPostToInbox() {
        var logservice = new MockLogService();
        var mockDatasource = mock(DataSource.class);
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = Person.with().build();
        var message = Article.with().build();
        assertThat(provider.postToInbox(actor, message)).isEmpty();
    }

    @Test
    void testListOutbox() {
        var logservice = new MockLogService();
        var mockDatasource = mock(DataSource.class);
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = Person.with().build();
        assertThat(provider.listOutbox(actor)).isEmpty();
    }

    @Test
    void testPostToOutbox() {
        var logservice = new MockLogService();
        var mockDatasource = mock(DataSource.class);
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(mockDatasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        var actor = Person.with().build();
        var message = Article.with().build();
        assertThat(provider.postToOutbox(actor, message)).isEmpty();
    }

    @Test
    void testIncrementAndDecrement() {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        // Create new account with default values for counter and increment step
        provider.lazilyCreateAccount("on");
        var initialCounterIncrementStep = provider.getCounterIncrementStep("on").orElseThrow();
        var initialCounterValue = provider.getCounter("on").orElseThrow();

        // Set the increment step to the existing step value plus one
        var newIncrementStep = CounterIncrementStepBean.with()
            .username("on")
            .counterIncrementStep(initialCounterIncrementStep.counterIncrementStep() + 1)
            .build();
        var updatedIncrementStep = provider.updateCounterIncrementStep(newIncrementStep).orElseThrow();
        assertThat(updatedIncrementStep.counterIncrementStep()).isGreaterThan(initialCounterIncrementStep.counterIncrementStep());

        // Increment and verify the expected result
        var expectedIncrementedValue = initialCounterValue.counter() + updatedIncrementStep.counterIncrementStep();
        var incrementedValue = provider.incrementCounter("on").orElseThrow();
        assertEquals(expectedIncrementedValue, incrementedValue.counter());

        // Decrement and verify the expected result
        var decrementedValue = provider.decrementCounter("on").orElseThrow();
        assertEquals(initialCounterValue, decrementedValue);
    }

    @Test
    void testGetCounterIncrementStepWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var incrementStep = provider.getCounterIncrementStep("jad");
        assertThat(incrementStep).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testUpdateCounterIncrementStepWithSQLException() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var updatedIncrementStep = provider.updateCounterIncrementStep(CounterIncrementStepBean.with().build());
        assertThat(updatedIncrementStep).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testGetCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var counter = provider.getCounter("jad");
        assertThat(counter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testIncrementCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var incrementedCounter = provider.incrementCounter("jad");
        assertThat(incrementedCounter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDecrementCounterWithSQLExceptio() throws Exception {
        var logservice = new MockLogService();
        var useradmin = mock(UserManagementService.class);
        var provider = new RatatoskrServiceProvider();
        var datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        var decrementedCounter = provider.decrementCounter("jad");
        assertThat(decrementedCounter).isEmpty();
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDefaultLocale() {
        var ratatoskr = new RatatoskrServiceProvider();
        var useradmin = mock(UserManagementService.class);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertEquals(NB_NO, ratatoskr.defaultLocale());
    }

    @Test
    void testAvailableLocales() {
        var ratatoskr = new RatatoskrServiceProvider();
        var useradmin = mock(UserManagementService.class);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var locales = ratatoskr.availableLocales();
        assertThat(locales).isNotEmpty().contains(LocaleBean.with().locale(ratatoskr.defaultLocale()).build());
    }

    @Test
    void testDisplayTextsForDefaultLocale() {
        var ratatoskr = new RatatoskrServiceProvider();
        var useradmin = mock(UserManagementService.class);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var displayTexts = ratatoskr.displayTexts(ratatoskr.defaultLocale());
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayText() {
        var ratatoskr = new RatatoskrServiceProvider();
        var useradmin = mock(UserManagementService.class);
        ratatoskr.setUseradmin(useradmin);
        ratatoskr.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        var text1 = ratatoskr.displayText("hi", "nb_NO");
        assertEquals("Hei", text1);
        var text2 = ratatoskr.displayText("hi", "en_GB");
        assertEquals("Hi", text2);
        var text3 = ratatoskr.displayText("hi", "");
        assertEquals("Hei", text3);
        var text4 = ratatoskr.displayText("hi", null);
        assertEquals("Hei", text4);
    }

    @Test
    void testZonedDateTimeOrNull() throws Exception {
        var ratatoskr = new RatatoskrServiceProvider();
        var zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        var results = mock(ResultSet.class);
        when(results.getTimestamp(anyString())).thenReturn(Timestamp.from(zonedDateTime.toInstant()));
        when(results.wasNull()).thenReturn(true).thenReturn(false);

        assertThat(ratatoskr.zonedDateTimeOrNull(results, "somecol")).isNull();
        assertThat(ratatoskr.zonedDateTimeOrNull(results, "somecol")).isEqualTo(zonedDateTime);
    }

    @Test
    void testFindId() {
        var ratatoskr = new RatatoskrServiceProvider();

        var personId = "https://person.company.com";
        assertThat(ratatoskr.findId(Person.with().id(personId).build())).isEqualTo(personId);
        assertThat(ratatoskr.findId(Link.with().href(personId).build())).isEqualTo(personId);
        var list = new LinkOrObjectList(Collections.emptyList());
        assertThrows(RatatoskrException.class, () -> ratatoskr.findId(list));
    }

    @Test
    void testFindHref() {
        var ratatoskr = new RatatoskrServiceProvider();

        var personId = "https://person.company.com";
        assertThat(ratatoskr.findHref(Person.with().id(personId).build())).isNull();
        assertThat(ratatoskr.findHref(Link.with().href(personId).build())).isEqualTo(personId);
        assertThat(ratatoskr.findHref(new LinkOrObjectList(Collections.emptyList()))).isNull();
    }

    @Test
    void testFindName() {
        var ratatoskr = new RatatoskrServiceProvider();

        assertThat(ratatoskr.findName(null)).isNull();
        assertThat(ratatoskr.findName(Person.with().name("Person name").build())).isEqualTo("Person name");
        assertThat(ratatoskr.findName(Link.with().name("Link name").build())).isEqualTo("Link name");
        assertThat(ratatoskr.findName(new LinkOrObjectList(Collections.emptyList()))).isNull();
    }

}
