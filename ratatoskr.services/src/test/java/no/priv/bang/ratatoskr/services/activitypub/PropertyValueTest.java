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

class PropertyValueTest {

    @Test
    void testCreatePropertyValue() {
        var type = "PropertyValue";
        var name = "Patreon";
        var value = "<a href=\\\"https://www.patreon.com/mastodon\\\" rel=\\\"me nofollow noopener noreferrer\\\" target=\\\"_blank\\\"><span class=\\\"invisible\\\">https://www.</span><span class=\\\"\\\">patreon.com/mastodon</span><span class=\\\"invisible\\\"></span}";

        var propertyValue = new PropertyValue(type, name, value);

        assertThat(propertyValue.type()).isEqualTo(type);
        assertThat(propertyValue.name()).isEqualTo(name);
        assertThat(propertyValue.value()).isEqualTo(value);
    }

    @Test
    void testCreatePropertyValueCustomConstructor() {
        var type = "PropertyValue";
        var name = "Patreon";
        var value = "<a href=\\\"https://www.patreon.com/mastodon\\\" rel=\\\"me nofollow noopener noreferrer\\\" target=\\\"_blank\\\"><span class=\\\"invisible\\\">https://www.</span><span class=\\\"\\\">patreon.com/mastodon</span><span class=\\\"invisible\\\"></span}";

        var propertyValue = new PropertyValue(name, value);

        assertThat(propertyValue.type()).isEqualTo(type);
        assertThat(propertyValue.name()).isEqualTo(name);
        assertThat(propertyValue.value()).isEqualTo(value);
    }

}
