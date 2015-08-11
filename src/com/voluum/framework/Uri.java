package com.voluum.framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Custom Uri class
 */
public class Uri {
    final String PATTERN = "subid=[A-Za-z0-9]{24}";
    private URI uri;
    private String url;

    public Uri(String url) throws URISyntaxException {
        this.url = url;
        this.uri = new URI(url);
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public String getHost() {
        return uri.getHost();
    }

    public String getHostWithProtocol(){
        return String.format("%s://%s", uri.getScheme(), uri.getHost());
    }

    public String getUrl(){
        return url;
    }

    public String getSubid() throws URISyntaxException {
        if(!getQuery().matches(PATTERN))
            throw new URISyntaxException("", "Query does not contain subId");
        return getQuery().replace("subid=", "");
    }

    public void setQuery(String newQuery) throws URISyntaxException {
        url = getHostWithProtocol() + newQuery;
        uri = new URI(url);
    }

    public static Uri fromResponseUrl(HttpResponse<JsonNode> response) throws URISyntaxException {
        return new Uri(response.getBody().getObject().getString("url"));
    }

    public static Uri fromResponseLocation(HttpResponse<String> response) throws URISyntaxException {
        return new Uri(response.getHeaders().getFirst("location"));
    }
}
