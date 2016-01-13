package com.company.Model;

import com.company.Parsers.CriticalParser;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 11/9/2015.
 */
public class FindFeasible {
    //List<Period> currentSchedule;
    private final List<Period> bank;

    public FindFeasible(List<Period> bank) {
        this.bank = bank;
    }

    private boolean takingAll(List<Period> attempt) throws IOException, JSONException {
        List<String> requiredCourses = CriticalParser.criticalParser();
        Schedule s = new Schedule(attempt);
        List<String> classesTaken = s.periodNames();
        for (String next : requiredCourses) {
            if (!(classesTaken.contains(next))) {
                return false;
            }
        }
        return true;
    }

    private boolean noClash(List<Period> attempt) {
        List<Integer> times = new ArrayList<Integer>();
        for (Period next : attempt) {
            if (times.contains(next.getTime())) {
                return false;
            } else times.add(next.getTime());
        }
        return true;
    }

    public List<Period> feasibleResult() throws IOException, JSONException {
        List<Period> rsf = new ArrayList<Period>();
        // initial try
        List<String> toTake = CriticalParser.criticalParser();
        for (String next : toTake) {
            Period p = new Period("", 1, 1, 1, 1);
            for (Period period : bank) {
                if (period.getName().equals(next)) {
                    p = period;
                }
            }
            rsf.add(p);
        }

        while (!(noClash(rsf) && takingAll(rsf))) {
            //keep iterating
            ScheduleProblem sp = new ScheduleProblem(bank, rsf, toTake);
            sp.iterator();
            rsf = sp.getCurrentSchedule();
        }
        return rsf;
    }

}
