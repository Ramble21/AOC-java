package com.github.Ramble21.helper_classes;

import java.util.ArrayList;

public record Location(int x, int y) {
    public String toString() {
        return "(" + y + "," + x + ")";
    }
    public static int getTaxicabDistance(Location first, Location second) {
        return Math.abs(first.x - second.x) + Math.abs(first.y - second.y);
    }
    public boolean isOnGrid(char[][] grid) {
        return x < grid[0].length && y < grid.length && x >= 0 && y >= 0;
    }
    public Location getDirectionalLoc(Direction dir) {
        return new Location(x + dir.getDeltaX(), y + dir.getDeltaY());
    }
    public static ArrayList<Location> getNeighbors(Location l, char[][] grid) {
        ArrayList<Location> output = new ArrayList<>();
        for (Direction d : Direction.getCardinalDirections()){
            output.add(l.getDirectionalLoc(d));
        }
        output.removeIf(loc -> !loc.isOnGrid(grid));
        return output;
    }
}
