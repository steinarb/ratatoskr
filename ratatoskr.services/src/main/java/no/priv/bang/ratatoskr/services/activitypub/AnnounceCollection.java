/*
 * Copyright 2025-2026 Steinar Bang
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
package no.priv.bang.ratatoskr.services.activitypub;

import java.util.List;

public record AnnounceCollection(
    String id,
    int totalItems,
    Announce current,
    Announce first,
    Announce last,
    List<Announce> orderedItems
)
{

    public static Builder with() {
        return new Builder();
    }

    static class Builder {

        private String id;
        private int totalItems;
        private Announce current;
        private Announce first;
        private Announce last;
        private List<Announce> orderedItems;

        public AnnounceCollection build() {
            return new AnnounceCollection(
                id,
                totalItems,
                current,
                first,
                last,
                orderedItems);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder current(Announce current) {
            this.current = current;
            return this;
        }

        public Builder orderedItems(List<Announce> orderedItems) {
            this.totalItems = orderedItems.size();
            this.first = orderedItems.stream().findFirst().orElse(null);
            this.last = orderedItems.stream().reduce((unused, second) -> second).orElse(null);
            this.orderedItems = orderedItems;
            return this;
        }

    }

}
