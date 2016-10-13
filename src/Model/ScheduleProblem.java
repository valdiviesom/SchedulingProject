package Model;

import Parsers.BuildingsParser;
import Parsers.CriticalParser;
import Parsers.PeriodParser;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Created by Mauricio on 11/8/2015.
 */
public class ScheduleProblem {
    private List<Period> bank;
    private Schedule currentSchedule;
    private List<String> criticalSubjects;
    private static final int numIterations = (int) 1000;
    static final boolean computeDataAnalysis = true;

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
                    (j + 1) + " is " + solutions.get(j).calcCost(solutions.get(j).getSchedule()) + " meters.");
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
