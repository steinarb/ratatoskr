/*
 * Copyright 2024 Steinar Bang
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
package no.priv.bang.ratatoskr.asvocabulary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public sealed interface IntransitiveActivity extends ActivityStreamObject permits Activity, Arrive, Question, Travel {
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject actor();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject target();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject origin();
    LinkOrObject location();
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject result();

}
