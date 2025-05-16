class Node {
    private char[][] grid;
    private int g;
    private int h;
    private int ID;
    private int parentID;
    private String move;

    Node(List<String> grid, int g, int h, int ID, int parentID, String move) {
        this.grid = grid;
        this.g = g;
        this.h = h;
        this.ID = ID;
        this.parentID = parentID;
        this.move = move;
    }

    public void printMove() {
        System.out.println(move);
    }

    public void printGrid() {
        for(int i = 0; i < grid.size(); i++) {
            for(int j = 0; j < grid.get(i).length(); j++) {
                System.out.print(grid.get(i).charAt(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setPiece(int i, int j, char piece) {
        grid[i][j] = piece;
    }

    public char getPiece(int i, int j) {
        return grid[i][j];
    }

    public int getPieceDirection(int i, int j) { // 0: vertical, 1: horizontal
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        for(int d = 0; d < 4; d++) {
            int ni = i + dx[d];
            int nj = j + dy[d];
            if(ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length) {
                if (grid[ni][nj] == grid[i][j]) {
                    return d % 2;
                }
            }
        }
    }

    public int getPieceLength(int i, int j) {
        int direction = getPieceDirection(i, j);
        int length = 1;

        if(direction) { // horizontal
            for(int nj = j + 1; nj < grid[0].length; nj++) {
                if (grid[i][nj] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
            for(int nj = j - 1; nj >= 0; nj--) {
                if (grid[i][nj] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
        } else { // vertical
            for(int ni = i + 1; ni < grid.length; ni++) {
                if (grid[ni][j] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
            for(int ni = i - 1; ni >= 0; ni--) {
                if (grid[ni][j] == grid[i][j]) {
                    length++;
                } else {
                    break;
                }
            }
        }
        return length;
    }

    public void removePiece(char pieceID) {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == pieceID) {
                    grid[i][j] = '.';
                }
            }
        }
    }

    public boolean canPutPiece(int i, int j, int direction, int length) {
        if(direction) { // horizontal
            for(int nj = j; nj < j + length; nj++) {
                if (grid[i][nj] != '.') {
                    return false;
                }
            }
        } else { // vertical
            for(int ni = i; ni < i + length; ni++) {
                if (grid[ni][j] != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public void putPiece(int i, int j, int direction, int length, char pieceID) {
        if(direction) { // horizontal
            for(int nj = j; nj < j + length; nj++) {
                grid[i][nj] = pieceID;
            }
        } else { // vertical
            for(int ni = i; ni < i + length; ni++) {
                grid[ni][j] = pieceID;
            }
        }
    }
}