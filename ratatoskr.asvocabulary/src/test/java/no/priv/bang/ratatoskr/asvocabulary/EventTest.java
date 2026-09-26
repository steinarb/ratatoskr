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

import static org.assertj.core.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void testCreateObject() {
        var now = ZonedDateTime.now();
        var urlLink = Link.with().href("https://example.com").build();
        var atomUriLink = Link.with().href("https://example.com").build();
        var inReplyToAtomUriLink = Link.with().href("https://example.com/article/123").build();
        var attributedToVal = Link.with().href("https://example.com/person/sally").build();
        var attachmentVal = Link.with().href("https://example.com/files/347").build();
        var audienceVal = Link.with().href("https://example.com/groups/678").build();
        var toVal = Link.with().href("https://example.com/followersof/sally").build();
        var bccVal = Link.with().href("https://example.com/localusers").build();
        var btoVal = Link.with().href("https://example.com/localusers").build();
        var ccVal = Link.with().href("https://example.com/localusers").build();
        var generatorVal = Link.with().href("https://example.com").build();
        var iconVal = Link.with().href("https://example.com/gemerator/1").build();
        var imageVal = Link.with().href("https://example.com/images/897").build();
        var inReplyToVal = Link.with().href("https://example.com/article/777").build();
        var locationVal = Link.with().href("https://example.com/locations/987").build();
        var previewVal = Link.with().href("https://example.com/preview/4456").build();
        var tagVal = Link.with().href("https://example.com/tags/norskpix").build();
        var repliesVal = Collection.with().id("https://example.com/replies").build();
        var likesVal = Collection.with().id("https://example.com/likes").build();
        var sharesVal = Collection.with().id("https://example.com/shares").build();

        var asobject = Event.with()
            .id("https://someserver.somedomain")
            .name("Test Name")
            .nameMap(Map.of("en", "Test Name"))
            .summary("Test Summary")
            .summaryMap(Map.of("en", "Test Summary"))
            .content("Test Content")
            .contentMap(Map.of("en", "Test Content"))
            .mediaType("text/html")
            .url(List.of(urlLink))
            .attributedTo(attributedToVal)
            .duration("PT1H")
            .startTime(now)
            .endTime(now.plusHours(1))
            .published(now)
            .updated(now)
            .attachment(attachmentVal)
            .audience(audienceVal)
            .to(toVal)
            .bcc(bccVal)
            .bto(btoVal)
            .cc(ccVal)
            .generator(generatorVal)
            .icon(iconVal)
            .image(imageVal)
            .inReplyTo(inReplyToVal)
            .location(locationVal)
            .preview(previewVal)
            .replies(repliesVal)
            .tag(tagVal)
            .atomUri(atomUriLink)
            .inReplyToAtomUri(inReplyToAtomUriLink)
            .conversation("https://someserver.somedomain")
            .likes(likesVal)
            .shares(sharesVal)
            .build();

        assertThat(asobject).isNotNull();
        assertThat(asobject.context()).isNull();
        assertThat(asobject.type()).isEqualTo(ActivityStreamObjectType.Event);
        assertThat(asobject.id()).isEqualTo("https://someserver.somedomain");
        assertThat(asobject.name()).isEqualTo("Test Name");
        assertThat(asobject.nameMap()).containsEntry("en", "Test Name");
        assertThat(asobject.summary()).isEqualTo("Test Summary");
        assertThat(asobject.summaryMap()).containsEntry("en", "Test Summary");
        assertThat(asobject.content()).isEqualTo("Test Content");
        assertThat(asobject.contentMap()).containsEntry("en", "Test Content");
        assertThat(asobject.mediaType()).isEqualTo("text/html");
        assertThat(asobject.url()).containsExactly(urlLink);
        assertThat(asobject.attributedTo()).isEqualTo(attributedToVal);
        assertThat(asobject.duration()).isEqualTo("PT1H");
        assertThat(asobject.startTime()).isEqualTo(now);
        assertThat(asobject.endTime()).isEqualTo(now.plusHours(1));
        assertThat(asobject.published()).isEqualTo(now);
        assertThat(asobject.updated()).isEqualTo(now);
        assertThat(asobject.attachment()).isEqualTo(attachmentVal);
        assertThat(asobject.audience()).isEqualTo(audienceVal);
        assertThat(asobject.to()).isEqualTo(toVal);
        assertThat(asobject.bcc()).isEqualTo(bccVal);
        assertThat(asobject.bto()).isEqualTo(btoVal);
        assertThat(asobject.cc()).isEqualTo(ccVal);
        assertThat(asobject.generator()).isEqualTo(generatorVal);
        assertThat(asobject.icon()).isEqualTo(iconVal);
        assertThat(asobject.image()).isEqualTo(imageVal);
        assertThat(asobject.inReplyTo()).isEqualTo(inReplyToVal);
        assertThat(asobject.location()).isEqualTo(locationVal);
        assertThat(asobject.preview()).isEqualTo(previewVal);
        assertThat(asobject.replies()).isEqualTo(repliesVal);
        assertThat(asobject.tag()).isEqualTo(tagVal);
        assertThat(asobject.atomUri()).isEqualTo(atomUriLink);
        assertThat(asobject.inReplyToAtomUri()).isEqualTo(inReplyToAtomUriLink);
        assertThat(asobject.conversation()).isEqualTo("https://someserver.somedomain");
        assertThat(asobject.likes()).isEqualTo(likesVal);
        assertThat(asobject.shares()).isEqualTo(sharesVal);
    }

    @Test
    void testCreateObjectUsingJustStringHrefValuesForLinks() {
        var now = ZonedDateTime.now();
        var urlLink = Link.with().href("https://example.com").build();
        var atomUriLink = Link.with().href("https://example.com").build();
        var inReplyToAtomUriLink = Link.with().href("https://example.com/article/123").build();
        var repliesVal = Collection.with().id("https://example.com/replies").build();
        var likesVal = Collection.with().id("https://example.com/likes").build();
        var sharesVal = Collection.with().id("https://example.com/shares").build();

        var asobject = Event.with()
            .id("https://someserver.somedomain")
            .name("Test Name")
            .nameMap(Map.of("en", "Test Name"))
            .summary("Test Summary")
            .summaryMap(Map.of("en", "Test Summary"))
            .content("Test Content")
            .contentMap(Map.of("en", "Test Content"))
            .mediaType("text/html")
            .url(List.of(urlLink))
            .attributedTo("https://example.com/person/sally")
            .duration("PT1H")
            .startTime(now)
            .endTime(now.plusHours(1))
            .published(now)
            .updated(now)
            .attachment("https://example.com/files/347")
            .audience("https://example.com/groups/678")
            .to("https://example.com/followersof/sally")
            .bcc("https://example.com/localusers")
            .bto("https://example.com/localusers")
            .cc("https://example.com/localusers")
            .generator("https://example.com")
            .icon("https://example.com/gemerator/1")
            .image("https://example.com/images/897")
            .inReplyTo("https://example.com/article/777")
            .location("https://example.com/locations/987")
            .preview("https://example.com/preview/4456")
            .replies(repliesVal)
            .tag("https://example.com/tags/norskpix")
            .atomUri(atomUriLink)
            .inReplyToAtomUri(inReplyToAtomUriLink)
            .conversation("https://someserver.somedomain")
            .likes(likesVal)
            .shares(sharesVal)
            .build();

        assertThat(asobject).isNotNull();
        assertThat(asobject.context()).isNull();
        assertThat(asobject.type()).isEqualTo(ActivityStreamObjectType.Event);
        assertThat(asobject.id()).isEqualTo("https://someserver.somedomain");
        assertThat(asobject.name()).isEqualTo("Test Name");
        assertThat(asobject.nameMap()).containsEntry("en", "Test Name");
        assertThat(asobject.summary()).isEqualTo("Test Summary");
        assertThat(asobject.summaryMap()).containsEntry("en", "Test Summary");
        assertThat(asobject.content()).isEqualTo("Test Content");
        assertThat(asobject.contentMap()).containsEntry("en", "Test Content");
        assertThat(asobject.mediaType()).isEqualTo("text/html");
        assertThat(asobject.url()).containsExactly(urlLink);
        assertThat(asobject.duration()).isEqualTo("PT1H");
        assertThat(asobject.startTime()).isEqualTo(now);
        assertThat(asobject.endTime()).isEqualTo(now.plusHours(1));
        assertThat(asobject.published()).isEqualTo(now);
        assertThat(asobject.updated()).isEqualTo(now);
        assertThat(asobject.replies()).isEqualTo(repliesVal);
        assertThat(asobject.atomUri()).isEqualTo(atomUriLink);
        assertThat(asobject.inReplyToAtomUri()).isEqualTo(inReplyToAtomUriLink);
        assertThat(asobject.conversation()).isEqualTo("https://someserver.somedomain");
        assertThat(asobject.likes()).isEqualTo(likesVal);
        assertThat(asobject.shares()).isEqualTo(sharesVal);
        assertThat(asobject.attributedTo()).extracting("href").isEqualTo("https://example.com/person/sally");
        assertThat(asobject.attachment()).extracting("href").isEqualTo("https://example.com/files/347");
        assertThat(asobject.audience()).extracting("href").isEqualTo("https://example.com/groups/678");
        assertThat(asobject.to()).extracting("href").isEqualTo("https://example.com/followersof/sally");
        assertThat(asobject.bcc()).extracting("href").isEqualTo("https://example.com/localusers");
        assertThat(asobject.bto()).extracting("href").isEqualTo("https://example.com/localusers");
        assertThat(asobject.cc()).extracting("href").isEqualTo("https://example.com/localusers");
        assertThat(asobject.generator()).extracting("href").isEqualTo("https://example.com");
        assertThat(asobject.icon()).extracting("href").isEqualTo("https://example.com/gemerator/1");
        assertThat(asobject.image()).extracting("href").isEqualTo("https://example.com/images/897");
        assertThat(asobject.inReplyTo()).extracting("href").isEqualTo("https://example.com/article/777");
        assertThat(asobject.location()).extracting("href").isEqualTo("https://example.com/locations/987");
        assertThat(asobject.preview()).extracting("href").isEqualTo("https://example.com/preview/4456");
        assertThat(asobject.tag()).extracting("href").isEqualTo("https://example.com/tags/norskpix");
    }

    @Test
    void testCopyObject() {
        var original = Event.with()
            .id("https://someserver.somedomain")
            .name("Test Name")
            .nameMap(Map.of("en", "Test Name"))
            .summary("Test Summary")
            .summaryMap(Map.of("en", "Test Summary"))
            .content("Test Content")
            .contentMap(Map.of("en", "Test Content"))
            .mediaType("text/html")
            .url(List.of(Link.with().href("http://example.com/samle").build()))
            .attributedTo("https://example.com/person/sally")
            .duration("PT1H")
            .startTime(ZonedDateTime.now())
            .endTime(ZonedDateTime.now().plusHours(1))
            .published(ZonedDateTime.now())
            .updated(ZonedDateTime.now())
            .attachment("https://example.com/files/347")
            .audience("https://example.com/groups/678")
            .to("https://example.com/followersof/sally")
            .bcc("https://example.com/localusers")
            .bto("https://example.com/localusers")
            .cc("https://example.com/localusers")
            .generator("https://example.com")
            .icon("https://example.com/gemerator/1")
            .image("https://example.com/images/897")
            .inReplyTo("https://example.com/article/777")
            .location("https://example.com/locations/987")
            .preview("https://example.com/preview/4456")
            .replies(Collection.with().build())
            .tag("https://example.com/tags/norskpix")
            .atomUri(Link.with().build())
            .conversation("https://someserver.somedomain")
            .build();

        var copy = Event.with(original).build();

        assertThat(copy).isEqualTo(original);
    }

    @Test
    void testCopyAndMutateObject() {
        var original = Event.with()
            .id("https://someserver.somedomain")
            .name("Test Name")
            .nameMap(Map.of("en", "Test Name"))
            .summary("Test Summary")
            .summaryMap(Map.of("en", "Test Summary"))
            .content("Test Content")
            .contentMap(Map.of("en", "Test Content"))
            .mediaType("text/html")
            .url(List.of(Link.with().href("http://example.com/samle").build()))
            .attributedTo("https://example.com/person/sally")
            .duration("PT1H")
            .startTime(ZonedDateTime.now())
            .endTime(ZonedDateTime.now().plusHours(1))
            .published(ZonedDateTime.now())
            .updated(ZonedDateTime.now())
            .attachment("https://example.com/files/347")
            .audience("https://example.com/groups/678")
            .to("https://example.com/followersof/sally")
            .bcc("https://example.com/localusers")
            .bto("https://example.com/localusers")
            .cc("https://example.com/localusers")
            .generator("https://example.com")
            .icon("https://example.com/gemerator/1")
            .image("https://example.com/images/897")
            .inReplyTo("https://example.com/article/777")
            .location("https://example.com/locations/987")
            .preview("https://example.com/preview/4456")
            .replies(Collection.with().build())
            .tag("https://example.com/tags/norskpix")
            .atomUri(Link.with().build())
            .conversation("https://someserver.somedomain")
            .build();

        var mutatedCopy = Event.with(original)
            .name("Replace")
            .build();

        assertThat(mutatedCopy).isNotEqualTo(original);
        assertThat(mutatedCopy)
            .usingRecursiveComparison()
            .ignoringFields("name")
            .isEqualTo(original);
        assertThat(mutatedCopy.name()).isEqualTo("Replace");
    }

}
