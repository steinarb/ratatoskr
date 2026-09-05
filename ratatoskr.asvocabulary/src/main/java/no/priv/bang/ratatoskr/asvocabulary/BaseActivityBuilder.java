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

public abstract class BaseActivityBuilder<B extends BaseActivityBuilder<B>> extends BaseIntransitiveActivityBuilder<B> {

    protected LinkOrObject object;
    protected LinkOrObject instrument;
    protected Signature signature;

    protected BaseActivityBuilder() {
        super();
    }

    protected BaseActivityBuilder(Activity source) {
        super(source);
        if (source != null) {
            this.object = source.object();
            this.instrument = source.instrument();
            this.signature = source.signature();
        }
    }

    public B object(LinkOrObject object) {
        this.object = object;
        return self();
    }

    public B object(String href) {
        this.object = Link.with().href(href).build();
        return self();
    }

    public B instrument(LinkOrObject instrument) {
        this.instrument = instrument;
        return self();
    }

    public B instrument(String href) {
        this.instrument = Link.with().href(href).build();
        return self();
    }

    public B signature(Signature signature) {
        this.signature = signature;
        return self();
    }

}
