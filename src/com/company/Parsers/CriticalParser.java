package com.company.Parsers;
import com.company.TfL.DataProvider;
import com.company.TfL.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Mauricio on 11/9/2015.
 */
public class CriticalParser {

    public static List<String> criticalParser() throws IOException, JSONException {
        DataProvider dataProvider = new FileDataProvider("./raw/critical_data.json");
        String data = dataProvider.dataSourceToString();
        JSONArray jsonArray = new JSONArray(data);
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject next = jsonArray.getJSONObject(i);
            String courseName = next.getString("name");
            result.add(courseName);
        }
        return result;
    }
}
