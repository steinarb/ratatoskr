/*
 * Copyright 2025 Steinar Bang7
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
package no.priv.bang.ratatoskr.services.activitypub;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObjectType;

class MentionTest {

    @Test
    void testCreateMention() {
        var type = ActivityStreamObjectType.Mention;
        var name = "@sally@example.org";
        var href = "http://sally.example.org";

        var hashtag = Mention.with()
            .name(name)
            .href(href)
            .build();

        assertThat(hashtag.type()).isEqualTo(type);
        assertThat(hashtag.name()).isEqualTo(name);
        assertThat(hashtag.href()).isEqualTo(href);
    }

}
