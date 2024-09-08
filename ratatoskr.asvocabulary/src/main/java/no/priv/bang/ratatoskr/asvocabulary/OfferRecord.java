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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record OfferRecord(
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
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject to,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject bcc,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject bto,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject cc,
    LinkOrObject generator,
    LinkOrObject icon,
    LinkOrObject image,
    LinkOrObject inReplyTo,
    LinkOrObject location,
    LinkOrObject preview,
    Collection replies,
    LinkOrObject tag,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject actor,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject target,
    LinkOrObject origin,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject object,
    LinkOrObject instrument,
    LinkOrObject result
) implements Offer {
}