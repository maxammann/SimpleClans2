/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 26.01.13 21:40
 */

package com.p000ison.dev.simpleclans2.dataserver;

import com.p000ison.dev.simpleclans2.dataserver.handshake.HandshakeHandler;
import com.p000ison.dev.simpleclans2.dataserver.util.Logging;
import org.jboss.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a TimeoutHandler
 */
public class TimeoutHandler implements Runnable {
    private final ConcurrentLinkedQueue<TimeoutChannel> channels = new ConcurrentLinkedQueue<TimeoutChannel>();

    public void await(ChannelHandlerContext ctx, Runnable runnable) {
        channels.add(new TimeoutChannel(ctx, runnable));
    }

    public void cancel(ChannelHandlerContext ctx) {
        for (Iterator<TimeoutChannel> it = channels.iterator(); it.hasNext(); ) {
            TimeoutChannel channel = it.next();
            if (channel.getContext().equals(ctx)) {
                it.remove();
            }
        }
    }

    public void clear() {
        channels.clear();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {

        while (true) {
            if (channels.isEmpty()) {
                continue;
            }

            long current = System.currentTimeMillis();

            for (Iterator<TimeoutChannel> it = channels.iterator(); it.hasNext(); ) {
                TimeoutChannel channel = it.next();
                if (current - HandshakeHandler.CLIENT_TIMEOUT > channel.getTime()) {
                    channel.getGoal().run();
                    it.remove();
                }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Logging.debug(e);
            }
        }
    }

    private static class TimeoutChannel {
        private ChannelHandlerContext ctx;
        private long time;
        private Runnable goal;

        private TimeoutChannel(ChannelHandlerContext ctx, Runnable goal) {
            this.ctx = ctx;
            this.time = System.currentTimeMillis();
            this.goal = goal;
        }

        public ChannelHandlerContext getContext() {
            return ctx;
        }

        public long getTime() {
            return time;
        }

        public Runnable getGoal() {
            return goal;
        }
    }
}
