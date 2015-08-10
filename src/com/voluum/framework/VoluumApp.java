package com.voluum.framework;

import com.cedarsoftware.util.io.JsonWriter;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.voluum.Settings;
import com.voluum.framework.exceptions.GetRequestException;
import com.voluum.framework.exceptions.LoginFailureException;
import com.voluum.framework.serialization.CampaignObject;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.HashMap;


public class VoluumApp extends RestBase implements AutoCloseable {

    public VoluumApp() {
        super("");
    }

    public void login(String user, String password) throws UnirestException, LoginFailureException {
        final String credentials = new String(Base64.getEncoder().encode(String.format("%s:%s", user, password).getBytes()));

        HttpResponse<JsonNode> response = execute(HttpMethod.GET, Settings.securityUrl + "/login", new Action<HttpRequestWithBody>() {
            @Override
            public void execute(HttpRequestWithBody request) {
                request.header("Authorization", "Basic " + credentials);
                request.header("accept", "application/json");
            }
        });

        boolean loggedIn = response.getBody().getObject().getBoolean("loggedIn");

        if (!loggedIn)
            throw new LoginFailureException();

        securityToken = response.getBody().getObject().getString("token");
    }

    public void login() throws UnirestException, LoginFailureException {
        login(Settings.user, Settings.password);
    }

    public void logout() throws UnirestException, GetRequestException {
        HttpResponse<JsonNode> response = executeSecurely(HttpMethod.GET, Settings.securityUrl + "/session/logout");

        if (response.getStatus() != HttpURLConnection.HTTP_OK)
            throw new GetRequestException("logout");
    }

    public HttpResponse<JsonNode> createCampaign(CampaignObject campaign) throws UnirestException, GetRequestException {

        final String jsonCampaign = JsonWriter.objectToJson(campaign, new HashMap<String, Object>() {{
            put("TYPE", false);
        }});

       return executeSecurely(HttpMethod.POST, Settings.coreUrl + "/campaigns", new Action<HttpRequestWithBody>() {
            @Override
            public void execute(HttpRequestWithBody request) {
                request.header("Content-Type","application/json");
                request.body(jsonCampaign);
            }
        });
    }

    @Override
    public void close() throws Exception {
        logout();
        Unirest.shutdown();
    }

    public static VoluumApp loggedIn() throws LoginFailureException, UnirestException {
        VoluumApp vApp = new VoluumApp();
        vApp.login();

        return vApp;
    }
}
