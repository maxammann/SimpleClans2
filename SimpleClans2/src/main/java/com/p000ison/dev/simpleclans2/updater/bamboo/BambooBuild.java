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
 *     Last modified: 19.02.13 21:27
 */

package com.p000ison.dev.simpleclans2.updater.bamboo;

import com.p000ison.dev.simpleclans2.api.logging.Logging;
import com.p000ison.dev.simpleclans2.updater.Build;
import com.p000ison.dev.simpleclans2.updater.FailedBuildException;
import com.p000ison.dev.simpleclans2.updater.UpdateType;
import com.p000ison.dev.simpleclans2.updater.jenkins.JenkinsArtifact;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a BambooBuild
 */
public class BambooBuild implements Build {

    private static final String BAMBOO_HOST = "build.greatmancode.com";
    private static final String PROJECT_GITHUB_URL = "https://github.com/p000ison/SimpleClans2/commit/";
    private static final String API_FILE = "/rest/api/latest/result/%s/%s.json?expand=labels,changes";
    private static final String LATEST_BUILD_BY_LABEL = "/rest/api/latest/result.json?label=%s";
    private static final String LATEST_BUILD = "/rest/api/latest/result.json";
    private static final String ARTIFACT_LINK = "http://build.greatmancode.com/browse/%s-%s/artifact";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final String job;
    private final UpdateType updateType;

    private int buildNumber;
    private long started, duration;
    private String pusher;
    private String commitId;

    public BambooBuild(String job, UpdateType updateType) {
        this.job = job;
        this.updateType = updateType;
    }

    @Override
    public void fetchInformation() throws IOException, FailedBuildException {
        Reader reader;
        JSONObject content;

        String latestBuild = updateType == UpdateType.LATEST ? LATEST_BUILD : String.format(LATEST_BUILD_BY_LABEL, updateType.toString());
        try {
            reader = connect(latestBuild);
        } catch (FileNotFoundException e) {
            Logging.debug("No build found in this channel!");
            return;
        }
        content = parseJSON(reader);
        JSONObject results = (JSONObject) content.get("results");
        JSONArray result = (JSONArray) results.get("result");
        if (result.isEmpty()) {
            Logging.debug("No build found in this channel!");
            return;
        }

        JSONObject firstResult = (JSONObject) result.get(0);
        int buildNumberToFetch = ((Long) firstResult.get("number")).intValue();
        String state = (String) firstResult.get("state");
        if (!state.equalsIgnoreCase("Successful")) {
            throw new FailedBuildException("The last build failed!");
        }

        reader.close();
        reader = connect(String.format(API_FILE, job, buildNumberToFetch));
        content = parseJSON(reader);

        try {
            this.started = DATE_FORMATTER.parse((String) content.get("buildStartedTime")).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.duration = (Long) content.get("buildDuration");

        this.buildNumber = ((Long) content.get("number")).intValue();
        this.commitId = (String) content.get("vcsRevisionKey");

        JSONArray changes = (JSONArray) ((JSONObject) content.get("changes")).get("change");

        if (!changes.isEmpty()) {
            JSONObject change = (JSONObject) changes.get(0);
            pusher = (String) change.get("userName");
        }

        reader.close();
    }

    private Reader connect(String file) throws IOException {
        URL buildAPIURL = new URL("http", BAMBOO_HOST, 80, file);
        URLConnection connection = buildAPIURL.openConnection();
        connection.setReadTimeout(1000);
        connection.setConnectTimeout(1000);
        return new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
    }

    private static JSONObject parseJSON(Reader reader) {
        Object parse = JSONValue.parse(reader);

        if (!(parse instanceof JSONObject)) {
            Logging.debug(Level.SEVERE, "Failed at reading the update info! Please contact the developers!");
        }

        return (JSONObject) parse;
    }

    @Override
    public List<JenkinsArtifact> getDownloadURLs() throws IOException {
        throw new UnsupportedOperationException("This action is not supported by this CI!");
    }

    @Override
    public UpdateType getUpdateType() {
        return updateType;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public long getStarted() {
        return started;
    }

    @Override
    public int getBuildNumber() {
        return buildNumber;
    }

    @Override
    public Set<String> getDeletedFiles() {
        throw new UnsupportedOperationException("This action is not supported by this CI!");
    }

    @Override
    public String getCommitURL() {
        return PROJECT_GITHUB_URL + commitId;
    }

    @Override
    public String getComment() {
        throw new UnsupportedOperationException("This action is not supported by this CI!");
    }

    @Override
    public Set<String> getModifiedFiles() {
        throw new UnsupportedOperationException("This action is not supported by this CI!");
    }

    @Override
    public Set<String> getCreatedFiles() {
        throw new UnsupportedOperationException("This action is not supported by this CI!");
    }

    @Override
    public String getAuthor() {
        return pusher;
    }

    @Override
    public String getDownloadLink() {
        return String.format(ARTIFACT_LINK, job, buildNumber);
    }
}
