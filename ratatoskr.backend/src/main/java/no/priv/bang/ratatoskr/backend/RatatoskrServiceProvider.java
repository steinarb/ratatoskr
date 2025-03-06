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

import static no.priv.bang.ratatoskr.services.RatatoskrConstants.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObject;
import no.priv.bang.ratatoskr.asvocabulary.Actor;
import no.priv.bang.ratatoskr.asvocabulary.Link;
import no.priv.bang.ratatoskr.asvocabulary.LinkOrObject;
import no.priv.bang.ratatoskr.asvocabulary.Person;
import no.priv.bang.ratatoskr.services.RatatoskrException;
import no.priv.bang.ratatoskr.services.RatatoskrService;
import no.priv.bang.ratatoskr.services.beans.Account;
import no.priv.bang.ratatoskr.services.beans.CounterBean;
import no.priv.bang.ratatoskr.services.beans.CounterIncrementStepBean;
import no.priv.bang.ratatoskr.services.beans.LocaleBean;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.UserManagementService;

@Component(service=RatatoskrService.class, immediate=true, property= { "defaultlocale=nb_NO" })
public class RatatoskrServiceProvider implements RatatoskrService {

    private static final String DISPLAY_TEXT_RESOURCES = "i18n.Texts";
    private Logger logger;
    private DataSource datasource;
    private UserManagementService useradmin;
    private Locale defaultLocale;

    @Reference
    public void setLogservice(LogService logservice) {
        this.logger = logservice.getLogger(RatatoskrServiceProvider.class);
    }

    @Reference(target = "(osgi.jndi.service.name=jdbc/ratatoskr)")
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        this.useradmin = useradmin;
    }

    @Activate
    public void activate(Map<String, Object> config) {
        defaultLocale = Locale.forLanguageTag(((String) config.get("defaultlocale")).replace('_', '-'));
        addRolesIfNotpresent();
    }

    @Override
    public boolean lazilyCreateAccount(String username) {
        try(var connection = datasource.getConnection()) {
            var accountid = findAccount(connection, username);

            if (accountid != -1) {
                return false;
            }

            try(var createAccount = connection.prepareStatement("insert into ratatoskr_accounts (username) values (?)")) {
                createAccount.setString(1, username);
                createAccount.executeUpdate();
            }

            accountid = findAccount(connection, username);
            try(var createIncrementStep = connection.prepareStatement("insert into counter_increment_steps (account_id) values (?)")) {
                createIncrementStep.setInt(1, accountid);
                createIncrementStep.executeUpdate();
            }

            try(var createCounter = connection.prepareStatement("insert into counters (account_id) values (?)")) {
                createCounter.setInt(1, accountid);
                createCounter.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            logger.warn("Failed to create ratatoskr account for username \"{}\"", username, e);
        }

        return false;
    }

    @Override
    public List<Account> getAccounts() {
        var accounts = new ArrayList<Account>();
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.createStatement()) {
                try(var results = statement.executeQuery("select account_id, username from ratatoskr_accounts")) {
                    while(results.next()) {
                        var accountId = results.getInt(1);
                        var username = results.getString(2);
                        var user = useradmin.getUser(username);
                        var account = Account.with().accountId(accountId).user(user).build();
                        accounts.add(account);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Ingen ratatoskr", e);
        }

        return accounts;
    }

    @Override
    public Optional<Actor> addActor(Actor person) {
        var sql = "insert into actors (id, preferred_username, name, summary, inbox, following, followers, liked, icon) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, person.id());
                statement.setString(2, person.preferredUsername());
                statement.setString(3, person.name());
                statement.setString(4, person.summary());
                statement.setString(5, person.inbox());
                statement.setString(6, person.following());
                statement.setString(7, person.followers());
                statement.setString(8, person.liked());
                statement.setString(9, findHref(person.icon()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            return Optional.empty();
        }

        return findActor(person.id());
    }

    @Override
    public Optional<Actor> findActor(String id) {
        var sql = "select id, preferred_username, name, summary, inbox, following, followers, liked, icon from actors where id=?";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, id);
                try(var results = statement.executeQuery()) {
                    while(results.next()) {
                        return unpackPerson(results);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Unable to fetch actor", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Actor> findActorWithUsername(String username) {
        var sql = "select id, preferred_username, name, summary, inbox, following, followers, liked, icon from actors where preferred_username=?";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try(var results = statement.executeQuery()) {
                    while(results.next()) {
                        return unpackPerson(results);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Unable to find actor using username", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Actor> findFollowersWithUsername(String username) {
        var list = new ArrayList<Actor>();
        var sql = "select a2.id, a2.preferred_username, a2.name, a2.summary, a2.inbox, a2.following, a2.followers, a2.liked, a2.icon from actors a join followers f on a.actor_id=f.followed join actors a2 on f.follower=a2.actor_id where a.preferred_username=?";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try(var results = statement.executeQuery()) {
                    while(results.next()) {
                        unpackPerson(results).ifPresent(list::add);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Error fetching followers list", e);
        }

        return list;
    }

    @Override
    public List<Actor> addFollowerToUsername(String username, String id) {
        var sql = "insert into followers (followed, follower) values ((select actor_id from actors where preferred_username=?), (select actor_id from actors where id=?))";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Error adding to followers list", e);
        }

        return findFollowersWithUsername(username);
    }

    @Override
    public List<Actor> findFollowingWithUsername(String username) {
        var list = new ArrayList<Actor>();
        var sql = "select a2.id, a2.preferred_username, a2.name, a2.summary, a2.inbox, a2.following, a2.followers, a2.liked, a2.icon from actors a join following f on a.actor_id=f.followed join actors a2 on f.follower=a2.actor_id where a.preferred_username=?";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try(var results = statement.executeQuery()) {
                    while(results.next()) {
                        unpackPerson(results).ifPresent(list::add);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Error fetching following list", e);
        }

        return list;
    }

    @Override
    public List<Actor> addFollowedToUsername(String username, String id) {
        var sql = "insert into following (followed, follower) values ((select actor_id from actors where preferred_username=?), (select actor_id from actors where id=?))";
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatatoskrException("Error adding to following list", e);
        }

        return findFollowingWithUsername(username);
    }

    private Optional<Actor> unpackPerson(ResultSet results) throws SQLException {
        return Optional.of(Person.with()
            .id(results.getString("id"))
            .preferredUsername(results.getString("preferred_username"))
            .name(results.getString("name"))
            .summary(results.getString("summary"))
            .inbox(results.getString("inbox"))
            .following(results.getString("following"))
            .followers(results.getString("followers"))
            .liked(results.getString("liked"))
            .icon(results.getString("icon"))
            .build());
    }

    @Override
    public List<ActivityStreamObject> listInbox(Actor actor) {
        return Collections.emptyList();
    }

    @Override
    public List<ActivityStreamObject> postToInbox(Actor actor, ActivityStreamObject message) {
        return Collections.emptyList();
    }

    @Override
    public List<ActivityStreamObject> listOutbox(Actor actor) {
        return Collections.emptyList();
    }

    @Override
    public List<ActivityStreamObject> postToOutbox(Actor actor, ActivityStreamObject message) {
        return Collections.emptyList();
    }

    @Override
    public Optional<CounterIncrementStepBean> getCounterIncrementStep(String username) {
        try(var connection = datasource.getConnection()) {
            var counterIncrementStep = findCounterIncrementStep(connection, username);
            if (counterIncrementStep != null) {
                var bean = CounterIncrementStepBean.with()
                    .username(username)
                    .counterIncrementStep(counterIncrementStep)
                    .build();
                return Optional.of(bean);
            }
        } catch (SQLException e) {
            logger.error("No increment steps could be found for user \"{}\"", username, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CounterIncrementStepBean> updateCounterIncrementStep(CounterIncrementStepBean updatedIncrementStep) {
        var username = updatedIncrementStep.username();
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.prepareStatement("update counter_increment_steps set counter_increment_step=? where account_id in (select account_id from ratatoskr_accounts where username=?)")) {
                statement.setInt(1, updatedIncrementStep.counterIncrementStep());
                statement.setString(2, username);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Unable to update increment step for user \"{}\"", username, e);
            return Optional.empty();
        }

        return getCounterIncrementStep(username);
    }

    @Override
    public Optional<CounterBean> getCounter(String username) {
        try(var connection = datasource.getConnection()) {
            return findAndCreateCounterBean(connection, username);
        } catch (SQLException e) {
            logger.error("No counter could be found for user \"{}\"", username, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CounterBean> incrementCounter(String username) {
        try(var connection = datasource.getConnection()) {
            var incrementStep = findCounterIncrementStep(connection, username);
            var counter = findCounter(connection, username);

            try(var statement = connection.prepareStatement("update counters set counter=? where account_id in (select account_id from ratatoskr_accounts where username=?)")) {
                statement.setInt(1, counter + incrementStep);
                statement.setString(2, username);
                statement.executeUpdate();
            }

            return findAndCreateCounterBean(connection, username);
        } catch (SQLException e) {
            logger.warn("Failed to increment counter for user \"{}\"", username, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CounterBean> decrementCounter(String username) {
        try(var connection = datasource.getConnection()) {
            var incrementStep = findCounterIncrementStep(connection, username);
            var counter = findCounter(connection, username);

            try(var statement = connection.prepareStatement("update counters set counter=? where account_id in (select account_id from ratatoskr_accounts where username=?)")) {
                statement.setInt(1, counter - incrementStep);
                statement.setString(2, username);
                statement.executeUpdate();
            }

            return findAndCreateCounterBean(connection, username);
        } catch (SQLException e) {
            logger.warn("Failed to decrement counter for user \"{}\"", username, e);
        }

        return Optional.empty();
    }

    @Override
    public Locale defaultLocale() {
        return defaultLocale;
    }

    @Override
    public List<LocaleBean> availableLocales() {
        return Arrays.asList(Locale.forLanguageTag("nb-NO"), Locale.UK).stream().map(l -> LocaleBean.with().locale(l).build()).toList();
    }

    @Override
    public Map<String, String> displayTexts(Locale locale) {
        return transformResourceBundleToMap(locale);
    }

    @Override
    public String displayText(String key, String locale) {
        var active = locale == null || locale.isEmpty() ? defaultLocale : Locale.forLanguageTag(locale.replace('_', '-'));
        var bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, active);
        return bundle.getString(key);
    }

    private int findAccount(Connection connection, String username) throws SQLException {
        try(var findAccount = connection.prepareStatement("select account_id from ratatoskr_accounts where username=?")) {
            findAccount.setString(1, username);
            try(var results = findAccount.executeQuery()) {
                while (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private Integer findCounterIncrementStep(Connection connection, String username) throws SQLException {
        try(var statement = connection.prepareStatement("select counter_increment_step from counter_increment_steps c join ratatoskr_accounts a on c.account_id=a.account_id where a.username=?")) {
            statement.setString(1, username);
            try(var results = statement.executeQuery()) {
                while(results.next()) {
                    return results.getInt("counter_increment_step");
                }
            }
        }

        return null;
    }

    private Integer findCounter(Connection connection, String username) throws SQLException {
        try(var statement = connection.prepareStatement("select counter from counters c join ratatoskr_accounts a on c.account_id=a.account_id where a.username=?")) {
            statement.setString(1, username);
            try(var results = statement.executeQuery()) {
                while(results.next()) {
                    return results.getInt("counter");
                }
            }
        }

        return null;
    }

    private Optional<CounterBean> findAndCreateCounterBean(Connection connection, String username) throws SQLException {
        var counter = findCounter(connection, username);
        return counter != null ?
            Optional.of(CounterBean.with().counter(counter).build()) :
            Optional.empty();
    }

    private void addRolesIfNotpresent() {
        var ratatoskroles = Map.of(
            RATATOSKRUSER_ROLE, "User of activitypub server ratatoskr",
            RATATOSKRADMIN_ROLE, "Administrator of activitypub server ratatoskr");
        var existingroles = useradmin.getRoles().stream().map(Role::rolename).collect(Collectors.toSet());
        ratatoskroles.entrySet().stream()
            .filter(r -> !existingroles.contains(r.getKey()))
            .forEach(r ->  useradmin.addRole(Role.with().id(-1).rolename(r.getKey()).description(r.getValue()).build()));
    }

    private String findHref(LinkOrObject icon) {
        return switch (icon) {
            case Link link -> link.href();
            default -> null;
        };
    }

    Map<String, String> transformResourceBundleToMap(Locale locale) {
        var map = new HashMap<String, String>();
        var bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, locale);
        var keys = bundle.getKeys();
        while(keys.hasMoreElements()) {
            var key = keys.nextElement();
            map.put(key, bundle.getString(key));
        }

        return map;
    }

}
