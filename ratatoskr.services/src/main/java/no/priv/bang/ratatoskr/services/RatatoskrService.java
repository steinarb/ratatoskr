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
package no.priv.bang.ratatoskr.services;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObject;
import no.priv.bang.ratatoskr.asvocabulary.Actor;
import no.priv.bang.ratatoskr.asvocabulary.Article;
import no.priv.bang.ratatoskr.asvocabulary.Group;
import no.priv.bang.ratatoskr.asvocabulary.Like;
import no.priv.bang.ratatoskr.services.beans.Account;
import no.priv.bang.ratatoskr.services.beans.CounterBean;
import no.priv.bang.ratatoskr.services.beans.CounterIncrementStepBean;
import no.priv.bang.ratatoskr.services.beans.LocaleBean;

public interface RatatoskrService {

    public List<Account> getAccounts();

    Optional<Actor> addActor(Actor person);

    Optional<Actor> findActor(String id);

    public Optional<Actor> findActorWithUsername(String username);

    Optional<Group> addGroup(Group group);

    Optional<Article> addArticle(Article article);

    Optional<Article> findArticle(String id);

    List<Actor> findFollowersWithUsername(String username);

    List<Actor> addFollowerToUsername(String username, String id);

    List<Actor> findFollowingWithUsername(String username);

    List<Actor> addFollowedToUsername(String username, String id);

    List<Like> findLikedWithUsername(String username);

    List<Like> addLikeToUsername(String username, Like like);

    List<Like> userLikeArticle(String username, Article article, Group audience, String localWebContext);

    List<ActivityStreamObject> listInbox(Actor actor);

    List<ActivityStreamObject> postToInbox(Actor actor, ActivityStreamObject message);

    List<ActivityStreamObject> listOutbox(Actor actor);

    List<ActivityStreamObject> postToOutbox(Actor actor, ActivityStreamObject message);

    public Optional<CounterIncrementStepBean> getCounterIncrementStep(String username);

    public Optional<CounterIncrementStepBean> updateCounterIncrementStep(CounterIncrementStepBean cupdatedIncrementStep);

    public Optional<CounterBean> getCounter(String username);

    public Optional<CounterBean> incrementCounter(String username);

    public Optional<CounterBean> decrementCounter(String username);

    Locale defaultLocale();

    List<LocaleBean> availableLocales();

    public Map<String, String> displayTexts(Locale locale);

    public String displayText(String key, String locale);

    public boolean lazilyCreateAccount(String username);

}
