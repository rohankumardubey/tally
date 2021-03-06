// Copyright (c) 2021 Uber Technologies, Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.uber.m3.tally.m3;

import com.uber.m3.tally.AbstractReporterBenchmark;
import com.uber.m3.util.Duration;
import com.uber.m3.util.ImmutableMap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class M3ReporterBenchmark extends AbstractReporterBenchmark<M3Reporter> {

    @Override
    public M3Reporter bootReporter() {
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 12345);
        return new M3Reporter.Builder(socketAddress)
                .service("test-service")
                .commonTags(ImmutableMap.of("env", "test"))
                .maxQueueSize(Integer.MAX_VALUE)
                .build();
    }

    @Override
    public void shutdownReporter(M3Reporter reporter) {
        reporter.close();

        try {
            // This is required to guarantee that reporter shutdowns cleanly
            // before we start new iteration
            reporter.awaitTermination(Duration.ofSeconds(300));
        } catch (InterruptedException e) {
            // no-op
        }
    }
}
