import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Player {
    private int id;
    private String name;
    private List<Card> hand;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getHandSize() {
        return hand.size();
    }

    public List<Card> getValidCards(Card topCard, String currentColor) {
        List<Card> validCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.matches(topCard, currentColor)) {
                validCards.add(card);
            }
        }
        return validCards;
    }

    public boolean hasValidCards(Card topCard, String currentColor) {
        for (Card card : hand) {
            if (card.matches(topCard, currentColor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Card {
    private String color;
    private String value;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public boolean isSpecialAction() {
        return value.equals("skip") || value.equals("reverse") || value.equals("draw-two") || value.equals("wild") || value.equals("wild-draw-four");
    }

    public boolean matches(Card card, String currentColor) {
        return color.equals(card.getColor()) || value.equals(card.getValue()) || color.equals(currentColor);
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
}

class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void initialize() {
        String[] colors = {"red", "blue", "green", "yellow"};
        String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "skip", "reverse", "draw-two"};
        int[] numCards = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

        for (String color : colors) {
            for (int i = 0; i < values.length; i++) {
                for (int j = 0; j < numCards[i]; j++) {
                    cards.add(new Card(color, values[i]));
                }
            }
        }

        // Add wild cards and wild draw four cards
        for (int i = 0; i < 4; i++) {
            cards.add(new Card("wild", "wild"));
            cards.add(new Card("wild", "wild-draw-four"));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}
public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter the name of player " + i + ": ");
            String name = scanner.next();
            players.add(new Player(i, name));
        }

        Game game = new Game(players);
        game.playGame();
    }

    private List<Player> players;
    private Deck deck;
    private List<Card> playedCards;
    private int currentPlayerIndex;
    private int direction;
    private String currentColor;

    public Game(List<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.playedCards = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.direction = 1;
        this.currentColor = "";

        deck.initialize();
        deck.shuffle();

        // Deal initial cards to players
        for (Player player : players) {
            List<Card> initialCards = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                Card card = deck.drawCard();
                player.addCardToHand(card);
            }
        }

        // Place the first card on the table
        Card firstCard = deck.drawCard();
        playedCards.add(firstCard);
        currentColor = firstCard.getColor();
    }

    public void playGame() {
        boolean gameWon = false;

        while (!gameWon) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn.");

            System.out.println("Current card on the table: " + playedCards.get(playedCards.size() - 1));
            System.out.println("Your hand: " + currentPlayer.getHand());

            Card topCard = playedCards.get(playedCards.size() - 1);
            List<Card> validCards = currentPlayer.getValidCards(topCard, currentColor);

            if (validCards.size() > 0) {
                System.out.println("Valid cards: " + validCards);
                System.out.print("Enter the index of the card you want to play (0-" + (validCards.size() - 1) + "): ");
                int cardIndex = getInput(0, validCards.size() - 1);
                Card selectedCard = validCards.get(cardIndex);

                if (selectedCard.isSpecialAction()) {
                    handleSpecialCard(selectedCard);
                } else {
                    playedCards.add(selectedCard);
                    currentPlayer.removeCardFromHand(selectedCard);

                    if (currentPlayer.getHandSize() == 0) {
                        gameWon = true;
                        System.out.println(currentPlayer.getName() + " wins the game!");
                    }
                }

                if (!selectedCard.getColor().equals("wild")) {
                    currentColor = selectedCard.getColor();
                }
            } else {
                System.out.println("No valid cards to play. Drawing a card...");
                Card drawnCard = deck.drawCard();
                currentPlayer.addCardToHand(drawnCard);

                if (!currentPlayer.hasValidCards(topCard, currentColor)) {
                    System.out.println("Drawn card: " + drawnCard);
                    System.out.println("No valid cards to play. Skipping your turn.");
                }
            }

            currentPlayerIndex = (currentPlayerIndex + direction) % players.size();
            if (currentPlayerIndex < 0) {
                currentPlayerIndex += players.size();
            }

            System.out.println();
        }
    }

    private void handleSpecialCard(Card card) {
        switch (card.getValue()) {
            case "skip":
                System.out.println("Skipping the next player's turn.");
                currentPlayerIndex = (currentPlayerIndex + (2 * direction)) % players.size();
                if (currentPlayerIndex < 0) {
                    currentPlayerIndex += players.size();
                }
                break;
            case "reverse":
                System.out.println("Reversing the direction of play.");
                direction *= -1;
                break;
            case "draw-two":
                System.out.println("Making the next player draw two cards and skipping their turn.");
                int nextPlayerIndex = (currentPlayerIndex + direction) % players.size();
                if (nextPlayerIndex < 0) {
                    nextPlayerIndex += players.size();
                }
                Player nextPlayer = players.get(nextPlayerIndex);
                nextPlayer.addCardToHand(deck.drawCard());
                nextPlayer.addCardToHand(deck.drawCard());
                break;
            case "wild":
                System.out.print("Enter the color you want to choose (red, blue, green, yellow): ");
                String chosenColor = getInput("red", "blue", "green", "yellow");
                System.out.println("Changing the color to " + chosenColor);
                currentColor = chosenColor;
                break;
            case "wild-draw-four":
                System.out.print("Enter the color you want to choose (red, blue, green, yellow): ");
                String chosenWildColor = getInput("red", "blue", "green", "yellow");
                System.out.println("Changing the color to " + chosenWildColor);
                currentColor = chosenWildColor;
                int nextPlayerIdx = (currentPlayerIndex + direction) % players.size();
                if (nextPlayerIdx < 0) {
                    nextPlayerIdx += players.size();
                }
                Player nextPlayerDrawFour = players.get(nextPlayerIdx);
                nextPlayerDrawFour.addCardToHand(deck.drawCard());
                nextPlayerDrawFour.addCardToHand(deck.drawCard());
                nextPlayerDrawFour.addCardToHand(deck.drawCard());
                nextPlayerDrawFour.addCardToHand(deck.drawCard());
                currentPlayerIndex = (currentPlayerIndex + (2 * direction)) % players.size();
                if (currentPlayerIndex < 0) {
                    currentPlayerIndex += players.size();
                }
                break;
        }
    }


    private <T> T getInput(T... validInputs) {
        Scanner scanner = new Scanner(System.in);
        T input = null;
        boolean validInput = false;

        while (!validInput) {
            if (validInputs.length > 0) {
                if (validInputs[0] instanceof Integer) {
                    if (scanner.hasNextInt()) {
                        int intValue = scanner.nextInt();
                        for (T validInt : validInputs) {
                            if (validInt.equals(intValue)) {
                                input = validInt;
                                validInput = true;
                                break;
                            }
                        }
                    } else {
                        scanner.nextLine();
                    }
                } else if (validInputs[0] instanceof String) {
                    String strValue = scanner.next();
                    for (T validStr : validInputs) {
                        if (strValue.equalsIgnoreCase(String.valueOf(validStr))) {
                            input = validStr;
                            validInput = true;
                            break;
                        }
                    }
                }
            }
        }

        return input;
    }

}
