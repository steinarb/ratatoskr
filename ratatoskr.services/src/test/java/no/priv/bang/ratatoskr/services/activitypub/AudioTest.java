/*
 * Copyright 2025 Steinar Bang
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

class AudioTest {

    @Test
    void testCreateAudio() {
        var type = ActivityStreamObjectType.Video;
        var mediaType = "video/x-matroska";
        var url = "http://example.org/video.mkv";
        var duration = "PT2H";

        var audio = Audio.with()
            .mediaType(mediaType)
            .url(url)
            .duration(duration)
            .build();

        assertThat(audio.type()).isEqualTo(type);
        assertThat(audio.mediaType()).isEqualTo(mediaType);
        assertThat(audio.url()).isEqualTo(url);
        assertThat(audio.duration()).isEqualTo(duration);
    }

}
