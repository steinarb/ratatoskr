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

class ProfileTest {

    @Test
    void testCreate() {
        var describes =  Person.with().name("Sally").build();

        var profile = Profile.with()
            .describes(describes)
            .build();

        assertThat(profile).isInstanceOf(Profile.class);
        assertThat(profile.type()).isEqualTo(ActivityStreamObjectType.Profile);
        assertThat(profile.describes()).isEqualTo(describes);
    }

    @Test
    void testCopy() {
        var describes =  Person.with().name("Sally").build();

        var originalProfile = Profile.with()
            .describes(describes)
            .build();

        var copiedProfile = Profile.with(originalProfile).build();

        assertThat(copiedProfile)
            .isInstanceOf(Profile.class)
            .isEqualTo(originalProfile);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Profile.with(null).build();
        assertThat(copyOfNull)
            .hasAllNullFieldsOrPropertiesExcept("type")
            .isInstanceOf(Profile.class);
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Profile);
    }

}
