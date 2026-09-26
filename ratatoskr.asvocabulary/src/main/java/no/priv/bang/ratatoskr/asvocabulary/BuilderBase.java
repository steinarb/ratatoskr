package no.priv.bang.ratatoskr.asvocabulary;
/*
 * Copyright 2026 Steinar Bang
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class BuilderBase<B extends BuilderBase<B>> {

    protected Object context;
    protected String id;
    protected String name;
    protected Map<String, String> nameMap;
    protected String summary;
    protected Map<String, String> summaryMap;
    protected String content;
    protected Map<String, String> contentMap;
    protected String mediaType;
    protected List<Link> url;
    protected LinkOrObject attributedTo;
    protected String duration;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;
    protected ZonedDateTime published;
    protected ZonedDateTime updated;
    protected LinkOrObject attachment;
    protected LinkOrObject audience;
    protected LinkOrObject to;
    protected LinkOrObject bcc;
    protected LinkOrObject bto;
    protected LinkOrObject cc;
    protected LinkOrObject generator;
    protected LinkOrObject icon;
    protected LinkOrObject image;
    protected LinkOrObject inReplyTo;
    protected LinkOrObject location;
    protected LinkOrObject preview;
    protected Collection replies;
    protected LinkOrObject tag;
    protected Link atomUri;
    protected Link inReplyToAtomUri;
    protected String conversation;
    protected Collection likes;
    protected Collection shares;

    protected BuilderBase() {
        // No-args constructor
    }

    protected BuilderBase(ActivityStreamObject source) {
        if (source != null) {
            this.context = source.context();
            this.id = source.id();
            this.name = source.name();
            this.nameMap = source.nameMap();
            this.summary = source.summary();
            this.summaryMap = source.summaryMap();
            this.content = source.content();
            this.contentMap = source.contentMap();
            this.mediaType = source.mediaType();
            this.url = source.url();
            this.attributedTo = source.attributedTo();
            this.duration = source.duration();
            this.startTime = source.startTime();
            this.endTime = source.endTime();
            this.published = source.published();
            this.updated = source.updated();
            this.attachment = source.attachment();
            this.audience = source.audience();
            this.to = source.to();
            this.bcc = source.bcc();
            this.bto = source.bto();
            this.cc = source.cc();
            this.generator = source.generator();
            this.icon = source.icon();
            this.image = source.image();
            this.inReplyTo = source.inReplyTo();
            this.location = source.location();
            this.preview = source.preview();
            this.replies = source.replies();
            this.tag = source.tag();
            this.atomUri = source.atomUri();
            this.inReplyToAtomUri = source.inReplyToAtomUri();
            this.conversation = source.conversation();
            this.likes = source.likes();
            this.shares = source.shares();
        }
    }

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    public B context(Object context) {
        this.context = context;
        return self();
    }

    public B id(String id) {
        this.id = id;
        return self();
    }

    public B name(String name) {
        this.name = name;
        return self();
    }

    public B nameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
        return self();
    }

    public B summary(String summary) {
        this.summary = summary;
        return self();
    }

    public B summaryMap(Map<String, String> summaryMap) {
        this.summaryMap = summaryMap;
        return self();
    }

    public B content(String content) {
        this.content = content;
        return self();
    }

    public B contentMap(Map<String, String> contentMap) {
        this.contentMap = contentMap;
        return self();
    }

    public B mediaType(String mediaType) {
        this.mediaType = mediaType;
        return self();
    }

    public B url(List<Link> url) {
        this.url = url;
        return self();
    }

    public B attributedTo(LinkOrObject attributedTo) {
        this.attributedTo = attributedTo;
        return self();
    }

    public B attributedTo(String href) {
        this.attributedTo = Optional.ofNullable(href).map(h -> Link.with().href(href).build()).orElse(null);
        return self();
    }

    public B duration(String duration) {
        this.duration = duration;
        return self();
    }

    public B startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return self();
    }

    public B endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return self();
    }

    public B published(ZonedDateTime published) {
        this.published = published;
        return self();
    }

    public B updated(ZonedDateTime updated) {
        this.updated = updated;
        return self();
    }

    public B attachment(LinkOrObject attachment) {
        this.attachment = attachment;
        return self();
    }

    public B attachment(String href) {
        this.attachment = Link.with().href(href).build();
        return self();
    }

    public B audience(LinkOrObject audience) {
        this.audience = audience;
        return self();
    }

    public B audience(String href) {
        this.audience = Link.with().href(href).build();
        return self();
    }

    public B to(LinkOrObject to) {
        this.to = to;
        return self();
    }

    public B to(String href) {
        this.to = Link.with().href(href).build();
        return self();
    }

    public B bcc(LinkOrObject bcc) {
        this.bcc = bcc;
        return self();
    }

    public B bcc(String href) {
        this.bcc = Link.with().href(href).build();
        return self();
    }

    public B bto(LinkOrObject bto) {
        this.bto = bto;
        return self();
    }

    public B bto(String href) {
        this.bto = Link.with().href(href).build();
        return self();
    }

    public B cc(LinkOrObject cc) {
        this.cc = cc;
        return self();
    }

    public B cc(String href) {
        this.cc = Link.with().href(href).build();
        return self();
    }

    public B generator(LinkOrObject generator) {
        this.generator = generator;
        return self();
    }

    public B generator(String href) {
        this.generator = Link.with().href(href).build();
        return self();
    }

    public B icon(LinkOrObject icon) {
        this.icon = icon;
        return self();
    }

    public B icon(String href) {
        this.icon = Link.with().href(href).build();
        return self();
    }

    public B image(LinkOrObject image) {
        this.image = image;
        return self();
    }

    public B image(String href) {
        this.image = Link.with().href(href).build();
        return self();
    }

    public B inReplyTo(LinkOrObject inReplyTo) {
        this.inReplyTo = inReplyTo;
        return self();
    }

    public B inReplyTo(String href) {
        this.inReplyTo = Link.with().href(href).build();
        return self();
    }

    public B location(LinkOrObject location) {
        this.location = location;
        return self();
    }

    public B location(String href) {
        this.location = Link.with().href(href).build();
        return self();
    }

    public B preview(LinkOrObject preview) {
        this.preview = preview;
        return self();
    }

    public B preview(String href) {
        this.preview = Link.with().href(href).build();
        return self();
    }

    public B replies(Collection replies) {
        this.replies = replies;
        return self();
    }

    public B tag(LinkOrObject tag) {
        this.tag = tag;
        return self();
    }

    public B tag(String href) {
        this.tag = Link.with().href(href).build();
        return self();
    }

    public B atomUri(Link atomUri) {
        this.atomUri = atomUri;
        return self();
    }

    public B atomUri(String href) {
        this.atomUri = Link.with().href(href).build();
        return self();
    }

    public B inReplyToAtomUri(Link inReplyToAtomUri) {
        this.inReplyToAtomUri = inReplyToAtomUri;
        return self();
    }

    public B inReplyToAtomUri(String href) {
        this.inReplyToAtomUri = Link.with().href(href).build();
        return self();
    }

    public B conversation(String conversation) {
        this.conversation = conversation;
        return self();
    }

    public B likes(Collection likes) {
        this.likes = likes;
        return self();
    }

    public B shares(Collection shares) {
        this.shares = shares;
        return self();
    }

}
