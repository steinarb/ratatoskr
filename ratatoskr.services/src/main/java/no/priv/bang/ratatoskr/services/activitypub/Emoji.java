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

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObjectType;

public record Emoji(
    ActivityStreamObjectType type,
    String name,
    Image icon) implements Tag
{

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Image icon;

        public Emoji build() {
            return new Emoji(
                ActivityStreamObjectType.Emoji,
                name,
                icon);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder icon(Image icon) {
            this.icon = icon;
            return this;
        }

    }

}
