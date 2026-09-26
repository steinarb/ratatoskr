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

class UndoTest {

    @Test
    void createObject() {
        var object = Undo.with()
            .id("http://sally.organization.com/activities/22")
            .name("Sometimes you just have to undo")
            .build();

        assertThat(object.id()).isEqualTo("http://sally.organization.com/activities/22");
        assertThat(object.name()).isEqualTo("Sometimes you just have to undo");
    }

    @Test
    void testCreateCopyAndMutate() {
        var original = Undo.with()
            .id("http://sally.organization.com/activities/22")
            .name("Sometimes you just have to undo")
            .build();

        var mutatedCopy = Undo.with(original).name("Replaced undo").build();

        assertThat(mutatedCopy.id()).isEqualTo("http://sally.organization.com/activities/22");
        assertThat(mutatedCopy.name()).isEqualTo("Replaced undo");
    }

}
