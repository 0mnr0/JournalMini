package com.svl.journalmini;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

public class JSONUtils {

    public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();
        try {
            // Copy all fields from json1 to mergedJSON
            Iterator<String> keys = json1.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mergedJSON.put(key, json1.get(key));
            }

            // Copy all fields from json2 to mergedJSON, overwriting any duplicate keys
            keys = json2.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mergedJSON.put(key, json2.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mergedJSON;
    }
}
