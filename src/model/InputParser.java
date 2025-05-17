package model;

import java.io.*;
import java.util.*;

public class InputParser {
    public static char[][] readInput(String fileName, int[] ukuran, int[] exitPos) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] dimension = reader.readLine().trim().split(" ");
            int height = Integer.parseInt(dimension[0]);
            int width = Integer.parseInt(dimension[1]);

            int nonPrimaryPieces = Integer.parseInt(reader.readLine().trim());

            int exitRow = -100;
            int exitCol = -100;
            List<String> initialBoard = new ArrayList<>();

            boolean foundK = false;

            for (int i = 0; i < height; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IllegalArgumentException("Jumlah baris papan tidak sesuai dengan inout yang diberikan.");
                }

                if (!foundK) {
                    boolean spaceChar = false;
                    for (int j = 0; j < line.length(); j++) {
                        if (line.charAt(j) == 'K') {
                            foundK = true;
                            exitRow = i;
                            exitCol = j;
                        }
                        if (line.charAt(j) == ' ') {
                            spaceChar = true;
                        }
                    }
                    if(foundK && i == 0 && exitCol < width && (spaceChar || line.length() == 1)) {
                        exitRow = -1;
                        continue;
                    }
                    if(line.length() == width + 1) {
                        if(line.charAt(0) == ' ') {
                            line = line.substring(1);
                        } else if(line.charAt(0) == 'K') {
                            exitCol = -1;
                            line = line.substring(1);
                        } else {
                            line = line.substring(0, width);
                        }
                    }
                }
                else {
                    if (line.length() == width + 1) {
                        if (line.charAt(0) == ' ') {
                            line = line.substring(1);
                        }
                        else {
                            line = line.substring(0, width);
                        }
                    }
                }

                initialBoard.add(line);
            }

            if (!foundK || exitRow == -1) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IllegalArgumentException("Jumlah baris papan tidak sesuai dengan input ukuran.");
                }

                if (exitRow == -1) {
                    for (int j = 0; j < line.length(); j++) {
                        if (line.charAt(j) == 'K') {
                            foundK = true;
                            exitRow = height;
                            exitCol = j;
                            break;
                        }
                    }
                }
                else {
                    for (int j = 0; j < line.length(); j++) {
                        if (line.charAt(j) == 'K') {
                            foundK = true;
                            exitRow = height;
                            exitCol = j;
                            break;
                        }
                    }
                }

                initialBoard.add(line);
            }

            if (!foundK) {
                throw new IllegalArgumentException("Papan tidak memiliki pintu keluar (K).");
            }


            char[][] grid = new char[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    grid[i][j] = initialBoard.get(i).charAt(j);
                }
            }

            ukuran[0] = height;
            ukuran[1] = width;
            exitPos[0] = exitRow;
            exitPos[1] = exitCol;

            return grid;
        }
    }
}
