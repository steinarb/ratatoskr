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

public abstract class BaseIntransitiveActivityBuilder<B extends BaseIntransitiveActivityBuilder<B>> extends BuilderBase<B> {

    protected LinkOrObject actor;
    protected LinkOrObject target;
    protected LinkOrObject origin;
    protected LinkOrObject result;

    protected BaseIntransitiveActivityBuilder() {
        super();
    }

    protected BaseIntransitiveActivityBuilder(IntransitiveActivity source) {
        super(source);
        if (source != null) {
            this.actor = source.actor();
            this.target = source.target();
            this.origin = source.origin();
            this.result = source.result();
        }
    }

    public B actor(LinkOrObject actor) {
        this.actor = actor;
        return self();
    }

    public B actor(String href) {
        this.actor = Link.with().href(href).build();
        return self();
    }

    public B target(LinkOrObject target) {
        this.target = target;
        return self();
    }

    public B target(String href) {
        this.target = Link.with().href(href).build();
        return self();
    }

    public B origin(LinkOrObject origin) {
        this.origin = origin;
        return self();
    }

    public B origin(String href) {
        this.origin = Link.with().href(href).build();
        return self();
    }

    public B result(LinkOrObject result) {
        this.result = result;
        return self();
    }

    public B result(String href) {
        this.result = Link.with().href(href).build();
        return self();
    }

}
