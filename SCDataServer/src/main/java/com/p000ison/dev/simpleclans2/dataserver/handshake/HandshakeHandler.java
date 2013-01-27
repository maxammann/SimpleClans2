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
 *     Last modified: 26.01.13 18:23
 */

package com.p000ison.dev.simpleclans2.dataserver.handshake;

import com.p000ison.dev.simpleclans2.dataserver.Server;
import com.p000ison.dev.simpleclans2.dataserver.util.Logging;
import org.jboss.netty.channel.*;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a HandshakeHandler
 */
public class HandshakeHandler extends SimpleChannelHandler {

    private static final String HANDSHAKE_VERSION = "1.0";
    public static final int CLIENT_TIMEOUT = 2000;

    private final AtomicBoolean handshakeComplete = new AtomicBoolean(false);
    private final AtomicBoolean handshakeFailed = new AtomicBoolean(false);
    private final Server server;

    public HandshakeHandler(Server server) {
        this.server = server;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (this.handshakeFailed.get()) {
            return;
        }

        if (this.handshakeComplete.get()) {
            super.messageReceived(ctx, e);
            return;
        }

        if (!(e.getMessage() instanceof String)) {
            fireHandshakeFailed(ctx);
            return;
        }

        String handshake = (String) e.getMessage(); // Example: 1.0:username:password
        String[] params = handshake.trim().split(":");

        if (params.length != 3) {
            Logging.debug("Invalid handshake: expecting 3 params, got %d -> '%s'", params.length, handshake);
            this.fireHandshakeFailed(ctx);
            return;
        }

        // Validating the client and server version
        if (!HANDSHAKE_VERSION.equals(params[0])) {
            Logging.debug("Invalid handshake: The version handshake version is %s and client thinks it's %s", HANDSHAKE_VERSION, params[0]);
            this.fireHandshakeFailed(ctx);
            return;
        }

        // Validate the the user and password
        if (!params[1].equals("user") && !params[2].equals("pass")) {
            Logging.debug("Invalid handshake: Login: '" + params[1] + "'");
            this.fireHandshakeFailed(ctx);
            return;
        }

        String response = "SUCCESS\n";
        writeDownstream(ctx, response);

        ctx.getPipeline().remove(this);
        server.registerChannel(ctx.getChannel());
        this.fireHandshakeSucceeded(params[0], ctx);
    }

    private void writeDownstream(ChannelHandlerContext ctx, Object data) {
        ChannelFuture f = Channels.succeededFuture(ctx.getChannel());
        SocketAddress address = ctx.getChannel().getRemoteAddress();
        Channel c = ctx.getChannel();

        ctx.sendDownstream(new DownstreamMessageEvent(c, f, data, address));
    }

    @Override
    public void channelConnected(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Logging.debug("Incoming connection established from: " + e.getChannel().getRemoteAddress());

        server.getTimeoutHandler().await(ctx, new Runnable() {

            @Override
            public void run() {
                if (handshakeFailed.get()) {
                    Logging.debug("[Handshake] Handshake for client %s already failed.", ctx.getChannel().getRemoteAddress());
                    return;
                }

                if (!handshakeComplete.get()) {
                    synchronized (this) {
                        Logging.debug("[Handshake] Client %s timed out.", ctx.getChannel().getRemoteAddress());
                        ctx.sendUpstream(HandshakeEvent.fail(ctx.getChannel()));
                        handshakeFailed.set(true);
                        ctx.getChannel().close();
                    }
                } else {
                    Logging.debug("[Handshake] Client %s authorized after %s ms.", ctx.getChannel().getRemoteAddress(), CLIENT_TIMEOUT);
                }
            }
        });

        super.channelConnected(ctx, e);
    }

    private void fireHandshakeFailed(ChannelHandlerContext ctx) {
        synchronized (this) {
            handshakeComplete.set(true);
            handshakeFailed.set(true);
            server.getTimeoutHandler().cancel(ctx);
            ctx.getChannel().close();
            ctx.sendUpstream(HandshakeEvent.fail(ctx.getChannel()));
        }
    }

    private void fireHandshakeSucceeded(String version, ChannelHandlerContext ctx) {
        synchronized (this) {
            handshakeComplete.set(true);
            handshakeFailed.set(false);
            server.getTimeoutHandler().cancel(ctx);
            ctx.sendUpstream(HandshakeEvent.success(version, ctx.getChannel()));
            Logging.debug("Handshake successful, connection to %s (version: %s) is up.", ctx.getChannel().getRemoteAddress(), version);
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Logging.debug("Connection to %s closed!", e.getChannel().getRemoteAddress());
        super.channelClosed(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        fireHandshakeFailed(ctx);
        server.handleException(e);
    }
}
