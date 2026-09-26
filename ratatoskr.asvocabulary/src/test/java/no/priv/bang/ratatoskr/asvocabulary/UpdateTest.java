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

class UpdateTest {

    @Test
    void createObject() {
        var object = Update.with()
            .id("http://sally.organization.com/activities/23")
            .name("Sometimes you just have to update")
            .build();

        assertThat(object.id()).isEqualTo("http://sally.organization.com/activities/23");
        assertThat(object.name()).isEqualTo("Sometimes you just have to update");
    }

    @Test
    void testCreateCopyAndMutate() {
        var original = Update.with()
            .id("http://sally.organization.com/activities/23")
            .name("Sometimes you just have to update")
            .build();

        var mutatedCopy = Update.with(original).name("Replaced update").build();

        assertThat(mutatedCopy.id()).isEqualTo("http://sally.organization.com/activities/23");
        assertThat(mutatedCopy.name()).isEqualTo("Replaced update");
    }

}
