package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class SolverIDS {
    char[][] startingGrid;
    HashMap<String, Integer> nodeToID;
    ArrayList<Node> nodes;
    int actualWidth;
    int actualHeight;
    int exitRow;
    int exitCol;
    int nodeID;
    int maxDepth;
    String filename;

    public SolverIDS(char[][] startingGrid, int actualWidth, int actualHeight, int exitRow, int exitCol, String filename) {
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
        this.filename = filename;
        nodeID = 0;
        maxDepth = 1000;
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

    public void showSolutionToFile(Node b) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            ArrayList<Node> path = new ArrayList<>();
            Node currentNode = b;
            while (currentNode.getParentID() != -1) {
                path.add(currentNode);
                currentNode = nodes.get(currentNode.getParentID());
            }
            path.add(currentNode);

            out.println("Total moves: " + (path.size() + 1));
            out.println();

            Collections.reverse(path);
            for (Node node : path) {
                node.printMove(out);
                node.printGrid(out);
                out.println();
            }

            Node lastNode = path.get(path.size() - 1);

            int minR = actualHeight, maxR = -1, minC = actualWidth, maxC = -1;
            for (int i = 0; i < actualHeight; ++i) {
                for (int j = 0; j < actualWidth; ++j) {
                    if (lastNode.getCell(i, j) == 'P') {
                        minR = Math.min(minR, i);
                        maxR = Math.max(maxR, i);
                        minC = Math.min(minC, j);
                        maxC = Math.max(maxC, j);
                    }
                }
            }

            if (exitCol == actualWidth) {
                int length = maxC - minC + 1;
                for (int c = minC; c <= maxC; ++c) {
                    lastNode.setCell(minR, c, '.');
                }
                for (int c = actualWidth - length; c < actualWidth; ++c) {
                    lastNode.setCell(minR, c, 'P');
                }
            } else if (exitCol == -1) {
                int length = maxC - minC + 1;
                for (int c = minC; c <= maxC; ++c) {
                    lastNode.setCell(minR, c, '.');
                }
                for (int c = 0; c < length; ++c) {
                    lastNode.setCell(minR, c, 'P');
                }
            } else if (exitRow == actualHeight) {
                int length = maxR - minR + 1;
                for (int r = minR; r <= maxR; ++r) {
                    lastNode.setCell(r, minC, '.');
                }
                for (int r = actualHeight - length; r < actualHeight; ++r) {
                    lastNode.setCell(r, minC, 'P');
                }
            } else if (exitRow == -1) {
                int length = maxR - minR + 1;
                for (int r = minR; r <= maxR; ++r) {
                    lastNode.setCell(r, minC, '.');
                }
                for (int r = 0; r < length; ++r) {
                    lastNode.setCell(r, minC, 'P');
                }
            }

            lastNode.printMove(out);
            lastNode.printGrid(out);
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void solve() {
        int solutionNodeID = -1;
        
        for (int depthLimit = 0; depthLimit <= maxDepth; depthLimit++) {
            System.out.println("Searching with depth limit: " + depthLimit);
            
            nodeToID.clear();
            nodes.clear();
            nodeID = 0;
            
            Node startNode = new Node(startingGrid, 0, 0, 0, -1, "");
            nodeToID.put(startNode.getStringGrid(), 0);
            nodes.add(startNode);
            
            solutionNodeID = depthLimitedDFS(startNode, depthLimit);
            
            if (solutionNodeID != -1) {
                System.out.println("Solution found at depth: " + depthLimit);
                break;
            }
        }

        if (solutionNodeID != -1) {
            showSolution(nodes.get(solutionNodeID));
            showSolutionToFile(nodes.get(solutionNodeID));
        } else {
            System.out.println("No solution found within depth limit " + maxDepth + ".");
        }
    }
    
    private int depthLimitedDFS(Node startNode, int depthLimit) {
        Stack<Node> stack = new Stack<>();
        HashMap<String, Integer> visitedAtDepth = new HashMap<>();
        
        stack.push(startNode);
        visitedAtDepth.put(startNode.getStringGrid(), 0);
        
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            
            if (isSolved(currentNode)) {
                return currentNode.getID();
            }
            
            if (currentNode.getG() >= depthLimit) {
                continue;
            }
            
            expandNode(currentNode, stack, visitedAtDepth);
        }
        
        return -1;
    }
    
    private void expandNode(Node currentNode, Stack<Node> stack, HashMap<String, Integer> visitedAtDepth) {
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

                    for(int k = right; k >= left; --k) {
                        if(k == i) {
                            continue;
                        }
                        if (currentNode.canPutPiece(k, j, 1, length)) {
                            Node newNode = new Node(currentNode);
                            for(int l = 0; l < length; ++l) {
                                newNode.setCell(k + l, j, pieceID);
                            }

                            String newNodeKey = newNode.getStringGrid();
                            int newDepth = currentNode.getG() + 1;
                            
                            if (visitedAtDepth.containsKey(newNodeKey) && visitedAtDepth.get(newNodeKey) <= newDepth) {
                                continue;
                            }
                            
                            visitedAtDepth.put(newNodeKey, newDepth);
                            newNode.setG(newDepth);
                            nodeID++;
                            newNode.setID(nodeID);
                            newNode.setParentID(currentNode.getID());
                            if(k < i) {
                                newNode.setMove("Move " + pieceID + " up " + (i - k));
                            }
                            else {
                                newNode.setMove("Move " + pieceID + " down " + (k - i));
                            }
                            
                            nodeToID.put(newNodeKey, nodeID);
                            nodes.add(newNode);
                            
                            stack.push(newNode);
                        }
                    }

                    for(int k = 0; k < length; ++k) {
                        currentNode.setCell(i + k, j, pieceID);
                    }
                }
                else {
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

                    for(int k = right; k >= left; --k) {
                        if(k == j) {
                            continue;
                        }
                        if (currentNode.canPutPiece(i, k, 0, length)) {
                            Node newNode = new Node(currentNode);
                            for(int l = 0; l < length; ++l) {
                                newNode.setCell(i, k + l, pieceID);
                            }

                            String newNodeKey = newNode.getStringGrid();
                            int newDepth = currentNode.getG() + 1;
                            
                            if (visitedAtDepth.containsKey(newNodeKey) && visitedAtDepth.get(newNodeKey) <= newDepth) {
                                continue;
                            }
                            
                            visitedAtDepth.put(newNodeKey, newDepth);
                            newNode.setG(newDepth);
                            nodeID++;
                            newNode.setID(nodeID);
                            newNode.setParentID(currentNode.getID());
                            if(k < j) {
                                newNode.setMove("Move " + pieceID + " left " + (j - k));
                            }
                            else {
                                newNode.setMove("Move " + pieceID + " right " + (k - j));
                            }
                            
                            nodeToID.put(newNodeKey, nodeID);
                            nodes.add(newNode);
                            
                            stack.push(newNode);
                        }
                    }

                    for(int k = 0; k < length; ++k) {
                        currentNode.setCell(i, j + k, pieceID);
                    }
                }
            }
        }
    }
}