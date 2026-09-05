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

class OrderedCollectionPageTest {

    @Test
    void testCreate() {
        var idUrl = "https://somecompany.com/collections/123";
        int startIndex = 3;

        var collectionpage = OrderedCollectionPage.with()
            .partOf(idUrl)
            .startIndex(startIndex)
            .build();

        assertThat(collectionpage.partOf()).isEqualTo(idUrl);
        assertThat(collectionpage.startIndex()).isEqualTo(startIndex);
    }

    @Test
    void testCopy() {
        var idUrl = "https://somecompany.com/collections/123";
        int startIndex = 3;

        var original = OrderedCollectionPage.with()
            .partOf(idUrl)
            .startIndex(startIndex)
            .build();

        var copyOfOriginal = OrderedCollectionPage.with(original).build();

        assertThat(copyOfOriginal).isEqualTo(original);
    }

    @Test
    void testCopyNull() {
        var copyOfNull = OrderedCollectionPage.with(null).build();

        assertThat(copyOfNull).hasAllNullFieldsOrPropertiesExcept("type", "totalItems", "startIndex");
        assertThat(copyOfNull.type()).isEqualTo(ActivityStreamObjectType.OrderedCollectionPage);
    }

}
