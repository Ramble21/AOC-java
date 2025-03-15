package com.github.Ramble21.y2024.days;

import java.io.IOException;
import java.util.*;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.BreadthFirstSearch;
import com.github.Ramble21.helper_classes.Location;

public class Day20 extends DaySolver {
    private final List<String> input;
    private char[][] grid;
    private final Location end;
    private final Location start;

    public Day20() throws IOException {
        input = getInputLines(2024, 20);
        inputToGrid();
        Location start = null;
        Location end = null;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == 'S') start = new Location(c, r);
                else if (grid[r][c] == 'E') end = new Location(c, r);
            }
        }
        this.end = end;
        this.start = start;
        inputToGrid();
    }
    public long solvePart1() throws IOException {
        return cheatsThatSave(2);
    }
    public long solvePart2() throws IOException {
        return cheatsThatSave(20);
    }
    private long cheatsThatSave(int maxPS){
        long count = 0;
        BreadthFirstSearch control = new BreadthFirstSearch(start, end, grid);
        int controlValue = control.getResult();
        HashMap<Location, Integer> distances = control.getDistances();
        for (Location step : distances.keySet()){
            if (Location.getTaxicabDistance(step, end) > (controlValue - distances.get(step))) continue;
            HashSet<Location> possibleCheatJumps = getPossibleCheatJumps(step, maxPS);
            for (Location cheat : possibleCheatJumps){
                if (step.equals(cheat)) continue;
                int newDistance = distances.get(step) + Location.getTaxicabDistance(step, cheat) + (controlValue - distances.get(cheat));
                if (controlValue - newDistance >= 100){
                    count++;
                }
            }
        }
        return count;
    }
    private HashSet<Location> getPossibleCheatJumps(Location center, int maxPS) {
        HashSet<Location> result = new HashSet<>();
        for (int r = center.y() - maxPS; r <= center.y() + maxPS; r++){
            if (r >= grid.length) break;
            if (r < 0){
                r = -1;
                continue;
            }
            for (int c = center.x() - maxPS; c <= center.x() + maxPS; c++){
                if (c >= grid[0].length) continue;
                if (Location.getTaxicabDistance(new Location(c, r), center) > maxPS) continue;
                if (c < 0){
                    c = -1;
                    continue;
                }
                if (grid[r][c] != '#') result.add(new Location(c, r));
            }
        }
        return result;
    }
    public void inputToGrid(){
        grid = new char[input.size()][input.get(0).length()];
        for (int r = 0; r < input.size(); r++){
            for (int c = 0; c < input.get(0).length(); c++){
                grid[r][c] = input.get(r).charAt(c);
            }
        }
    }
}