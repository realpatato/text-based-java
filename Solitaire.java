import java.util.Scanner;

class Random {
    public static int randInt(int min, int max) {
        //generates random number between given values, both values included
        return (int)(Math.random() * (max - min + 1)) + min;
    }
}

class Card {
    int val;
    String face, suit;
    boolean isHidden;

    public Card(int v, String s) {
        val = v;
        if (val > 1 && val < 11) {
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
        for (int ra = 1; ra < 3; ra++) { //ra = "row additive"
            int sc = (15 * c - s.lines[r * 2 + ra].length()) / 15; //sc = "skipped columns"
            for (int cc = 0; cc < sc; cc++) { //cc = "current column"
                s.lines[r * 2 + ra] += "               ";
            }
        }
        s.lines[r * 2 + 1] += "O ------- O    ";
        s.lines[r * 2 + 2] += "| " + getFace() + getSuit() + "    | " + (r + 1) + " ";
        if (r < 9) {
            s.lines[r * 2 + 2] += " ";
        }
    }

    public void bottomToScreen(int r, int c, Screen s) {
        for (int ra = 3; ra < 8; ra++) { //ra = "row additive"
            int sc = (15 * c - s.lines[r * 2 + ra].length()) / 15; //sc = "skipped columns"
            for (int cc = 0; cc < sc; cc++) { //cc = "current column"
                s.lines[r * 2 + ra] += "               ";
            }
        }
        s.lines[r * 2 + 3] += "|         |    ";
        s.lines[r * 2 + 4] += "|         |    ";
        s.lines[r * 2 + 5] += "|         |    ";
        s.lines[r * 2 + 6] += "|     " + getFace() + getSuit() +"|    ";
        s.lines[r * 2 + 7] += "O ------- O    ";
    }
}

class Deck {
    Card[] cards;

    public Deck() {
        cards = new Card[52];
        String[] suits = {"C", "D", "H", "S"};
        for (int si = 0; si < 4; si++) { //si = suit index
            for (int v = 1; v < 14; v++) { //v = value
                cards[(si * 13) + v - 1] = new Card(v, suits[si]);
            }
        }
    }

    public void shuffle() {
        for (int i = 0; i < 1000; i++) {
            int card1Index = Random.randInt(0, 51);
            int card2Index = Random.randInt(0, 51);
            Card temp = cards[card1Index];
            cards[card1Index] = cards[card2Index];
            cards[card2Index] = temp;
        }
    }
}

class Screen {
    String[] lines;

    public Screen() {
        //Shouldn't have any more lines than this
        lines = new String[75];
        //Sets every value in the lines to be an empty string so the output looks good.
        for (int i = 0; i < 75; i++) {
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
        for (int i = 0; i < 75; i++) {
            if (lines[i].equals("") == false) {
                System.out.println(lines[i]);
                //System.out.println(Random.randInt(0, 10));
                lines[i] = "";
            }
        }
    }
}

public class Solitaire {
    public static void main(String args[]) {
        Screen screen = new Screen();
        Deck deck = new Deck();
        deck.shuffle();
        screen.outputScreen();
    }
}
