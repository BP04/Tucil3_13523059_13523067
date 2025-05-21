import java.io.IOException;
import model.*;

public class Main {
    public static void main(String[] args) {
        String fileName = "test/input/test5.txt";
        int[] ukuran = new int[2];
        int[] exitPos = new int[2];

        try {
            char[][] board = InputParser.readInput(fileName, ukuran, exitPos);
            // System.out.println("Height: " + ukuran[0]);
            // System.out.println("Width: " + ukuran[1]);
            // System.out.println("Exit Position: (" + exitPos[0] + ", " + exitPos[1] + ")");

            // for (char[] row : board) {
            //     System.out.println(row);
            // }

            fileName = "test/output/" + fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.')) + ".txt";

            System.out.println("Output file: " + fileName);

            // SolverAstar solver = new SolverAstar(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], 1, fileName);
            // SolverGBFS solver = new SolverGBFS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], 0, fileName);
            // SolverUCS solver = new SolverUCS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], fileName);
            SolverIDS solver = new SolverIDS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], fileName);

            solver.solve();
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }
}