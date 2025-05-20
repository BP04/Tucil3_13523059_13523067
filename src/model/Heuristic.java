package model;

import java.util.HashSet;
import java.util.Set;

public class Heuristic {
    int actualHeight;
    int actualWidth;
    int exitRow;
    int exitCol;
    int mode;

    public Heuristic(int actualHeight, int actualWidth, int exitRow, int exitCol, int mode) {
        this.actualHeight = actualHeight;
        this.actualWidth = actualWidth;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
        this.mode = mode;
    }

    public int calculateBlockerDistanceHeuristic(Node node) {
        int minR = actualHeight, maxR = -1, minC = actualWidth, maxC = -1;
        for(int i = 0; i < actualHeight; ++i) {
            for(int j = 0; j < actualWidth; ++j) {
                if (node.getCell(i, j) == 'P') {
                    minR = Math.min(minR, i);
                    maxR = Math.max(maxR, i);
                    minC = Math.min(minC, j);
                    maxC = Math.max(maxC, j);
                }
            }
        }
        
        boolean horizontal = (minR == maxR);
        int length = horizontal ? (maxC - minC + 1) : (maxR - minR + 1);
        int distance;
        Set<Character> blockers = new HashSet<>();

        if (horizontal) {
            if(exitRow != minR) {
                return Integer.MAX_VALUE;
            }

            if(exitCol == actualWidth) {
                distance = exitCol - maxC;
                for(int c = maxC + 1; c < exitCol; c++) {
                    char cell = node.getCell(minR, c);
                    if(cell != '.') {
                        blockers.add(cell);
                    }
                }
            } 
            else {
                distance = minC - exitCol;
                for(int c = 0; c < minC; c++) {
                    char cell = node.getCell(minR, c);
                    if(cell != '.') {
                        blockers.add(cell);
                    }
                }
            }
        } 
        else {
            if(exitCol != minC) {
                return Integer.MAX_VALUE;
            }

            if(exitRow == actualHeight) {
                distance = exitRow - maxR;
                for (int r = maxR + 1; r < exitRow; r++) {
                    char cell = node.getCell(r, minC);
                    if(cell != '.'){
                        blockers.add(cell);
                    }
                }
            } 
            else {
                distance = minR - exitRow;
                for (int r = 0; r < minR; r++) {
                    char cell = node.getCell(r, minC);
                    if(cell != '.') {
                        blockers.add(cell);
                    }
                }
            }
        }
        
        return distance + blockers.size();
    }

    public int calculateDistanceHeuristic(Node node) {
        int minR = actualHeight, maxR = -1, minC = actualWidth, maxC = -1;
        for(int i = 0; i < actualHeight; ++i) {
            for(int j = 0; j < actualWidth; ++j) {
                if (node.getCell(i, j) == 'P') {
                    minR = Math.min(minR, i);
                    maxR = Math.max(maxR, i);
                    minC = Math.min(minC, j);
                    maxC = Math.max(maxC, j);
                }
            }
        }
        
        boolean horizontal = (minR == maxR);
        int length = horizontal ? (maxC - minC + 1) : (maxR - minR + 1);
        int distance;
        
        if (horizontal) {
            if(exitRow != minR) {
                return Integer.MAX_VALUE;
            }

            if(exitCol == actualWidth) {
                distance = exitCol - maxC;
            } 
            else {
                distance = minC - exitCol;
            }
        } 
        else {
            if(exitCol != minC) {
                return Integer.MAX_VALUE;
            }

            if(exitRow == actualHeight) {
                distance = exitRow - maxR;
            } 
            else {
                distance = minR - exitRow;
            }
        }
        
        return distance;
    }

    int calculateHeuristic(Node node) {
        if(mode == 0) {
            return calculateDistanceHeuristic(node);
        }
        return calculateBlockerDistanceHeuristic(node);
    }
}