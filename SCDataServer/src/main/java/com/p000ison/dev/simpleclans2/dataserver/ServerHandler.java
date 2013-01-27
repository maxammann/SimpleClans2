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
 *     Last modified: 26.01.13 18:06
 */

package com.p000ison.dev.simpleclans2.dataserver;

import com.p000ison.dev.simpleclans2.dataserver.handshake.HandshakeEvent;
import org.jboss.netty.channel.*;

/**
 * Represents a ServerHandler
 */
public class ServerHandler extends SimpleChannelUpstreamHandler {

    private Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        server.registerChannel(ctx.getChannel());
        super.channelConnected(ctx, e);
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof HandshakeEvent) {
            if (!((HandshakeEvent) e).isSuccessful()) {
                return;
            }
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println(e.getMessage());
        String message = (String) e.getMessage();

        if (message.equalsIgnoreCase("stop")) {
            server.stop();
        }
        super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        server.handleException(e);
    }
}
