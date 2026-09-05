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

import org.junit.jupiter.api.Test;

class ArriveTest {

    @Test
    void createObject() {
        var id = "http://example.org/activities/126";
        var summary = "Sally arrived at work";
        var actor = Person.with().name("Sally").build();
        var location = Place.with().name("Work").build();
        var target = Collection.with().summary("John's Connections").build();

        var accept = Arrive.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .location(location)
            .target(target)
            .build();

        assertThat(accept).isInstanceOf(Arrive.class);
        assertThat(accept.id()).isEqualTo(id);
        assertThat(accept.summary()).isEqualTo(summary);
        assertThat(accept.actor()).isEqualTo(actor);
        assertThat(accept.location()).isEqualTo(location);
        assertThat(accept.target()).isEqualTo(target);
    }

    @Test
    void testCreateCopyAndMutate() {
        var id = "http://example.org/activities/126";
        var summary = "Sally arrived at work";
        var actor = Person.with().name("Sally").build();
        var location = Place.with().name("Work").build();
        var target = Collection.with().summary("John's Connections").build();

        var original = Arrive.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .location(location)
            .target(target)
            .build();
        var mutatedCopy = Arrive.with(original).summary("Replaced arrive").build();

        assertThat(mutatedCopy)
            .isInstanceOf(Arrive.class)
            .usingRecursiveComparison()
            .ignoringFields("summary")
            .isEqualTo(original);
        assertThat(mutatedCopy.summary()).isEqualTo("Replaced arrive");
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Arrive.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Arrive.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Arrive);
    }

}
