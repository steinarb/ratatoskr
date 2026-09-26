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


public record Relationship(
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
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject subject,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject relationship,
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject object,
    LinkOrObject instrument,
    LinkOrObject result,
    Link atomUri,
    Link inReplyToAtomUri,
    String conversation,
    Collection likes,
    Collection shares
) implements ActivityStreamObject {

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Relationship source) {
        return new Builder(source);
    }

    public static class Builder extends BuilderBase<Builder> {
        LinkOrObject subject;
        LinkOrObject relationship;
        LinkOrObject object;
        LinkOrObject instrument;
        LinkOrObject result;

        public Builder() {
            super();
        }

        public Builder(Relationship source) {
            super(source);
            if (source != null) {
                subject = source.subject();
                relationship = source.relationship();
                object = source.object();
                instrument = source.instrument();
                result = source.result();
            }
        }

        public Relationship build() {
            return new Relationship(
                context,
                ActivityStreamObjectType.Relationship,
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
                subject,
                relationship,
                object,
                instrument,
                result,
                atomUri,
                inReplyToAtomUri,
                conversation,
                likes,
                shares
            );
        }

        public Builder subject(LinkOrObject subject) {
            this.subject = subject;
            return self();
        }

        public Builder relationship(LinkOrObject relationship) {
            this.relationship = relationship;
            return self();
        }

        public Builder object(LinkOrObject object) {
            this.object = object;
            return self();
        }

        public Builder instrument(LinkOrObject instrument) {
            this.instrument = instrument;
            return self();
        }

        public Builder result(LinkOrObject result) {
            this.result = result;
            return self();
        }
    }

}
