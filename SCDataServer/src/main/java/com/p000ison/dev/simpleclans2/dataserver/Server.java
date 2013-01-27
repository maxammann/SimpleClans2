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
 *     Last modified: 26.01.13 18:05
 */

package com.p000ison.dev.simpleclans2.dataserver;

import com.p000ison.dev.simpleclans2.dataserver.util.Logging;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Represents a Server
 */
public class Server {

    private ChannelGroup channelGroup;
    private int port;
    private TimeoutHandler timeoutHandler;
    private ExecutorService bossPool;
    private ExecutorService workerPool;
    private ServerBootstrap bootstrap;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("Server");
        logger.addHandler(new ServerLoggerHandler());
        Logging.setInstance(logger);

        Server server = new Server(8080);
        server.connect();
    }

    public Server(int port) {
        this.port = port;
        channelGroup = new DefaultChannelGroup();
    }

    public void connect() {
        bossPool = Executors.newCachedThreadPool();
        workerPool = Executors.newCachedThreadPool();

        timeoutHandler = new TimeoutHandler();
        Thread timeoutThread = new Thread(timeoutHandler, "Timeout handler");
        timeoutThread.setDaemon(true);
        timeoutThread.start();

        ChannelFactory factory = new NioServerSocketChannelFactory(bossPool, workerPool);
        bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ServerPipelineFactory(this));

        InetSocketAddress address = new InetSocketAddress(port);
        channelGroup.add(bootstrap.bind(address));
        Logging.debug("Bound to address: %s:%d", address.getHostName(), port);

        Logging.debug("SimpleClans data-server started!");
    }

    public TimeoutHandler getTimeoutHandler() {
        return timeoutHandler;
    }

    public void registerChannel(Channel channel) {
        channelGroup.add(channel);
    }

    public void stop() {
        channelGroup.close().awaitUninterruptibly();
        bossPool.shutdown();
        workerPool.shutdown();

        new Thread() {
            @Override
            public void run() {
                bootstrap.releaseExternalResources();
            }
        }.start();
    }

    public void handleException(ExceptionEvent e) throws Exception {
        if (e.getCause() instanceof TooLongFrameException) {
            Logging.debug("The client %s sent a too long message!", e.getChannel().getRemoteAddress());
            e.getChannel().close();
        } else {
            Logging.debug(e.getCause());
        }
    }
}
