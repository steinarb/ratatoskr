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

public record OrderedCollectionPage(
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
    int totalItems,
    List<LinkOrObject> items,
    LinkOrObject current,
    LinkOrObject first,
    LinkOrObject last,
    List<LinkOrObject> orderedItems,
    String partOf,
    int startIndex,
    Link atomUri,
    Link inReplyToAtomUri,
    String conversation,
    Collection likes,
    Collection shares
) implements OrderedCollection
{

    static Builder with() {
        return new Builder();
    }

    static Builder with(OrderedCollectionPage source) {
        return new Builder(source);
    }

    public static class Builder extends OrderedCollectionBuilderBase<Builder> {
        String partOf;
        int startIndex;

        public Builder() {
            super();
        }

        public Builder(OrderedCollectionPage source) {
            super(source);
            if (source != null) {
                partOf = source.partOf();
                startIndex = source.startIndex();
            }
        }

        public OrderedCollectionPage build() {
            return new OrderedCollectionPage(
                context,
                ActivityStreamObjectType.OrderedCollectionPage,
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
                last,
                orderedItems,
                partOf,
                startIndex,
                atomUri,
                inReplyToAtomUri,
                conversation,
                likes,
                shares
            );
        }

        public Builder partOf(String partOf) {
            this.partOf = partOf;
            return self();
        }

        public Builder startIndex(int startIndex) {
            this.startIndex = startIndex;
            return self();
        }

    }
}
