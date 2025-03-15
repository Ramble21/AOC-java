package com.github.Ramble21.helper_classes;
import java.util.*;

public class BreadthFirstSearch {

    private final Location start;
    private final Location end;
    private final char[][] grid;
    private int result;
    private HashMap<Location, Integer> distances;
    private boolean noSolution;

    public BreadthFirstSearch(Location start, Location end, char[][] grid){
        this.start = start;
        this.end = end;
        char[][] ogGrid = new char[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++){
            System.arraycopy(grid[r], 0, ogGrid[r], 0, grid[0].length);
        }
        this.grid = ogGrid;
        run();
    }
    public int getResult(){
        return result;
    }
    public HashMap<Location, Integer> getDistances(){
        return distances;
    }
    public boolean isNoSolution(){
        return noSolution;
    }

    public void run() throws RuntimeException {
        HashSet<Location> visited = new HashSet<>();
        HashMap<Location, Integer> distances = new HashMap<>();
        Queue<Location> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()){
            Location current = queue.poll();
            if (grid[current.y()][current.x()] == '.') grid[current.y()][current.x()] = 'x';
            if (current.equals(end)){
                result = distances.get(current);
                this.distances = distances;
                noSolution = false;
                return;
            }
            for (Location neighbor : Location.getNeighbors(current, grid)){
                if (!visited.contains(neighbor)){
                    visited.add(neighbor);
                    distances.put(neighbor, distances.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }
        noSolution = true;
    }
}
