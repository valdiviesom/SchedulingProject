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
 * Created by Mauricio on 11/9/2015.
 */
public class PeriodParser {


    public static List<Period> periodsParser() throws IOException, JSONException {
        DataProvider dataProvider = new FileDataProvider("./raw/period_data.json");
        String data = dataProvider.dataSourceToString();
        JSONArray json = new JSONArray(data);
        List<Period> result = new ArrayList<Period>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject next = json.getJSONObject(i);
            String bName = next.getString("building");
            List<Building> campus = BuildingsParser.buildingsParser();
            Integer xCoordinate = 1000;
            Integer yCoordinate = 1000;
            for (Building b: campus){
                if (b.toString().equals(bName)){
                    xCoordinate = b.getX();
                    yCoordinate = b.getY();
                }
            }
            String subjectName = next.getString("name");
            Integer room = next.getInt("room");
            Integer time = next.getInt("time");
            Period p = new Period(subjectName,room, xCoordinate,yCoordinate,time);
            result.add(p);
        }
        return result;
    }
}
