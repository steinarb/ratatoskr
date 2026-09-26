package no.priv.bang.ratatoskr.asvocabulary;
/*
 * Copyright 2024-2026 Steinar Bang
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public record Question(
    Object context,
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
    @JsonDeserialize(converter = StringToLinkConverter.class)
    List<LinkOrObject> oneOf,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    List<LinkOrObject> anyOf,
    ZonedDateTime closed,
    LinkOrObject result,
    Link atomUri,
    Link inReplyToAtomUri,
    String conversation,
    Collection likes,
    Collection shares
) implements IntransitiveActivity {

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Question source) {
        return new Builder(source);
    }

    public static class Builder extends BaseIntransitiveActivityBuilder<Builder> {
        List<LinkOrObject> oneOf;
        List<LinkOrObject> anyOf;
        ZonedDateTime closed;

        public Builder() {
            super();
        }

        public Builder(Question source) {
            super(source);
            if (source != null) {
                oneOf = source.oneOf();
                anyOf = source.anyOf();
                closed = source.closed();
            }
        }

        public Question build() {
            return new Question(
                context,
                ActivityStreamObjectType.Question,
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
                oneOf,
                anyOf,
                closed,
                result,
                atomUri,
                inReplyToAtomUri,
                conversation,
                likes,
                shares
            );
        }

        public Builder oneOf(List<LinkOrObject> oneOf) {
            this.oneOf = oneOf;
            return self();
        }

        public Builder anyOf(List<LinkOrObject> anyOf) {
            this.anyOf = anyOf;
            return self();
        }

        public Builder closed(ZonedDateTime closed) {
            this.closed = closed;
            return self();
        }
    }

}
