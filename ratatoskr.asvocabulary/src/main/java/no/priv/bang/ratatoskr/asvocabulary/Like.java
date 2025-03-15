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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Like(
    @JsonProperty("@context") @JsonAlias("context") Object context,
    ActivityStreamObjectType type,
    String id,
    String name,
    Map<String, String> nameMap,
    String summary,
    Map<String, String> summaryMap,
    String content,
    Map<String, String> contentMap,
    String mediaType,
    List<Link> url,
    LinkOrObject attributedTo,
    String duration,
    ZonedDateTime startTime,
    ZonedDateTime endTime,
    ZonedDateTime published,
    ZonedDateTime updated,
    LinkOrObject attachment,
    LinkOrObject audience,
    LinkOrObject to,
    LinkOrObject bcc,
    LinkOrObject bto,
    LinkOrObject cc,
    LinkOrObject generator,
    LinkOrObject icon,
    LinkOrObject image,
    LinkOrObject inReplyTo,
    LinkOrObject location,
    LinkOrObject preview,
    Collection replies,
    LinkOrObject tag,
    LinkOrObject actor,
    LinkOrObject target,
    LinkOrObject origin,
    LinkOrObject object,
    LinkOrObject instrument,
    LinkOrObject result
) implements Activity {
    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Like like) {
        var builder = new Builder();
        builder.id = like.id();
        builder.name = like.name();
        builder.nameMap = like.nameMap();
        builder.summary = like.summary();
        builder.summaryMap = like.summaryMap();
        builder.content = like.content();
        builder.contentMap = like.contentMap();
        builder.mediaType = like.mediaType();
        builder.url = like.url();
        builder.attributedTo = like.attributedTo();
        builder.duration = like.duration();
        builder.startTime = like.startTime();
        builder.endTime = like.endTime();
        builder.published = like.published();
        builder.updated = like.updated();
        builder.attachment = like.attachment();
        builder.audience = like.audience();
        builder.to = like.to();
        builder.bcc = like.bcc();
        builder.bto = like.bto();
        builder.cc = like.cc();
        builder.generator = like.generator();
        builder.icon = like.icon();
        builder.image = like.image();
        builder.inReplyTo = like.inReplyTo();
        builder.location = like.location();
        builder.preview = like.preview();
        builder.replies = like.replies();
        builder.tag = like.tag();
        builder.actor = like.actor();
        builder.target = like.target();
        builder.origin = like.origin();
        builder.object = like.object();
        builder.instrument = like.instrument();
        builder.result = like.result();
        return builder;
    }

    public static class Builder {
        private Object context = "https://www.w3.org/ns/activitystreams";
        private ActivityStreamObjectType type = ActivityStreamObjectType.Like;
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
        private LinkOrObject actor;
        private LinkOrObject target;
        private LinkOrObject origin;
        private LinkOrObject object;
        private LinkOrObject instrument;
        private LinkOrObject result;

        public Like build() {
            return new Like(
                context,
                type,
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
                actor,
                target,
                origin,
                object,
                instrument,
                result);
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

        public Builder actor(LinkOrObject actor) {
            this.actor = actor;
            return this;
        }

        public Builder target(LinkOrObject target) {
            this.target = target;
            return this;
        }

        public Builder origin(LinkOrObject origin) {
            this.origin = origin;
            return this;
        }

        public Builder object(LinkOrObject object) {
            this.object = object;
            return this;
        }

        public Builder instrument(LinkOrObject instrument) {
            this.instrument = instrument;
            return this;
        }

        public Builder result(LinkOrObject result) {
            this.result = result;
            return this;
        }
    }
}
