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

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.priv.bang.ratatoskr.asvocabulary.ActivityStreamObjectType;

class PersonTest {

    @Test
    void testCreatePerson() {
        var id = "http://localhost/ap/person/jd";
        var preferredUsername = "jd";
        var name = "John Doe";
        var summary = "A regular guy";
        var type = ActivityStreamObjectType.Person;
        var url = id;
        var icon = "http://localhost/ap/icons/person/jd";
        var image = "http://localhost/ap/images/person/jd";
        var manuallyApprovesFollowers = false;
        var discoverable = true;
        var indexable = true;
        var publicKey = PublicKey.with()
            .id("http://localhost/ap/person/jd#main-key")
            .owner(id)
            .publicKey("-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvXc4vkECU2/CeuSo1wtn\\nFoim94Ne1jBMYxTZ9wm2YTdJq1oiZKif06I2fOqDzY/4q/S9uccrE9Bkajv1dnkO\\nVm31QjWlhVpSKynVxEWjVBO5Ienue8gND0xvHIuXf87o61poqjEoepvsQFElA5ym\\novljWGSA/jpj7ozygUZhCXtaS2W5AD5tnBQUpcO0lhItYPYTjnmzcc4y2NbJV8hz\\n2s2G8qKv8fyimE23gY1XrPJg+cRF+g4PqFXujjlJ7MihD9oqtLGxbu7o1cifTn3x\\nBfIdPythWu5b4cujNsB3m3awJjVmx+MHQ9SugkSIYXV0Ina77cTNS0M2PYiH1PFR\\nTwIDAQAB\\n-----END PUBLIC KEY-----\\n")
            .build();
        var inbox = "http://localhost:8181/ratatoskr/ap/inbox/kenzoishii";
        var outbox = "http://localhost:8181/ratatoskr/ap/outbox/kenzoishii";
        var following = "http://localhost:8181/ratatoskr/ap/following/kenzoishii";
        var followers = "http://localhost:8181/ratatoskr/ap/followers/kenzoishii";
        var liked = "http://localhost:8181/ratatoskr/ap/liked/kenzoishii";
        var featured = StatusCollection.with().build();
        var attachment1 = new PropertyValue("Patreon", "<a href=\"https://www.patreon.com/mastodon\" rel=\"me nofollow noopener noreferrer\" target=\"_blank\"><span class=\"invisible\">https://www.</span><span class=\"\">patreon.com/mastodon</span><span class=\"invisible\"></span}");
        var attachment2 = new PropertyValue("Homepage", "<a href=\\\"https://zeonfederated.com\\\" rel=\\\"me nofollow noopener noreferrer\\\" target=\\\"_blank\\\"><span class=\\\"invisible\\\">https://</span><span class=\\\"\\\">zeonfederated.com</span><span class=\\\"invisible\\\"></span}");
        var attachment = List.of(attachment1, attachment2);
        var alsoKnownAs = "https://mastodon.social/@johndoe";
        var created = ZonedDateTime.now();
        var memorial = false;
        var suspended = true;
        var attributionDomains = List.of("johndoe.com", "joinmastodon.org");

        var person = Person.with()
            .id(id)
            .preferredUsername(preferredUsername)
            .name(name)
            .summary(summary)
            .url(url)
            .icon(icon)
            .image(image)
            .manuallyApprovesFollowers(manuallyApprovesFollowers)
            .discoverable(discoverable)
            .indexable(indexable)
            .publicKey(publicKey)
            .inbox(inbox)
            .outbox(outbox)
            .following(following)
            .followers(followers)
            .liked(liked)
            .featured(featured)
            .attachment(attachment)
            .alsoKnownAs(alsoKnownAs)
            .created(created)
            .memorial(memorial)
            .suspended(suspended)
            .attributionDomains(attributionDomains)
            .build();

        assertThat(person.id()).isEqualTo(id);
        assertThat(person.preferredUsername()).isEqualTo(preferredUsername);
        assertThat(person.name()).isEqualTo(name);
        assertThat(person.summary()).isEqualTo(summary);
        assertThat(person.type()).isEqualTo(type);
        assertThat(person.icon()).isEqualTo(icon);
        assertThat(person.image()).isEqualTo(image);
        assertThat(person.manuallyApprovesFollowers()).isEqualTo(manuallyApprovesFollowers);
        assertThat(person.discoverable()).isEqualTo(discoverable);
        assertThat(person.indexable()).isEqualTo(indexable);
        assertThat(person.publicKey()).isEqualTo(publicKey);
        assertThat(person.featured()).isEqualTo(featured);
        assertThat(person.attachment()).isEqualTo(attachment);
        assertThat(person.alsoKnownAs()).isEqualTo(alsoKnownAs);
        assertThat(person.created()).isEqualTo(created);
        assertThat(person.memorial()).isEqualTo(memorial);
        assertThat(person.suspended()).isEqualTo(suspended);
        assertThat(person.attributionDomains()).isEqualTo(attributionDomains);
    }

    @Test
    void testCopyPerson() {
        var id = "http://localhost/ap/person/jd";
        var preferredUsername = "jd";
        var name = "John Doe";
        var summary = "A regular guy";
        var url = id;
        var icon = "http://localhost/ap/icons/person/jd";
        var image = "http://localhost/ap/images/person/jd";
        var manuallyApprovesFollowers = false;
        var discoverable = true;
        var indexable = true;
        var publicKey = PublicKey.with()
            .id("http://localhost/ap/person/jd#main-key")
            .owner(id)
            .publicKey("-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvXc4vkECU2/CeuSo1wtn\\nFoim94Ne1jBMYxTZ9wm2YTdJq1oiZKif06I2fOqDzY/4q/S9uccrE9Bkajv1dnkO\\nVm31QjWlhVpSKynVxEWjVBO5Ienue8gND0xvHIuXf87o61poqjEoepvsQFElA5ym\\novljWGSA/jpj7ozygUZhCXtaS2W5AD5tnBQUpcO0lhItYPYTjnmzcc4y2NbJV8hz\\n2s2G8qKv8fyimE23gY1XrPJg+cRF+g4PqFXujjlJ7MihD9oqtLGxbu7o1cifTn3x\\nBfIdPythWu5b4cujNsB3m3awJjVmx+MHQ9SugkSIYXV0Ina77cTNS0M2PYiH1PFR\\nTwIDAQAB\\n-----END PUBLIC KEY-----\\n")
            .build();
        var inbox = "http://localhost:8181/ratatoskr/ap/inbox/kenzoishii";
        var outbox = "http://localhost:8181/ratatoskr/ap/outbox/kenzoishii";
        var following = "http://localhost:8181/ratatoskr/ap/following/kenzoishii";
        var followers = "http://localhost:8181/ratatoskr/ap/followers/kenzoishii";
        var liked = "http://localhost:8181/ratatoskr/ap/liked/kenzoishii";
        var featured = StatusCollection.with().build();
        var attachment1 = new PropertyValue("Patreon", "<a href=\"https://www.patreon.com/mastodon\" rel=\"me nofollow noopener noreferrer\" target=\"_blank\"><span class=\"invisible\">https://www.</span><span class=\"\">patreon.com/mastodon</span><span class=\"invisible\"></span}");
        var attachment2 = new PropertyValue("Homepage", "<a href=\\\"https://zeonfederated.com\\\" rel=\\\"me nofollow noopener noreferrer\\\" target=\\\"_blank\\\"><span class=\\\"invisible\\\">https://</span><span class=\\\"\\\">zeonfederated.com</span><span class=\\\"invisible\\\"></span}");
        var attachment = List.of(attachment1, attachment2);
        var alsoKnownAs = "https://mastodon.social/@johndoe";
        var created = ZonedDateTime.now();
        var memorial = false;
        var suspended = true;
        var attributionDomains = List.of("johndoe.com", "joinmastodon.org");

        var originalPerson = Person.with()
            .id(id)
            .preferredUsername(preferredUsername)
            .name(name)
            .summary(summary)
            .url(url)
            .icon(icon)
            .image(image)
            .manuallyApprovesFollowers(manuallyApprovesFollowers)
            .discoverable(discoverable)
            .indexable(indexable)
            .publicKey(publicKey)
            .inbox(inbox)
            .outbox(outbox)
            .following(following)
            .followers(followers)
            .liked(liked)
            .featured(featured)
            .attachment(attachment)
            .alsoKnownAs(alsoKnownAs)
            .created(created)
            .memorial(memorial)
            .suspended(suspended)
            .attributionDomains(attributionDomains)
            .build();

        var copyOfOriginalPerson = Person.with(originalPerson).build();
        assertThat(copyOfOriginalPerson).isEqualTo(originalPerson);
    }


}
