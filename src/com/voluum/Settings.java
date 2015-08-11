package com.voluum;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

public class Settings {

    public static String user;
    public static String password;
    public static String securityUrl;
    public static String coreUrl;
    public static String reportsUrl;
    public static String redirectUrl;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));

            for (Field setting : Settings.class.getDeclaredFields()) {
                if (Modifier.isStatic(setting.getModifiers())){
                    setting.setAccessible(true);
                    setting.set(null, properties.getProperty(setting.getName()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
