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

import org.junit.jupiter.api.Test;

class TombstoneTest {

    @Test
    void testCreate() {
        var formerType = ActivityStreamObjectType.Image.toString();
        var deleted = ZonedDateTime.now();

        var tombstone = Tombstone.with()
            .formerType(formerType)
            .deleted(deleted)
            .build();

        assertThat(tombstone).isInstanceOf(Tombstone.class);
        assertThat(tombstone.type()).isEqualTo(ActivityStreamObjectType.Tombstone);
        assertThat(tombstone.formerType()).isEqualTo(formerType);
        assertThat(tombstone.deleted()).isEqualTo(deleted);
    }

    @Test
    void testCopy() {
        var formerType = ActivityStreamObjectType.Image.toString();
        var deleted = ZonedDateTime.now();

        var originalTombstone = Tombstone.with()
            .formerType(formerType)
            .deleted(deleted)
            .build();

        var copiedTombstone = Tombstone.with(originalTombstone).build();

        assertThat(copiedTombstone)
            .isEqualTo(originalTombstone)
            .isInstanceOf(Tombstone.class);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Tombstone.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Tombstone.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Tombstone);
    }

}
