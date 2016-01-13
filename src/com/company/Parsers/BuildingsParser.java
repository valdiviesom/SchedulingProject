package com.company.Parsers;

import com.company.Model.Building;
import com.company.Model.Period;
import com.company.TfL.DataProvider;
import com.company.TfL.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mauricio on 11/8/2015.
 */
public class BuildingsParser {




    public static List<Building> buildingsParser() throws JSONException, IOException {
        DataProvider dataProvider = new FileDataProvider("./raw/building_data.json");
        String data = dataProvider.dataSourceToString();
        JSONArray json = new JSONArray(data);
        List<Building> result = new ArrayList<Building>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject next = json.getJSONObject(i);
            Integer x = next.getInt("x");
            Integer y = next.getInt("y");
            String name = next.getString("name");
            Building b = new Building(name, x, y);
            result.add(b);
        }
        return result;
    }

    public Building findBuilding(Period period) throws IOException, JSONException {
        Building b = null;
        for (Building next : buildingsParser()) {
            if (period.getX() == next.getX() && period.getY() == next.getY()){
                b = next;
            }
        }
        return b;
    }
    public Building findBuilding(String name) throws IOException, JSONException {
        Building b = null;
        for (Building next : buildingsParser()) {
            if (next.toString().equals(name)){
                b = next;
            }
        }
        return b;
    }

}
