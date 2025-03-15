package com.github.Ramble21.y2024.classes;

import com.github.Ramble21.helper_classes.Direction;
import com.github.Ramble21.helper_classes.Location;

import java.util.ArrayList;

public class WideBox {
    private final Location index1;
    private final Location index2;
    private static final ArrayList<WideBox> boxesPushed = new ArrayList<>();

    public Location getIndex1() {
        return index1;
    }
    public int getY(){
        return index1.y();
    }
    public static void resetBoxesPushed(){
        boxesPushed.clear();
    }

    public WideBox(char[][] grid, Location index1, Location index2){
        char char1 = grid[index1.y()][index1.x()];
        char char2 = grid[index2.y()][index2.x()];
        if (char1 == '[' && char2 == ']'){
            this.index1 = index1;
            this.index2 = index2;
        }
        else if (char1 == ']' && char2 == '['){
            this.index1 = index2;
            this.index2 = index1;
        }
        else{
            throw new RuntimeException("Invalid position!");
        }
    }
    public void forceMove (Direction dir, char[][] grid){
        grid[index1.y()][index1.x()] = '.';
        grid[index2.y()][index2.x()] = '.';
        grid[index1.y()+dir.getDeltaY()][index1.x()+dir.getDeltaX()] = '[';
        grid[index2.y()+dir.getDeltaY()][index2.x()+dir.getDeltaX()] = ']';
        boxesPushed.add(this);
    }
    public void undoForceMove (Direction dir, char[][] grid){
        grid[index1.y()+dir.getDeltaY()][index1.x()+dir.getDeltaX()] = '.';
        grid[index2.y()+dir.getDeltaY()][index2.x()+dir.getDeltaX()] = '.';
        grid[index1.y()][index1.x()] = '[';
        grid[index2.y()][index2.x()] = ']';
    }
    public void undoAllMistakes(Direction dir, char[][] grid){
        for (int i = boxesPushed.size()-1; i >= 0; i--){
            boxesPushed.get(i).undoForceMove(dir, grid);
        }
        resetBoxesPushed();
    }
    public boolean tryToMove(Direction dir, char[][] grid){
        Location target1 = index1.getDirectionalLoc(dir);
        Location target2 = index2.getDirectionalLoc(dir);
        if (dir == Direction.RIGHT || dir == Direction.LEFT){
            Location target = target1;
            if (dir == Direction.RIGHT) target = target2;
            if (grid[target.y()][target.x()] == '.'){
                forceMove(dir, grid);
                return true;
            }
            else if (grid[target.y()][target.x()] == '#'){
                undoAllMistakes(dir, grid);
                return false;
            }
            else{
                WideBox newBox = grabBox(grid, target);
                assert newBox != null;
                if (newBox.tryToMove(dir, grid)){

                    forceMove(dir, grid);
                    return true;
                }
                else{

                    return false;
                }
            }
        }
        else{
            if (grid[target1.y()][target1.x()] == '.' && grid[target2.y()][target2.x()] == '.'){
                forceMove(dir, grid);
                return true;
            }
            else if (grid[target1.y()][target1.x()] == '#' || grid[target2.y()][target2.x()] == '#'){
                undoAllMistakes(dir, grid);
                return false;
            }
            else{
                Location[] targets = {target1, target2};
                for (Location target : targets){
                    WideBox newBox = grabBox(grid, target);
                    if (newBox != null){
                        if (!newBox.tryToMove(dir, grid)){
                            return false;
                        }
                    }
                }
                forceMove(dir, grid);
                return true;
            }
        }
    }

    public static WideBox grabBox(char[][] grid, Location loc){
        char middle = grid[loc.y()][loc.x()];
        char left = grid[loc.y()][loc.x()-1];
        char right = grid[loc.y()][loc.x()+1];
        if (middle == '[' && right == ']'){
            return new WideBox(grid, loc, loc.getDirectionalLoc(Direction.RIGHT));
        }
        else if (middle == ']' && left == '['){
            return new WideBox(grid, loc, loc.getDirectionalLoc(Direction.LEFT));
        }
        else return null;
    }
    public String toString(){
        return "box: " + index1 + "," + index2;
    }
}
