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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Represents a PHPConnection
 */
public class PHPConnection {

    /**
     * The Connection to the URL
     */
    private URLConnection connection;
    private boolean read = false;

    public PHPConnection(String protocol, String host, int port, String file, boolean read) throws IOException
    {
        this(protocol, host, port, file);
        this.read = read;
    }

    public PHPConnection(String protocol, String host, int port, String file) throws IOException
    {

        connection = new URL(protocol, host, port, file).openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
    }

    /**
     * Sending data to the target-URL
     *
     * @param data The data which should be send
     * @throws IOException
     */
    public void write(String data) throws IOException
    {
        final OutputStream out = connection.getOutputStream();
        out.write(data.getBytes());
        out.flush();
        out.close();
        if (!read) {
            connection.getInputStream().close();
        }
    }

    public String getResponse() throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();
        return response;
    }

    /**
     * Reading incoming data from the target-URL
     *
     * @return The incoming data
     * @throws IOException
     */
    public String read() throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder incoming = new StringBuilder();

        String response;
        while ((response = reader.readLine()) != null) {
            incoming.append(response);
        }

        return incoming.toString();
    }
}