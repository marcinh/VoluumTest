package com.voluum.framework.serialization;

public class CampaignObject {
    private String namePostfix;
    private String directRedirectUrl;
    private String costModel;
    private String clickRedirectType;
    private String redirectTarget;

    private ClientObject client;
    private TrafficClassObject trafficSource;
    private CountryObject country;

    public boolean costModelHidden;

    public CampaignObject(String name, String redirectUrl, TrafficClassObject trafficSource)
    {
        this.namePostfix = name;
        this.directRedirectUrl = redirectUrl;
        this.trafficSource = trafficSource;
        this.costModel="NOT_TRACKED";
        this.clickRedirectType = "REGULAR";
        this.redirectTarget =  "DIRECT_URL";
        this.costModelHidden = true;
        client = new ClientObject();
        country = CountryObject.getPoland();
    }

    public String getNamePostfix(){
        return namePostfix;
    }

    public String getDirectRedirectUrl() {
        return directRedirectUrl;
    }

    public String getCostModel() {
        return costModel;
    }

    public String getClickRedirectType() {
        return clickRedirectType;
    }

    public String getRedirectTarget() {
        return redirectTarget;
    }

    public ClientObject getClient() {
        return client;
    }

    public TrafficClassObject getTrafficSource() {
        return trafficSource;
    }

    public CountryObject getCountry() {
        return country;
    }
}
