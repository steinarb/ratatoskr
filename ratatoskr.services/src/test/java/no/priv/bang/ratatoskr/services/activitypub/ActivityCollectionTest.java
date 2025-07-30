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

class ActivityCollectionTest {

    @Test
    void testCreateActivityCollectionWithBuilder() {
        var id = "http://localhost/ap/featured/jd";
        var like1 = Like.with().build();
        var like2 = Like.with().build();
        var like3 = Like.with().build();
        List<Activity> orderedItems = List.of(like1, like2, like3);

        var collection = ActivityCollection.with().id(id).current(like2).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isEqualTo(like2);
        assertThat(collection.first()).isEqualTo(like1);
        assertThat(collection.last()).isEqualTo(like3);
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

    @Test
    void testCreateActivityCollectionWithBuilderAndEmptyCollection() {
        var id = "http://localhost/ap/featured/jd";
        List<Activity> orderedItems = Collections.emptyList();

        var collection = ActivityCollection.with().id(id).orderedItems(orderedItems).build();

        assertThat(collection.id()).isEqualTo(id);
        assertThat(collection.totalItems()).isEqualTo(orderedItems.size());
        assertThat(collection.current()).isNull();
        assertThat(collection.first()).isNull();
        assertThat(collection.last()).isNull();
        assertThat(collection.orderedItems()).isEqualTo(orderedItems);
    }

}
