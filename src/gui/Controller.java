package gui;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import model.InputParser;
import javafx.scene.layout.StackPane;
import java.io.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.util.*;
import model.SolverAstar;
import model.SolverUCS;
import model.SolverGBFS;

public class Controller {
    private final BorderPane rootPane;
    private final VBox controlPanel;
    private final Label headerLabel;
    private final ComboBox<String> algorithmSelector;
    private final ComboBox<String> heuristicSelector;
    private final Button startButton;
    private final Button uploadButton;
    private final Label moveCountLabel;
    private File inputFile;
    private String selectedAlgorithm;
    private BoardView boardView;
    private StackPane centerPane;

    private List<char[][]> loadedStates = new ArrayList<>();

    public Controller() {
        rootPane = new BorderPane();

        controlPanel = new VBox(10);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(20));

        headerLabel = new Label("Rush Hour PathFinder");
        headerLabel.getStyleClass().add("header-label");

        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll("UCS", "GBFS", "A*", "IDS");
        algorithmSelector.setPromptText("Select Algorithm");

        heuristicSelector = new ComboBox<>();
        heuristicSelector.getItems().addAll("Distance", "Blocking + Distance");
        heuristicSelector.setPromptText("Select Heuristic");

        HBox controlsGroup = new HBox(20);
        controlsGroup.setAlignment(Pos.CENTER);
        controlsGroup.getChildren().addAll(algorithmSelector, heuristicSelector);

        moveCountLabel = new Label("Total Moves: -");

        controlPanel.getChildren().addAll(headerLabel, controlsGroup, moveCountLabel);
        rootPane.setTop(controlPanel);
        BorderPane.setAlignment(controlPanel, Pos.CENTER);

        uploadButton = new Button("Upload Input File");
        uploadButton.setOnAction(e -> loadInputFile());

        startButton = new Button("Start");
        startButton.setOnAction(e -> {
            selectedAlgorithm = algorithmSelector.getValue();
            if (inputFile != null && selectedAlgorithm != null) {
                runSolverAndLoadOutput(inputFile.getName(), selectedAlgorithm);
            } else {
                moveCountLabel.setText("Please select algorithm and input file.");
            }
        });

        HBox bottomControls = new HBox(20);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setPadding(new Insets(20, 20, 40, 20));
        bottomControls.getChildren().addAll(uploadButton, startButton);
        rootPane.setBottom(bottomControls);

        boardView = new BoardView(new char[1][1], 50); 
        centerPane = new StackPane(boardView.getView());
        centerPane.setAlignment(Pos.CENTER);
        rootPane.setCenter(centerPane);
    }

    public Parent getView() {
        return rootPane;
    }

    private void loadInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Input File");

        fileChooser.setInitialDirectory(new File("test/input/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        inputFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (inputFile != null) {
            moveCountLabel.setText("File loaded: " + inputFile.getName());
        }
    }

    private void parseFile(File file) throws IOException {
        loadedStates.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<char[]> currentBoard = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (!currentBoard.isEmpty()) {
                        loadedStates.add(currentBoard.toArray(new char[0][]));
                        currentBoard.clear();
                    }
                } else if (!line.startsWith("Move") && !line.startsWith("Total")) {
                    currentBoard.add(line.toCharArray());
                }
            }
            if (!currentBoard.isEmpty()) {
                loadedStates.add(currentBoard.toArray(new char[0][]));
            }
        }
    }

    private void runSolverAndLoadOutput(String inputFileName, String algorithm) {
        try {
            String inputFolder = "test/input/";
            String outputFolder = "test/output/";

            String fileName = inputFolder + inputFileName;
            int[] ukuran = new int[2];
            int[] exitPos = new int[2];

            char[][] board = InputParser.readInput(fileName, ukuran, exitPos);

            if (algorithm.equals("UCS")) {
                SolverUCS solver = new SolverUCS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1]);
                solver.solve();
            } else if (algorithm.equals("GBFS")) {
                SolverGBFS solver = new SolverGBFS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], 1);
                solver.solve();
            } else if (algorithm.equals("A*")) {
                SolverAstar solver = new SolverAstar(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1], 1);
                solver.solve();
            } else if (algorithm.equals("IDS")) {
                SolverIDS solver = new SolverIDS(board, ukuran[0], ukuran[1], exitPos[0], exitPos[1]);
            } else {
                moveCountLabel.setText("Algorithm not supported.");
                return;
            }

            File outputFile = new File(outputFolder + inputFileName);

            if (!outputFile.exists()) {
                moveCountLabel.setText("Output file not found: " + outputFile.getName());
                return;
            }

            parseFile(outputFile);

            if (!loadedStates.isEmpty()) {
                boardView = new BoardView(loadedStates.get(0), 50);
                centerPane.getChildren().setAll(boardView.getView());
                moveCountLabel.setText("Total Moves: " + (loadedStates.size() - 1));
                playAnimation();
            } else {
                moveCountLabel.setText("No states loaded from output file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            moveCountLabel.setText("Failed to read input/output files.");
        } catch (Exception e) {
            e.printStackTrace();
            moveCountLabel.setText("Error running solver.");
        }
    }


    private void playAnimation() {
        if (loadedStates.isEmpty()) return;

        new Thread(() -> {
            try {
                for (char[][] frame : loadedStates) {
                    javafx.application.Platform.runLater(() -> boardView.updateBoard(frame));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
