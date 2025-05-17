import java.util.Arrays;

class Node {
    private char[][] grid;
    private int g; // cost from start to current node
    private int h; // heuristic cost to goal
    private int ID;
    private int parentID;
    private String move;

    Node(char[][] grid, int g, int h, int ID, int parentID, String move) {
        this.grid = new char[grid.length][];
        for (int i = 0; i < grid.length; ++i) {
            this.grid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        this.g = g;
        this.h = h;
        this.ID = ID;
        this.parentID = parentID;
        this.move = move;
    }

    public Node(Node other) {
        this.grid = new char[other.grid.length][];
        for (int i = 0; i < other.grid.length; ++i) {
            this.grid[i] = Arrays.copyOf(other.grid[i], other.grid[i].length);
        }
        this.g = other.g;
        this.h = other.h;
        this.ID = other.ID;
        this.parentID = other.parentID;
        this.move = other.move;
    }

    public int cost() {
        return g + h;
    }

    public void printMove() {
        System.out.println(move);
    }

    public void printGrid() {
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid[i].length; ++j) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getG() {
        return g;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getH() {
        return h;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setCell(int i, int j, char piece) {
        grid[i][j] = piece;
    }

    public char getCell(int i, int j) {
        return grid[i][j];
    }

    public int getPieceDirection(int i, int j) { // 0: horizontal, 1: vertical
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        for(int d = 0; d < 4; d++) {
            int ni = i + dx[d];
            int nj = j + dy[d];
            if(ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length) {
                if (grid[ni][nj] == grid[i][j]) {
                    return d & 1;
                }
            }
        }
        return -1;
    }

    public int getPieceLength(int i, int j) {
        int direction = getPieceDirection(i, j);
        int length = 1;

        if(direction == 1) { // vertical
            for(int ni = i + 1; ni < grid.length; ++ni) {
                if (grid[ni][j] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
            for(int ni = i - 1; ni >= 0; --ni) {
                if (grid[ni][j] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
        } else { // horizontal
            for(int nj = j + 1; nj < grid[0].length; ++nj) {
                if (grid[i][nj] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
            for(int nj = j - 1; nj >= 0; --nj) {
                if (grid[i][nj] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
        }
        return length;
    }

    public boolean canPutPiece(int i, int j, int direction, int length) {
        if(direction == 1) { // vertical
            for(int ni = i; ni < i + length; ++ni) {
                if (grid[ni][j] != '.') {
                    return false;
                }
            }
        } else { // horizontal
            for(int nj = j; nj < j + length; ++nj) {
                if (grid[i][nj] != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public String getStringGrid() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid[0].length; ++j) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }
}