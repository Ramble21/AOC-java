package com.github.Ramble21;

import java.io.IOException;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Exception {
        testSpecificDay(2023, 10);
    }
    public static void testEveryDay(int year) throws IOException {
        long startTime = System.currentTimeMillis();
        DaySolver[] solvers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> {
                    try {
                        return (DaySolver) Class.forName("com.github.Ramble21.y" + year + ".days.Day" + i)
                                .getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(DaySolver[]::new);
        long totalMs = (System.currentTimeMillis() - startTime);
        System.out.println("DaySolver initializations: " + totalMs + " ms");
        for (DaySolver solver : solvers){
            solver.solve();
            totalMs += solver.getPartOneMS() + solver.getPartTwoMS();
        }
        System.out.println("Advent of Code " + year + " Total: " + totalMs + " ms");
    }
    public static void testSpecificDay(int year, int dayNo) throws Exception {
        String className = "com.github.Ramble21.y" + year + ".days.Day" + dayNo;
        DaySolver solver = (DaySolver) Class.forName(className).getDeclaredConstructor().newInstance();
        solver.solve();
    }
}