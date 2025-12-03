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
        isHidden = true;
    }

    public String getFace() {
        if (isHidden) {
            return "? ";
        } 
        if (val == 10) {
            return face;
        } else {
            return face + " ";
        }
    }

    public String getSuit() {
        if (isHidden) {
            return "? ";
        }
        return suit + " ";
    }

    public void flip() {
        isHidden = !isHidden;
    }

    public void topToScreen(int r, int c, Screen s) {
        for (int ra = 1; ra < 3; ra++) { //ra = "row additive"
            int sc = (15 * c - s.lines[r * 2 + ra].length()) / 15; //sc = "skipped columns"
            for (int cc = 0; cc < sc; cc++) { //cc = "current column"
                s.lines[r * 2 + ra] += "               ";
            }
        }
        s.lines[r * 2 + 1] += "O ------- O    ";
        s.lines[r * 2 + 2] += "| " + getFace() + getSuit() + "    | ";
        if (!isHidden) {
            s.lines[r * 2 + 2] += (r + 1) + " ";
            if (r < 9) {
                s.lines[r * 2 + 2] += " ";
            }
        } else {
            s.lines[r * 2 + 2] += "X  ";
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
        //the weird looking parts of the strings are color codes 
        String[] suits = {"\u001B[37mC\u001b[0m", "\u001b[31mD\u001b[0m", "\u001B[31mH\u001b[0m", "\u001B[37mS\u001b[0m"};
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

    public Card[] deal(Card[] s) {
        Card[] td = new Card[cards.length - 1]; //temp deck
        Card[] ts = new Card[s.length + 1]; //temp stack
        for (int i = 1; i < cards.length; i++) {
            td[i - 1] = cards[i];
        }
        for (int i = 0; i < s.length; i++) {
            ts[i] = s[i];
        } 
        ts[ts.length - 1] = cards[0];
        cards = td;
        return ts;
    }
}

class Stack {
    Card[] stack;
    int col;

    public Stack(Deck d, int c) {
        stack = new Card[0];
        col = c;
        for (int i = 0; i < c + 1; i++) {
            stack = d.deal(stack);
        }
        stack[stack.length - 1].isHidden = false;
    }

    public void stackToScreen(Screen s) {
        for (int i = 0; i < stack.length; i++) {
            stack[i].topToScreen(i, col, s);
            if (i == stack.length - 1) {
                stack[i].bottomToScreen(i, col, s);
            }
        }
    }
}

class Table {
    Stack[] ts; //table stacks

    public Table(Deck d) {
        ts = new Stack[7];
        for (int i = 0; i < 7; i++) {
            ts[i] = new Stack(d, i);
        }
    }

    public void tableToScreen(Screen s) {
        for (int i = 0; i < 7; i++) {
            ts[i].stackToScreen(s);
        }
    }
}

class Screen { //class for the screen (holds all of the text)
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
        Table table = new Table(deck);
        table.tableToScreen(screen);
        screen.outputScreen();
    }
}
