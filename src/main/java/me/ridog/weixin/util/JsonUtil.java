package me.ridog.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Tate on 2016/11/8.
 */
public class JsonUtil {


    public static String getJSON(String jsonUrl) {

        try {
            URL url = new URL(jsonUrl);
            InputStream inputStream = url.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            inputStream.close();
            inputStreamReader.close();
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{}";
    }
}
