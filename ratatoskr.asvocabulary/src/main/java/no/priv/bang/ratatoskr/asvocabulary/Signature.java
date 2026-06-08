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

import java.time.ZonedDateTime;

public record Signature(
    String type,
    String creator,
    ZonedDateTime created,
    String signatureValue
) {

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        String type;
        String creator;
        ZonedDateTime created;
        String signatureValue;

        public Signature build() {
            return new Signature(
                type,
                creator,
                created,
                signatureValue
            );
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder creator(String creator) {
            this.creator = creator;
            return this;
        }

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder signatureValue(String signatureValue) {
            this.signatureValue = signatureValue;
            return this;
        }

    }
}
