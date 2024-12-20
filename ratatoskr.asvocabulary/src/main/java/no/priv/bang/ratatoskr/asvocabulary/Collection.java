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

import java.util.List;
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
}
