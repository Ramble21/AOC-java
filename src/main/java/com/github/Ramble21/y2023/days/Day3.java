package com.github.Ramble21.y2023.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day3 extends DaySolver {
    private final char[][] grid;
    public Day3() throws IOException {
        List<String> input = getInputLines(2023, 3);
        grid = new char[input.size()][input.get(0).length()];
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                grid[r][c] = input.get(r).charAt(c);
            }
        }
    }
    public long solvePart1() throws IOException {
        long sum = 0;
        for (int r = 0; r < grid.length; r++){
            int currentNumStartIndex = -1;
            int currentNumEndIndex = -1;
            for (int c = 0; c <= grid[0].length; c++){
                if (c == grid[0].length || !Character.isDigit(grid[r][c])) {
                    sum += getPartNumber(r, currentNumStartIndex, currentNumEndIndex);
                    currentNumEndIndex = -1;
                    currentNumStartIndex = -1;
                    if (c == grid[0].length) continue;
                    while (c < grid[0].length && !Character.isDigit(grid[r][c])){
                        c++;
                    }
                    c--;
                }
                else if (currentNumStartIndex == -1) {
                    currentNumStartIndex = c;
                    currentNumEndIndex = c;
                }
                else {
                    currentNumEndIndex++;
                }
            }
        }
        return sum;
    }
    public long solvePart2() throws IOException {
        long sum = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == '*') {
                    ArrayList<Location> adjacentPartNumbers = getAdjacentPartNumbers(new Location(c, r));
                    if (adjacentPartNumbers.size() == 2){
                        sum += getFullNum(adjacentPartNumbers.get(0)) * getFullNum(adjacentPartNumbers.get(1));
                    }
                }
            }
        }
        return sum;
    }
    public long getFullNum(Location l) {
        int start = l.x();
        while (new Location(start, l.y()).isOnGrid(grid) && Character.isDigit(grid[l.y()][start])){
            start--;
        }
        start++;
        int end = l.x();
        while (new Location(end, l.y()).isOnGrid(grid) && Character.isDigit(grid[l.y()][end])){
            end++;
        }
        end--;
        return parseCharArr(l.y(), start, end);
    }
    public ArrayList<Location> getAdjacentPartNumbers(Location gear) {
        ArrayList<Location> locs = new ArrayList<>();
        for (Direction d : Direction.getAllDirections()){
            Location loc = gear.getDirectionalLoc(d);
            if (!loc.isOnGrid(grid)) continue;
            if (Character.isDigit(grid[loc.y()][loc.x()])){
                locs.add(loc);
            }
        }
        for (int i = 0; i < locs.size(); i++){
            for (int j = 0; j < locs.size(); j++){
                if (locs.get(i).equals(locs.get(j))) continue;
                if (locs.get(i).y() == locs.get(j).y() && Math.abs(locs.get(i).x() - locs.get(j).x()) == 1){
                    locs.remove(j);
                    if (i >= j) i--;
                    j--;
                }
            }
        }
        return locs;
    }
    private long getPartNumber(int rowNum, int start, int end) {
        if (start == -1 || end == -1) return 0;

        for (int i = start; i <= end; i++) {
            for (Direction dir : Direction.getAllDirections()) {
                int newRow = rowNum + dir.getDeltaX();
                int newCol = i + dir.getDeltaY();
                if (isValid(newRow, newCol) && isSpecialCharacter(grid[newRow][newCol])) {
                    return parseCharArr(rowNum, start, end);
                }
            }
        }

        return 0;
    }
    private boolean isValid(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
    private boolean isSpecialCharacter(char c) {
        return !Character.isDigit(c) && c != '.';
    }
    private long parseCharArr(int rowNum, int start, int end){
        StringBuilder result = new StringBuilder();
        for (int c = start; c <= end; c++){
            result.append(grid[rowNum][c]);
        }
        return Integer.parseInt(result.toString());
    }
}