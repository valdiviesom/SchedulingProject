package com.company.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 11/9/2015.
 */
public class Schedule {
    List<Period> sched;
    public Schedule (List<Period> sched){
        this.sched = sched;
    }
    // return all names of classes being taken
    public List<String> periodNames(){
        List<String> result = new ArrayList<String>();
        for (Period next: sched){
            String x = next.getName();
            result.add(x);
        }
        return result;
    }
    public int valueCalc(){
        int rsf = 0;
        Period p0 = sched.get(0);
        for (int i = 1; i < sched.size(); i++) {
            Period p = sched.get(i);
            int dx = Math.abs(p.getX() - p0.getX());
            int dy = Math.abs(p.getY() - p0.getY());
            rsf = Math.abs(dx + dy);
            p0 = p;
        }
        return rsf;
    }
    public int size(){
        return sched.size();
    }

}
