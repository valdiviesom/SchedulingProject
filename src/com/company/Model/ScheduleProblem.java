package com.company.Model;

import com.company.Parsers.CriticalParser;
import com.company.Parsers.PeriodParser;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Mauricio on 11/8/2015.
 */
public class ScheduleProblem {
    private List<Period> bank;
    private Schedule currentSchedule;
    private List<String> criticalSubjects;

    public ScheduleProblem() throws IOException, JSONException {
        bank = PeriodParser.periodsParser();
        criticalSubjects = CriticalParser.criticalParser();
        currentSchedule = new Schedule(findInitialFeasible());
        iterator();
    }

    public List<Period> findInitialFeasible() {
        List<Period> rsf = new ArrayList<Period>();
        List<List<Period>> filteredBankList = new ArrayList<List<Period>>();
        for (String s : criticalSubjects) {
            filteredBankList.add(filterBank(s));
        }
        for (int i = 0; i < 10; i++) {
            rsf.add(filteredBankList.get(i).get(0));
        }
        return rsf;
    }

    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }

    public ScheduleProblem(List<Period> bank, Schedule feasible, List<String> criticalSubjects) {
        this.bank = bank;
        this.currentSchedule = feasible;
        this.criticalSubjects = criticalSubjects;
    }


    public boolean isTimeOccupied(int t) {
        int x = 0;
        for (Period next : currentSchedule.getSchedule()) {
            if (next.getTime() == t) {
                x = 1;
            }
        }
        return (x == 1);
    }


    public boolean hasAllSubjects() {
        for (String s : criticalSubjects) {
            if (!(currentSchedule.criticalInCurrent(s)))
                return false;
        }
        return true;
    }

    public List<Period> filterBank(String name) {
        List<Period> result = new ArrayList<Period>();
        for (Period next : bank) {
            if (next.getName().equals(name)) {
                result.add(next);
            }
        }
        return result;
    }

    public Period randomToIterate() {
        Random x = new Random();
        int y = x.nextInt(currentSchedule.size());
        return currentSchedule.getPeriod(y);
    }

    public List<String> namesInCurrentSchedule() {
        List<String> strings = new ArrayList<String>();
        for (Period next : currentSchedule.getSchedule()) {
            strings.add(next.getName());
        }
        return strings;
    }

    public String missingSubject(String original) {
        String result = original;
        for (String next : criticalSubjects) {
            if (!namesInCurrentSchedule().contains(next))
                result = next;
        }
        return result;
    }

    public void iterator() {
        // find which Subject to iterate
        Period toIterate = randomToIterate();
        //removePeriod(toIterate);
        currentSchedule.removePeriod(toIterate);
        String nameToIterate = toIterate.getName();
        while (!hasAllSubjects()) {
            // Get everywhere that subject can be replaced to
            List<Period> toChoose;
            toChoose = filterBank(nameToIterate);
            // select period to put back in
            Random q = new Random();
            int z = q.nextInt(toChoose.size());
            Period putIn = toChoose.get(z);
            currentSchedule.addPeriod(putIn);
            //currentSchedule.add(putIn);
            nameToIterate = missingSubject(nameToIterate);
        }
        currentSchedule.updateCost();
    }


}
