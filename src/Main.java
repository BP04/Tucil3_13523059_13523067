import model.InputParser;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input filename (without .txt): ");
        String userInputFile = scanner.nextLine().trim();

        String inputFolder = "../test/input/";
        String fileName = inputFolder + userInputFile + ".txt";

        String filenameBase = userInputFile; 

        int[] ukuran = new int[2];
        int[] exitPos = new int[2];

        try {
            char[][] board = InputParser.readInput(fileName, ukuran, exitPos);

            for (char[] row : board) {
                System.out.println(row);
            }

            SolverUCS solver = new SolverUCS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], filenameBase);
            solver.solve();
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }
}
