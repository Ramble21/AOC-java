package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.y2024.classes.Guard;
import com.github.Ramble21.helper_classes.Location;

import java.io.IOException;

import java.util.HashSet;
import java.util.List;

public class Day6 extends DaySolver {
    private final List<String> input;
    private char[][] grid;
    private final HashSet<Location> locationsTraveled = new HashSet<>();
    private Location startingLoc;

    public Day6() throws IOException {
        input = getInputLines(2024, 6);
        inputToGrid();
    }
    public long solvePart1() throws IOException {
        Guard guard = null;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == '^'){
                    startingLoc = new Location(c, r);
                    guard = new Guard(startingLoc, grid);
                    locationsTraveled.add(startingLoc);
                    break;
                }
            }
        }
        assert guard != null;
        while (guard.isOnMappedArea()){
            guard.move();
            locationsTraveled.add(guard.getCurrentLoc());
        }
        return locationsTraveled.size();
    }
    public long solvePart2() throws IOException {
        int count = 0;
        for (Location l : locationsTraveled){
            if (l.equals(startingLoc)) continue;
            if (causesLoop(l)) count++;
        }
        return count;
    }
    public boolean causesLoop(Location obstacle){
        char[][] newGrid = new char[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++){
            System.arraycopy(grid[r], 0, newGrid[r], 0, grid[0].length);
        }
        newGrid[obstacle.y()][obstacle.x()] = '#';
        Guard test = new Guard(startingLoc, newGrid);
        HashSet<String> locDirPairs = new HashSet<>();
        while (test.isOnMappedArea()){
            if (locDirPairs.contains(test.getCurrentLoc().toString() + test.getCurrentDir().getCharRep())){
                return true;
            }
            locDirPairs.add(test.getCurrentLoc().toString() + test.getCurrentDir().getCharRep());
            test.move();
        }
        return false;
    }
    public void inputToGrid(){
        char[][] g = new char[input.size()][input.get(0).length()];
        for (int r = 0; r < g.length; r++){
            for (int c = 0; c < input.get(1).length(); c++){
                g[r][c] = input.get(r).charAt(c);
            }
        }
        grid = g;
    }
}