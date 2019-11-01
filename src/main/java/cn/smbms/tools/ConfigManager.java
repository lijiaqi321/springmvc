package cn.smbms.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties;


    private ConfigManager() {


        String configFile = "database.properties";
        properties = new Properties();
        InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(configFile);
        try {
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




public static class ConfigManagerhttper{
        private static ConfigManager configManager =new ConfigManager();



    }

    public static ConfigManager getConfigManager(){

        return  ConfigManagerhttper.configManager;
    }

public  String getValueKey(String key){
return  properties.getProperty(key);


}



}
