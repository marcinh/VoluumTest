package com.voluum.framework.serialization;

/**
 * Object for JSON deserializtion
 */
public class CampaignReportObject
{
    private int clicks;
    private int visits;
    private int conversions;

    public int getClicks()
    {
        return clicks;
    }

    public void setClicks(int clicks)
    {
        this.clicks = clicks;
    }

    public int getVisits()
    {
        return visits;
    }

    public void setVisits(int visits)
    {
        this.visits = visits;
    }

    public int getConversions()
    {
        return conversions;
    }

    public void setConversions(int conversions)
    {
        this.conversions = conversions;
    }
}
