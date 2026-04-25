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

public class ImageTest {

    @Test
    void testCreateImage() {
        var type = ActivityStreamObjectType.Image;
        var mediaType = "image/png";
        var url = "https://example.com/files/cats.png";
        var blurHash = "UBL_:rOpGG-oBUNG,qRj2so|=eE1w^n4S5NH";

        var image = Image.with()
            .mediaType(mediaType)
            .url(url)
            .blurHash(blurHash)
            .build();
        assertThat(image.type()).isEqualTo(type);
        assertThat(image.mediaType()).isEqualTo(mediaType);
        assertThat(image.url()).isEqualTo(url);
        assertThat(image.blurHash()).isEqualTo(blurHash);
    }
}
