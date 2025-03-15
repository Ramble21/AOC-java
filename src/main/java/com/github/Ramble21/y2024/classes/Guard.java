package com.github.Ramble21.y2024.classes;

import com.github.Ramble21.helper_classes.Direction;
import com.github.Ramble21.helper_classes.Location;

public class Guard {
    private Location currentLoc;
    private Direction currentDir;
    private boolean isOnMappedArea = true;
    private final char[][] grid;
    public Guard(Location startingLoc, char[][] grid){
        currentLoc = startingLoc;
        currentDir = Direction.UP;
        this.grid = grid;
    }
    public Location getCurrentLoc(){
        return currentLoc;
    }
    public Direction getCurrentDir(){
        return currentDir;
    }
    public boolean isOnMappedArea(){
        return isOnMappedArea;
    }
    public void move(){
        Location inFront = currentLoc.getDirectionalLoc(currentDir);
        if (!inFront.isOnGrid(grid)) isOnMappedArea = false;
        else if (grid[inFront.y()][inFront.x()] != '#'){
            currentLoc = inFront;
        }
        else currentDir = currentDir.getClockwise();
    }
}
