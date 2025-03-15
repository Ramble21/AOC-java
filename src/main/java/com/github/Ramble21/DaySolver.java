package com.github.Ramble21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class DaySolver {

    private long partOneMS;
    private long partTwoMS;
    public long getPartOneMS() {
        return partOneMS;
    }
    public long getPartTwoMS() {
        return partTwoMS;
    }

    public static String getInputFilePath(int year, int dayNumber) {
        return "inputs/" + year + "/day" + dayNumber + ".txt";
    }

    public static List<String> getInputLines(int year, int dayNumber) throws IOException {
        return new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getResourceAsStream(getInputFilePath(year, dayNumber)))
        )).lines().toList();
    }
    public static char[][] getInputAsGrid(int year, int dayNumber) throws IOException {
        List<String> input = getInputLines(year, dayNumber);
        char[][] output = new char[input.size()][input.get(0).length()];
        for (int i = 0; i < output.length; i++){
            output[i] = input.get(i).toCharArray();
        }
        return output;
    }
    public static void print2DArr(char[][] array) {
        for (char[] row : array) {
            System.out.println(row);
        }
    }
    public static void print2DIntArr(int[][] array) {
        for (int[] row : array) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void solve() throws IOException {
        System.out.println(solvePart1String());
        System.out.println(solvePart2String());
    }

    public abstract long solvePart1() throws IOException;
    public abstract long solvePart2() throws IOException;

    public String solvePart1String() throws IOException {
        return timePart1();
    }
    public String solvePart2String() throws IOException {
        return timePart2();
    }
    public String timePart1() throws IOException {
        long start = System.currentTimeMillis();
        long result = solvePart1();
        long end = System.currentTimeMillis();
        System.out.println("Day " + parseDayNumber() + " part 1: " + (end - start) + " ms");
        partOneMS = end - start;
        return String.valueOf(result);
    }
    public String timePart2() throws IOException {
        long start = System.currentTimeMillis();
        long result = solvePart2();
        long end = System.currentTimeMillis();
        partTwoMS = end - start;
        System.out.println("Day " + parseDayNumber() + " part 2: " + (end - start) + " ms");
        return String.valueOf(result);
    }
    private int parseDayNumber() {
        String className = this.getClass().getSimpleName();
        return Integer.parseInt(className.replaceAll("[^0-9]", ""));
    }
}

/* copy and paste for a new day class because im lazy - replace X with day number

package com.github.Ramble21.y2023.days;

import com.github.Ramble21.DaySolver;

import java.io.IOException;
import java.util.List;

public class DayX extends DaySolver {
    private final List<String> input;
    public DayX() throws IOException {
        input = getInputLines(2023, X);
    }
    public long solvePart1() throws IOException {
        return 0;
    }

    public long solvePart2() throws IOException {
        return 0;
    }
}

 */