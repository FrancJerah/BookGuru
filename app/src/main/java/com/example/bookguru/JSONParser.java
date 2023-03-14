package com.example.bookguru;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class JSONParser {
    static URL urlObj = null;
    static String jSONString = "";
    static JSONObject json = null;

    public JSONParser(){ }

    public  JSONObject makeHTTPRequest (String url, String processTag, ContentValues cv){
        String postValues = "";
        URLConnection conn = null;
        BufferedReader reader = null;

        try {
            for (Map.Entry<String, Object> entry : cv.valueSet()) {
                postValues += "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8");
            }
            urlObj = new URL(url);
            conn = urlObj.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postValues);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            jSONString = sb.toString();
            json = new JSONObject(jSONString);
            conn = null;
            return json;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}