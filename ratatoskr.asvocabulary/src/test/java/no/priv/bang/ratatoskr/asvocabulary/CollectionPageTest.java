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

class CollectionPageTest {

    @Test
    void testCreate() {
        var idUrl = "https://somecompany.com/collections/123";
        var next = Link.with().href("https://somecompany.com/collections/123/345").build();
        var prev = Link.with().href("https://somecompany.com/collections/123/300").build();

        var collectionpage = CollectionPage.with()
            .partOf(idUrl)
            .next(next)
            .prev(prev)
            .build();

        assertThat(collectionpage.partOf()).isEqualTo(idUrl);
        assertThat(collectionpage.next()).isEqualTo(next);
        assertThat(collectionpage.prev()).isEqualTo(prev);
    }

    @Test
    void testCopy() {
        var idUrl = "https://somecompany.com/collections/123";
        var next = Link.with().href("https://somecompany.com/collections/123/345").build();
        var prev = Link.with().href("https://somecompany.com/collections/123/300").build();

        var original = CollectionPage.with()
            .partOf(idUrl)
            .next(next)
            .prev(prev)
            .build();

        var copyOfOriginal = CollectionPage.with(original).build();

        assertThat(copyOfOriginal).isEqualTo(original);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = CollectionPage.with(null).build();

        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type", "totalItems");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.CollectionPage);
    }

}
