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

class ApplicationTest {

    @Test
    void testCreate() {
        var name = "Exampletron 3000";

        var application = Application.with()
            .name(name)
            .build();

        assertThat(application.type()).isEqualTo(ActivityStreamObjectType.Application);
        assertThat(application.name()).isEqualTo(name);
    }

    @Test
    void testCopy() {
        var name = "Exampletron 3000";

        var originalApplication = Application.with()
            .name(name)
            .build();

        var copiedApplication = Application.with(originalApplication).build();

        assertThat(copiedApplication).isEqualTo(originalApplication);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Application.with(null).build();
        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Application);
    }

}
