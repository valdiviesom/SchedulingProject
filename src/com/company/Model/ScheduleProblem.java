package com.company.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Mauricio on 11/8/2015.
 */
public class ScheduleProblem {
    List<Period> bank;
    private List<Period> currentSchedule;
    List<String> criticalSubjects;


    public ScheduleProblem(List<Period> bank, List<Period> feasible, List<String> criticalSubjects) {
        this.bank = bank;
        this.currentSchedule = feasible;
        this.criticalSubjects = criticalSubjects;

    }
    public List<Period> getCurrentSchedule(){
        return currentSchedule;
    }
    public List<Period> getBank(){
        return bank;
    }

    public boolean isTimeOccupied(int t) {
        int x = 0;
        for (Period next : currentSchedule) {
            if (next.getTime() == t) {
                x = 1;
            }
        }
        return (x == 1);
    }

    // Remove a Period from currentSchedule, if its there
    public void removePeriod(Period ts) {
        if (currentSchedule.contains(ts)) currentSchedule.remove(ts);
    }

    public void addPeriod(Period ts) {
        boolean taken = false;
        Period toRemove = null;
        // check if TimeSlot is already taken, record if so
        for (Period next : currentSchedule) {
            if (next.getTime().equals(ts.getTime())) {
                taken = true;
                toRemove = next;
            }
        }
        if (taken) {
            this.removePeriod(toRemove);
        }
        currentSchedule.add(ts);
    }

    // check if a critical class is in the current schedule
    public boolean criticalInCurrent(String critical) {
        int a = 0;
        for (Period next2 : currentSchedule) {
            if (next2.getName().equalsIgnoreCase(critical)) {
                a = 1;
            }
        }
        return (a == 1);
    }

    public boolean hasAllSubjects() {
        boolean x = true;
        for (String next : criticalSubjects) {
            int a = 0;
            for (Period next2 : currentSchedule) {
                if (next2.getName().equalsIgnoreCase(next)) {
                    a = 1;
                }
            }
            if (a == 0) {
                x = false;
            }
        }
        return x;
    }

    public List<Period> filterBank(String name){
        List<Period> result = new ArrayList<Period>();
        for (Period next: bank){
            if (next.getName().equals(name)){
                result.add(next);
            }
        }
        return result;
    }
    public Period randomToIterate(){
        Random x = new Random();
        int y = x.nextInt(currentSchedule.size());
        return currentSchedule.get(y);
    }
    public List<String> namesInCurrentSchedule(){
        List<String> strings = new ArrayList<String>();
        for (Period next: currentSchedule){
            strings.add(next.getName());
        }
        return strings;
    }
    public String missingSubject(String original){
        String result = original;
        for (String next: criticalSubjects){
            if (!namesInCurrentSchedule().contains(next))
                result = next;
        }
        return result;
    }
    public void iterator(){
        // find which Subject to iterate
        Period toIterate = randomToIterate();
        //removePeriod(toIterate);
        currentSchedule.remove(toIterate);
        String nameToIterate = toIterate.getName();
        while (!hasAllSubjects()) {

            // Get everywhere that subject can be replaced to
            List<Period> toChoose;
            toChoose = filterBank(nameToIterate);
            // select period to put back in
            Random q = new Random();
            int z = q.nextInt(toChoose.size());
            Period putIn = toChoose.get(z);
            addPeriod(putIn);
            //currentSchedule.add(putIn);
            nameToIterate = missingSubject(nameToIterate);
        }
    }


}
