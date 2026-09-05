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

import java.util.UUID;

import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void testCreate() {
        int width = 640;
        int height = 1028;
        String blurhash = UUID.randomUUID().toString();

        var document = Document.with()
            .width(width)
            .height(height)
            .blurhash(blurhash)
            .build();

        assertThat(document.type()).isEqualTo(ActivityStreamObjectType.Document);
        assertThat(document.width()).isEqualTo(width);
        assertThat(document.height()).isEqualTo(height);
        assertThat(document.blurhash()).isEqualTo(blurhash);
    }

    @Test
    void testCopy() {
        int width = 640;
        int height = 1028;
        String blurhash = UUID.randomUUID().toString();

        var originalDocument = Document.with()
            .width(width)
            .height(height)
            .blurhash(blurhash)
            .build();

        var copiedDocument = Document.with(originalDocument).build();

        assertThat(copiedDocument).isEqualTo(originalDocument);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = Document.with(null).build();
        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type", "width", "height");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.Document);
    }

}
