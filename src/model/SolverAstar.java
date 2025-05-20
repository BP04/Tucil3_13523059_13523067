package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class SolverAstar {
    char[][] startingGrid;
    HashMap<String, Integer> nodeToID;
    ArrayList<Node> nodes;
    int actualWidth;
    int actualHeight;
    int exitRow;
    int exitCol;
    int nodeID;
    Heuristic heuristic;
    int heuristicType = 0; // 0: distance, 1: blocker-distance

    public SolverAstar(char[][] startingGrid, int actualWidth, int actualHeight, int exitRow, int exitCol,int heuristicType) {
        this.startingGrid = new char[startingGrid.length][];
        for (int i = 0; i < startingGrid.length; ++i) {
            this.startingGrid[i] = Arrays.copyOf(startingGrid[i], startingGrid[i].length);
        }
        nodeToID = new HashMap<>();
        nodes = new ArrayList<>();
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
        nodeID = 0;
        this.heuristicType = heuristicType;
        heuristic = new Heuristic(actualHeight, actualWidth, exitRow, exitCol, heuristicType);
    }

    public boolean isSolved(Node b) {
        int minR = actualHeight, maxR = -1, minC = actualWidth, maxC = -1;
        for(int i = 0; i < actualHeight; ++i) {
            for(int j = 0; j < actualWidth; ++j) {
                if (b.getCell(i, j) == 'P') {
                    minR = Math.min(minR, i);
                    maxR = Math.max(maxR, i);
                    minC = Math.min(minC, j);
                    maxC = Math.max(maxC, j);
                }
            }
        }

        boolean horizontal = (minR == maxR);

        if(exitCol == actualWidth) {
            if(!horizontal || exitRow != minR) {
                return false;
            }

            for(int c = maxC + 1; c < actualWidth; ++c) {
                if(b.getCell(minR, c) != '.') {
                    return false;
                }
            }
            return true;
        }
        if(exitCol == -1) {
            if(!horizontal || exitRow != minR) {
                return false;
            }

            for(int c = minC - 1; c >= 0; --c) {
                if(b.getCell(minR, c) != '.') {
                    return false;
                }
            }
            return true;
        }
        if(exitRow == actualHeight) {
            if(horizontal || exitCol != minC) {
                return false;
            }

            for(int r = maxR + 1; r < actualHeight; ++r) {
                if(b.getCell(r, minC) != '.') {
                    return false;
                }
            }
            return true;
        }
        if(exitRow == -1) { 
            if(horizontal || exitCol != minC) {
                return false;
            }

            for(int r = minR - 1; r >= 0; --r) {
                if(b.getCell(r, minC) != '.') {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public void showSolution(Node b) {
        ArrayList<Node> path = new ArrayList<>();
        Node currentNode = b;
        while(currentNode.getParentID() != -1) {
            path.add(currentNode);
            currentNode = nodes.get(currentNode.getParentID());
        }
        path.add(currentNode);

        System.out.println("Papan Awal");
        currentNode.printGrid();

        int moveCount = 0;

        Collections.reverse(path);
        for(Node node : path) {
            moveCount++;
            System.out.print("Gerakan " + moveCount + ": ");
            node.printMove();
            node.printGrid();
            System.out.println();
        }

        Node lastNode = path.get(path.size() - 1);

        int minR = actualHeight, maxR = -1, minC = actualWidth, maxC = -1;
        for(int i = 0; i < actualHeight; ++i) {
            for(int j = 0; j < actualWidth; ++j) {
                if (lastNode.getCell(i, j) == 'P') {
                    minR = Math.min(minR, i);
                    maxR = Math.max(maxR, i);
                    minC = Math.min(minC, j);
                    maxC = Math.max(maxC, j);
                }
            }
        }

        if(exitCol == actualWidth) {
            int length = maxC - minC + 1;
            for(int c = minC; c <= maxC; ++c) {
                lastNode.setCell(minR, c, '.');
            }
            for(int c = actualWidth - length; c < actualWidth; ++c) {
                lastNode.setCell(minR, c, 'P');
            }
        }
        else if(exitCol == -1) {
            int length = maxC - minC + 1;
            for(int c = minC; c <= maxC; ++c) {
                lastNode.setCell(minR, c, '.');
            }
            for(int c = 0; c < length; ++c) {
                lastNode.setCell(minR, c, 'P');
            }
        }
        else if(exitRow == actualHeight) {
            int length = maxR - minR + 1;
            for(int r = minR; r <= maxR; ++r) {
                lastNode.setCell(r, minC, '.');
            }
            for(int r = actualHeight - length; r < actualHeight; ++r) {
                lastNode.setCell(r, minC, 'P');
            }
        }
        else if(exitRow == -1) { 
            int length = maxR - minR + 1;
            for(int r = minR; r <= maxR; ++r) {
                lastNode.setCell(r, minC, '.');
            }
            for(int r = 0; r < length; ++r) {
                lastNode.setCell(r, minC, 'P');
            }
        }

        moveCount++;
        System.out.println("Gerakan " + moveCount + ": Keluar");
        lastNode.printGrid();
        System.out.println();
    }

    public void solve() {
        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> a.getH() - b.getH());

        Node startNode = new Node(startingGrid, 0, 0, 0, -1, "");

        queue.add(startNode);
        nodeToID.put(startNode.getStringGrid(), 0);
        nodes.add(startNode);

        int solutionNodeID = -1;

        while(!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (isSolved(currentNode)) {
                solutionNodeID = currentNode.getID();
                break;
            }

            boolean[] visited = new boolean[256];

            for(int i = 0; i < actualHeight; ++i) {
                for(int j = 0; j < actualWidth; ++j) {
                    if (currentNode.getCell(i, j) == '.') {
                        continue;
                    }

                    char pieceID = currentNode.getCell(i, j);
                    if (visited[pieceID]) {
                        continue;
                    }
                    visited[pieceID] = true;

                    int vertical = currentNode.getPieceDirection(i, j);
                    int length = currentNode.getPieceLength(i, j);

                    if(vertical == 1) {
                        // System.out.println(pieceID + " vertical");

                        for(int k = 0; k < length; ++k) {
                            currentNode.setCell(i + k, j, '.');
                        }

                        int left = i;
                        while(left >= 0 && currentNode.getCell(left, j) == '.') {
                            left--;
                        }
                        left++;
                        int right = i;
                        while(right + length - 1 < actualHeight && currentNode.getCell(right + length - 1, j) == '.') {
                            right++;
                        }
                        right--;

                        for(int k = left; k <= right; ++k) {
                            if(k == i) {
                                continue;
                            }
                            if (currentNode.canPutPiece(k, j, 1, length)) {
                                Node newNode = new Node(currentNode);
                                for(int l = 0; l < length; ++l) {
                                    newNode.setCell(k + l, j, pieceID);
                                }

                                String newNodeKey = newNode.getStringGrid();
                                if (!nodeToID.containsKey(newNodeKey)) {
                                    newNode.setG(currentNode.getG() + 1);
                                    newNode.setH(heuristic.calculateHeuristic(newNode));
                                    nodeID++;
                                    newNode.setID(nodeID);
                                    newNode.setParentID(currentNode.getID());
                                    if(k < i) {
                                        newNode.setMove("move " + pieceID + " up " + (i - k));
                                    }
                                    else {
                                        newNode.setMove("move " + pieceID + " down " + (k - i));
                                    }
                                    
                                    nodeToID.put(newNodeKey, nodeID);
                                    nodes.add(newNode);

                                    // System.out.println(pieceID + " " + i + " " + j + " " + length);
                                    // newNode.printMove();
                                    // newNode.printGrid();
                                    // System.out.println();
                                    
                                    queue.add(newNode);
                                }
                            }
                        }

                        for(int k = 0; k < length; ++k) {
                            currentNode.setCell(i + k, j, pieceID);
                        }
                    }
                    else {
                        // System.out.println(pieceID + " horizontal");

                        for(int k = 0; k < length; ++k) {
                            currentNode.setCell(i, j + k, '.');
                        }

                        int left = j;
                        while(left >= 0 && currentNode.getCell(i, left) == '.') {
                            left--;
                        }
                        left++;
                        int right = j;
                        while(right + length - 1 < actualWidth && currentNode.getCell(i, right + length - 1) == '.') {
                            right++;
                        }
                        right--;

                        for(int k = left; k <= right; ++k) {
                            if(k == j) {
                                continue;
                            }
                            if (currentNode.canPutPiece(i, k, 0, length)) {
                                Node newNode = new Node(currentNode);
                                for(int l = 0; l < length; ++l) {
                                    newNode.setCell(i, k + l, pieceID);
                                }

                                String newNodeKey = newNode.getStringGrid();
                                if (!nodeToID.containsKey(newNodeKey)) {
                                    newNode.setG(currentNode.getG() + 1);
                                    newNode.setH(heuristic.calculateHeuristic(newNode));
                                    nodeID++;
                                    newNode.setID(nodeID);
                                    newNode.setParentID(currentNode.getID());
                                    if(k < j) {
                                        newNode.setMove("move " + pieceID + " left " + (j - k));
                                    }
                                    else {
                                        newNode.setMove("move " + pieceID + " right " + (k - j));
                                    }
                                    
                                    nodeToID.put(newNodeKey, nodeID);
                                    nodes.add(newNode);

                                    // System.out.println(pieceID + " " + i + " " + j + " " + length);
                                    // newNode.printMove();
                                    // newNode.printGrid();
                                    // System.out.println();
                                    
                                    queue.add(newNode);
                                }
                            }
                        }

                        for(int k = 0; k < length; ++k) {
                            currentNode.setCell(i, j + k, pieceID);
                        }
                    }
                }
            }
        }

        if (solutionNodeID != -1) {
            showSolution(nodes.get(solutionNodeID));
        } else {
            System.out.println("No solution found.");
        }
    }
}