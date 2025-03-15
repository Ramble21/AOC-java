package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.BreadthFirstSearch;
import com.github.Ramble21.helper_classes.Location;
import com.github.Ramble21.y2024.classes.Regex;

import java.io.IOException;
import java.util.*;

public class Day18 extends DaySolver {
    private final List<String> input;
    private final ArrayList<Location> coords;
    private final char[][] grid;
    private final int gridSize = 71;
    private String part2;

    public Day18() throws IOException {
        input = getInputLines(2024, 18);
        coords = inputToCoords();
        grid = createGrid(gridSize);
    }
    public long solvePart1() throws IOException {
        addLocations(1024);
        return new BreadthFirstSearch(new Location(0, 0), new Location(gridSize-1, gridSize-1), grid).getResult();
    }

    public long solvePart2() throws IOException {
        String goal = null;
        int left = 1024;
        int right = coords.size();
        while (left <= right){
            int mid = left + (right - left) / 2;
            addLocations(mid);
            BreadthFirstSearch search = new BreadthFirstSearch(new Location(0, 0), new Location(gridSize-1, gridSize-1), grid);
            if (isFirstBadSearch(search, mid)){
                goal = coords.get(mid - 1).x() + "," + coords.get(mid - 1).y();
                break;
            }
            else if (search.isNoSolution()){
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }
        part2 = goal;
        return 0; // need string
    }
    public String solvePart2String() throws IOException {
        timePart2();
        return part2;
    }
    public boolean isFirstBadSearch(BreadthFirstSearch b, int i){
        addLocations(i - 1);
        BreadthFirstSearch before = new BreadthFirstSearch(new Location(0, 0), new Location(gridSize-1, gridSize-1), grid);
        return !before.isNoSolution() && b.isNoSolution();
    }
    public ArrayList<Location> inputToCoords(){
        ArrayList<Location> gyatt = new ArrayList<>();
        for (String s : input){
            int[] nums = Regex.parseFirstTwoIntegers(s);
            gyatt.add(new Location(nums[0], nums[1]));
        }
        return gyatt;
    }
    public char[][] createGrid(int dimension){
        char[][] x = new char[dimension][dimension];
        for (int r = 0; r < x.length; r++){
            for (int c = 0; c < x[0].length; c++){
                x[r][c] = '.';
            }
        }
        return x;
    }
    public void addLocations(int count){
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                grid[r][c] = '.';
            }
        }
        for (int i = 0; i < count; i++){
            grid[coords.get(i).y()][coords.get(i).x()] = '#';
        }
    }
}
