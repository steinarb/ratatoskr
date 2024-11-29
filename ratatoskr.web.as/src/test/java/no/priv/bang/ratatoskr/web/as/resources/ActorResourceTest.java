/*
 * Copyright 2023-2024 Steinar Bang
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
package no.priv.bang.ratatoskr.web.as.resources;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Optional;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.jupiter.api.Test;

import no.priv.bang.ratatoskr.asvocabulary.Person;
import no.priv.bang.ratatoskr.services.RatatoskrService;

class ActorResourceTest {

    @Test
    void testGetActor() {
        var person = Person.with()
            .id("http://localhost:8181/ratatoskr/as/actor/johnd")
            .preferredUsername("johnd")
            .name("John Doe")
            .summary("The common man")
            .inbox("http://localhost:8181/ratatoskr/as/commoninbox")
            .following("http://localhost:8181/ratatoskr/as/following/johnd")
            .followers("http://localhost:8181/ratatoskr/as/followers/johnd")
            .liked("http://localhost:8181/ratatoskr/as/liked/johnd")
            .icon("http://localhost:8181/ratatoskr/image/165987aklre4")
            .build();

        var resource = new ActorResource();
        var ratatoskr = mock(RatatoskrService.class);
        when(ratatoskr.findActorWithUsername(anyString())).thenReturn(Optional.of(person));
        resource.ratatoskr = ratatoskr;
        UriInfo uriInfo = mock(UriInfo.class);
        var baseUri = URI.create("http://localhost:8181/ratatoskr/as");
        when(uriInfo.getBaseUriBuilder())
            .thenReturn(JerseyUriBuilder.fromUri(baseUri))
            .thenReturn(JerseyUriBuilder.fromUri(baseUri))
            .thenReturn(JerseyUriBuilder.fromUri(baseUri))
            .thenReturn(JerseyUriBuilder.fromUri(baseUri))
            .thenReturn(JerseyUriBuilder.fromUri(baseUri));
        var actor = resource.getActor(uriInfo, "johnd");
        assertNotNull(actor);
        assertThat(actor).isEqualTo(person);
    }

    @Test
    void testGetActorWhenNotFound() {
        var resource = new ActorResource();
        var ratatoskr = mock(RatatoskrService.class);
        resource.ratatoskr = ratatoskr;
        UriInfo uriInfo = mock(UriInfo.class);
        var e = assertThrows(NotFoundException.class, () -> resource.getActor(uriInfo, "kenzoishii"));
        assertThat(e.getMessage()).isEqualTo("Did not find actor \"kenzoishii\"");
    }

}
