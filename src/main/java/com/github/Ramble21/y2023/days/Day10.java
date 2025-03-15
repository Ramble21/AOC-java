package com.github.Ramble21.y2023.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.Location;

import java.io.IOException;
import java.util.*;

public class Day10 extends DaySolver {
    private final char[][] grid;
    private final HashMap<Location, Integer> distances = new HashMap<>();
    private Location start;

    public Day10() throws IOException {
        grid = getInputAsGrid(2023, 10);
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == 'S') start = new Location(c, r);
            }
        }
    }
    public long solvePart1() throws IOException {
        customBFS();
        int max = 0;
        for (int distance : distances.values()){
            max = Math.max(distance, max);
        }
        return max;
    }
    public long solvePart2() throws IOException {
        return 0;
    }

    private void customBFS(){
        HashSet<Location> visited = new HashSet<>();
        Queue<Location> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        distances.put(start, 0);
        while (!queue.isEmpty()) {
            Location current = queue.poll();
            for (Location neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distances.put(neighbor, distances.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }
    }
    private List<Location> getNeighbors(Location current) {
        int x = current.x();
        int y = current.y();
        if (grid[y][x] == 'S'){
            List<Location> result = new ArrayList<>();
            for (Location neighbor : Location.getNeighbors(current, grid)){
                int nx = neighbor.x();
                int ny = neighbor.y();
                if (switch (grid[ny][nx]) {
                    case '|' -> nx == x;
                    case '-' -> ny == y;
                    case 'L' -> (ny == y + 1 && nx == x) || (ny == y && nx == x - 1);
                    case 'J' -> (ny == y + 1 && nx == x) || (ny == y && nx == x + 1);
                    case '7' -> (ny == y - 1 && nx == x) || (ny == y && nx == x + 1);
                    case 'F' -> (ny == y - 1 && nx == x) || (ny == y && nx == x - 1);
                    default -> false;
                }) {
                    result.add(neighbor);
                }
            }
            return result;
        }
        return switch (grid[y][x]) {
            case '|' -> List.of(new Location(x, y - 1), new Location(x, y + 1));
            case '-' -> List.of(new Location(x - 1, y), new Location(x + 1, y));
            case 'L' -> List.of(new Location(x, y - 1), new Location(x + 1, y));
            case 'J' -> List.of(new Location(x, y - 1), new Location(x - 1, y));
            case '7' -> List.of(new Location(x, y + 1), new Location(x - 1, y));
            case 'F' -> List.of(new Location(x, y + 1), new Location(x + 1, y));
            default -> List.of();
        };
    }
}