package com.voluum.test;

import com.mashape.unirest.http.HttpResponse;
import com.sun.jndi.toolkit.url.Uri;
import com.voluum.framework.VoluumApp;
import org.apache.http.Header;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class VoluumTest {

    //Example campaigh name
    String CAPMPAIGNNAME = "TEST108";
    //Example campaign ID for test purposes
    String CAMPAIGNID = "806f4cf7-3168-4866-a212-837ecce37fef";

    private Uri redirectUri;

    @Before
    public void Setup() throws MalformedURLException {
        redirectUri = new Uri("http://example.com?subid={clickId}");
    }

    @Test
    public void Test_01() throws Exception
    {
        redirectUri = new Uri("http://example.com?subid={clickId}");
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
//            HttpResponse<JsonNode> response = vApp.createCampaign(new CampaignObject(CAPMPAIGNNAME, redirectUri.toString(), TrafficClassObject.getZeroPark()));
//
//            Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_CREATED);
//
//            String linkResponse = response.getBody().getObject().getString("url");
            String linkResponse = "http://auayi.voluumtrk.com/voluum/6601c945-2601-4e23-8a79-d7e35f859f43";

            HttpResponse<String> response = vApp.get(linkResponse, false);

            Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_MOVED_TEMP);

            Uri currentUri = new Uri(response.getHeaders().getFirst("location"));

            System.out.println(currentUri);

            System.out.println(redirectUri.());

            String expSubId = redirectUri.getQuery().replace("{clickId}", "[A-Za-z0-9]{24}");

            System.out.println(expSubId);

            Assert.assertFalse(currentUri.getQuery().matches(expSubId)); //Not perfect - implement custom matcher
        }
    }

    public void Test_02() throws Exception
    {
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
//            HttpResponse<JsonNode> response = vApp.createCampaign(new CampaignObject(CAPMPAIGNNAME, redirectUri.toString(), TrafficClassObject.getZeroPark()));
//
//            Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_CREATED);
//
//            String linkResponse = response.getBody().getObject().getString("url");

        }
    }
}
