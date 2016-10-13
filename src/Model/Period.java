package Model;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mauricio on 11/8/2015.
 */

/*
    A time and place when a subject is available
 */
public class Period {
    private Integer x;
    private Integer y;
    private Integer time;
    private String subjectName;
    Integer room;

    public Period (String subjectName,Integer room, int x, int y, int t){
        this.x = x;
        this.y= y;
        this.time = t;
        this.subjectName = subjectName;
        this.room = room;
    }
    public Integer getTime(){
        return this.time;
    }
    public String getName(){
        return subjectName;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (x != null ? !x.equals(period.x) : period.x != null) return false;
        if (y != null ? !y.equals(period.y) : period.y != null) return false;
        return !(time != null ? !time.equals(period.time) : period.time != null) && !(subjectName != null ? !subjectName.equals(period.subjectName) : period.subjectName != null);

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (subjectName != null ? subjectName.hashCode() : 0);
        return result;
    }

    public Building findBuilding(List<Building> parsedBuildings) throws IOException, JSONException {
        Building b = null;
        for (Building next : parsedBuildings) {
            if (x == next.getX() && y == next.getY()){
                b = next;
            }
        }
        return b;
    }

}
