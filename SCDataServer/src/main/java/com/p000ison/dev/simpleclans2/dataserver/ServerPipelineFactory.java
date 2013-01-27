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

import com.p000ison.dev.simpleclans2.dataserver.handshake.HandshakeHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * Represents a ServerPipelineFactory
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {

    private final Server server;

    private static final int MAX_MESSAGE_LENGTH = 100;

    public ServerPipelineFactory(Server server) {
        this.server = server;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        //New messages are detected by a \n so a new line not by a NUL
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(MAX_MESSAGE_LENGTH, Delimiters.lineDelimiter()));
        //Receiving string messages
        pipeline.addLast("decoder", new StringDecoder());
        //Sending string messages
        pipeline.addLast("encoder", new StringEncoder());
        //Handshake handler
        pipeline.addLast("handshake", new HandshakeHandler(server));

        //Handel everything
        pipeline.addLast("handler", new ServerHandler(server));

        return pipeline;
    }
}
