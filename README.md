
# UNO Game

This is a Java implementation of the popular card game UNO. The program allows multiple players to participate and supports the standard rules of UNO, including drawing cards, playing cards, skipping turns, reversing the direction of play, and declaring a winner.

## Class Structure

The class structure for the UNO game is designed as follows:

1. Player:
   - Represents a player in the game.
   - Properties:
     - Unique identifier
     - Name
     - Hand of cards
   - Methods:
     - `receiveInitialCards`: Receives a set of initial cards at the start of the game.
     - `drawCard`: Draws a card from the deck.
     - `playCard`: Plays a card from their hand.
     - `applyCardEffects`: Receives and applies the effects of special cards (e.g., skip, reverse).
     - `hasValidCards`: Checks if the player has any valid cards to play.

2. Card:
   - Represents a single card in the UNO deck.
   - Properties:
     - Color (red, blue, green, yellow)
     - Value (numbered cards 0-9, special action cards)
   - Methods:
     - Getters and setters for color and value
     - `isSpecialAction`: Determines if the card is a special action card.
     - `performAction`: Performs any special actions associated with the card.

3. Deck:
   - Represents the deck of cards used in the game.
   - Properties:
     - List of cards
   - Methods:
     - `initialize`: Initializes the deck with the standard set of cards.
     - `shuffle`: Shuffles the cards.
     - `drawCard`: Draws a card from the deck.
     - `replenishDeck`: Replenishes the deck when it runs out.

4. Game:
   - Represents the game itself.
   - Properties:
     - List of players
     - Current player index
     - Direction of play
     - Current color
   - Methods:
     - `startGame`: Initializes the deck, distributes cards to players, and determines the first player.
     - `handleTurns`: Handles the order of turns, taking into account skips and reverses.
     - `validateAndPlayCard`: Validates and applies card plays based on the current card in play.
     - `checkWinConditions`: Checks for win conditions, such as a player running out of cards.
     - `declareWinner`: Declares the winner at the end of the game.

## Additional Rules

The UNO game implementation includes the following additional rules:

- Wild Card: Allows the player to choose the color that continues the game. It can be played on any card.
- Wild Draw Four Card: Similar to the Wild Card, the player gets to choose the color. In addition, the next player must draw four cards from the deck and skip their turn. However, this card can only be played if the player does not have a card of the current color.

These additional rules are implemented in the Card class, where the card type is determined, and special actions are performed accordingly.

