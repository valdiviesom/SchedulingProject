package com.company.Model;

import com.company.Parsers.BuildingsParser;
import com.company.Parsers.CriticalParser;
import com.company.Parsers.PeriodParser;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int numIterations = (int) 100;
    static final boolean computeDataAnalysis = false;

    public static void main(String[] args) throws IOException, JSONException {
        long startTime = System.nanoTime();
        //====================  define problem  ===========================
        List<Period> bank;
        bank = PeriodParser.periodsParser();
        List<Period> feasible;
        FindFeasible findFeasible = new FindFeasible(bank);
        feasible = findFeasible.feasibleResult();
        List<String> critical;
        critical = CriticalParser.criticalParser();

        ScheduleProblem problem = new ScheduleProblem(bank, feasible, critical);

        //==================== solve =====================================

        List<List<Period>> solutions = new ArrayList<List<Period>>();
        problem.iterator();
        int i = 0;
        while (i < numIterations) {
            problem.iterator();
            List<Period> particular = new ArrayList<Period>();
            for (Period p : problem.getCurrentSchedule()) {
                particular.add(p);
            }
            solutions.add(particular);
            i++;
        }

        //=====================  output  =====================================
        FindBest fb = new FindBest(solutions);
        List<Period> finalSched = fb.returnBest();
        List<Building> campus = BuildingsParser.buildingsParser();


        int d1 = 10;
        int d2 = 16;
        int d3 = 26;
        int d4 = 32;
        List<Period> t1mwfL = new ArrayList<Period>();
        List<Period> t1tthL = new ArrayList<Period>();
        List<Period> t2mwfL = new ArrayList<Period>();
        List<Period> t2tthL = new ArrayList<Period>();
        for (Period next : finalSched) {
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
        t1mwfL = fb.sort(t1mwfL);
        t1tthL = fb.sort(t1tthL);
        t2mwfL = fb.sort(t2mwfL);
        t2tthL = fb.sort(t2tthL);
        int w1 = fb.calcValue(t1mwfL);
        int w2 = fb.calcValue(t1tthL);
        int w3 = fb.calcValue(t2mwfL);
        int w4 = fb.calcValue(t2tthL);

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
        List<List<Period>> seeImprovement = fb.improvingList();
        for (int j = 0; j < seeImprovement.size(); j++) {
            int k = j + 1;
            System.out.println("Walking time at schedule improvement " +
                    k + " is " + fb.calcValue(seeImprovement.get(j)) + " meters.");
        }

        System.out.println("        ====    Total Walking Distance    ====");
        System.out.println("Your total walking distance is " + fb.calcValue(finalSched) + " meters.");
        // ================ Analysis ====================
        if (computeDataAnalysis) {
            System.out.println("        ====   Data Analysis   ====");
            int iterationsCounter = 0;
            boolean alreadyFound = false;
            int toCompare = fb.calcValue(finalSched);
            for (List<Period> next : solutions) {
                if (fb.calcValue(next) == toCompare && !alreadyFound) {
                    System.out.println("The optimum schedule was found at iteration # "
                            + iterationsCounter + ", having run " + numIterations + " iterations.");
                    alreadyFound = true;
                } else iterationsCounter++;
            }
            int distinctSolutions = fb.distinct();
            System.out.println("There are " + distinctSolutions + " distinct schedules computed.");
            long dataTime = System.nanoTime();
            System.out.println("Local Optimum Schedule runtime: " + (schedTime - startTime) / (1E9) + " seconds.");
            System.out.println("Data Analysis runtime: " + (dataTime - schedTime) / (1E9) + " seconds.");
        }
    }
}

