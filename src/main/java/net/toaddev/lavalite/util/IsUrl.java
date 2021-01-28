package net.toaddev.lavalite.util;

public class IsUrl
{
    public static boolean isUrl(String url)
    {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!url.isEmpty() && url.matches(pattern))
        {
            success = true;
            return success;
        }
        success = false;
        return success;
    }
}