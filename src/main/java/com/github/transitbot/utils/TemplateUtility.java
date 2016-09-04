package com.github.transitbot.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle templates.
 */
public class TemplateUtility {

    /**
     * template config.
     */
    private Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);

    /**
     * Constructor by default.
     */
    public TemplateUtility() {
        try {
            cfg.setDirectoryForTemplateLoading(new File("templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to render template into string.
     *
     * @param tmplName tmplName filename of template
     * @param tmplObjectName name to represent object in template
     * @param tmplObject object passing into template
     * @return string
     */
    public String renderTemplate(String tmplName, String tmplObjectName, Object tmplObject) {
        Template template = null;
        Writer out = new StringWriter();
        try {
            template = cfg.getTemplate(tmplName);
            Map root = new HashMap();
            root.put(tmplObjectName, tmplObject);
            template.process(root, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return out.toString();
    }


}
