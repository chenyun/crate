/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.operation.reference.sys.node;

import io.crate.operation.reference.sys.SysNodeObjectReference;
import org.elasticsearch.monitor.network.NetworkStats;

class NodeNetworkTCPExpression extends SysNodeObjectReference {

    public static final String NAME = "tcp";

    public NodeNetworkTCPExpression(NetworkStats stats) {
        childImplementations.put(TCPConnectionsExpression.NAME, new TCPConnectionsExpression(stats));
        childImplementations.put(TCPPacketsExpression.NAME, new TCPPacketsExpression(stats));
    }

    static class TCPConnectionsExpression extends SysNodeObjectReference {

        public static final String NAME = "connections";

        private static final String INITIATED = "initiated";
        private static final String ACCEPTED = "accepted";
        private static final String CURR_ESTABLISHED = "curr_established";
        private static final String DROPPED = "dropped";
        private static final String EMBRYONIC_DROPPED = "embryonic_dropped";

        protected TCPConnectionsExpression(NetworkStats stats) {
            addChildImplementations(stats);
        }

        private void addChildImplementations(final NetworkStats stats) {
            childImplementations.put(INITIATED, new TCPConnectionsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().activeOpens();
                }
            });
            childImplementations.put(ACCEPTED, new TCPConnectionsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().passiveOpens();
                }
            });
            childImplementations.put(CURR_ESTABLISHED, new TCPConnectionsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().currEstab();
                }
            });
            childImplementations.put(DROPPED, new TCPConnectionsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().estabResets();
                }
            });
            childImplementations.put(EMBRYONIC_DROPPED, new TCPConnectionsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().attemptFails();
                }
            });
        }

        private abstract class TCPConnectionsChildExpression extends SysNodeExpression<Long> {
        }
    }

    static class TCPPacketsExpression extends SysNodeObjectReference {

        public static final String NAME = "packets";

        private static final String SENT = "sent";
        private static final String RECEIVED = "received";
        private static final String RETRANSMITTED = "retransmitted";
        private static final String ERRORS_RECEIVED = "errors_received";
        private static final String RST_SENT = "rst_sent";

        protected TCPPacketsExpression(NetworkStats stats) {
            addChildImplementations(stats);
        }

        private void addChildImplementations(final NetworkStats stats) {
            childImplementations.put(SENT, new TCPPacketsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().outSegs();
                }
            });
            childImplementations.put(RECEIVED, new TCPPacketsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().inSegs();
                }
            });
            childImplementations.put(RETRANSMITTED, new TCPPacketsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().retransSegs();
                }
            });
            childImplementations.put(ERRORS_RECEIVED, new TCPPacketsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().inErrs();
                }
            });
            childImplementations.put(RST_SENT, new TCPPacketsChildExpression() {
                @Override
                public Long value() {
                    return stats.tcp().outRsts();
                }
            });
        }

        private abstract class TCPPacketsChildExpression extends SysNodeExpression<Long> {
        }

    }

}
