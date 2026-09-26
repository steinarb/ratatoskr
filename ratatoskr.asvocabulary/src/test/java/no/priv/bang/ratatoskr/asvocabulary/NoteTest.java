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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class NoteTest {

    @Test
    void testCreate() {
        Object context = null;
        String id = "https://sally.example.com/posts/1";
        String name = "What a Crazy Day I Had";
        Map<String, String> nameMap = Collections.emptyMap();
        String summary = "Followers collection";
        Map<String, String> summaryMap = Collections.emptyMap();
        String content = "<div>... you will never believe ...</div>";
        Map<String, String> contentMap = Collections.emptyMap();
        String mediaType = "";
        List<Link> url = Collections.emptyList();
        LinkOrObject attributedTo = Link.with().href("https://sally.example.com").build();
        String duration = "";
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        ZonedDateTime published = null;
        ZonedDateTime updated = null;
        LinkOrObject attachment = null;
        LinkOrObject audience = null;
        LinkOrObject to = null;
        LinkOrObject bcc = null;
        LinkOrObject bto = null;
        LinkOrObject cc = null;
        LinkOrObject generator = null;
        LinkOrObject icon = null;
        LinkOrObject image = null;
        LinkOrObject inReplyTo = null;
        LinkOrObject location = null;
        LinkOrObject preview = null;
        Collection replies = null;
        LinkOrObject tag = null;
        var atomUri = "https://activitypub.academy/users/braussia_vrottariul/statuses/116664828968716446";
        var inReplyToAtomUri = "https://mastodon.social/users/steinarb/statuses/116664951745885706";
        var conversation = "tag:activitypub.academy,2026-05-30:objectId=239324:objectType=Conversation";
        Collection likes = null;
        Collection shares = null;
        var interActionPolicy = new InteractionPolicy(new InteractionSubPolicy(List.of(Link.with().href("http://server/article").build())));

        var note = Note.with()
            .context(context)
            .id(id)
            .name(name)
            .nameMap(nameMap)
            .summary(summary)
            .summaryMap(summaryMap)
            .content(content)
            .contentMap(contentMap)
            .mediaType(mediaType)
            .url(url)
            .attributedTo(attributedTo)
            .duration(duration)
            .startTime(startTime)
            .endTime(endTime)
            .published(published)
            .updated(updated)
            .attachment(attachment)
            .audience(audience)
            .to(to)
            .bcc(bcc)
            .bto(bto)
            .cc(cc)
            .generator(generator)
            .icon(icon)
            .image(image)
            .inReplyTo(inReplyTo)
            .location(location)
            .preview(preview)
            .replies(replies)
            .tag(tag)
            .atomUri(atomUri)
            .inReplyToAtomUri(inReplyToAtomUri)
            .conversation(conversation)
            .likes(likes)
            .shares(shares)
            .sensitive(true)
            .interactionPolicy(interActionPolicy)
            .build();

        assertThat(note.type()).isEqualTo(ActivityStreamObjectType.Note);
        assertThat(note.name()).isEqualTo(name);
        assertThat(note.content()).isEqualTo(content);
        assertThat(note.attributedTo()).isEqualTo(attributedTo);
        assertThat(note.sensitive()).isTrue();
        assertThat(note.interactionPolicy()).isEqualTo(interActionPolicy);
    }

    @Test
    void testCopyAndModify() {
        Object context = null;
        String id = "https://sally.example.com/posts/1";
        String name = "What a Crazy Day I Had";
        Map<String, String> nameMap = Collections.emptyMap();
        String summary = "Followers collection";
        Map<String, String> summaryMap = Collections.emptyMap();
        String content = "<div>... you will never believe ...</div>";
        Map<String, String> contentMap = Collections.emptyMap();
        String mediaType = "";
        List<Link> url = Collections.emptyList();
        String duration = "";
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        ZonedDateTime published = null;
        ZonedDateTime updated = null;
        LinkOrObject attachment = null;
        LinkOrObject audience = null;
        LinkOrObject to = null;
        LinkOrObject bcc = null;
        LinkOrObject bto = null;
        LinkOrObject cc = null;
        LinkOrObject generator = null;
        LinkOrObject icon = null;
        LinkOrObject image = null;
        LinkOrObject inReplyTo = null;
        LinkOrObject location = null;
        LinkOrObject preview = null;
        Collection replies = null;
        LinkOrObject tag = null;

        var note = Note.with()
            .context(context)
            .id(id)
            .name(name)
            .nameMap(nameMap)
            .summary(summary)
            .summaryMap(summaryMap)
            .content(content)
            .contentMap(contentMap)
            .mediaType(mediaType)
            .url(url)
            .attributedTo((Link)null)
            .duration(duration)
            .startTime(startTime)
            .endTime(endTime)
            .published(published)
            .updated(updated)
            .attachment(attachment)
            .audience(audience)
            .to(to)
            .bcc(bcc)
            .bto(bto)
            .cc(cc)
            .generator(generator)
            .icon(icon)
            .image(image)
            .inReplyTo(inReplyTo)
            .location(location)
            .preview(preview)
            .replies(replies)
            .tag(tag)
            .build();

        var attributedTo = Link.with().href("https://sally.example.com").build();
        var noteCopy = Note.with(note).attributedTo(attributedTo).build();

        assertThat(noteCopy.type()).isEqualTo(ActivityStreamObjectType.Note);
        assertThat(noteCopy.name()).isEqualTo(name);
        assertThat(noteCopy.content()).isEqualTo(content);
        assertThat(noteCopy.attributedTo()).isEqualTo(attributedTo);
    }

}
