/*
 * Copyright 2015 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.server.file;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import com.linecorp.armeria.common.HttpHeaders;

import io.netty.util.AsciiString;

/**
 * {@link HttpFileService} configuration.
 */
public final class HttpFileServiceConfig {

    private final HttpVfs vfs;
    private final Clock clock;
    private final int maxCacheEntries;
    private final int maxCacheEntrySizeBytes;
    private final boolean serveCompressedFiles;
    private final boolean autoIndex;
    private final HttpHeaders headers;

    HttpFileServiceConfig(HttpVfs vfs, Clock clock, int maxCacheEntries, int maxCacheEntrySizeBytes,
                          boolean serveCompressedFiles, boolean autoIndex, HttpHeaders headers) {
        this.vfs = requireNonNull(vfs, "vfs");
        this.clock = requireNonNull(clock, "clock");
        this.maxCacheEntries = validateMaxCacheEntries(maxCacheEntries);
        this.maxCacheEntrySizeBytes = validateMaxCacheEntrySizeBytes(maxCacheEntrySizeBytes);
        this.serveCompressedFiles = serveCompressedFiles;
        this.autoIndex = autoIndex;
        this.headers = requireNonNull(headers, "headers");
    }

    static int validateMaxCacheEntries(int maxCacheEntries) {
        return validateNonNegativeParameter(maxCacheEntries, "maxCacheEntries");
    }

    static int validateMaxCacheEntrySizeBytes(int maxCacheEntrySizeBytes) {
        return validateNonNegativeParameter(maxCacheEntrySizeBytes, "maxCacheEntrySizeBytes");
    }

    private static int validateNonNegativeParameter(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + ": " + value + " (expected: >= 0)");
        }
        return value;
    }

    /**
     * Returns the {@link HttpVfs} that provides the static files to an {@link HttpFileService}.
     */
    public HttpVfs vfs() {
        return vfs;
    }

    /**
     * Returns the {@link Clock} the provides the current date and time to an {@link HttpFileService}.
     */
    public Clock clock() {
        return clock;
    }

    /**
     * Returns the maximum allowed number of cached file entries.
     */
    public int maxCacheEntries() {
        return maxCacheEntries;
    }

    /**
     * Returns the maximum allowed size of a cached file entry. Files bigger than this value will not be
     * cached.
     */
    public int maxCacheEntrySizeBytes() {
        return maxCacheEntrySizeBytes;
    }

    /**
     * Returns whether pre-compressed files should be served.
     */
    public boolean serveCompressedFiles() {
        return serveCompressedFiles;
    }

    /**
     * Returns whether a directory listing for a directory without an {@code index.html} file will be
     * auto-generated.
     */
    public boolean autoIndex() {
        return autoIndex;
    }

    /**
     * Returns the additional {@link HttpHeaders} to send in a response.
     */
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public String toString() {
        return toString(this, vfs(), clock(), maxCacheEntries(), maxCacheEntrySizeBytes(),
                        serveCompressedFiles(), autoIndex(), headers());
    }

    static String toString(Object holder, HttpVfs vfs, Clock clock,
                           int maxCacheEntries, int maxCacheEntrySizeBytes,
                           boolean serveCompressedFiles, boolean autoIndex,
                           @Nullable Iterable<Entry<AsciiString, String>> headers) {

        return MoreObjects.toStringHelper(holder).omitNullValues()
                          .add("vfs", vfs)
                          .add("clock", clock)
                          .add("maxCacheEntries", maxCacheEntries)
                          .add("maxCacheEntrySizeBytes", maxCacheEntrySizeBytes)
                          .add("serveCompressedFiles", serveCompressedFiles)
                          .add("autoIndex", autoIndex)
                          .add("headers", headers)
                          .toString();
    }
}
