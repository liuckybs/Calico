package com.taozeyu.calico.generator;

import com.taozeyu.calico.javascript_helper.JavaScriptLoader;
import com.taozeyu.calico.resource.ResourceManager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

/**
 * Created by taozeyu on 15/10/9.
 */
public class PageService {

    private final ResourceManager resource;

    private final File templatePath;
    private final File routeDir;
    private final String params;

    PageService(ResourceManager resource, File templatePath, File routeDir, String params) {
        this.resource = resource;
        this.templatePath = templatePath;
        this.routeDir = routeDir;
        this.params = params;
    }

    public void requestPage(PrintStream printStream) throws IOException, ScriptException {
        ScriptEngine engine = createScriptEngine();
        try (Reader reader = getTemplateReader()){
            setPrintStreamToEngin(engine, printStream);
            engine.eval(getFileContentFromReader(reader));
        }
    }

    private ScriptEngine createScriptEngine() throws IOException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("__resource", resource);
        engine.put("params", params);
        try {
            new JavaScriptLoader().loadSystemJavaScriptLib(engine);
        } catch (ScriptException e) {
            e.printStackTrace();
            System.exit(1); //can only exit when JS lib throws error.
        }
        return engine;
    }

    private void setPrintStreamToEngin(ScriptEngine jse, PrintStream printStream) throws ScriptException {
        jse.put("__printStream", printStream);
        jse.eval("Output = __printStream;");
        jse.eval("__printStream = undefined;");
    }

    // Nashorn engine would omit some content if read from a long stream.
    // But all will be fine if buffered them before reading.
    // I don't know why.
    private String getFileContentFromReader(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(int ch = reader.read(); ch >= 0; ch = reader.read()) {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    private Reader getTemplateReader() throws IOException {
        RequireListReader templateReader = new RequireListReader(templatePath, routeDir);
        RequireListReader layoutReader = templateReader.getLayoutRequireListReader();
        if (layoutReader != null) {
            layoutReader.setYieldReader(templateReader);
            return new HtmlTemplateReader(layoutReader);
        } else {
            return new HtmlTemplateReader(templateReader);
        }
    }
}
