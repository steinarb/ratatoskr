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
import no.priv.bang.ratatoskr.asvocabulary.Person;

class PollStatusTest {

    @Test
    void testCreatePollStatus() {
        var type = ActivityStreamObjectType.Question;
        var id = "http://localhost/ap/statuses/12345679";
        var content = "Does the squirrel Ratatoskr exist?";
        var name = "Ratatoskr real or not";
        var summary = "Is Ratatoskr real or a figment of the imagination";
        var sensitive = false;
        var inReplyTo = PollStatus.with().build();
        var published = ZonedDateTime.now();
        var url = id;
        var authoredBy = Person.with().id("http://localhost/ap/person/jd").preferredUsername("jd").name("John Doe").build();
        var to = List.of(Person.with().id("http://john.example.org").build());
        var cc = List.of(Person.with().id("http://sally.example.org").build());
        List<Tag> tags = List.of(Mention.with().build(), Hashtag.with().build(), Emoji.with().build());
        List<Document> attachments = List.of(Image.with().build(), Video.with().build(), Audio.with().build());
        var replies = StatusCollection.with().build();
        var likes = ActivityCollection.with().build();
        var shares = AnnounceCollection.with().build();
        var endTime = ZonedDateTime.now().plusDays(2);
        var closed = false;
        var votersCount = PersonCollection.with().build();
        var replies1 = PersonCollection.with().totalItems(3).build();
        var pollItem1 = new PollItem("Is real", replies1);
        var pollItem2 = new PollItem("Is not real", replies1);
        var oneOf = List.of(pollItem1, pollItem2);
        var anyOf = List.of(pollItem2, pollItem1);

        var status = PollStatus.with()
            .id(id)
            .content(content)
            .name(name)
            .summary(summary)
            .sensitive(sensitive)
            .inReplyTo(inReplyTo)
            .published(published)
            .url(url)
            .authoredBy(authoredBy)
            .to(to)
            .cc(cc)
            .tags(tags)
            .attachments(attachments)
            .replies(replies)
            .likes(likes)
            .shares(shares)
            .endTime(endTime)
            .closed(closed)
            .votersCount(votersCount)
            .oneOf(oneOf)
            .anyOf(anyOf)
            .build();

        assertThat(status.type()).isEqualTo(type);
        assertThat(status.id()).isEqualTo(id);
        assertThat(status.content()).isEqualTo(content);
        assertThat(status.name()).isEqualTo(name);
        assertThat(status.summary()).isEqualTo(summary);
        assertThat(status.sensitive()).isEqualTo(sensitive);
        assertThat(status.inReplyTo()).isEqualTo(inReplyTo);
        assertThat(status.published()).isEqualTo(published);
        assertThat(status.authoredBy()).isEqualTo(authoredBy);
        assertThat(status.to()).isEqualTo(to);
        assertThat(status.cc()).isEqualTo(cc);
        assertThat(status.tags()).isEqualTo(tags);
        assertThat(status.attachments()).isEqualTo(attachments);
        assertThat(status.replies()).isEqualTo(replies);
        assertThat(status.likes()).isEqualTo(likes);
        assertThat(status.shares()).isEqualTo(shares);
        assertThat(status.endTime()).isEqualTo(endTime);
        assertThat(status.closed()).isEqualTo(closed);
        assertThat(status.votersCount()).isEqualTo(votersCount);
        assertThat(status.oneOf()).isEqualTo(oneOf);
        assertThat(status.anyOf()).isEqualTo(anyOf);
    }

}
