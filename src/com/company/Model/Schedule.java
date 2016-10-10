package com.company.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 11/9/2015.
 */
public class Schedule {
    private List<Period> sched;
    private int cost;

    public Schedule(List<Period> sched) {
        this.sched = sched;
    }

    public Schedule(List<Period> sched, boolean evalCost) {
        this.sched = sched;
        if (evalCost) cost = calcCost(sched);
    }

    public List<Period> getSchedule() {
        return sched;
    }
    public int size(){
        return sched.size();
    }
    public Period getPeriod(int index){
        return sched.get(index);
    }

    public void removePeriod(Period remove) {
        if (sched.contains(remove)) sched.remove(remove);
    }

    // add a given Period. If it causes a time clash remove pre existing period
    public void addPeriod(Period toAdd) {
        boolean taken = false;
        Period toRemove = null;
        // check if TimeSlot is already taken, record if so
        for (Period next : sched) {
            if (next.getTime().equals(toAdd.getTime())) {
                removePeriod(next);
                break;
            }
        }
        sched.add(toAdd);
    }

    // check if a critical class is in the current schedule
    public boolean criticalInCurrent(String critical) {
        for (Period next2 : sched) {
            if (next2.getName().equalsIgnoreCase(critical)) {
                return true;
            }
        }
        return false;
    }


    public int cost() {
        return cost;
    }

    public Schedule findBest(List<Schedule> solutions) {
        Schedule rsf = solutions.get(0);
        for (Schedule next : solutions) {
            if (next.cost < rsf.cost) rsf = next;
        }
        return rsf;
    }

    // return all names of classes being taken
    public List<String> periodNames() {
        List<String> result = new ArrayList<String>();
        for (Period next : sched) {
            String x = next.getName();
            result.add(x);
        }
        return result;
    }

    public List<Period> sort(List<Period> given) {
        int upperBound = 50;
        int counter = 0;
        List<Period> rsf = new ArrayList<Period>();
        while (counter < upperBound) {
            for (Period next : given) {
                if (next.getTime().equals(counter)) {
                    rsf.add(next);
                }
            }
            counter++;
        }
        return rsf;
    }


    // takes a sorted day Schedule.
    public int calcDayCost(List<Period> given) {
        if (given.size() == 0) return 0;
        int bLx = 13; // bus loop X
        int bLy = 19; // bus loop Y
        Period current = given.get(0);
        // walk from bus loop
        int rsf = Math.abs(bLx - current.getX()) + Math.abs(bLy - current.getY());
        for (Period next : given) {
            rsf += Math.abs(current.getX() - next.getX()) + (current.getY() - next.getY());
            current = next;
        }
        // walk to bus loop
        rsf += Math.abs(current.getX() - bLx)
                + Math.abs(current.getY() - bLy);
        return rsf;
    }

    public int calcCost(List<Period> currentSchedule) {
        List<Period> t1mwfL = new ArrayList<Period>();
        List<Period> t1tthL = new ArrayList<Period>();
        List<Period> t2mwfL = new ArrayList<Period>();
        List<Period> t2tthL = new ArrayList<Period>();
        // sort classes by day type and term
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
        // sort each day type and term by class time
        t1mwfL = sort(t1mwfL);
        t1tthL = sort(t1tthL);
        t2mwfL = sort(t2mwfL);
        t2tthL = sort(t2tthL);

        //T1 MFF

        int t1mwf = calcDayCost(t1mwfL);
        // T1 T TH
        int t1tth = calcDayCost(t1tthL);

        // T2 MWF
        int t2mwf = calcDayCost(t2mwfL);

        // T2 TTH
        int t2tth = calcDayCost(t2tthL);

        return (3 * t1mwf + 2 * t1tth + 3 * t2mwf + 2 * t2tth) * 50; // 50 was the proportionality in our grid
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        // first, this subset of o
        boolean p1 = true;
        for (Period next : sched) {
            if (!(((Schedule) o).sched.contains(next)))
                return false;
            /*
            if (!(((Schedule) o).sched.))
                if (!(o.contains(next))) {
                    p1 = false;
                }*/
        }
        // now check if o is subset of this
        // todo: test this method
        for (Period next : schedule.sched) {
            if (!(sched).contains(next)) return false;
        }
        return true;
    }


}
