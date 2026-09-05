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

class TravelTest {

    @Test
    void createObject() {
        var id = "http://example.org/activities/127";
        var summary = "Sally went home from work";
        var actor = "http://sally.example.org";
        var target = "http://john.example.org";
        var origin = "http://example.org/foo";
        var result = "http://example.org/activities/123";

        var accept = Travel.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .target(target)
            .origin(origin)
            .result(result)
            .build();

        assertThat(accept).isInstanceOf(Travel.class);
        assertThat(accept.id()).isEqualTo(id);
        assertThat(accept.summary()).isEqualTo(summary);
        assertThat(accept.actor()).hasFieldOrPropertyWithValue("href", actor);
        assertThat(accept.target()).hasFieldOrPropertyWithValue("href", target);
        assertThat(accept.origin()).hasFieldOrPropertyWithValue("href", origin);
        assertThat(accept.result()).hasFieldOrPropertyWithValue("href", result);
    }

    @Test
    void testCreateCopyAndMutate() {
        var id = "http://example.org/activities/127";
        var summary = "Sally went home from work";
        var actor = Person.with().name("Sally").build();
        var target = Place.with().name("Home").build();
        var origin = Place.with().name("Work").build();

        var original = Travel.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .target(target)
            .origin(origin)
            .build();
        var mutatedCopy = Travel.with(original).summary("Replaced travel").build();

        assertThat(mutatedCopy)
            .isInstanceOf(Travel.class)
            .usingRecursiveComparison()
            .ignoringFields("summary")
            .isEqualTo(original);
        assertThat(mutatedCopy.summary()).isEqualTo("Replaced travel");
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Travel.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Travel.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Travel);
    }

}
