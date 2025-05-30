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
package no.priv.bang.ratatoskr.web.as.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import no.priv.bang.ratatoskr.asvocabulary.Collection;
import no.priv.bang.ratatoskr.asvocabulary.LinkOrObject;
import no.priv.bang.ratatoskr.services.RatatoskrService;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class FollowersResource {

    @Inject
    public RatatoskrService ratatoskr;

    @GET
    @Path("followers/{username}")
    public Collection getFollowers(@Context UriInfo uriInfo, @PathParam("username") String username) {
        List<LinkOrObject> followers = List.copyOf(ratatoskr.findFollowersWithUsername(username));
        return Collection.with()
            .id(followersid(uriInfo, username))
            .totalItems(followers.size())
            .items(followers)
            .current(followers.getFirst())
            .first(followers.getFirst())
            .last(followers.getLast())
            .build();
    }

    private String followersid(UriInfo uriInfo, String username) {
        return uriInfo.getBaseUriBuilder().path("followers").path(username).build().toString();
    }

}
