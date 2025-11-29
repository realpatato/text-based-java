import java.util.Scanner;

class Card {
    int val;
    String face, suit;

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
    }

    public void printTop() {
        System.out.println("O ------ O");
        System.out.println("| " + face + suit +  "     |");
    }

    public void printBottom() {
        System.out.println("|        |");
        System.out.println("|        |");
        System.out.println("|        |");
        System.out.println("|     " + face + suit + " |");
        System.out.println("O ------ O");
    }
}

public class Solitaire {
    public static void main(String args[]) {
        Card card1 = new Card(11, "C");
        Card card2 = new Card(13, "\u20AC");
        card2.printTop();
        card1.printTop();
        card1.printBottom();
    }
}
