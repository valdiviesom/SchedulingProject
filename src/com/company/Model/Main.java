package com.company.Model;

import com.company.Parsers.BuildingsParser;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static final int numIterations = (int) 1000;
    static final boolean computeDataAnalysis = true;

    public static void main(String[] args) throws IOException, JSONException {
        long startTime = System.nanoTime();
        //====================  define problem  ===========================
        ScheduleProblem scheduleProblem = new ScheduleProblem();

        //==================== solve =====================================

        List<Schedule> solutions = new LinkedList<Schedule>();
        int bestCost = scheduleProblem.getCurrentSchedule().getCost();
        int bestIteration = 0;
        solutions.add(scheduleProblem.getCurrentSchedule());
        for (int i = 0; i < numIterations; i++) {
            scheduleProblem.iterator();
            if (Schedule.contains(solutions, scheduleProblem.getCurrentSchedule())
                    && scheduleProblem.getCurrentSchedule().getCost() < bestCost) {
                solutions.add(scheduleProblem.getCurrentSchedule());
                bestCost = scheduleProblem.getCurrentSchedule().getCost();
                bestIteration = i;
            }

        }
        //=====================  output  =====================================
        Schedule bestSchedule = Schedule.findBest(solutions);
        List<Building> campus = BuildingsParser.buildingsParser();

        // time breakpoints
        int d1 = 10;
        int d2 = 16;
        int d3 = 26;
        int d4 = 32;
        List<Period> t1mwfL = new ArrayList<Period>();
        List<Period> t1tthL = new ArrayList<Period>();
        List<Period> t2mwfL = new ArrayList<Period>();
        List<Period> t2tthL = new ArrayList<Period>();
        for (Period next : bestSchedule.getSchedule()) {
            if (next.getTime() <= d1) {
                t1mwfL.add(next);
            } else if (next.getTime() <= d2) {
                t1tthL.add(next);
            } else if (next.getTime() <= d3) {
                t2mwfL.add(next);
            } else if (next.getTime() <= d4) {
                t2tthL.add(next);
            }
        }
        t1mwfL = Schedule.sort(t1mwfL);
        t1tthL = Schedule.sort(t1tthL);
        t2mwfL = Schedule.sort(t2mwfL);
        t2tthL = Schedule.sort(t2tthL);
        int w1 = Schedule.calcDayCost(t1mwfL);
        int w2 = Schedule.calcDayCost(t1tthL);
        int w3 = Schedule.calcDayCost(t2mwfL);
        int w4 = Schedule.calcDayCost(t2tthL);

        System.out.println("        ====    Your Schedule is:    ====");
        if (t1mwfL.size() + t1tthL.size() > 0) {
            System.out.println("<< Term One: >>");
            if (t1mwfL.size() > 0) {
                System.out.println("Mondays, Wednesdays, and Fridays: (Weekly walking distance: " + w1 + " meters)");
                for (Period next : t1mwfL) {
                    System.out.println("  " + next.getName() + " from " +
                            (7 + next.getTime()) + ":00" + " to " + (8 + next.getTime()) + ":00"
                            + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                }
            }
            if (t1tthL.size() > 0) {
                System.out.println("Tuesdays and Thursdays: (Weekly walking distance: " + w2 + " meters)");
                for (Period next : t1tthL) {
                    if (next.getTime() % 2 == 1) {
                        int start = (int) (next.getTime() * 1.5 - 8.5);
                        int end = start + 1;
                        System.out.println("  " + next.getName() + " from "
                                + start + ":00" + " to " + ":30" + end
                                + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                    } else {
                        int start = (int) (next.getTime() * 1.5 - 9);
                        int end = start + 2;
                        System.out.println("  " + next.getName() + " from " +
                                start + ":30" + " to " + end + ":00"
                                + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                    }
                }
            }

        }

        if (t2mwfL.size() + t2tthL.size() > 0) {
            System.out.println("<< Term Two: >>");
            if (t2mwfL.size() > 0) {
                System.out.println("Mondays, Wednesdays, and Fridays: (Weekly walking distance: " + w3 + " meters)");
                for (Period next : t2mwfL) {
                    System.out.println("  " + next.getName() + " from " +
                            (-9 + next.getTime()) + ":00" + " to " + (-8 + next.getTime()) + ":00"
                            + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                }
            }

            if (t2tthL.size() > 0) {
                System.out.println("Tuesdays and Thursdays: (Weekly walking distance: " + w4 + " meters)");
                for (Period next : t2tthL) {
                    if (next.getTime() % 2 == 1) {
                        int start = (int) ((next.getTime() - 16) * 1.5 - 8.5);
                        int end = start + 1;
                        System.out.println("  " + next.getName() + " from "
                                + start + ":00" + " to " + end + ":30"
                                + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                    } else {
                        int start = (int) ((next.getTime() - 16) * 1.5 - 9);
                        int end = start + 2;
                        System.out.println("  " + next.getName() + " from " +
                                start + ":30" + " to " + end + ":00"
                                + " in " + next.findBuilding(campus).toString() + " " + next.room + ".");
                    }
                }
            }

        }
        long schedTime = System.nanoTime();


        System.out.println("        ==== Walking Time Improvement ====");
        for (int j = 0; j < solutions.size(); j++) {
            System.out.println("Walking time at schedule improvement " +
                    (j + 1) + " is " + solutions.get(j).getCost() + " meters.");
        }

        System.out.println("        ====    Total Walking Distance    ====");
        System.out.println("Your total walking distance is " + bestCost + " meters.");
        // ================ Analysis ====================
        if (computeDataAnalysis) {
            System.out.println("        ====   Data Analysis   ====");
            System.out.println("The optimum schedule was found at iteration # "
                    + bestIteration + ", having run " + numIterations + " iterations.");
            System.out.println("There were " + solutions.size() + " distinct and non-decreasing-cost schedules computed.");
            long dataTime = System.nanoTime();
            System.out.println("Local Optimum Schedule runtime: " + (schedTime - startTime) / (1E9) + " seconds.");
            System.out.println("Data Analysis runtime: " + (dataTime - schedTime) / (1E9) + " seconds.");
        }
    }
}

