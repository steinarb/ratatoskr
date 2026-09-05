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

import static java.util.Optional.ofNullable;

import java.util.List;

public class CollectionBuilderBase<B extends CollectionBuilderBase<B>> extends BuilderBase<B> {
    int totalItems;
    List<LinkOrObject> items;
    LinkOrObject current;
    LinkOrObject first;
    LinkOrObject last;

    public CollectionBuilderBase() {
        super();
    }

    public CollectionBuilderBase(Collection source) {
        super(source);
        if (source != null) {
            totalItems = source.totalItems();
            items = ofNullable(source.items()).map(List::copyOf).orElse(null);
            current = source.current();
            first = source.first();
            last = source.last();
        }
    }

    public B totalItems(int totalItems) {
        this.totalItems = totalItems;
        return self();
    }

    public B items(List<LinkOrObject> items) {
        this.items = items;
        return self();
    }

    public B current(LinkOrObject current) {
        this.current = current;
        return self();
    }

    public B first(LinkOrObject first) {
        this.first = first;
        return self();
    }

    public B last(LinkOrObject last) {
        this.last = last;
        return self();
    }

}
