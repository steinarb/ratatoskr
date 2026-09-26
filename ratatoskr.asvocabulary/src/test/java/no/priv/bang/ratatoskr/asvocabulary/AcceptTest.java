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

class AcceptTest {

    @Test
    void createObject() {
        var id = "http://example.org/activities/125";
        var summary = "John added Sally to his friends list";
        var actor = Link.with().href("acct:john@example.org").build();
        var object = Link.with().href("http://example.org/connections/123").build();
        var target = Collection.with().summary("John's Connections").build();
        var instrument = Service.with().name("ACME Music Service").build();

        var accept = Accept.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .object(object)
            .target(target)
            .instrument(instrument)
            .build();

        assertThat(accept).isInstanceOf(Accept.class);
        assertThat(accept.id()).isEqualTo(id);
        assertThat(accept.summary()).isEqualTo(summary);
        assertThat(accept.actor()).isEqualTo(actor);
        assertThat(accept.object()).isEqualTo(object);
        assertThat(accept.target()).isEqualTo(target);
        assertThat(accept.instrument()).isEqualTo(instrument);
    }

    @Test
    void testCreateCopyAndMutate() {
        var id = "http://example.org/activities/125";
        var summary = "John added Sally to his friends list";
        var actor = Link.with().href("acct:john@example.org").build();
        var object = Link.with().href("http://example.org/connections/123").build();
        var target = Collection.with().summary("John's Connections").build();
        var instrument = Service.with().name("ACME Music Service").build();

        var original = Accept.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .object(object)
            .target(target)
            .instrument(instrument)
            .build();
        var mutatedCopy = Accept.with(original).summary("Replaced add").build();

        assertThat(mutatedCopy)
            .isInstanceOf(Accept.class)
            .usingRecursiveComparison()
            .ignoringFields("summary")
            .isEqualTo(original);
        assertThat(mutatedCopy.summary()).isEqualTo("Replaced add");
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Accept.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Accept.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Accept);
    }

}
