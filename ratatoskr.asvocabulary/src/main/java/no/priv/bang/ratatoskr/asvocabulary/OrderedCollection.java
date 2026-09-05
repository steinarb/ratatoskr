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

import java.util.List;

public sealed interface OrderedCollection extends Collection permits OrderedCollectionPage, OrderedCollectionRecord {
    List<LinkOrObject> orderedItems();

    static Builder with() {
        return new Builder();
    }

    static Builder with(OrderedCollection source) {
        return new Builder(source);
    }

    public static class Builder extends OrderedCollectionBuilderBase<Builder> {

        private Builder() {
            super();
        }

        private Builder(OrderedCollection source) {
            super(source);
        }

        public OrderedCollection build() {
            return new OrderedCollectionRecord(
                context,
                ActivityStreamObjectType.OrderedCollection,
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
                orderedItems, atomUri,
                inReplyToAtomUri,
                conversation,
                likes,
                shares
            );
        }

    }
}
