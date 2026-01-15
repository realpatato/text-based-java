class ToolBox {
    /* Any misc methods needed for the project! */
    public static int getIntLen(int n) {
        /* Gets the length of a positive integer */
        int p = 1;
        double d = n / 10.0;
        while (d > 1) {
            d /= 10.0;
            p++;
        }
        return p;
    }

    public static String intToEvenLenString(int n) {
        /* Converts an integer into a string with an even length */
        if (getIntLen(n) % 2 == 1) {
            return "0" + n;
        }
        return "" + n;
    }

    public static String addSpacing(String s, int sc) {
        /* Adds the given amount of spacing to the string */
        String ss = ""; //ss for "space string"
        for (int i = 0; i < sc; i++) {
            ss += " ";
        }
        return ss + s + ss;
    }
    
    public static int randInt(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public static void printCoords(int[] c) {
        System.out.print("(" + c[0] + ", "+ c[1] + ")");
    }

    public static void printArray(Tile[] a) {
        System.out.print("[");
        for (Tile t : a) {
            //printCoords(t.pos);
            System.out.print(t.vsi);
        }
        System.out.println("]");
    }

    public static void printArray(Tile[][] a) {
        System.out.print("[");
        for (Tile[] t : a) {
            printArray(t);
        }
        System.out.println("]");
    }
}

class Tile {
    /* Represents a tile in the game */
    public int pow, vsi, hsi; //pow: stores the power of the tile | vsi + hsi: slide importances vertically and horizontally
    public int[] pos; //position of the tile

    public Tile(int x, int y) {
        pow = 0;
        pos = new int[2];
        pos[0] = x;
        pos[1] = y;
    }

    public void incrementPow() {
        pow++;
    }

    public String toString() {
        String retStr = "|";
        String numStr = "";
        if (pow != 0) {
            numStr = ToolBox.intToEvenLenString((int)(Math.pow(2, pow)));
        } else {
            numStr = "00";
        }
        numStr = ToolBox.addSpacing(numStr, 4 - (numStr.length() / 2));
        retStr += numStr;
        if (pos[0] == 3) {
            retStr += "|";
        }
        return retStr;
    }
}

class Tiles {
    /* Class used for game starting, tiles are created here and stored sorted */
    Tile[] tiles;

    public Tiles() {
        tiles = new Tile[16];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                tiles[y * 4 + x] = new Tile(x, y);
            }
        }
    }
}

class Rows {
    /* 
    Stores the rows in order to handle left and right movement with ease 
    Additionally holds the methods to get the horizontal importances
    */
    public Tile[][] rows;

    public Rows(Tiles tiles) {
        rows = new Tile[4][4];
        for (int r = 0; r < 4; r++) {
            for (int i = 0; i < 4; i++) {
                rows[r][i] = tiles.tiles[4 * r + i];
            }
        }
    }

    public int getHorizontalSlideImportance(int x, int y) {
        Tile[] rowToSearchOver = rows[y];
        Tile tileToCompare = rowToSearchOver[x];
        int hsi = 1;
        for (Tile t : rowToSearchOver) {
            if (t.equals(tileToCompare)) {
                if (t.pow == 0) {
                    return 0;
                } else {
                    break;
                }
            } else {
                if (t.pow != 0) {
                    hsi++;
                }
            }
        }
        return hsi;
    }

    public void updateHorizontalSlideImportances() {
        for (Tile[] ts : rows) {
            for (Tile t : ts) {
                t.hsi = getHorizontalSlideImportance(t.pos[0], t.pos[1]);
            }
        }
    }
}

class Columns {
    /* 
    Stores the columns to handle up and down movement with ease
    Additionally holds the methods to get the vertical importances
    */
    public Tile[][] columns;

    public Columns(Rows r) {
        columns = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int cr = 0; cr < 4; cr++) {
                columns[i][cr] = r.rows[cr][i];
            }
        }
    }

    public int getVerticalSlideImportance(int x, int y) {
        Tile[] columnToSearchOver = columns[x];
        Tile tileToCompare = columnToSearchOver[y];
        int vsi = 1;
        for (Tile t : columnToSearchOver) {
            if (t.equals(tileToCompare)) {
                if (t.pow == 0) {
                    return 0;
                } else {
                    break;
                }
            } else {
                if (t.pow != 0) {
                    vsi++;
                }
            }
        }
        return vsi;
    }

    public void updateVerticalSlideImportances() {
        for (Tile[] ts : columns) {
            for (Tile t : ts) {
                t.vsi = getVerticalSlideImportance(t.pos[0], t.pos[1]);
            }
        }
    }
}

class Board {
    /*
    Handles the movement by storing a set of rows and a set of columns
    */
    public Rows bRows;
    public Columns columns;

    public Board() {
        Tiles t = new Tiles();
        bRows = new Rows(t);
        columns = new Columns(bRows);
    }

    public void swapPos(Tile a, Tile b) {
        int[] tempPos = a.pos;
        a.pos = b.pos;
        b.pos = tempPos;
    }

    public void left() {
        bRows.updateHorizontalSlideImportances();
        for (Tile[] r : bRows.rows) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3 - i; j++) {
                    if (r[j].hsi <= r[j + 1].hsi) {
                        swapPos(r[j], r[j + 1]);
                        Tile temp = r[j];
                        r[j] = r[j + 1];
                        r[j + 1] = temp;
                    }
                }
            }
        }
    }

    public void right() {
        bRows.updateHorizontalSlideImportances();
        for (Tile[] r : bRows.rows) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3 - i; j++) {
                    if (r[j].hsi >= r[j + 1].hsi) {
                        swapPos(r[j], r[j + 1]);
                        Tile temp = r[j];
                        r[j] = r[j + 1];
                        r[j + 1] = temp;
                    }
                }
            }
        }
    }

    public void up() {
        columns.updateVerticalSlideImportances();
        for (Tile[] c : columns.columns) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3 - i; j++) {
                    System.out.println(c[j].vsi + " " + c[j + 1].vsi);
                    if (c[j].vsi >= c[j + 1].vsi) {
                        swapPos(c[j], c[j + 1]);
                        Tile temp = c[j];
                        c[j] = c[j + 1];
                        c[j + 1] = temp;
                    }
                }
            }
        }
    }
}

class Screen {
    /* Stores the screen for easy and clean output */
    private String[] lines;

    public Screen() {
        lines = new String[20]; //the needed amount for the board plus some extra
        for (int i = 0; i < 20; i++) {
            lines[i] = "";
        }
        for (int i = 0; i < 17; i++) {
            if (i % 4 == 0) {
                lines[i] += "O--------O--------O--------O--------O";
            } else if (i % 4 != 2) {
                lines[i] += "|        |        |        |        |";
            }
        }
    }

    public String toString(Rows r) {
        String retStr = "";
        for (int cr = 0; cr < 4; cr++) {
            lines[2 + cr * 4] = "";
            for (int i = 0; i < 4; i++) {
                lines[2 + cr * 4] += r.rows[cr][i].toString();
            }
        }
        for (String line : lines) {
            retStr += line + "\n";
        }
        return retStr;
    }
}

public class Game2048 {
    public static void main(String args[]) {
        Screen s = new Screen();
        Board b = new Board();
        b.bRows.rows[0][0].pow = 2;
        b.bRows.rows[0][3].pow = 2;
        System.out.print(s.toString(b.bRows));
        b.left();
        System.out.print(s.toString(b.bRows));
        b.right();
        System.out.print(s.toString(b.bRows));
        b.up();
        System.out.print(s.toString(b.bRows));
        ToolBox.printArray(b.columns.columns);
    }
}