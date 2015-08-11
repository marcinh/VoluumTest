package com.voluum.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.sun.jndi.toolkit.url.Uri;
import com.voluum.Settings;
import com.voluum.framework.VoluumApp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoluumTest {

    //Example campaigh name
    String CAPMPAIGNNAME = "TEST108";
    //Example campaign ID for test purposes
    String CAMPAIGNID = "806f4cf7-3168-4866-a212-837ecce37fef";
    String PATTERN = "/?subid=[A-Za-z0-9]{24}";

    private Uri redirectUri;

    @Before
    public void Setup() throws MalformedURLException {
        redirectUri = new Uri(Settings.redirectUrl);
    }

    @Test
    public void Test_01() throws Exception
    {
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
//            HttpResponse<JsonNode> response = vApp.createCampaign(new CampaignObject(CAPMPAIGNNAME, redirectUri.toString(), TrafficClassObject.getZeroPark()));
//
//            Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_CREATED);
//
//            String linkResponse = response.getBody().getObject().getString("url");
            String linkResponse = "http://auayi.voluumtrk.com/voluum/6601c945-2601-4e23-8a79-d7e35f859f43";

            HttpResponse<String> getResponse = vApp.get(linkResponse, false);

            Assert.assertEquals(getResponse.getStatus(), HttpURLConnection.HTTP_MOVED_TEMP);
            Uri currentUri = new Uri(getResponse.getHeaders().getFirst("location"));

//            System.out.println(currentUri);

            Assert.assertFalse(currentUri.getQuery().matches(PATTERN)); //Not perfect - implement custom matcher
        }
    }

    @Test
    public void Test_02() throws Exception
    {
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
            int visitsNumber = vApp.getCampaignStatistics(CAMPAIGNID).getVisits();

            HttpResponse<JsonNode> response = vApp.getCampaighDetails(CAMPAIGNID);

            String url = response.getBody().getObject().getString("url");

            vApp.get(url, true);

            boolean visitIncremented = false;

            for (int i = 0; i < 10; i++)
            {
                Thread.sleep(1000);

                if ( vApp.getCampaignStatistics(CAMPAIGNID).getVisits() == visitsNumber + 1)
                {
                    visitIncremented = true;
                    break;
                }
            }

            Assert.assertTrue(visitIncremented);
        }
    }

    @Test
    public void Test_03() throws Exception
    {
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
//            int conversionsNumber  = vApp.getCampaignStatistics(CAMPAIGNID).getConversions();
//
//            HttpResponse<JsonNode> response = vApp.getCampaighDetails(CAMPAIGNID);
//
//            String url = response.getBody().getObject().getString("url");
//
//            HttpResponse<String> getResponse = vApp.get(url, false);
//
//            String redirection = getResponse.getHeaders().getFirst("location");

            String redirection = "http://example.com?subid=w02S4BIFT82BBEAMGM0PLF3E";

            URI uri = new URI(redirection);

            Matcher matcher = Pattern.compile(PATTERN).matcher(uri.getQuery());

            if(!matcher.find()){
                Assert.fail("subid not found in request");
            }
            String cId = uri.getQuery().replace("subid=", "");


            System.out.println(String.format("%s/postback?cid=%s", uri.getHost(), cId));


            System.out.println(uri.getHost());
            System.out.println(uri.getQuery());
            System.out.println(uri.getPath());
            System.out.println(uri.getHost());


            vApp.get(String.format("%s/postback?cid=%s", redirection.replace(uri.getQuery(), ""), cId), true);


//            for(String key : response.getHeaders().keySet()){
//                System.out.println(key);
//                System.out.println(response.getHeaders().getFirst(key));
//            }



//            boolean visitIncremented = false;
//
//            for (int i = 0; i < 10; i++)
//            {
//                Thread.sleep(1000);
//
//                if ( vApp.getCampaignStatistics(CAMPAIGNID).getVisits() == conversionsNumber  + 1)
//                {
//                    visitIncremented = true;
//                    break;
//                }
//            }
//
//            Assert.assertTrue(visitIncremented);
        }
    }
}
