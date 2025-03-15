package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day12 extends DaySolver {
    private final List<String> input;
    private char[][] grid;
    private final HashSet<Location> currentRegion = new HashSet<>();
    private final HashSet<Location> allRegions = new HashSet<>();
    public Day12() throws IOException {
        input = getInputLines(2024, 12);
        inputToGrid();
    }
    public long solvePart1() throws IOException {
        int count = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                Location loc = new Location(c, r);
                if (allRegions.contains(loc)) continue;

                getRegion(loc, grid[r][c]);
                allRegions.addAll(currentRegion);

                int area = currentRegion.size();
                int perimeter = getPerimeter();
                count += area * perimeter;
                currentRegion.clear();
            }
        }
        return count;
    }

    public long solvePart2() throws IOException {
        allRegions.clear();
        int count = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                Location loc = new Location(c, r);
                if (allRegions.contains(loc)) continue;

                getRegion(loc, grid[r][c]);
                allRegions.addAll(currentRegion);
                int area = currentRegion.size();
                int perimeter = getDiscountPerimeter();
                count += area * perimeter;
                currentRegion.clear();
            }
        }
        return count;
    }
    public void getRegion(Location loc, char target){
        Location[] possiblePaths = possiblePaths(loc.y(), loc.x());
        if (grid[loc.y()][loc.x()] == target) currentRegion.add(loc);
        if (possiblePaths == null) return;
        for (Location path : possiblePaths){
            if (!currentRegion.contains(path)) getRegion(path, target);
        }
    }
    public int getPerimeter(){
        int count = 0;
        for (Location l : currentRegion){
            int x = 4;
            if (currentRegion.contains(new Location(l.x()+1, l.y()))) x--;
            if (currentRegion.contains(new Location(l.x()-1, l.y()))) x--;
            if (currentRegion.contains(new Location(l.x(), l.y()+1))) x--;
            if (currentRegion.contains(new Location(l.x(), l.y()-1))) x--;
            count += x;
        }
        return count;
    }
    public int getDiscountPerimeter(){
        int noCorners = 0;
        for (Location loc : currentRegion){
            noCorners += countCorners(loc);
        }
        return noCorners;
    }
    private int countCorners(Location loc) {
        int corners = 0;
        Location upper = new Location(loc.x(), loc.y() - 1);
        Location lower = new Location(loc.x(), loc.y() + 1);
        Location right = new Location(loc.x() + 1, loc.y());
        Location left = new Location(loc.x() - 1, loc.y());
        Location topLeft = new Location(loc.x() - 1, loc.y() - 1);
        Location topRight = new Location(loc.x() + 1, loc.y() - 1);
        Location bottomLeft = new Location(loc.x() - 1, loc.y() + 1);
        Location bottomRight = new Location(loc.x() + 1, loc.y() + 1);
        if (!currentRegion.contains(upper) && !currentRegion.contains(left)) {
            corners++;
        }
        if (!currentRegion.contains(upper) && !currentRegion.contains(right)) {
            corners++;
        }
        if (!currentRegion.contains(lower) && !currentRegion.contains(left)) {
            corners++;
        }
        if (!currentRegion.contains(lower) && !currentRegion.contains(right)) {
            corners++;
        }
        if (!currentRegion.contains(topLeft) && currentRegion.contains(left) && currentRegion.contains(upper)) {
            corners++;
        }
        if (!currentRegion.contains(topRight) && currentRegion.contains(right) && currentRegion.contains(upper)) {
            corners++;
        }
        if (!currentRegion.contains(bottomLeft) && currentRegion.contains(left) && currentRegion.contains(lower)) {
            corners++;
        }
        if (!currentRegion.contains(bottomRight) && currentRegion.contains(right) && currentRegion.contains(lower)) {
            corners++;
        }
        return corners;
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
    public Location[] possiblePaths(int r, int c){
        ArrayList<Location> list = new ArrayList<>();
        char x = grid[r][c];

        if (r+1 < grid.length &&
                grid[r+1][c] == x &&
                !currentRegion.contains(new Location(c, r+1)))
            list.add(new Location(c, r+1));

        if (r-1 >= 0 &&
                grid[r-1][c] == x &&
                !currentRegion.contains(new Location(c, r-1)))
            list.add(new Location(c, r-1));

        if (c+1 < grid[0].length &&
                grid[r][c+1] == x &&
                !currentRegion.contains(new Location(c+1, r)))
            list.add(new Location(c+1, r));

        if (c-1 >= 0 &&
                grid[r][c-1] == x &&
                !currentRegion.contains(new Location(c-1, r)))
            list.add(new Location(c-1, r));

        if (list.isEmpty()) return null;

        Location[] fin = new Location[list.size()];
        for (int i = 0; i < list.size(); i++){
            fin[i] = list.get(i);
        }
        return fin;
    }
}