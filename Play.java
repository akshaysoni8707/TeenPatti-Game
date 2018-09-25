import java.util.Random;
import java.util.Scanner;

final class Card {
    private String message = "";
    private Card[] winner = new Card[3];
    private char type[] = {'\u2660', '\u2665', '\u2666', '\u2663'}, singleCard;
    private int numbers[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, singleNumber;
    private int randomCheck[] = new int[9];
    private String name = "Player";
    private int index = 0;
    private Random random = new Random();
    private Card totalCard[] = new Card[9];
    private Card playerCard[][];

    private Card(int size) {
        int SIZE = 3;
        randomCheck = new int[SIZE * size];
        totalCard = new Card[(SIZE * size)];
        playerCard = new Card[size][SIZE];
    }

    private Card(char type, int numbers) {
        this.singleCard = type;
        this.singleNumber = numbers;
    }

    static void gamePlay() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers;
        System.out.println("Enter the number of players :");
        numberOfPlayers = scanner.nextInt();
        Card card = new Card(numberOfPlayers);
        card.randomCard();
        for (int y = 0; y < numberOfPlayers; y++) {
            System.out.println("player " + (y + 1) + " Enter your name :");
            card.display(card.playerCard[y], scanner.next());
        }
        card.winner = card.playerCard[0];
        for (int y = 1; y < numberOfPlayers; y++) {
            System.out.print("Between " + card.playerCard[y][0].name + " and " + card.winner[0].name + " ,");
            card.winner = card.checkWin(card.playerCard[y], card.winner);
            System.out.println(card.message + " ,high card of " + card.winner[0].name + " is " + card.maxCard(card.winner) + "\n");
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println("\t\t\tFinal winner is " + card.winner[0].name + " \n" + card.message + " ," + card.winner[0].name + "'s high card is " + card.maxCard(card.winner));
    }

    private void display(Card[] cards, String name) {
        int i = 0;
        cards[i].name = name;
        cards[i + 1].name = name;
        cards[i + 2].name = name;
        System.out.println("---------------------------------------");
        System.out.println(" _____\t _____ \t _____");
        System.out.println("|" + numberToString(cards[i].singleNumber) + "   |\t" + "|" + numberToString(cards[i + 1].singleNumber) + "   |\t" + "|" + numberToString(cards[i + 2].singleNumber) + "   " + "|");
        System.out.println("|     |\t|     |\t|     |\t" + name);
        System.out.println("|  " + cards[i].singleCard + "  |\t" + "|  " + cards[i + 1].singleCard + "  |\t" + "|  " + cards[i + 2].singleCard + "  |");
        System.out.println("|     |\t|     |\t|     |");
        System.out.println("|_____|\t|_____|\t|_____|");
        System.out.println();
        System.out.println("---------------------------------------");
    }

    private void randomCard() {
        int i = 0;
       /* randomCheck[0] = 0;
        randomCheck[1] = 24;
        randomCheck[2] = 49;*/
        for (; i < randomCheck.length; i++) {
            int r = random.nextInt(51);
            for (int i1 = 0; i1 < randomCheck.length; i1++) {
                int x = randomCheck[i1];
                if (r == x) {
                    r = random.nextInt(51);
                    i1 = 0;
                }
            }
            randomCheck[i] = r;
            totalCard[i] = new Card(typeCapture(r), getNumber(r));
        }
        int secondIndex = 0;
        for (int z = 0; z < i; ++z) {
            if (index < playerCard.length) {
                if (z % 3 == 0 && z > 0) {
                    index++;
                    secondIndex = 0;
                    if (index == playerCard.length) break;
                }
                playerCard[index][secondIndex] = totalCard[z];
                secondIndex++;
            }
        }
    }

    private char typeCapture(int number) {
        if (number >= 0 && number <= 12)
            return type[0];
        else if (number >= 13 && number <= 25)
            return type[1];
        else if (number >= 26 && number <= 38)
            return type[2];
        else if (number >= 39 && number <= 51)
            return type[3];
        else
            return '\0';
    }

    private int getNumber(int number) {
        if (number <= 12)
            return numbers[number];
        else if (number < 52)
            return numbers[number % 13];
        else
            return 0;
    }

    private Card maxCard(Card card[]) {
        Card max = card[0];
        for (int i = 1; i < card.length; i++) {
            if (max.singleNumber < card[i].singleNumber) {
                max = card[i];
            } else if (max.singleNumber == card[i].singleNumber) {
                max = maxTypeCheck(max, card[i]);
            }
        }
        return max;
    }

    private Card maxTypeCheck(Card card1, Card card2) {
        int max1 = 0, max2 = 0;
        for (int i = 0; i < type.length; i++) {
            if (card1.singleCard == type[i]) max1 = i;
            if (card2.singleCard == type[i]) max2 = i;
        }
        if (max1 < max2)
            return card1;
        else
            return card2;
    }

    private String numberToString(int data) {
        String string;
        if (data < 10) {
            string = " " + String.valueOf(data);
            return string;
        } else if (data == 10) {
            string = String.valueOf(data);
            return string;
        } else if (data == 11) {
            string = " J";
            return string;
        } else if (data == 12) {
            string = " Q";
            return string;
        } else if (data == 13) {
            string = " K";
            return string;
        } else if (data == 14) {
            string = " A";
            return string;
        } else
            return null;
    }

    private boolean sameNumber(Card card[]) {
        return card[0].singleNumber == card[1].singleNumber && card[1].singleNumber == card[2].singleNumber;
    }

    private boolean sameType(Card card[]) {
        return card[0].singleCard == card[1].singleCard && card[1].singleCard == card[2].singleCard;
    }

    private boolean sequenceChecker(Card card[]) {
        Card[] newCards = sortNumbers(card);
        return newCards[0].singleNumber == newCards[1].singleNumber - 1 && newCards[1].singleNumber == newCards[2].singleNumber - 1;
    }

    private Card[] sortNumbers(Card card[]) {
        for (int i = 0; i < card.length; i++) {

            for (int j = 0; j < card.length; j++) {
                if (card[i].singleNumber < card[j].singleNumber) {
                    Card temp = card[i];
                    card[i] = card[j];
                    card[j] = temp;
                }
            }
        }
        return card;
    }

    private Card[] checkWin(Card card1[], Card card2[]) {
        ///Same number
        if (sameNumber(card1)) {
            if (sameNumber(card2)) {
                if (maxCard(card1).singleNumber > maxCard(card2).singleNumber) {
                    message = card1[0].name + " wins ,Same numbers ";
                    return card1;
                } else {
                    message = card2[0].name + " wins ,Same numbers ";
                    return card2;
                }
            } else {
                message = card1[0].name + " wins ,Same numbers ";
                return card1;
            }
        } else if (sameNumber(card2)) {
            message = card2[0].name + " wins ,Same numbers ";
            return card2;
        }
        ///Same Card Type and Numbers in Sequence
        else if (sameType(card1) && sequenceChecker(card1)) {
            if (sameType(card2) && sequenceChecker(card2)) {
                if (maxCard(card1).singleNumber > maxCard(card2).singleNumber) {
                    message = card1[0].name + " wins Same Card Type and Numbers in Sequence";
                    return card2;
                } else if (maxCard(card1).singleNumber < maxCard(card2).singleNumber) {
                    message = card2[0].name + " wins Same Card Type and Numbers in Sequence";
                    return card2;
                } else {
                    if (maxCard(card1) == maxTypeCheck(maxCard(card1), maxCard(card2))) {
                        message = card1[0].name + " wins Same Card Type and Numbers in Sequence";
                        return card1;
                    } else {
                        message = card2[0].name + " wins Same Card Type and Numbers in Sequence";
                        return card2;
                    }
                }
            } else {
                message = card1[0].name + " wins Same Card Type and Numbers in Sequence";
                return card1;
            }
        } else if (sameType(card2) && sequenceChecker(card2)) {
            message = card2[0].name + " wins Same Card Type and Numbers in Sequence";
            return card2;
        }
        ///Sequence of Numbers
        else if (sequenceChecker(card1)) {
            if (sequenceChecker(card2)) {
                if (maxCard(card1).singleNumber > maxCard(card2).singleNumber) {
                    message = card1[0].name + " wins Sequence of numbers";
                    return card1;
                } else if (maxCard(card1).singleNumber < maxCard(card2).singleNumber) {
                    message = card2[0].name + " wins Sequence of numbers";
                    return card2;
                } else {
                    if (maxCard(card1) == maxTypeCheck(maxCard(card1), maxCard(card2))) {
                        message = card1[0].name + " wins Sequence of numbers";
                        return card1;
                    } else {
                        message = card2[0].name + " wins Sequence of numbers";
                        return card2;
                    }
                }
            } else {
                message = card1[0].name + " wins Sequence of numbers";
                return card1;
            }
        } else if (sequenceChecker(card2)) {
            message = card2[0].name + " wins Sequence of numbers";
            return card2;
        }
        ///All Cards Same type
        else if (sameType(card1) && sameType(card2) && maxCard(card1).singleCard == maxCard(card2).singleCard) {
            if (maxCard(card1).singleNumber > maxCard(card2).singleNumber) {
                message = card1[0].name + " wins high card number,all cards are same";
                return card1;
            } else {
                message = card2[0].name + " wins high card number,all cards are same";
                return card2;
            }
        }
        ///Same Card type
        else if (sameType(card1)) {
            if (sameType(card2)) {
                if (maxCard(card1) == maxTypeCheck(maxCard(card1), maxCard(card2))) {
                    message = card1[0].name + " wins Same cards";
                    return card1;
                } else {
                    message = card2[0].name + " wins Same cards";
                    return card2;
                }
            } else {
                message = card1[0].name + " wins Same cards";
                return card1;
            }
        } else if (sameType(card2)) {
            message = card2[0].name + " wins Same cards";
            return card2;
        }
        ///High card
        else {
            if (maxCard(card1).singleNumber > maxCard(card2).singleNumber) {
                message = card1[0].name + " wins by high card";
                return card1;
            } else if (maxCard(card1).singleNumber < maxCard(card2).singleNumber) {
                message = card2[0].name + " wins by high card";
                return card2;
            } else {
                if (maxCard(card1) == maxTypeCheck(maxCard(card1), maxCard(card2))) {
                    message = card1[0].name + " wins by high card";
                    return card1;
                } else {
                    message = card2[0].name + " wins by high card";
                    return card2;
                }
            }
        }
    }

    @Override
    public final String toString() {
        return "Card{" +
                " " + singleCard +
                " " + singleNumber +
                '}';
    }
}

public class Play {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Card.gamePlay();
        long end = System.currentTimeMillis();
        System.out.println("\n\nstart :" + start + " end :" + end + " time taken : " + (end - start));
    }
}
