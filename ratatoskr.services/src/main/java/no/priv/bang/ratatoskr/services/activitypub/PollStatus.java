/*
 * Copyright 2025 Steinar Bang
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
package no.priv.bang.ratatoskr.services.activitypub;

import java.time.ZonedDateTime;
import java.util.List;

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObjectType;
import no.priv.bang.ratatoskr.asvocabulary.Person;

public record PollStatus(
    ActivityStreamObjectType type,
    String id,
    String content,
    String name,
    String summary,
    Boolean sensitive,
    Status inReplyTo,
    ZonedDateTime published,
    String url,
    Person authoredBy,
    List<Person> to,
    List<Person> cc,
    List<Tag> tags,
    List<Document> attachments,
    StatusCollection replies,
    ActivityCollection likes,
    AnnounceCollection shares,
    ZonedDateTime endTime,
    Boolean closed,
    PersonCollection votersCount,
    List<PollItem> oneOf,
    List<PollItem> anyOf) implements Status
{

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String content;
        private String name;
        private String summary;
        private Boolean sensitive;
        private Status inReplyTo;
        private ZonedDateTime published;
        private String url;
        private Person authoredBy;
        private List<Person> to;
        private List<Person> cc;
        private List<Tag> tags;
        private List<Document> attachments;
        private StatusCollection replies;
        private ActivityCollection likes;
        private AnnounceCollection shares;
        private ZonedDateTime endTime;
        private Boolean closed;
        private PersonCollection votersCount;
        private List<PollItem> oneOf;
        private List<PollItem> anyOf;

        public PollStatus build() {
            return new PollStatus(
                ActivityStreamObjectType.Question,
                id,
                content,
                name,
                summary,
                sensitive,
                inReplyTo,
                published,
                url,
                authoredBy,
                to,
                cc,
                tags,
                attachments,
                replies,
                likes,
                shares,
                endTime,
                closed,
                votersCount,
                oneOf,
                anyOf);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder sensitive(boolean sensitive) {
            this.sensitive = sensitive;
            return this;
        }

        public Builder inReplyTo(Status inReplyTo) {
            this.inReplyTo = inReplyTo;
            return this;
        }

        public Builder published(ZonedDateTime published) {
            this.published = published;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder authoredBy(Person authoredBy) {
            this.authoredBy = authoredBy;
            return this;
        }

        public Builder to(List<Person> to) {
            this.to = to;
            return this;
        }

        public Builder cc(List<Person> cc) {
            this.cc = cc;
            return this;
        }

        public Builder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder attachments(List<Document> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder replies(StatusCollection replies) {
            this.replies = replies;
            return this;
        }

        public Builder likes(ActivityCollection likes) {
            this.likes = likes;
            return this;
        }

        public Builder shares(AnnounceCollection shares) {
            this.shares = shares;
            return this;
        }

        public Builder endTime(ZonedDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder closed(boolean closed) {
            this.closed = closed;
            return this;
        }

        public Builder votersCount(PersonCollection votersCount) {
            this.votersCount = votersCount;
            return this;
        }

        public Builder oneOf(List<PollItem> oneOf) {
            this.oneOf = oneOf;
            return this;
        }

        public Builder anyOf(List<PollItem> anyOf) {
            this.anyOf = anyOf;
            return this;
        }

    }

}
