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

class PersonCollectionTest {

    @Test
    void testCreateFeaturedCollectionWithBuilder() {
        var id = "http://localhost/ap/featured/jd";
        var person1 = Person.with().build();
        var person2 = Person.with().build();
        var person3 = Person.with().build();
        var orderedItems = List.of(person1, person2, person3);

        var collection = PersonCollection.with().id(id).current(person2).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isEqualTo(person2);
        assertThat(collection.first()).isEqualTo(person1);
        assertThat(collection.last()).isEqualTo(person3);
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

    @Test
    void testCreateFeaturedCollectionWithBuilderAndEmptyCollection() {
        var id = "http://localhost/ap/featured/jd";
        List<Person> orderedItems = Collections.emptyList();

        var collection = PersonCollection.with().id(id).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isNull();
        assertThat(collection.first()).isNull();
        assertThat(collection.last()).isNull();
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

}
