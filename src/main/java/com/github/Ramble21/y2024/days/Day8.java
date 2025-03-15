package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.Location;

import java.io.IOException;
import java.util.*;

public class Day8 extends DaySolver {
    private final List<String> input;
    private char[][] grid;
    public Day8() throws IOException {
        input = getInputLines(2024, 8);
        inputToGrid();
    }
    public long solvePart1() throws IOException {
        HashMap<Location, Character> map = new HashMap<>();
        HashSet<Location> set = new HashSet<>();
        HashSet<HashSet<Location>> done = new HashSet<>();

        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == '.') continue;
                map.put(new Location(c, r), grid[r][c]);
            }
        }
        for (char frequency : map.values()){
            for (Location antenna1 : map.keySet()){
                if (map.get(antenna1) != frequency) continue;
                for (Location antenna2 : map.keySet()){
                    if (map.get(antenna2) != frequency || antenna1 == antenna2) continue;

                    HashSet<Location> lox = new HashSet<>();
                    lox.add(antenna1); lox.add(antenna2);
                    if (done.contains(lox)) continue;
                    done.add(lox);

                    Location[] potentialAntinodes = getAntinodes(antenna1, antenna2);
                    for (Location potential : potentialAntinodes){
                        if (potential != null) set.add(potential);
                    }
                }
            }
        }
        return set.size();
    }

    public long solvePart2() throws IOException {
        HashMap<Location, Character> map = new HashMap<>();
        HashSet<Location> set = new HashSet<>();
        HashSet<HashSet<Location>> done = new HashSet<>();

        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == '.') continue;
                map.put(new Location(c, r), grid[r][c]);
            }
        }
        for (char frequency : map.values()){
            for (Location antenna1 : map.keySet()){
                if (map.get(antenna1) != frequency) continue;
                for (Location antenna2 : map.keySet()){
                    if (map.get(antenna2) != frequency || antenna1 == antenna2) continue;

                    HashSet<Location> lox = new HashSet<>();
                    lox.add(antenna1); lox.add(antenna2);
                    if (done.contains(lox)) continue;
                    done.add(lox);

                    Location[] potentialAntinodes = getUpdatedAntinodes(antenna1, antenna2);
                    for (Location potential : potentialAntinodes){
                        if (potential != null){
                            set.add(potential);
                            if (grid[potential.y()][potential.x()] == '.') grid[potential.y()][potential.x()] = '#';
                            else if (grid[potential.y()][potential.x()] != '#') grid[potential.y()][potential.x()] = '%';
                        }
                    }
                }
            }
        }
        ArrayList<Location> x = new ArrayList<>(set);
        x.sort(Comparator.comparing(Location::y));
        return set.size();
    }

    public Location[] getAntinodes(Location loc1, Location loc2){
        Location[] antinodes = new Location[2];
        int xDiff = loc1.x() - loc2.x();
        int yDiff = loc2.y() - loc1.y();
        antinodes[0] = new Location(loc1.x()-2*xDiff, loc1.y()+2*yDiff);
        antinodes[1] = new Location(loc2.x()+2*xDiff, loc2.y()-2*yDiff);
        for (int i = 0; i < antinodes.length; i++){
            assert antinodes[i] != null;
            if (!antinodes[i].isOnGrid(grid)){
                antinodes[i] = null;
            }
        }
        return antinodes;
    }
    public Location[] getUpdatedAntinodes(Location loc1, Location loc2){
        Location[] antinodes = new Location[100];
        antinodes[0] = loc1;
        antinodes[1] = loc2;
        int xDiff = loc1.x() - loc2.x();
        int yDiff = loc2.y() - loc1.y();
        int times = 2;
        for (int i = 2; i < antinodes.length; i+=2){
            antinodes[i] = new Location(loc1.x()-times*xDiff, loc1.y()+times*yDiff);
            antinodes[i+1] = new Location(loc2.x()+times*xDiff, loc2.y()-times*yDiff);
            times++;
        }

        for (int i = 0; i < antinodes.length; i++){
            assert antinodes[i] != null;
            if (!antinodes[i].isOnGrid(grid)){
                antinodes[i] = null;
            }
        }
        antinodes = Arrays.stream(antinodes).filter(Objects::nonNull).toArray(Location[]::new);
        return antinodes;
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