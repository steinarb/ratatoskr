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

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class SignatureTest {

    @Test
    void testCreate() {
        var type = "RsaSignature2017";
        var creator = "https://activitypub.academy/users/braussia_vrottariul#main-key";
        var created = ZonedDateTime.now();
        var signatureValue = "UGnB934eV+DRamLxE26gtmw6P721HCG6nAo5u3FXKAAYgugdgSfgnOdiv7i5A6hxz5ypxl0jxk2SrT00YBDSQfovF3Gd/DWpuliqSYFu0tWqY3iF4u17KFtZyaGhLnFwzY63Nqtsr6UwG+2odli/Wk3d5bFgxWYV2T3ci0nCcnyg9QKX0KBCIoNI+65bBfD3WEbA6YR8SYv7Styr92AwH9JIJCdxKHcVV66s1x8TzgKrZEXDqr+TR4mK2xpz9q4Ewe+ziRN5IGRy3Awpm2DZJ2wyRA1mXmbiP5JCHvCnG0COJWgIv+ZuJvoLN0oiWX0kgxIMf0nX2vy1VoVqic60qQ==";

        Signature signature = Signature.with()
            .type(type)
            .creator(creator)
            .created(created)
            .signatureValue(signatureValue)
            .build();

        assertThat(signature)
            .isNotNull()
            .hasFieldOrPropertyWithValue("type", type)
            .hasFieldOrPropertyWithValue("creator", creator)
            .hasFieldOrPropertyWithValue("created", created)
            .hasFieldOrPropertyWithValue("signatureValue", signatureValue);
    }

}
