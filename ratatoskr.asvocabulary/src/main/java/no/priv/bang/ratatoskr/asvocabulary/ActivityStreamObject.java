/*
 * Copyright 2024 Steinar Bang
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

public sealed interface ActivityStreamObject extends LinkOrObject permits IntransitiveActivity, Actor, ActivityStreamObjectRecord, Article, Collection, Document, Event, Group, Note, Place, Application, Organization, Service, Relationship, Profile, Tombstone {
    public String id();
    public String name();
    public Map<String, String> nameMap();
    public String summary();
    public Map<String, String> summaryMap();
    public String content();
    public Map<String, String> contentMap();
    public String mediaType();
    @JsonDeserialize(converter = StringAndLinkToLinkListConverter.class)
    public List<Link> url();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject attributedTo();
    public String duration();
    @JsonDeserialize(converter = ToZonedDateTimeConverter.class)
    public ZonedDateTime startTime();
    public ZonedDateTime endTime();
    public ZonedDateTime published();
    public ZonedDateTime updated();
    public LinkOrObject attachment();
    public LinkOrObject audience();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject to();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject bcc();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject bto();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject cc();
    public LinkOrObject generator();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject icon(); // actually just Image or Link, but hard to do
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject image(); // actually just Image or Link, but hard to do
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject inReplyTo();
    public LinkOrObject location();
    public LinkOrObject preview();
    public Collection replies();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    public LinkOrObject tag();
}
