import java.util.Scanner;
import java.time.LocalTime;

public class ConnectFour {
    //stores all of the colors as a constant array
    public static final String[] COLORS = {
        "\u001B[37m", //white, for weird terminals where it isn't the default
        "\u001b[31m", //red
        "\u001b[33m", //yellow
        "\u001b[0m" //reset 
    };

    //variable to store the board of the game
    public static int[][] board = {
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
    };

    //clears the screen (allows for animation, and making it look nice nice)
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //prints with given color number
    public static void colorPrint(String str, int cNum) {
        System.out.print(COLORS[cNum] + str + COLORS[3]);
    }

    //prints with default color, white
    public static void colorPrint(String str) {
        System.out.print(COLORS[0] + str + COLORS[3]);
    }

    public static void drawBoard() {
        //clears the screen before drawing the board
        clearScreen();
        //line telling the user the column numbers, it also goes ahead an extra couple lines for a gap
        colorPrint("-1-|-2-|-3-|-4-|-5-|-6-|-7-\n\n");
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 7; k++) {
                //prints the current piece at the given spot
                colorPrint(" O ", board[k][i]);
                //determines the end and stuff
                if (k < 6) {
                    colorPrint("|");
                } else {
                    if (i < 5) {
                        colorPrint("\n---+---+---+---+---+---+---\n");
                    } else {
                        System.out.println();
                    }
                }
            }
        }
        System.out.println();
    }

    public static boolean newPiece(int col, int color) {
        for (int i = 0; i < board[col].length; i++) {
            if (board[col][i] != 0) {
                if (i == 0) {
                    if (color == 1) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (color == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                if (i > 0) {
                    board[col][i - 1] = 0;
                }
                board[col][i] = color;
                drawBoard();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (color == 1) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String args[]) {
        //controls gameloop
        boolean playing = true;

        //controls who is currently playing
        boolean isPlayer1 = true;

        Scanner input = new Scanner(System.in);

        while (playing) {
            drawBoard();
            if (isPlayer1) {
                System.out.print("Input Column: ");
                int playerInput = input.nextInt() - 1;
                isPlayer1 = newPiece(playerInput, 1);
            } else {
                System.out.print("Input Column: ");
                int playerInput = input.nextInt() - 1;
                isPlayer1 = newPiece(playerInput, 2);
            }
            System.out.print("Continue Playing? [Y/N]: ");
            String kpQuestion = input.next().toLowerCase();
            if (!kpQuestion.equals("y")) {
                playing = false;
            }
        }

        input.close();
    }
}
