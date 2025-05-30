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

public sealed interface Activity extends IntransitiveActivity permits Accept, ActivityRecord, Add, Announce, Create, Delete, Follow, Ignore, Join, Leave, Like, Listen, Offer, Move, Read, Reject, Remove, Undo, Update, View, Flag, Dislike, UntypedObject {
    @JsonDeserialize(converter = StringToLinkConverter.class)
    LinkOrObject object();
    LinkOrObject instrument();
}
