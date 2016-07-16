package com.taozeyu.calico;

import java.io.File;
import java.util.Map;

/**
 * Created by taozeyu on 16/7/10.
 */
public class RuntimeContext {

    private File templateDirectory;
    private File targetDirectory;
    private File systemEntityDirectory;
    private String rootPage;
    private Map<String, File> librerayDirectoryMap;

    public File getTemplateDirectory() {
        return templateDirectory;
    }

    public void setTemplateDirectory(File templateDirectory) {
        this.templateDirectory = templateDirectory;
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getRootPage() {
        return rootPage;
    }

    public void setRootPage(String rootPage) {
        this.rootPage = rootPage;
    }

    public File getSystemEntityDirectory() {
        return systemEntityDirectory;
    }

    public void setSystemEntityDirectory(File systemEntityDirectory) {
        this.systemEntityDirectory = systemEntityDirectory;
    }

    public Map<String, File> getLibrerayDirectoryMap() {
        return librerayDirectoryMap;
    }

    public void setLibrerayDirectoryMap(Map<String, File> librerayDirectoryMap) {
        this.librerayDirectoryMap = librerayDirectoryMap;
    }
}
