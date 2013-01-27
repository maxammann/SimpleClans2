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
 *     Last modified: 26.01.13 19:01
 */

package com.p000ison.dev.simpleclans2.dataserver.handshake;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.Channels;

/**
 * Represents a HandshakeEvent
 */
public class HandshakeEvent implements ChannelEvent {

    private final String clientVersion;
    private final Channel channel;

    private HandshakeEvent(String clientVersion, Channel channel) {
        this.clientVersion = clientVersion;
        this.channel = channel;
    }

    public static HandshakeEvent success(String clientVersion, Channel channel) {
        return new HandshakeEvent(clientVersion, channel);
    }

    public static HandshakeEvent fail(Channel channel) {
        return new HandshakeEvent(null, channel);
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public ChannelFuture getFuture() {
        return Channels.succeededFuture(this.channel);
    }

    public boolean isSuccessful() {
        return clientVersion != null;
    }

    public String getClientVersion() {
        return clientVersion;
    }
}
