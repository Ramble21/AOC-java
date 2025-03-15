package com.github.Ramble21.y2024.classes;
import com.github.Ramble21.helper_classes.Direction;
import com.github.Ramble21.helper_classes.Location;

import java.util.*;

public class Maze {

    private final char[][] grid;
    private Location currentLoc;
    private final Direction currentDir;
    private MazeNode finalNode;
    private int totalLocationsInBestPaths;

    private final HashSet<MazeNode> visited = new HashSet<>();
    private final PriorityQueue<MazeNode> queue = new PriorityQueue<>();

    private int totalPoints;
    public int getTotalPoints() {
        return totalPoints;
    }
    public Maze(char[][] grid){
        this.grid = grid;
        currentDir = Direction.UP;
        for (int r = 1; r < grid.length-1; r++){
            for (int c = 1; c < grid[0].length-1; c++){
                if (grid[r][c] == 'S') {
                    grid[r][c] = '@';
                    currentLoc = new Location(c, r);
                }
            }
        }

        MazeNode origin = new MazeNode(currentLoc, null, getNodeDirections(currentLoc), Direction.RIGHT);
        queue.add(origin);
    }
    public boolean hasFinishedAllNodes() {
        return queue.isEmpty();
    }

    public Direction[] getNodeDirections(Location l){
        ArrayList<Direction> dirs = new ArrayList<>();
        if (canTurnClockwise(l, currentDir)) dirs.add(currentDir.getClockwise());
        if (canTurnCounterClockwise(l, currentDir)) dirs.add(currentDir.getCounterClockwise());
        if (canMoveForward(l, currentDir)) dirs.add(currentDir);
        if (canMoveBackward(l, currentDir)) {
            assert currentDir.getClockwise() != null;
            dirs.add(currentDir.getClockwise().getClockwise());
        }
        return dirs.toArray(new Direction[0]);
    }
    public int getTotalLocationsInBestPaths() {
        return totalLocationsInBestPaths;
    }
    public void executeAlgorithm(){
        MazeNode node = queue.poll();
        assert node != null;
        HashSet<Location> locs = new HashSet<>();
        for (MazeNode m : visited){
            locs.add(m.getLoc());
        }
        if (locs.contains(node.getLoc())) return;

        grid[currentLoc.y()][currentLoc.x()] = 'x';
        currentLoc = node.getLoc();
        grid[currentLoc.y()][currentLoc.x()] = '@';
        checkNeighbors(node);
        visited.add(node);
    }

    public void checkNeighbors(MazeNode node){
        for (MazeNode neighbor : getNeighbors(node)){
            int tentativeDistance = node.getDistance() + getEdgeWeight(node, neighbor);

            if (node.getDirectionUponArriving() != neighbor.getDirectionUponArriving()){
                tentativeDistance += 1000;
            }

            if (tentativeDistance < neighbor.getDistance()){
                neighbor.setDistance(tentativeDistance);
                if (!queue.contains(neighbor)) queue.add(neighbor);
                if (grid[neighbor.getLoc().y()][neighbor.getLoc().x()] == 'E') {
                    finalNode = neighbor;
                    totalPoints = neighbor.getDistance();
                }
            }
        }
    }

    public ArrayList<Location> getAllLocationsBetween(Location start, Location end) {
        ArrayList<Location> locations = new ArrayList<>();
        int x1 = start.x();
        int y1 = start.y();
        int x2 = end.x();
        int y2 = end.y();
        int dx = Integer.compare(x2, x1);
        int dy = Integer.compare(y2, y1);
        int currentX = x1;
        int currentY = y1;
        while (currentX != x2 || currentY != y2) {
            currentX += dx;
            currentY += dy;
            locations.add(new Location(currentX, currentY));
        }
        locations.add(start);
        return locations;
    }
    public int getEdgeWeight(MazeNode node1, MazeNode node2){
        Location loc1 = node1.getLoc();
        Location loc2 = node2.getLoc();
        if (loc1.x() == loc2.x()){
            return Math.abs(loc1.y() - loc2.y());
        }
        else if (loc1.y() == loc2.y()){
            return Math.abs(loc2.x() - loc1.x());
        }
        else throw new RuntimeException("Nodes " + node1 + " and " + node2 + " cannot be compared!");
    }
    public Direction getDirectionToGo(Location original, Location neighbor) throws RuntimeException{
        int dx = neighbor.x() - original.x();
        int dy = neighbor.y() - original.y();
        if (dx > 0) return Direction.RIGHT;
        if (dx < 0) return Direction.LEFT;
        if (dy > 0) return Direction.DOWN;
        if (dy < 0) return Direction.UP;
        throw new RuntimeException("Nodes " + original + " and " + neighbor + " cannot be compared!");
    }
    public ArrayList<MazeNode> getNeighbors(MazeNode current) {
        ArrayList<MazeNode> output = new ArrayList<>();
        for (Direction direction : current.getDirections()) {
            Location loc = current.getLoc();
            while (true) {
                Location newLoc = loc.getDirectionalLoc(direction);
                Direction dir = getDirectionToGo(loc, newLoc);
                if (grid[newLoc.y()][newLoc.x()] == 'E'){
                    output.add(new MazeNode(newLoc, current, getNodeDirections(newLoc), dir));
                    break;
                }
                else if (grid[newLoc.y()][newLoc.x()] == '#' || isDeadEnd(newLoc, direction)){
                    break;
                }
                else if (canTurn(newLoc)) {
                    output.add(new MazeNode(newLoc, current, getNodeDirections(newLoc), dir));
                    break;
                }
                loc = newLoc;
            }
        }
        return output;
    }
    public boolean canMoveForward(Location currentLoc, Direction currentDir){
        Location targetLoc = currentLoc.getDirectionalLoc(currentDir);
        return grid[targetLoc.y()][targetLoc.x()] == '.' || grid[targetLoc.y()][targetLoc.x()] == 'E';
    }
    public boolean canTurnClockwise(Location currentLoc, Direction currentDir){
        Location targetLoc = currentLoc.getDirectionalLoc(currentDir.getClockwise());
        return grid[targetLoc.y()][targetLoc.x()] == '.' || grid[targetLoc.y()][targetLoc.x()] == 'E';
    }
    public boolean canTurnCounterClockwise(Location currentLoc, Direction currentDir){
        Location targetLoc = currentLoc.getDirectionalLoc(currentDir.getCounterClockwise());
        return grid[targetLoc.y()][targetLoc.x()] == '.' || grid[targetLoc.y()][targetLoc.x()] == 'E';
    }
    public boolean canMoveBackward(Location currentLoc, Direction currentDir){
        assert currentDir.getClockwise() != null;
        Location targetLoc = currentLoc.getDirectionalLoc(currentDir.getClockwise().getClockwise());
        return grid[targetLoc.y()][targetLoc.x()] == '.' || grid[targetLoc.y()][targetLoc.x()] == 'E';
    }
    public boolean isDeadEnd(Location currentLoc, Direction currentDir){
        return !canMoveForward(currentLoc, currentDir) && !canTurn(currentLoc);
    }
    public boolean canTurn(Location loc){
        for (Direction d : Direction.getCardinalDirections()){
            Location left = loc.getDirectionalLoc(d);
            Location up = loc.getDirectionalLoc(d.getClockwise());
            if ((grid[up.y()][up.x()] == '.' || grid[up.y()][up.x()] == '@')
                    && (grid[left.y()][left.x()] == '.' || grid[left.y()][left.x()] == '@')) return true;
        }
        return false;
    }
    public void findAllPaths(){
        HashSet<Location> allLocs = new HashSet<>();
        backtrack(finalNode, null, allLocs);
        totalLocationsInBestPaths = allLocs.size();
        removeX();
    }
    public void removeX(){
        for (int r = 0; r < grid.length; r++){
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == 'x') grid[r][c] = '.';
            }
        }
    }
    public void backtrack(MazeNode current, MazeNode child, HashSet<Location> set){

        ArrayList<MazeNode> parents = current.getSafeParentNodes(visited);

        if (!current.isOrigin()){

            ArrayList<MazeNode> neighbors = getNewNeighbors(current);
            neighbors.removeIf(neighbor -> {
                ArrayList<Location> parentLocs = new ArrayList<>();
                for (MazeNode m : parents){
                    parentLocs.add(m.getLoc());
                }
                if (parentLocs.contains(neighbor.getLoc())) {
                    return true;
                }
                else {

                    ArrayList<MazeNode> neighborParents = neighbor.getSafeParentNodes(visited);
                    ArrayList<Location> neighborParentLocs = new ArrayList<>();
                    for (MazeNode m : neighborParents) {
                        neighborParentLocs.add(m.getLoc());
                    }
                    return (neighborParentLocs.contains(current.getLoc()) || neighbor.getLoc().equals(finalNode.getLoc()));
                }
            });

            for (MazeNode neighbor : neighbors){
                if (checkIfExtraParent(child, current, neighbor)){
                    parents.add(neighbor);
                }
            }

            for (MazeNode parent : parents){

                ArrayList<Location> list = getAllLocationsBetween(current.getLoc(), parent.getLoc());
                set.addAll(list);
                for (Location l : list){
                    grid[l.y()][l.x()] = 'O';
                }
                backtrack(parent, current, set);
            }
        }
    }
    public boolean checkIfExtraParent(MazeNode C, MazeNode B, MazeNode A){
        int num = Math.abs(getEdgeWeight(A, B));


        Direction RED = getDirectionToGo(A.getLoc(), B.getLoc());
        Direction BLUE = A.getDirectionUponArriving();
        Direction ORANGE = B.getDirectionUponArriving();
        Direction GREEN = (C != null) ? getDirectionToGo(B.getLoc(), C.getLoc()) : null;

        if (BLUE != RED) num += 1000;
        if (GREEN != null && ORANGE != GREEN && RED == GREEN) num -= 1000;

        return A.getDistance() + num == B.getDistance();
    }
    public ArrayList<MazeNode> getNewNeighbors(MazeNode current){
        ArrayList<MazeNode> output = new ArrayList<>();
        for (MazeNode node : visited){
            if (canAccess(node, current) && noIntermediateNode(node, current)) output.add(node);
        }
        return output;
    }
    public boolean canAccess(MazeNode one, MazeNode two){
        if ((!(one.getLoc().x() == two.getLoc().x() || one.getLoc().y() == two.getLoc().y())) || (one.getLoc().equals(two.getLoc()))) return false;
        Location loc = one.getLoc();
        Direction dir = getDirectionToGo(two.getLoc(), one.getLoc());
        dir = dir.getFlipped();
        while (!loc.equals(two.getLoc())){
            loc = loc.getDirectionalLoc(dir);
            if (grid[loc.y()][loc.x()] == '#'){
                return false;
            }
        }
        return true;
    }
    public boolean noIntermediateNode(MazeNode one, MazeNode two){
        Location loc = one.getLoc();
        Direction dir = getDirectionToGo(two.getLoc(), one.getLoc());
        dir = dir.getFlipped();
        while (!loc.equals(two.getLoc())){
            loc = loc.getDirectionalLoc(dir);
            if (canTurnNew(loc) && !loc.equals(two.getLoc()) && !loc.equals(one.getLoc())){
                return false;
            }
        }
        return true;
    }
    public boolean canTurnNew(Location loc){
        for (Direction d : Direction.getCardinalDirections()){
            Location left = loc.getDirectionalLoc(d);
            Location up = loc.getDirectionalLoc(d.getClockwise());
            if (((grid[up.y()][up.x()] == '.' || grid[up.y()][up.x()] == 'x' || grid[up.y()][up.x()] == 'O'))
                    && (grid[left.y()][left.x()] == '.' || grid[left.y()][left.x()] == 'x' || grid[left.y()][left.x()] == 'O')) return true;
        }
        return false;
    }
}
