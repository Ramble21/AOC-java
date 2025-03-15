package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day10 extends DaySolver {
    private final List<String> input;
    private int[][] grid;
    public Day10() throws IOException {
        input = getInputLines(2024, 10);
        inputToGrid();
    }
    public long solvePart1() throws IOException {
        int count = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] != 0) continue;
                Location pathStart = new Location(c,r);
                HashSet<Location> completedPaths = new HashSet<>();
                dfsPath(pathStart, completedPaths);
                count += completedPaths.size();
            }
        }
        return count;
    }

    public long solvePart2() throws IOException {
        int count = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] != 0) continue;
                Location pathStart = new Location(c,r);
                ArrayList<Location> completedPaths = new ArrayList<>();
                dfsPathTwo(pathStart, completedPaths);
                count += completedPaths.size();
            }
        }
        return count;
    }
    public void inputToGrid(){
        int[][] g = new int[input.size()][input.get(0).length()];
        for (int r = 0; r < g.length; r++){
            for (int c = 0; c < input.get(0).length(); c++){
                g[r][c] = Integer.parseInt(String.valueOf(input.get(r).charAt(c)));
            }
        }
        grid = g;
    }
    public void dfsPath(Location position, HashSet<Location> completedPaths){
        Location[] paths = possiblePaths(position.y(), position.x());
        if (grid[position.y()][position.x()] == 9) completedPaths.add(position);
        if (paths == null){
            return;
        }
        for (Location path : paths){
            dfsPath(path, completedPaths);
        }
    }
    public void dfsPathTwo(Location position, ArrayList<Location> completedPaths){
        Location[] paths = possiblePaths(position.y(), position.x());
        if (grid[position.y()][position.x()] == 9) completedPaths.add(position);
        if (paths == null){
            return;
        }
        for (Location path : paths){
            dfsPathTwo(path, completedPaths);
        }
    }
    public Location[] possiblePaths(int r, int c){
        ArrayList<Location> list = new ArrayList<>();
        int num = grid[r][c];
        if (r+1 < grid.length && grid[r+1][c] == num+1){
            list.add(new Location(c, r+1));
        }
        if (r-1 >= 0 && grid[r-1][c] == num+1){
            list.add(new Location(c, r-1));
        }
        if (c+1 < grid[0].length && grid[r][c+1] == num+1){
            list.add(new Location(c+1, r));
        }
        if (c-1 >= 0 && grid[r][c-1] == num+1){
            list.add(new Location(c-1, r));
        }
        if (list.isEmpty()) return null;
        Location[] fin = new Location[list.size()];
        for (int i = 0; i < list.size(); i++){
            fin[i] = list.get(i);
        }
        return fin;
    }
}