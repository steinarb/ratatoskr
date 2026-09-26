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

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void createObject() {
        var id = "http://example.org/activities/126";
        var summary = "Sally arrived at work";
        var actor = Person.with().name("Sally").build();
        var location = Place.with().name("Work").build();
        var target = Collection.with().summary("John's Connections").build();
        List<LinkOrObject> oneOf = List.of(Application.with().name("arduino").build(), Application.with().name("raspberry pi").build());
        List<LinkOrObject> anyOf = List.of(Note.with().name("Option A").build(), Note.with().name("Option B").build());
        var closed = ZonedDateTime.now();

        var accept = Question.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .location(location)
            .target(target)
            .oneOf(oneOf)
            .anyOf(anyOf)
            .closed(closed)
            .build();

        assertThat(accept).isInstanceOf(Question.class);
        assertThat(accept.id()).isEqualTo(id);
        assertThat(accept.summary()).isEqualTo(summary);
        assertThat(accept.actor()).isEqualTo(actor);
        assertThat(accept.location()).isEqualTo(location);
        assertThat(accept.target()).isEqualTo(target);
        assertThat(accept.oneOf()).isEqualTo(oneOf);
        assertThat(accept.anyOf()).isEqualTo(anyOf);
        assertThat(accept.closed()).isEqualTo(closed);
    }

    @Test
    void testCreateCopyAndMutate() {
        var id = "http://example.org/activities/126";
        var summary = "Sally arrived at work";
        var actor = Person.with().name("Sally").build();
        var location = Place.with().name("Work").build();
        var target = Collection.with().summary("John's Connections").build();
        List<LinkOrObject> oneOf = List.of(Application.with().name("arduino").build(), Application.with().name("raspberry pi").build());
        List<LinkOrObject> anyOf = List.of(Note.with().name("Option A").build(), Note.with().name("Option B").build());
        var closed = ZonedDateTime.now();

        var original = Question.with()
            .id(id)
            .summary(summary)
            .actor(actor)
            .location(location)
            .target(target)
            .oneOf(oneOf)
            .anyOf(anyOf)
            .closed(closed)
            .build();
        var mutatedCopy = Question.with(original).summary("Replaced question").build();

        assertThat(mutatedCopy)
            .isInstanceOf(Question.class)
            .usingRecursiveComparison()
            .ignoringFields("summary")
            .isEqualTo(original);
        assertThat(mutatedCopy.summary()).isEqualTo("Replaced question");
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Question.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Question.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Question);
    }

}
