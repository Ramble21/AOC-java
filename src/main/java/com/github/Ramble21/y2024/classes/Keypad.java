package com.github.Ramble21.y2024.classes;

import com.github.Ramble21.helper_classes.Direction;
import com.github.Ramble21.helper_classes.Location;

import java.util.*;

public class Keypad {
    private static final char[][] numericKeypad = {
            {'7', '8', '9'},
            {'4', '5', '6'},
            {'1', '2', '3'},
            {'#', '0', 'A'}
    };
    private static final char[][] directionalKeypad = {
            {'#', '^', 'A'},
            {'<', 'v', '>'}
    };
    private static Location charToLoc(char ch, boolean isDirectional){
        char[][] keypad = isDirectional ? directionalKeypad : numericKeypad;
        for (int r = 0; r < keypad.length; r++) {
            for (int c = 0; c < keypad[r].length; c++) {
                if (keypad[r][c] == ch) {
                    return new Location(c, r);
                }
            }
        }
        throw new IllegalArgumentException(isDirectional ? "Character not found in directional keypad: " + ch : "Character not found in numerical keypad: " + ch);
    }

    public static final HashMap<Pair, Long> gelMemo = new HashMap<>();
    public static long getEncodedLength(String full, int remainingIterations){
        Pair pair = new Pair(full, remainingIterations);
        if (gelMemo.containsKey(pair)) return gelMemo.get(pair);
        if (remainingIterations == 0){
            gelMemo.put(pair, (long) full.length());
            return full.length();
        }
        String[] parts = splitCode(full);
        long total = 0;
        for (String part : parts){
            String encoded = encode(part, true);
            total += getEncodedLength(encoded, remainingIterations-1);
        }
        gelMemo.put(pair, total);
        return total;
    }

    private static final HashMap<String, String> encodeMemo = new HashMap<>();
    public static String encode(String s, boolean isDirectional){
        if (encodeMemo.containsKey(s)) return encodeMemo.get(s);
        HashSet<String> candidates = robot(s, isDirectional);
        if (candidates.size() == 1) for (String c : candidates) return c;
        int min = Integer.MAX_VALUE;
        String best = "";
        for (String candi1 : candidates){
            HashSet<String> candidates2 = robot(candi1, true);
            for (String candi2 : candidates2){
                HashSet<String> candidates3 = robot(candi2, true);
                for (String finalCandi : candidates3){
                    if (finalCandi.length() < min){
                        min = finalCandi.length();
                        best = candi1;
                    }
                }
            }
        }
        encodeMemo.put(s, best);
        return best;
    }

    private static String[] splitCode(String s){
        return s.split("(?<=A)");
    }

    public static HashSet<String> robot(String s, boolean isDirectional){
        char last = 'A';
        boolean firstIteration = true;
        HashSet<String> result = new HashSet<>();
        for (char c : s.toCharArray()){
            HashSet<String> miniRoutes = findRoutes(charToLoc(last, isDirectional), charToLoc(c, isDirectional), isDirectional);
            last = c;
            if (firstIteration){
                firstIteration = false;
                result.addAll(miniRoutes);
            }
            else if (!miniRoutes.isEmpty()){
                appendHashSets(result, miniRoutes);
            }
        }
        return result;
    }
    private static void appendHashSets(HashSet<String> A, HashSet<String> B) {
        HashSet<String> result = new HashSet<>();
        for (String a : A) {
            for (String b : B) {
                result.add(a + b);
            }
        }
        A.clear();
        A.addAll(result);
    }
    private static HashSet<String> findRoutes(Location start, Location end, boolean isDirectional) {
        if (start.equals(end)) {
            HashSet<String> routes = new HashSet<>();
            routes.add("A");
            return routes;
        }
        String moves = (
                (end.x() - start.x() > 0 ? ">" : "<").repeat(Math.max(0, Math.abs(end.x() - start.x()))) +
                (end.y() - start.y() > 0 ? "v" : "^").repeat(Math.max(0, Math.abs(end.y() - start.y())))
        );
        HashSet<String> routes = permute(moves, start, isDirectional);
        return new HashSet<>(routes.stream().map(route -> route + "A").toList());
    }
    private static HashSet<String> permute(String moves, Location start, boolean isDirectional){
        String ONE = getLR(moves) + getUD(moves);
        String TWO = getUD(moves) + getLR(moves);
        HashSet<String> result = ONE.equals(TWO) ? new HashSet<>(Set.of(ONE)) : new HashSet<>(Set.of(ONE, TWO));
        result.removeIf(s -> !isValidRoute(s, start, isDirectional));
        return result;
    }
    private static String getLR(String full){
        StringBuilder LR = new StringBuilder();
        for (char c : full.toCharArray()){
            if (c == '<' || c == '>') LR.append(c);
        }
        return LR.toString();
    }
    private static String getUD(String full){
        StringBuilder UD = new StringBuilder();
        for (char c : full.toCharArray()){
            if (c == 'v' || c == '^') UD.append(c);
        }
        return UD.toString();
    }
    private static boolean isValidRoute(String route, Location start, boolean isDirectional) {
        char[][] keypad = isDirectional ? directionalKeypad : numericKeypad;
        HashSet<Location> locs = getLocationsAlongRoute(start, route);
        for (Location loc : locs){
            if (!loc.isOnGrid(keypad) || keypad[loc.y()][loc.x()] == '#'){
                return false;
            }
        }
        return true;
    }
    private static HashSet<Location> getLocationsAlongRoute(Location start, String route) {
        HashSet<Location> locations = new HashSet<>();
        locations.add(new Location(start.x(), start.y()));
        Location current = new Location(start.x(), start.y());
        for (char move : route.toCharArray()) {
            if (move == 'A') break;
            current = current.getDirectionalLoc(Direction.charToDir(move));
            locations.add(new Location(current.x(), current.y()));
        }
        return locations;
    }
}
