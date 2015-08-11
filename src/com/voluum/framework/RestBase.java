package com.voluum.framework;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * Abstract class to handle Rest communication
 */
public abstract class RestBase{
    protected String securityToken;
    protected Gson gson;

    protected RestBase(String token){
        securityToken = token;
        gson = new Gson();
    }

    /**
     * Sends REST request and returns response
     * @param method
     * @param url
     * @param action Additional action on REST request
     * @return
     * @throws UnirestException
     */
    protected HttpResponse<JsonNode> execute(HttpMethod method, String url, IAction<HttpRequestWithBody> action) throws UnirestException
    {
        HttpRequestWithBody request = new HttpRequestWithBody(method, url);

        if(action != null)
            action.execute(request);

        return request.asJson();
    }

    /**
     * Sends REST request and returns response
     * @param method
     * @param url
     * @return
     * @throws UnirestException
     */
    protected HttpResponse<JsonNode> execute(HttpMethod method, String url) throws UnirestException
    {
        return execute(method, url, null);
    }

    /**
     * Sends REST request and returns response within secure context
     * @param method
     * @param url
     * @param action Additional action on REST request
     * @return
     * @throws UnirestException
     */
    protected HttpResponse<JsonNode> executeSecurely(HttpMethod method, String url, IAction<HttpRequestWithBody> action) throws UnirestException
    {
        return execute(method, url, (request) ->
        {
            request.header("cwauth-token", securityToken);

            if (action != null)
                action.execute(request);

        });
    }

    /**
     * Sends REST request and returns response within secure context
     * @param method
     * @param url
     * @return
     * @throws UnirestException
     */
    protected HttpResponse<JsonNode> executeSecurely(HttpMethod method, String url) throws UnirestException
    {
        return executeSecurely(method, url, null);
    }

    /**
     * Sends simple GET request and returns response
     * @param url
     * @param enableRedirect Indicates if request should follow redirection
     * @return
     * @throws UnirestException
     */
    public HttpResponse<String> get(String url, boolean enableRedirect) throws UnirestException {
        Unirest.enableRedirect(enableRedirect); //Requires modified unirest library from https://github.com/marcinh/unirest-java.git
        return Unirest.get(url).asString();
    }
}
