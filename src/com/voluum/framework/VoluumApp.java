package com.voluum.framework;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.voluum.Settings;
import com.voluum.framework.exceptions.GetRequestException;
import com.voluum.framework.exceptions.LoginFailureException;
import com.voluum.framework.serialization.CampaignObject;
import com.voluum.framework.serialization.CampaignReportObject;
import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;


public class VoluumApp extends RestBase implements AutoCloseable {

    public VoluumApp() {
        super("");
    }

    public void login(String user, String password) throws UnirestException, LoginFailureException {
        final String credentials = new String(Base64.getEncoder().encode(String.format("%s:%s", user, password).getBytes()));

        HttpResponse<JsonNode> response = execute(HttpMethod.GET, Settings.securityUrl + "/login", request ->
        {
            request.header("Authorization", "Basic " + credentials);
            request.header("accept", "application/json");
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

        return executeSecurely(HttpMethod.POST, Settings.coreUrl + "/campaigns", request ->
       {
           request.header("Content-Type","application/json");
           request.body(gson.toJson(campaign));
       });
    }

    public CampaignReportObject getCampaignStatistics(String id) throws UnirestException
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        String resource = "/report?from={today}T00%3A00%3A00Z&to={tomorrow}T00%3A00%3A00Z&groupBy=lander&include=active&filter1=campaign&filter1Value={id}";

        HttpResponse<JsonNode> response = executeSecurely(HttpMethod.GET, Settings.reportsUrl + resource, request -> {
            request.routeParam("id", id);
            request.routeParam("today", dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
            request.routeParam("tomorrow", dateFormat.format(calendar.getTime()));
        });

        JSONArray array = response.getBody().getObject().getJSONArray("rows");

        return gson.fromJson(array.getJSONObject(0).toString(), CampaignReportObject.class);
    }

    public HttpResponse<JsonNode> getCampaighDetails(String id) throws UnirestException
    {
        return executeSecurely(HttpMethod.GET, Settings.coreUrl + "/campaigns/{id}",request ->
                request.routeParam("id", id));
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
