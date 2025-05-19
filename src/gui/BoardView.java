package gui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import java.util.*;

public class BoardView {
    private final GridPane grid;
    private final Map<Character, Color> colorMap = new HashMap<>();
    private int cellSize;

    public BoardView(char[][] initialBoard, int cellSize) {
        this.cellSize = cellSize;
        grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setStyle("-fx-background-color: lightgray;");
        grid.setAlignment(Pos.CENTER);

        drawBoard(initialBoard);
    }

    public void updateBoard(char[][] newBoard) {
        drawBoard(newBoard);
    }

    public void setCellSize(int size) {
        this.cellSize = size;
    }

    public Pane getView() {
        return grid;
    }

    private void drawBoard(char[][] board) {
        grid.getChildren().clear();
        Random rand = new Random();

        int rows = board.length;
        int cols = board[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = board[row][col];
                Rectangle rect = new Rectangle(cellSize, cellSize);

                if (c == '.') {
                    rect.setFill(Color.WHITE);
                } else if (c == 'P') {
                    rect.setFill(Color.RED);
                } else {
                    colorMap.putIfAbsent(c, Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
                    rect.setFill(colorMap.get(c));
                }

                rect.setStroke(Color.BLACK);
                grid.add(rect, col, row);
            }
        }
    }
}
