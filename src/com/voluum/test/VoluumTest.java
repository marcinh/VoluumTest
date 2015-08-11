package com.voluum.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.voluum.Settings;
import com.voluum.framework.Uri;
import com.voluum.framework.VoluumApp;
import com.voluum.framework.serialization.CampaignObject;
import com.voluum.framework.serialization.TrafficClassObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

public class VoluumTest {

    //Example campaigh name
    String CAPMPAIGNNAME = "TEST114";
    //Example campaign ID for test purposes
    String CAMPAIGNID = "6601c945-2601-4e23-8a79-d7e35f859f43";
    String PATTERN = "subid=[A-Za-z0-9]{24}";

    private String redirectUri;

    @Before
    public void Setup() throws Exception {
        redirectUri = Settings.redirectUrl;
    }

    @Test
    public void Test_01() throws Exception
    {
        try(VoluumApp vApp = VoluumApp.loggedIn())
        {
            HttpResponse<JsonNode> response = vApp.createCampaign(new CampaignObject(CAPMPAIGNNAME, redirectUri, TrafficClassObject.getZeroPark()));

            Assert.assertEquals(HttpURLConnection.HTTP_CREATED, response.getStatus() );

            Uri responseUri = Uri.fromResponseUrl(response);

            HttpResponse<String> getResponse = vApp.get(responseUri.getUrl(), false);

            Assert.assertEquals(HttpURLConnection.HTTP_MOVED_TEMP, getResponse.getStatus());

            Uri currentUri = Uri.fromResponseLocation(getResponse);

            Assert.assertTrue(currentUri.getQuery().matches(PATTERN)); //Not perfect - implement custom matcher
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
            int conversionsNumber  = vApp.getCampaignStatistics(CAMPAIGNID).getConversions();

            HttpResponse<JsonNode> response = vApp.getCampaighDetails(CAMPAIGNID);

            Uri uri = Uri.fromResponseUrl(response);

            HttpResponse<String> getResponse = vApp.get(uri.getUrl(), false);

            uri.setQuery("/postback?cid=" + Uri.fromResponseLocation(getResponse).getSubid());

            vApp.get(uri.getUrl(), true);

            boolean conversionsIncremented = false;

            for (int i = 0; i < 15; i++) {
                Thread.sleep(2000);

                if ( vApp.getCampaignStatistics(CAMPAIGNID).getConversions() == conversionsNumber + 1)
                {
                    conversionsIncremented = true;
                    break;
                }
            }

            Assert.assertTrue(conversionsIncremented);
        }
    }
}
