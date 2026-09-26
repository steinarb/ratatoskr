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

public class DocumentBuilderBase<B extends DocumentBuilderBase<B>> extends BuilderBase<B> {
    protected int width;
    protected int height;
    protected String blurhash;

    public DocumentBuilderBase() {
        super();
    }

    public DocumentBuilderBase(Document source) {
        super(source);
        if (source != null) {
            width = source.width();
            height = source.height();
            blurhash = source.blurhash();
        }
    }

    public B width(int width) {
        this.width = width;
        return self();
    }

    public B height(int height) {
        this.height = height;
        return self();
    }

    public B blurhash(String blurhash) {
        this.blurhash = blurhash;
        return self();
    }

}
