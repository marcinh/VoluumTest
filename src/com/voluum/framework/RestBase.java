package com.voluum.framework;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.http.client.HttpClient;


public abstract class RestBase{
    protected String securityToken;

    protected RestBase(String token){
        securityToken = token;
    }

    protected HttpResponse<JsonNode> execute(HttpMethod method, String url, final Action<HttpRequestWithBody> action) throws UnirestException
    {
        HttpRequestWithBody request = new HttpRequestWithBody(method, url);

        if(action != null)
            action.execute(request);

        return request.asJson();
    }

    protected HttpResponse<JsonNode> execute(HttpMethod method, String url) throws UnirestException
    {
        return execute(method, url, null);
    }

    protected HttpResponse<JsonNode> executeSecurely(HttpMethod method, String url, final Action<HttpRequestWithBody> action) throws UnirestException
    {
        return execute(method, url, new Action<HttpRequestWithBody>() {
            @Override
            public void execute(HttpRequestWithBody request) {
                request.header("cwauth-token", securityToken);

                if (action != null)
                    action.execute(request);
            }
        });
    }

    protected HttpResponse<JsonNode> executeSecurely(HttpMethod method, String url) throws UnirestException
    {
        return executeSecurely(method, url, null);
    }

    public HttpResponse<String> get(String url, boolean enableRedirect) throws UnirestException {
        Unirest.enableRedirect(enableRedirect);
        return Unirest.get(url).asString();
    }
}
