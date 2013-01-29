package com.p000ison.dev.simpleclans2.language;

import java.io.File;
import java.nio.charset.Charset;

public class Language {
    private static LanguageMap bundle;
    private static final String DEFAULT_FILE_NAME = "lang.properties";

    public static void setInstance(File folder, Charset charset) {
        LanguageMap defaultBundle = new LanguageMap("/languages/" + DEFAULT_FILE_NAME, true, charset);
        defaultBundle.load();
        bundle = new LanguageMap(new File(folder, DEFAULT_FILE_NAME).getAbsolutePath(), false, charset);
        bundle.setDefault(defaultBundle);
        bundle.load();
        bundle.save();
    }

    public static String getTranslation(String key, Object... args) {
        return bundle.getTranslation(key, args);
    }

    public static void clear() {
        bundle.clear();
        bundle = null;
    }
}