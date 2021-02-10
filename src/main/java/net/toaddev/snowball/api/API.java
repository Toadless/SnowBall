package net.toaddev.snowball.api;

public enum API
{
    // stats APIs
    TOP_GG("top.gg", "https://top.gg/api/bots/%s/stats", "server_count");

    private final String name;
    private final String url;
    private final String statsParameter;

    API(final String name, final String url, final String statsParameter){
        this.name = name;
        this.url = url;
        this.statsParameter = statsParameter;
    }

    API(final String name, final String url){
        this.name = name;
        this.url = url;
        this.statsParameter = null;
    }

    public String getName(){
        return this.name;
    }

    public String getUrl(){
        return this.url;
    }

    public String getStatsParameter(){
        return this.statsParameter;
    }
}
