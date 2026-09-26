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

class RelationshipTest {

    @Test
    void testCreate() {
        var subject = Link.with().href("http://sally.example.org").build();
        var relationship = Link.with().href("http://purl.org/vocab/relationship/friendOf").build();
        var object = Link.with().href("http://matt.example.org").build();
        var instrument = Service.with().name("Acme Music Service").build();
        var result = Note.with().content("Users are favoriting &quot;arduino&quot; by a 33% margin.").build();

        var service = Relationship.with()
            .subject(subject)
            .relationship(relationship)
            .object(object)
            .instrument(instrument)
            .result(result)
            .build();

        assertThat(service).isInstanceOf(Relationship.class);
        assertThat(service.type()).isEqualTo(ActivityStreamObjectType.Relationship);
        assertThat(service.subject()).isEqualTo(subject);
        assertThat(service.relationship()).isEqualTo(relationship);
        assertThat(service.object()).isEqualTo(object);
        assertThat(service.instrument()).isEqualTo(instrument);
        assertThat(service.result()).isEqualTo(result);
    }

    @Test
    void testCopy() {
        var subject = Link.with().href("http://sally.example.org").build();
        var relationship = Link.with().href("http://purl.org/vocab/relationship/friendOf").build();
        var object = Link.with().href("http://matt.example.org").build();
        var instrument = Service.with().name("Acme Music Service").build();
        var result = Note.with().content("Users are favoriting &quot;arduino&quot; by a 33% margin.").build();

        var originalRelationship = Relationship.with()
            .subject(subject)
            .relationship(relationship)
            .object(object)
            .instrument(instrument)
            .result(result)
            .build();

        var copiedRelationship = Relationship.with(originalRelationship).build();

        assertThat(copiedRelationship)
            .isInstanceOf(Relationship.class)
            .isEqualTo(originalRelationship);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Relationship.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Relationship.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Relationship);
    }

}
