package net.devromik.app.deploy.utils;

import java.io.*;
import static com.google.common.io.Resources.*;

/**
 * @author Shulnyaev Roman
 */
public final class ResourceUtils {

    /**
     * @param relativeResourceFilePathname a relative path of a resource without a leading '/': resources/relativeResourceFilePathname.
     * @return the resource file.
     */
    public static File getResourceFile(String relativeResourceFilePathname) throws Exception {
        return new File(getResource(relativeResourceFilePathname).toURI());
    }
}
