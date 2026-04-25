/*
 * Copyright 2025 Steinar Bang
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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class AnnounceCollectionTest {

    @Test
    void testCreateAnnounceCollectionWithBuilder() {
        var id = "http://localhost/ap/featured/jd";
        var announce1 = Announce.with().build();
        var announce2 = Announce.with().build();
        var announce3 = Announce.with().build();
        var orderedItems = List.of(announce1, announce2, announce3);

        var collection = AnnounceCollection.with().id(id).current(announce2).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isEqualTo(announce2);
        assertThat(collection.first()).isEqualTo(announce1);
        assertThat(collection.last()).isEqualTo(announce3);
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

    @Test
    void testCreateAnnounceCollectionWithBuilderAndEmptyCollection() {
        var id = "http://localhost/ap/featured/jd";
        List<Announce> orderedItems = Collections.emptyList();

        var collection = AnnounceCollection.with().id(id).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isNull();
        assertThat(collection.first()).isNull();
        assertThat(collection.last()).isNull();
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

}
