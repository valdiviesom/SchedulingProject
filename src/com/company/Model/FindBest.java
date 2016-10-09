package com.company.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 11/14/2015.
 */
public class FindBest {
    List<List<Period>> solutions;
    List<Schedule> solns;

    public FindBest(List<List<Period>> solutions) {
        this.solutions = solutions;
    }

    public int distinct(){
        int resultCounter;
        List<List<Period>> distinctSoFar = new ArrayList<List<Period>>();
        for (List<Period> next: solutions){
            //next = sort(next);
            if (!(contiene(distinctSoFar, next))){
                distinctSoFar.add(next);
            }
        }
        resultCounter=distinctSoFar.size();
        return resultCounter;
    }

    public boolean contiene(List<List<Period>> all, List<Period> particular){
        // check if a particular schedule is in a list (all)
        boolean found = false;
        for (List<Period> next: all){
            if (sameSched(next, particular)){
                found = true;
            }
        }
        return found;
    }

    public boolean sameSched(List<Period> s1, List<Period> s2){
        // check if two scheds are equal

        // first, s1 subset of s2
        boolean p1 = true;
        for (Period next: s1){
            if (!(s2.contains(next))){
                p1=false;
            }
        }
        boolean p2 = true;
        for (Period next: s2){
            if (!(s1.contains(next))){
                p2=false;
            }
        }
        return (p1&&p2);
    }


    public List<Period> sort(List<Period> given){
        int upperBound = 50;
        int counter = 0;
        List<Period> rsf = new ArrayList<Period>();
        while (counter < upperBound){
            for (Period next: given){
                if (next.getTime().equals(counter)){
                    rsf.add(next);
                }
            }
            counter++;
        }
        return rsf;
    }


    public Period firstPeriodOfTheDay(List<Period> given) {
        Period result = given.get(0);
        for (Period next : given) {
            if (next.getTime() < result.getTime()) {
                result = next;
            }
        }
        return result;
    }
    public Period lastPeriodOfTheDay(List<Period> given){
        Period result = given.get(0);
        for (Period next : given) {
            if (next.getTime() > result.getTime()) {
                result = next;
            }
        }
        return result;
    }

    public int calcValue(List<Period> currentSchedule) {
        List<Period> t1mwfL = new ArrayList<Period>();
        List<Period> t1tthL = new ArrayList<Period>();
        List<Period> t2mwfL = new ArrayList<Period>();
        List<Period> t2tthL = new ArrayList<Period>();
        for (Period next : currentSchedule) {
            if (next.getTime() <= 10) {
                t1mwfL.add(next);
            } else if (next.getTime() <= 16) {
                t1tthL.add(next);
            } else if (next.getTime() <= 26) {
                t2mwfL.add(next);
            } else if (next.getTime() <= 32) {
                t2tthL.add(next);
            }
        }
        t1mwfL = sort(t1mwfL);
        t1tthL = sort(t1tthL);
        t2mwfL= sort(t2mwfL);
        t2tthL = sort(t2tthL);
        int bLx = 13;
        int bLy = 19;

        //T1 MFF
        int t1mwf;
        if (t1mwfL.size() == 0) {
            t1mwf = 0;
        } else {
            Period t1mwfP = firstPeriodOfTheDay(t1mwfL);
            t1mwf = Math.abs(bLx - t1mwfP.getX()) + Math.abs(bLy - t1mwfP.getY());
            Period current1 = t1mwfP;
            for (Period next : t1mwfL) {
                t1mwf += Math.abs(current1.getX() - next.getX())
                        + Math.abs(current1.getY() - next.getY());
                current1 = next;
            }
            current1 = lastPeriodOfTheDay(t1mwfL);
            t1mwf += Math.abs(current1.getX() - bLx)
                    + Math.abs(current1.getY() - bLy);
        }
        // T1 T TH
        int t1tth;
        if (t1tthL.size() == 0) {
            t1tth = 0;
        } else {
            Period t1TTHP = firstPeriodOfTheDay(t1mwfL);
            t1tth = Math.abs(bLx - t1TTHP.getX()) + Math.abs(bLy - t1TTHP.getY());
            Period current2 = t1TTHP;
            for (Period next : t1tthL) {
                t1tth += Math.abs(current2.getX() - next.getX())
                        + Math.abs(current2.getY() - next.getY());
                current2 = next;
            }
            current2 = lastPeriodOfTheDay(t1tthL);
            t1tth+= Math.abs(bLx - current2.getX()) + Math.abs(bLy - current2.getY());
        }
        // T2 MWF
        int t2mwf;
        if (t2mwfL.size() == 0) {
            t2mwf = 0;
        } else {
            Period t2MWFp = firstPeriodOfTheDay(t2mwfL);
            t2mwf = Math.abs(bLx - t2MWFp.getX()) + Math.abs(bLy - t2MWFp.getY());
            Period current3 = t2MWFp;
            for (Period next : t2mwfL) {
                t2mwf += Math.abs(current3.getX() - next.getX()) +
                        Math.abs(current3.getY() - next.getY());
                current3 = next;
            }
            current3 = lastPeriodOfTheDay(t2mwfL);
            t2mwf += Math.abs(bLx - current3.getX()) + Math.abs(bLy - current3.getY());
        }
        // T2 TTH
        int t2tth;
        if (t2tthL.size() == 0) {
            t2tth = 0;
        } else {
            Period current4 = firstPeriodOfTheDay(t2tthL);
            t2tth = Math.abs(bLx - current4.getX()) +
                    Math.abs(bLy - current4.getY());
            for (Period next : t2tthL) {
                t2tth += Math.abs(current4.getX() - next.getX()) +
                        Math.abs(current4.getY() - next.getY());
                current4 = next;
            }
            current4 = lastPeriodOfTheDay(t2tthL);
            t2tth += Math.abs(bLx - current4.getX()) +
                    Math.abs(bLy - current4.getY());
        }
        return (3*t1mwf + 2*t1tth + 3*t2mwf + 2*t2tth)*50;
    }

    public List<Period> returnBest() {
        List<Period> best = solutions.get(0);
        for (List<Period> next : solutions) {
            if (calcValue(next) < calcValue(best)) {
                best = next;
            }
        }
        return best;
    }

    public List<List<Period>> improvingList() {
        List<Period> lastAdded = solutions.get(0);
        List<List<Period>> rsf = new ArrayList<List<Period>>();
        rsf.add(lastAdded);
        for (List<Period> next : solutions) {
            if (calcValue(next) < calcValue(lastAdded)) {
                rsf.add(next);
                lastAdded = next;
            }
        }
        return rsf;
    }
}
