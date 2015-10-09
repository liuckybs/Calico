package com.taozeyu.calico.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by taozeyu on 15/10/9.
 */
public class PathUtil {

    public static boolean isAbsolutePath(String path) {
        return path.matches("([a-zA-Z]+:)?(\\\\|/).*");
    }

    public static  String normalizePath(String absolutePath) {
        return absolutePath.replaceAll("\\\\", "/").replaceAll("(\\.(\\w|\\-)+/?)?$", "");
    }

    public static File getFile(String path, String currentDirectoryPath) {
        if (isAbsolutePath(path)) {
            return new File(path);
        }
        path = path.replaceFirst("^\\.(/|\\\\)", "");
        return new File(currentDirectoryPath, path);
    }

    public static String getExtensionName(String path) {
        return getExtensionName(path, "html");
    }

    public static String getExtensionName(String path, String holderExtensionName) {
        String extensionName;
        Matcher matcher = Pattern.compile("\\.(\\w|\\-)+$").matcher(path);
        if(matcher.find()) {
            extensionName = matcher.group().replaceAll("^\\.", "");
        } else {
            extensionName = holderExtensionName;
        }
        return extensionName;
    }

    public static String clearExtensionName(String path) {
        return path.replaceAll("\\.(\\w|\\-)+$", "");
    }
}
