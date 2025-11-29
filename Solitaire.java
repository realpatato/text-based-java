import java.util.Scanner;

class Card {
    int val;
    String face, suit;
    boolean isHidden;

    public Card(int v, String s) {
        val = v;
        if (val > 1 && val < 10) {
            face = String.valueOf(val);
        } else if (val == 1) {
            face = "A";
        } else if (val == 11) {
            face = "J";
        } else if (val == 12) {
            face = "Q";
        } else {
            face = "K";
        }
        suit = s;
        isHidden = false;
    }

    public String getFace() {
        if (isHidden) {
            return "? ";
        } 
        return face + " ";
    }

    public String getSuit() {
        if (isHidden) {
            return "? ";
        }
        return suit + " ";
    }

    public void topToScreen(int r, int c, Screen s) {
        s.lines[r * 2 + 1] += "O ------- O    ";
        s.lines[r * 2 + 2] += "| " + getFace() + getSuit() + "    | " + (r+1) + " ";
        if (r < 10) {
            s.lines[r * 2 + 2] += " ";
        }
    }

    public void bottomToScreen(int r, Screen s) {
        s.lines[r * 2 + 3] += "|         |    ";
        s.lines[r * 2 + 4] += "|         |    ";
        s.lines[r * 2 + 5] += "|         |    ";
        s.lines[r * 2 + 6] += "|     " + getFace() + getSuit() +"|    ";
        s.lines[r * 2 + 7] += "O ------- O    ";
    }
}

class Screen {
    String[] lines;

    public Screen() {
        //This length is VERY specfic. This is the maximum amount of lines that could possibly be needed.
        lines = new String[38];
        //Sets every value in the lines to be an empty string so the output looks good.
        for (int i = 0; i < 38; i++) {
            lines[i] = "";
        }
    }

    public void addTop() {
        //adds the column numbers
        for (int i = 0; i < 7; i++) {
            lines[0] += "     " + (i + 1) + "         ";
        }
    }

    public void outputScreen() {
        addTop();
        for (int i = 0; i < 38; i++) {
            if (lines[i].equals("") == false) {
                System.out.println(lines[i]);
                lines[i] = "";
            }
        }
    }
}

public class Solitaire {
    public static void main(String args[]) {
        Screen screen = new Screen();
        Card card1 = new Card(11, "C");
        Card card2 = new Card(13, "D");
        Card card3 = new Card(12, "S");
        card2.topToScreen(0, 0, screen);
        card1.topToScreen(1, 0, screen);
        card3.topToScreen(0, 1, screen);
        card1.bottomToScreen(1, screen);
        card3.bottomToScreen(0, screen);
        screen.outputScreen();
    }
}
