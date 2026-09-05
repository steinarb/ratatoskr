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

class AddTest {

    @Test
    void createObject() {
        var id = "http://example.org/activities/125";
        var summary = "John added Sally to his friends list";
        var actor = Link.with().href("acct:john@example.org").build();
        var object = "http://example.org/connections/123";
        var target = Collection.with().summary("John's Connections").build();
        var instrument = "https://acme.com/service/music";

        var add = Add.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .object(object)
            .target(target)
            .instrument(instrument)
            .build();

        assertThat(add).isInstanceOf(Add.class);
        assertThat(add.id()).isEqualTo(id);
        assertThat(add.summary()).isEqualTo(summary);
        assertThat(add.actor()).isEqualTo(actor);
        assertThat(add.object()).hasFieldOrPropertyWithValue("href", object);
        assertThat(add.target()).isEqualTo(target);
        assertThat(add.instrument()).hasFieldOrPropertyWithValue("href", instrument);
    }

    @Test
    void testCreateCopyAndMutate() {
        var id = "http://example.org/activities/125";
        var summary = "John added Sally to his friends list";
        var actor = Link.with().href("acct:john@example.org").build();
        var object = Link.with().href("http://example.org/connections/123").build();
        var target = Collection.with().summary("John's Connections").build();
        var instrument = Service.with().name("ACME Music Service").build();

        var original = Add.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .object(object)
            .target(target)
            .instrument(instrument)
            .build();
        var mutatedCopy = Add.with(original).summary("Replaced add").build();

        assertThat(mutatedCopy)
            .isInstanceOf(Add.class)
            .usingRecursiveComparison()
            .ignoringFields("summary")
            .isEqualTo(original);
        assertThat(mutatedCopy.summary()).isEqualTo("Replaced add");
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Add.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Add.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Add);
    }

}
