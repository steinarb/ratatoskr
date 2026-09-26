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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class OrderedCollectionTest {

    @Test
    void testCreate() {
        int totalItems = 2;
        LinkOrObject person1 = Person.with().id("person1").build();
        LinkOrObject person2 = Person.with().id("person2").build();
        List<LinkOrObject> items = List.of(person1, person2);
        List<LinkOrObject> orderedItems = items.reversed();

        var collectionpage = OrderedCollection.with()
            .totalItems(totalItems)
            .items(items)
            .orderedItems(orderedItems)
            .build();

        assertThat(collectionpage.totalItems()).isEqualTo(totalItems);
        assertThat(collectionpage.items()).isEqualTo(items);
        assertThat(collectionpage.orderedItems()).isEqualTo(orderedItems);
    }

    @Test
    void testCopy() {
        int totalItems = 2;
        LinkOrObject person1 = Person.with().id("person1").build();
        LinkOrObject person2 = Person.with().id("person2").build();
        List<LinkOrObject> items = List.of(person1, person2);
        List<LinkOrObject> orderedItems = items.reversed();

        var original = OrderedCollection.with()
            .totalItems(totalItems)
            .items(items)
            .orderedItems(orderedItems)
            .build();

        var copyOfOriginal = OrderedCollection.with(original).build();

        assertThat(copyOfOriginal).isEqualTo(original);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = OrderedCollection.with(null).build();

        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type", "totalItems");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.OrderedCollection);
    }

}
