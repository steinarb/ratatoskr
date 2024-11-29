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
package no.priv.bang.ratatoskr.web.as.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import no.priv.bang.ratatoskr.asvocabulary.Actor;
import no.priv.bang.ratatoskr.asvocabulary.Person;
import no.priv.bang.ratatoskr.services.RatatoskrService;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class ActorResource {

    @Inject
    public RatatoskrService ratatoskr;

    @GET
    @Path("actor/{username}")
    public Actor getActor(@Context UriInfo uriInfo, @PathParam("username") String username ) {
        return ratatoskr.findActorWithUsername(username)
            .map(a -> setUrls(a, uriInfo))
            .orElseThrow(() -> new NotFoundException("Did not find actor \"" + username + "\""));
    }

    private Actor setUrls(Actor actor, UriInfo uriInfo) {
        return Person.with(actor)
            .inbox(commonInbox(uriInfo))
            .following(following(uriInfo, actor))
            .followers(followers(uriInfo, actor))
            .liked(liked(uriInfo, actor))
            .build();
    }

    private String commonInbox(UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path("commoninbox").build().toString();
    }

    private String following(UriInfo uriInfo, Actor actor) {
        return uriInfo.getBaseUriBuilder().path("following").path(actor.preferredUsername()).build().toString();
    }

    private String followers(UriInfo uriInfo, Actor actor) {
        return uriInfo.getBaseUriBuilder().path("followers").path(actor.preferredUsername()).build().toString();
    }

    private String liked(UriInfo uriInfo, Actor actor) {
        return uriInfo.getBaseUriBuilder().path("liked").path(actor.preferredUsername()).build().toString();
    }

}
