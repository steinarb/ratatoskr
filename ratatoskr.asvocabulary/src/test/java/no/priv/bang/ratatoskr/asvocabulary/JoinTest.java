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

class JoinTest {

    @Test
    void createObject() {
        var object = Join.with()
            .id("http://sally.organization.com/activities/12")
            .name("Sometimes you just have to accept")
            .build();

        assertThat(object.id()).isEqualTo("http://sally.organization.com/activities/12");
        assertThat(object.name()).isEqualTo("Sometimes you just have to accept");
    }

    @Test
    void testCreateCopyAndMutate() {
        var original = Join.with()
            .id("http://sally.organization.com/activities/12")
            .name("Sometimes you just have to accept")
            .build();

        var mutatedCopy = Join.with(original).name("Replaced join").build();

        assertThat(mutatedCopy.id()).isEqualTo("http://sally.organization.com/activities/12");
        assertThat(mutatedCopy.name()).isEqualTo("Replaced join");
    }

}
