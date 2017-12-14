package com.gouermazi.util.file;


import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class ResourceUtils {
    private static final ResourceUtils.MyResourceBundleControl ctl = new ResourceUtils.MyResourceBundleControl();

    public ResourceUtils() {
    }

    public static String getString(String baseName, String key) {
        return _getStringForLocale(Locale.getDefault(), baseName, key);
    }

    public static String ui(String key) {
        return getString("ui", key);
    }

    private static String _getStringForLocale(Locale locale, String baseName, String key) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle(baseName, locale, ResourceUtils.class.getClassLoader(), ctl);
            return rb != null ? rb.getString(key) : null;
        } catch (MissingResourceException var4) {
            return null;
        } catch (NullPointerException var5) {
            return null;
        }
    }

    public static String getString(String baseName, String key, Object... args) {
        String text = getString(baseName, key);
        return text != null ? MessageFormat.format(text, args) : null;
    }

    public static String getStringForLocale(Locale locale, String baseName, String key, Object... args) {
        String text = _getStringForLocale(locale, baseName, key);
        return text != null ? MessageFormat.format(text, args) : null;
    }

    public static String loadFromResource(String resource) {
        InputStream in = null;
        BufferedReader reader = null;

        String var5;
        try {
            in = new FileInputStream(resource);
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            var5 = IOUtils.toString(reader);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
            reader = null;
        }

        return var5;
    }

    private static class MyResourceBundleControl extends Control {
        private MyResourceBundleControl() {
        }

        public long getTimeToLive(String baseName, Locale locale) {
            return 3600000L;
        }

        public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
            return true;
        }
    }
}
