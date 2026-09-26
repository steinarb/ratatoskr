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

class ServiceTest {

    @Test
    void testCreate() {
        var name = "Acme Web Service";

        var service = Service.with()
            .name(name)
            .build();

        assertThat(service).isInstanceOf(Service.class);
        assertThat(service.type()).isEqualTo(ActivityStreamObjectType.Service);
        assertThat(service.name()).isEqualTo(name);
    }

    @Test
    void testCopy() {
        var name = "Acme Music Service";

        var originalService = Service.with()
            .name(name)
            .build();

        var copiedService = Service.with(originalService).build();

        assertThat(copiedService)
            .isEqualTo(originalService)
            .isInstanceOf(Service.class);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Service.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Service.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Service);
    }

}
