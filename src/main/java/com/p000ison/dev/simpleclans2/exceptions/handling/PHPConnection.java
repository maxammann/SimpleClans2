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
 *     Last modified: 14.10.12 01:55
 */

package com.p000ison.dev.simpleclans2.exceptions.handling;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Represents a PHPConnection
 */
public class PHPConnection {

    /**
     * The Connection to the URL
     */
    private URLConnection con;

    public PHPConnection(String protocol, String host, int port, String file) throws IOException
    {
        con = new URL(protocol, host, port, file).openConnection();
        con.setDoOutput(true);
        con.setUseCaches(false);
    }

    /**
     * Sending data to the target-URL
     *
     * @param data The data which should be send
     * @throws IOException
     */
    public void write(String data) throws IOException
    {
        OutputStream out = con.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        out.close();
        con.getInputStream().close();
    }

//    /**
//     * Reading incoming data from the target-URL
//     *
//     * @return The incoming data
//     * @throws IOException
//     */
//    public String read() throws IOException
//    {
//        InputStream in = con.getInputStream();
//        int current;
//        StringBuilder incoming = new StringBuilder();
//
//        while ((current = in.read()) > 0) {
//            incoming.append((char) current);
//        }
//        in.close();
//        return incoming.toString();
//    }
}