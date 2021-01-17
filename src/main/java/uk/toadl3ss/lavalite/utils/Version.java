package uk.toadl3ss.lavalite.utils;

import uk.toadl3ss.lavalite.main.Lavalite;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Version {
    public static File Version() throws URISyntaxException {
        URL resource = Lavalite.class.getClassLoader().getResource("version.txt");
        if (resource == null) {
            throw new IllegalArgumentException("No Version Found.");
        } else {
            return new File(resource.toURI());
        }
    }
}