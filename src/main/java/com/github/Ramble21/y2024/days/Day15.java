package com.github.Ramble21.y2024.days;

import com.github.Ramble21.DaySolver;
import com.github.Ramble21.helper_classes.Direction;
import com.github.Ramble21.helper_classes.Location;
import com.github.Ramble21.y2024.classes.WideBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day15 extends DaySolver {
    private final List<String> input;
    private String movements;
    private char[][] grid;

    public Day15() throws IOException {
        input = getInputLines(2024, 15);
    }
    public long solvePart1() throws IOException {
        splitInput();
        for (int i = 0; i < movements.length(); i++){
            Location currentLoc = null;
            for (int r = 0; r < grid.length; r++){
                for (int c = 0; c < grid[0].length; c++){
                    if (grid[r][c] == '@'){
                        currentLoc = new Location(c, r);
                        break;
                    }
                }
            }
            assert currentLoc != null;
            moveRobot(movements.charAt(i), currentLoc);
        }
        int sum = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == 'O'){
                    sum += 100*r + c;
                }
            }
        }
        return sum;
    }

    public long solvePart2() throws IOException {
        splitInput();
        widenGrid();
        for (int i = 0; i < movements.length(); i++){
            Location currentLoc = null;
            for (int r = 0; r < grid.length; r++){
                for (int c = 0; c < grid[0].length; c++){
                    if (grid[r][c] == '@'){
                        currentLoc = new Location(c, r);
                        break;
                    }
                }
            }
            for (char[] chars : grid) {
                for (int c = 1; c < grid[0].length - 1; c++) {
                    if (chars[c] == '[' && chars[c + 1] != ']') {
                        return 0;
                    } else if (chars[c] == ']' && chars[c - 1] != '[') {
                        return 0;
                    }
                }
            }
            assert currentLoc != null;
            moveRobotBigGrid(movements.charAt(i), currentLoc);
        }
        ArrayList<WideBox> boxes = new ArrayList<>();
        int sum = 0;
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length-1; c++){
                if (grid[r][c] == '[' && grid[r][c+1] == ']'){
                    boxes.add(new WideBox(grid, new Location(c, r), new Location(c+1, r)));
                }
            }
        }

        for (WideBox box : boxes){
            sum += (100 * box.getY() + box.getIndex1().x());
        }
        return sum;
    }
    public void moveRobotBigGrid(char direction, Location currentLoc){
        Direction dir = Direction.charToDir(direction);
        assert dir != null;
        int currentX = currentLoc.x();
        int currentY = currentLoc.y();
        int targetX = currentX + dir.getDeltaX();
        int targetY = currentY + dir.getDeltaY();
        Location targetLoc = new Location(targetX, targetY);
        if (grid[targetY][targetX] == '.'){
            grid[targetY][targetX] = '@';
            grid[currentY][currentX] = '.';
        }
        else if (!(grid[targetY][targetX] == '#')){
            WideBox box = WideBox.grabBox(grid, targetLoc);
            assert box != null;
            WideBox.resetBoxesPushed();
            if (box.tryToMove(dir, grid)){
                if (grid[targetY][targetX] == '.'){
                    grid[targetY][targetX] = '@';
                    grid[currentY][currentX] = '.';
                }
            }
        }
    }
    public void moveRobot(char direction, Location currentLoc){
        if (direction != '^' && direction != 'v' && direction != '<' && direction != '>') return;

        int deltaX = 0;
        int deltaY = 0;

        if (direction == '^') deltaY = -1;
        else if (direction == 'v') deltaY = 1;
        else if (direction == '<') deltaX = -1;
        else deltaX = 1;

        int currentX = currentLoc.x();
        int currentY = currentLoc.y();
        tryToMove(currentX, currentY, deltaX, deltaY);
    }
    public void tryToMove(int currentX, int currentY, int deltaX, int deltaY){
        int targetY = currentY+deltaY;
        int targetX = currentX+deltaX;
        if (grid[targetY][targetX] == '.'){
            grid[targetY][targetX] = '@';
            grid[currentY][currentX] = '.';
        }
        else if (!(grid[targetY][targetX] == '#')){
            boolean moved = tryToMoveBox(targetX, targetY, deltaX, deltaY, '.');
            if (moved){
                grid[targetY][targetX] = '@';
                grid[currentY][currentX] = '.';
            }
        }
    }
    public boolean tryToMoveBox(int boxX, int boxY, int deltaX, int deltaY, char currentChar){
        int targetY = boxY+deltaY;
        int targetX = boxX+deltaX;

        if (grid[targetY][targetX] == '.'){
            grid[targetY][targetX] = 'O';
            grid[boxY][boxX] = currentChar;
            return true;
        }
        else if (grid[targetY][targetX] == '#'){
            return false;
        }
        else{
            return tryToMoveBox(targetX, targetY, deltaX, deltaY, 'O');
        }
    }
    public void splitInput(){
        int newLineIndex = -1;
        for (int i = 0; i < input.size(); i++){
            if (input.get(i).isBlank()){
                newLineIndex = i;
                break;
            }
        }
        grid = new char[newLineIndex][input.get(0).length()];
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                grid[r][c] = input.get(r).charAt(c);
            }
        }
        StringBuilder s = new StringBuilder();
        for (int i = newLineIndex+1; i < input.size(); i++){
            s.append(input.get(i));
        }
        movements = s.toString();
    }
    public void widenGrid(){
        char[][] widenedGrid = new char[grid.length][grid[0].length*2];
        for (int r = 0; r < grid.length; r++){
            int widenedC = 0;
            for (int c = 0; c < grid[0].length && widenedC < widenedGrid[0].length; c++){
                if (grid[r][c] == '.'){
                    widenedGrid[r][widenedC] = '.';
                    widenedGrid[r][widenedC+1] = '.';
                }
                else if (grid[r][c] == '@') {
                    widenedGrid[r][widenedC] = '@';
                    widenedGrid[r][widenedC+1] = '.';
                }
                else if (grid[r][c] == 'O') {
                    widenedGrid[r][widenedC] = '[';
                    widenedGrid[r][widenedC+1] = ']';
                }
                else if (grid[r][c] == '#') {
                    widenedGrid[r][widenedC] = '#';
                    widenedGrid[r][widenedC+1] = '#';
                }
                widenedC += 2;
            }
        }
        grid = widenedGrid;
    }
}