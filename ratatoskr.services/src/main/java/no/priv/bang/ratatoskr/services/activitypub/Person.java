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

public record Person(
    Object context,
    ActivityStreamObjectType type,
    String id,
    String preferredUsername,
    String name,
    String summary,
    String url,
    String icon,
    String image,
    Boolean manuallyApprovesFollowers,
    Boolean discoverable,
    Boolean indexable,
    PublicKey publicKey,
    String inbox,
    String outbox,
    String following,
    String followers,
    String liked,
    StatusCollection featured,
    List<PropertyValue> attachment,
    String alsoKnownAs,
    ZonedDateTime created,
    Boolean memorial,
    Boolean suspended,
    List<String> attributionDomains)
{

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Person person) {
        return new Builder()
            .id(person.id())
            .preferredUsername(person.preferredUsername())
            .name(person.name())
            .summary(person.summary())
            .url(person.url())
            .icon(person.icon())
            .image(person.image())
            .manuallyApprovesFollowers(person.manuallyApprovesFollowers())
            .discoverable(person.discoverable())
            .indexable(person.indexable())
            .publicKey(person.publicKey())
            .inbox(person.inbox())
            .outbox(person.outbox())
            .followers(person.followers())
            .following(person.following())
            .liked(person.liked())
            .featured(person.featured())
            .attachment(person.attachment())
            .alsoKnownAs(person.alsoKnownAs())
            .created(person.created())
            .memorial(person.memorial())
            .suspended(person.suspended())
            .attributionDomains(person.attributionDomains());
    }

    public static class Builder {
        private String id;
        private String preferredUsername;
        private String name;
        private String summary;
        private String url;
        private String icon;
        private String image;
        private boolean manuallyApprovesFollowers;
        private boolean discoverable;
        private boolean indexable;
        private PublicKey publicKey;
        private String inbox;
        private String outbox;
        private String following;
        private String followers;
        private String liked;
        private StatusCollection featured;
        private List<PropertyValue> attachment;
        private String alsoKnownAs;
        private ZonedDateTime created;
        private boolean memorial;
        private boolean suspended;
        private List<String> attributionDomains;

        public Person build() {
            return new Person(
                "https://www.w3.org/ns/activitystreams",
                ActivityStreamObjectType.Person,
                id,
                preferredUsername,
                name,
                summary,
                url,
                icon,
                image,
                manuallyApprovesFollowers,
                discoverable,
                indexable,
                publicKey,
                inbox,
                outbox,
                following,
                followers,
                liked,
                featured,
                attachment,
                alsoKnownAs,
                created,
                memorial,
                suspended,
                attributionDomains);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder preferredUsername(String preferredUsername) {
            this.preferredUsername = preferredUsername;
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

        public Builder url(String url) {
            this.url = icon;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder manuallyApprovesFollowers(boolean manuallyApprovesFollowers) {
            this.manuallyApprovesFollowers = manuallyApprovesFollowers;
            return this;
        }

        public Builder discoverable(boolean discoverable) {
            this.discoverable = discoverable;
            return this;
        }

        public Builder indexable(boolean indexable) {
            this.indexable = indexable;
            return this;
        }

        public Builder publicKey(PublicKey publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder inbox(String inbox) {
            this.inbox = inbox;
            return this;
        }

        public Builder outbox(String outbox) {
            this.outbox = outbox;
            return this;
        }

        public Builder following(String following) {
            this.following = following;
            return this;
        }

        public Builder followers(String followers) {
            this.followers = followers;
            return this;
        }

        public Builder liked(String liked) {
            this.liked = liked;
            return this;
        }

        public Builder featured(StatusCollection featured) {
            this.featured = featured;
            return this;
        }

        public Builder attachment(List<PropertyValue> attachment) {
            this.attachment = attachment;
            return this;
        }

        public Builder alsoKnownAs(String alsoKnownAs) {
            this.alsoKnownAs = alsoKnownAs;
            return this;
        }

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder memorial(boolean memorial) {
            this.memorial = memorial;
            return this;
        }

        public Builder suspended(boolean suspended) {
            this.suspended = suspended;
            return this;
        }

        public Builder attributionDomains(List<String> attributionDomains) {
            this.attributionDomains = attributionDomains;
            return this;
        }

    }
}
