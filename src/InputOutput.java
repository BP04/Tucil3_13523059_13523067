import java.io.*;
import java.util.*;

public class InputOutput {
    public static char[][] readInput(String fileName, int[] ukuran, int[] exitPos) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] dimension = reader.readLine().trim().split(" ");
            int height = Integer.parseInt(dimension[0]);
            int width = Integer.parseInt(dimension[1]);

            int nonPrimaryPieces = Integer.parseInt(reader.readLine().trim());

            int exitRow = -1;
            int exitCol = -1;
            List<String> initialBoard = new ArrayList<>();

            boolean foundK = false;

            for (int i = 0; i < height; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IllegalArgumentException("Jumlah baris papan tidak sesuai dengan input ukuran.");
                }

                if (exitRow == -1) {
                    for (int j = 0; j < line.length(); j++) {
                        if (line.charAt(j) == 'K') {
                            foundK = true;
                            exitRow = i;
                            exitCol = j;
                            break;
                        }
                    }
                }

                initialBoard.add(line);
            }

            if (!foundK || exitRow == 0) {
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

                initialBoard.add(line);
            }

            if (exitRow == -1 || exitCol == -1) {
                throw new IllegalArgumentException("Papan tidak memiliki pintu keluar (K).");
            }


            char[][] grid = new char[height][width];
            if (exitRow != 0 && exitRow != height) {
                for (int i = 0; i < height; i++) {
                    int idx = 0;
                    for (int j = 0; j < width; j++) {
                        if (j != exitCol) {
                            grid[i][idx++] = initialBoard.get(i).charAt(j);
                        }
                    }
                }

            } else {
                if (exitRow == 0) {
                    for (int i = 1; i < height + 1; i++) {
                        grid[i-1] = initialBoard.get(i).toCharArray();
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        grid[i] = initialBoard.get(i).toCharArray();
                    }
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
