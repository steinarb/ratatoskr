/*
 * Copyright 2024-2025 Steinar Bang
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
package no.priv.bang.ratatoskr.asvocabulary;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public sealed interface Collection extends ActivityStreamObject permits CollectionPage, CollectionRecord, OrderedCollection {
    int totalItems();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    List<LinkOrObject> items();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject current(); // actually just CollectionPage or Link, but hard to do
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject first(); // actually just CollectionPage or Link, but hard to do
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject last(); // actually just CollectionPage or Link, but hard to do
    static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private Object context;
        private String id;
        private String name;
        private Map<String, String> nameMap;
        private String summary;
        private Map<String, String> summaryMap;
        private String content;
        private Map<String, String> contentMap;
        private String mediaType;
        private List<Link> url;
        private LinkOrObject attributedTo;
        private String duration;
        private ZonedDateTime startTime;
        private ZonedDateTime endTime;
        private ZonedDateTime published;
        private ZonedDateTime updated;
        private LinkOrObject attachment;
        private LinkOrObject audience;
        private LinkOrObject to;
        private LinkOrObject bcc;
        private LinkOrObject bto;
        private LinkOrObject cc;
        private LinkOrObject generator;
        private LinkOrObject icon;
        private LinkOrObject image;
        private LinkOrObject inReplyTo;
        private LinkOrObject location;
        private LinkOrObject preview;
        private Collection replies;
        private LinkOrObject tag;
        private int totalItems;
        private List<LinkOrObject> items;
        private LinkOrObject current;
        private LinkOrObject first;
        private LinkOrObject last;

        public Collection build() {
            return new CollectionRecord(
                context,
                ActivityStreamObjectType.Collection,
                id,
                name,
                nameMap,
                summary,
                summaryMap,
                content,
                contentMap,
                mediaType,
                url,
                attributedTo,
                duration,
                startTime,
                endTime,
                published,
                updated,
                attachment,
                audience,
                to,
                bcc,
                bto,
                cc,
                generator,
                icon,
                image,
                inReplyTo,
                location,
                preview,
                replies,
                tag,
                totalItems,
                items,
                current,
                first,
                last
            );
        }

        public Builder context(Object context) {
            this.context = context;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nameMap(Map<String, String> nameMap) {
            this.nameMap = nameMap;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder summaryMap(Map<String, String> summaryMap) {
            this.summaryMap = summaryMap;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder contentMap(Map<String, String> contentMap) {
            this.contentMap = contentMap;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder url(List<Link> url) {
            this.url = url;
            return this;
        }

        public Builder attributedTo(LinkOrObject attributedTo) {
            this.attributedTo = attributedTo;
            return this;
        }

        public Builder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public Builder startTime(ZonedDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(ZonedDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder published(ZonedDateTime published) {
            this.published = published;
            return this;
        }

        public Builder updated(ZonedDateTime updated) {
            this.updated = updated;
            return this;
        }

        public Builder attachment(LinkOrObject attachment) {
            this.attachment = attachment;
            return this;
        }

        public Builder audience(LinkOrObject audience) {
            this.audience = audience;
            return this;
        }

        public Builder to(LinkOrObject to) {
            this.to = to;
            return this;
        }

        public Builder bcc(LinkOrObject bcc) {
            this.bcc = bcc;
            return this;
        }

        public Builder bto(LinkOrObject bto) {
            this.bto = bto;
            return this;
        }

        public Builder cc(LinkOrObject cc) {
            this.cc = cc;
            return this;
        }

        public Builder generator(LinkOrObject generator) {
            this.generator = generator;
            return this;
        }

        public Builder icon(LinkOrObject icon) {
            this.icon = icon;
            return this;
        }

        public Builder image(LinkOrObject image) {
            this.image = image;
            return this;
        }

        public Builder inReplyTo(LinkOrObject inReplyTo) {
            this.inReplyTo = inReplyTo;
            return this;
        }

        public Builder location(LinkOrObject location) {
            this.location = location;
            return this;
        }

        public Builder preview(LinkOrObject preview) {
            this.preview = preview;
            return this;
        }

        public Builder replies(Collection replies) {
            this.replies = replies;
            return this;
        }

        public Builder tag(LinkOrObject tag) {
            this.tag = tag;
            return this;
        }

        public Builder totalItems(int totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public Builder items(List<LinkOrObject> items) {
            this.items = items;
            return this;
        }

        public Builder current(LinkOrObject current) {
            this.current = current;
            return this;
        }

        public Builder first(LinkOrObject first) {
            this.first = first;
            return this;
        }

        public Builder last(LinkOrObject last) {
            this.last = last;
            return this;
        }

    }
}
