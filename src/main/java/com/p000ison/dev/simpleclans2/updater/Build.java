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
 *     Last modified: 03.11.12 17:32
 */

package com.p000ison.dev.simpleclans2.updater;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Build
 */
public class Build {

    private static final String DEFAULT_ARTIFACT = "SimpleClans2-1.0.jar";
    private static final String PROJECT_GITHUB_URL = "https://github.com/p000ison/SimpleClans2/commit/";
    private static final String JENKINS_HOST = "jenkins.greatmancode.com";
    private static final String API_FILE = "/api/json";
    // --> http://
    private static final byte httpLength = 7;

    private int buildNumber;
    private String fetchFile;
    private boolean isBuilding;

    private String job;
    private UpdateType updateType;

    private long started, duration;

    private String pusher;
    private String commitId, comment;
    private Set<String> modifiedFiles = new HashSet<String>();
    private Set<String> createdFiles = new HashSet<String>();
    private Set<String> deletedFiles = new HashSet<String>();


    public Build(String job, UpdateType updateType)
    {
        this.job = job;
        this.updateType = updateType;
    }

    public void fetchInformation() throws IOException
    {
        URL buildAPIURL = new URL("http", JENKINS_HOST, 80, "/job/" + job + "/" + updateType + "/" + API_FILE);

        URLConnection connection = buildAPIURL.openConnection();
        Reader reader = new InputStreamReader(connection.getInputStream());

        JSONObject content = parseJSON(reader);

        this.buildNumber = content.get("number").hashCode();
        this.isBuilding = content.get("building").hashCode() == 1231;

        if (isBuilding) {
            return;
        }

        this.started = (Long)content.get("timestamp");
        this.duration = (Long) content.get("duration");
        this.fetchFile = content.get("url").toString().substring(httpLength + JENKINS_HOST.length()) + "artifact/target/" + DEFAULT_ARTIFACT;

        try {
            JSONObject changes = (JSONObject) content.get("changeSet");
            JSONArray items = (JSONArray) changes.get("items");
            if (!items.isEmpty()) {
                JSONObject buildInfo = (JSONObject) items.get(0);

                this.commitId = buildInfo.get("commitId").toString();
                this.pusher = ((JSONObject) buildInfo.get("author")).get("fullName").toString();
                this.comment = buildInfo.get("msg").toString();

                for (Object element : (JSONArray) buildInfo.get("paths")) {
                    JSONObject entry = (JSONObject) element;

                    if (entry.get("editType").equals("edit")) {
                        modifiedFiles.add(entry.get("file").toString());
                    } else if (entry.get("editType").equals("delete")) {
                        deletedFiles.add(entry.get("file").toString());
                    } else if (entry.get("editType").equals("add")) {
                        createdFiles.add(entry.get("file").toString());
                    }
                }
            }
        } catch (ClassCastException e) {
            Logging.debug(e, true, "The format of the api changed! Could not fetch the cause!");
            e.printStackTrace();
        }
    }

    public static JSONObject parseJSON(Reader reader)
    {
        Object parse = JSONValue.parse(reader);

        if (!(parse instanceof JSONObject)) {
            throw new RuntimeException("Failed at reading from the reader!");
        }

        return (JSONObject) parse;
    }

    public InputStream getDownloadStream() throws IOException
    {
        URL downloadURL = new URL("http", JENKINS_HOST, 80, fetchFile);

        return downloadURL.openConnection().getInputStream();
    }

    public void saveToFile(File file) throws IOException
    {
        InputStream input = getDownloadStream();
        OutputStream output = new FileOutputStream(file);

        byte[] buffer = new byte[1024];

        int realLength;

        while ((realLength = input.read(buffer)) > 0) {
            output.write(buffer, 0, realLength);
        }

        output.flush();
        output.close();
    }

    public boolean saveToDirectory(File directory) throws IOException
    {
        if (!directory.mkdirs()) {
            return false;
        }

        if (!directory.isDirectory()) {
            return false;
        }

        File file = new File(directory, DEFAULT_ARTIFACT);

        saveToFile(file);
        return true;
    }

    public UpdateType getUpdateType()
    {
        return updateType;
    }

    public long getDuration()
    {
        return duration;
    }

    public long getStarted()
    {
        return started;
    }

    public int getBuildNumber()
    {
        return buildNumber;
    }

    public boolean wasBuilding()
    {
        return isBuilding;
    }

    public static enum UpdateType {
        LATEST("lastSuccessfulBuild"),
        LATEST_PROMOTED("Promoted");

        private String type;

        private UpdateType(String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return type;
        }

        public String getType()
        {
            return type;
        }
    }

    public Build(Set<String> deletedFiles, Set<String> createdFiles, Set<String> modifiedFiles, String comment, String commitId, String pusher)
    {
        this.deletedFiles = deletedFiles;
        this.createdFiles = createdFiles;
        this.modifiedFiles = modifiedFiles;
        this.comment = comment;
        this.commitId = commitId;
        this.pusher = pusher;
    }

    public Set<String> getDeletedFiles()
    {
        return deletedFiles;
    }

    public String getCommitURL()
    {
        return PROJECT_GITHUB_URL + commitId;
    }

    public String getComment()
    {
        return comment;
    }

    public Set<String> getModifiedFiles()
    {
        return modifiedFiles;
    }

    public Set<String> getCreatedFiles()
    {
        return createdFiles;
    }

    public String getAuthor()
    {
        return pusher;
    }
}
