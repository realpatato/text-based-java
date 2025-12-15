import java.util.Scanner;

class Random {
    public static int randInt(int min, int max) {
        //generates random number between given values, both values included
        return (int)(Math.random() * (max - min + 1)) + min;
    }
}

class Card {
    /* Class for the card object */
    int val; //the actual value of the card
    String face, suit; //face = the value the user can see, suit is the suit of the card
    boolean isHidden; //whether or not the card is hidden

    public Card(int v, String s) {
        /* Instantiation of a card object */
        val = v; //the value is the number inputted
        //V determining the face value of the card
        if (val > 1 && val < 11) { //if the card isn't an ace, a king, a queen, or a jack (1, 11, 12, 13) the value and face value are the same
            face = String.valueOf(val);
        } else if (val == 1) { //the face value is "A" for an ace
            face = "A";
        } else if (val == 11) { //the face value is "J" for a jack
            face = "J";
        } else if (val == 12) { //the face value is "Q" for a queen
            face = "Q";
        } else { //if the value isn't any of the aforementioned values, it is a "K", or a king
            face = "K";
        }
        suit = s; //the suit of the card that was inputted is stored
        isHidden = true; //cards default to being hidden
    }

    public String getFace() {
        /* Returns the face value depending on if the card is hidden or not */
        if (isHidden) { //the card is hidden, so we return a "?"
            return "? ";
        } 
        if (val == 10) { //if the value is 10, the text would mess up the display of the card, so we don't add a space like the others
            return face;
        } else { //adds a space and returns the face value
            return face + " ";
        }
    }

    public String getSuit() {
        /* Returns the suit of the card based on if it is hidden or not */
        if (isHidden) { //the card is hidden, so we return a "?"
            return "? ";
        }
        return suit + " "; //return the suit with a space
    }

    public void flip() { 
        /* Reverses if the card is hidden or not */
        isHidden = !isHidden;
    }

    public void topToScreen(int r, int c, Screen s) {
        if (r != -1) { //-1 means its part of top
            for (int ra = 9; ra < 11; ra++) { //ra = "row additive"
                int sc = (15 * c - s.lines[r * 2 + ra].length()) / 15; //sc = "skipped columns"
                for (int cc = 0; cc < sc; cc++) { //cc = "current column"
                    s.lines[r * 2 + ra] += "               ";
                }
            }
            s.lines[r * 2 + 9] += "O ------- O    ";
            s.lines[r * 2 + 10] += "| " + getFace() + getSuit() + "    | ";
            if (!isHidden) {
                s.lines[r * 2 + 10] += (r + 1) + " ";
                if (r < 9) {
                    s.lines[r * 2 + 10] += " ";
                }
            } else {
            s.lines[r * 2 + 10] += "X  ";
            }
        } else {
            if (c == 4) {
                int ss = 45 - s.lines[0].length(); //ss = "skipped spaces"
                for (int cs = 0; cs < ss; cs++) { //cs = "current space"
                    for (int cr = 0; cr < 2; cr++) { //cr = "current row"
                        s.lines[cr] += " ";
                    }
                }
            }
            s.lines[0] += "O ------- O";
            s.lines[1] += "| " + getFace() + getSuit() + "    |";
            if (c != 2) {
                for (int i = 0; i < 2; i++) {
                    s.lines[i] += "    ";
                }
            }
        }
    }

    public void bottomToScreen(int r, int c, Screen s) {
        if (r != -1) {
            for (int ra = 11; ra < 16; ra++) { //ra = "row additive"
                int sc = (15 * c - s.lines[r * 2 + ra].length()) / 15; //sc = "skipped columns"
                for (int cc = 0; cc < sc; cc++) { //cc = "current column"
                    s.lines[r * 2 + ra] += "               ";
                }
            }
            s.lines[r * 2 + 11] += "|         |    ";
            s.lines[r * 2 + 12] += "|         |    ";
            s.lines[r * 2 + 13] += "|         |    ";
            s.lines[r * 2 + 14] += "|     " + getFace() + getSuit() +"|    ";
            s.lines[r * 2 + 15] += "O ------- O    ";
        } else {
            if (c == 4) {
                int ss = 45 - s.lines[3].length(); //ss = "skipped spaces"
                for (int cs = 0; cs < ss; cs++) { //cs = "current space"
                    for (int cr = 3; cr < 7; cr++) { //cr = "current row"
                        s.lines[cr] += " ";
                    }
                }
            }
            s.lines[2] += "|         |";
            s.lines[3] += "|         |";
            s.lines[4] += "|         |";
            s.lines[5] += "|     " + getFace() + getSuit() +"|";
            s.lines[6] += "O ------- O";
            if (c != 2) {
                for (int i = 2; i < 7; i++) {
                    s.lines[i] += "    ";
                }
            }
        }
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

    public void deckToScreen(Screen s) {
        s.lines[0] += "O ------- O    ";
        s.lines[1] += "|         |    ";
        s.lines[2] += "|         |    ";
        if (cards.length > 0) {
            s.lines[3] += "|    ?    |    ";
        } else {
            s.lines[3] += "|    X    |    ";
        }
        s.lines[4] += "|         |    ";
        s.lines[5] += "|         |    ";
        s.lines[6] += "O ------- O    ";
    }
}

class Waste {
    Card[] cards;

    public Waste() {
        cards = new Card[0];
    }

    public void draw(Deck d) {
        for (int i = 0; i < 3; i++) {
            cards = d.deal(cards);
        }
        for (Card c : cards) {
            c.isHidden = false;
        }
    }

    public void wasteToScreen(Screen s) {
        if (cards.length > 0) {
            for (int i = cards.length - 1; i >= 0 ; i--) {
                if (i == cards.length - 1) {
                    cards[i].topToScreen(-1, 2, s);
                    cards[i].bottomToScreen(-1, 2, s);
                } else if (i >= cards.length - 3) {
                    s.lines[0] += "--- O";
                    s.lines[1] += "    |";
                    s.lines[2] += "    |";
                    s.lines[3] += "    |";
                    s.lines[4] += "    |";
                    s.lines[5] += cards[i].getFace() + cards[i].getSuit() + "|";
                    s.lines[6] += "--- O";
                } else {
                    break;
                }
            }
        } else {
            s.lines[0] += "O ------- O    ";
            s.lines[1] += "|         |    ";
            s.lines[2] += "|         |    ";
            s.lines[3] += "|    X    |    ";
            s.lines[4] += "|         |    ";
            s.lines[5] += "|         |    ";
            s.lines[6] += "O ------- O    ";
        }
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

class Tower {
    Card[] tower;
    int col;
    String suit;

    public Tower(int c) {
        tower = new Card[0];
        col = c + 4;
        String[] suits = {"\u001B[37mC\u001b[0m", "\u001b[31mD\u001b[0m", "\u001B[31mH\u001b[0m", "\u001B[37mS\u001b[0m"};
        suit = suits[c];
    }

    public void towerToScreen(Screen s) {
        if (tower.length > 0) {
            tower[tower.length - 1].topToScreen(-1, col, s);
            tower[tower.length - 1].bottomToScreen(-1, col, s);
        } else {
            if (col == 4) {
                int ss = 45 - s.lines[0].length(); //ss = "skipped spaces"
                for (int cs = 0; cs < ss; cs++) { //cs = "current space"
                    for (int cr = 0; cr < 7; cr++) { //cr = "current row"
                        s.lines[cr] += " ";
                    }
                }
            }
            s.lines[0] += "O ------- O    ";
            s.lines[1] += "|         | ";
            if (suit.equals("\u001B[37mS\u001b[0m")) {
                s.lines[1] += "0  ";
            } else {
                s.lines[1] += "   ";
            }
            s.lines[2] += "|         |    ";
            s.lines[3] += "|    " + suit + "    |    ";
            s.lines[4] += "|         |    ";
            s.lines[5] += "|         |    ";
            s.lines[6] += "O ------- O    ";
        }
    }
}

class Header {
    Tower[] towers;
    Waste waste;
    Deck deck;

    public Header(Deck d) {
        towers = new Tower[4];
        for (int c = 0; c < 4; c++) {
            towers[c] = new Tower(c);
        }
        deck = d;
        waste = new Waste();
        waste.draw(deck);
    }

    public void headerToScreen(Screen s) {
        deck.deckToScreen(s);
        waste.wasteToScreen(s);
        for (int i = 0; i < 4; i++) {
            towers[i].towerToScreen(s);
        }
        for (int i = 0; i < 7; i++) {
            s.lines[8] += "     " + (i + 1) + "         ";
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

    public void outputScreen() {
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
        Header header = new Header(deck);
        header.headerToScreen(screen);
        screen.outputScreen();
    }
}
