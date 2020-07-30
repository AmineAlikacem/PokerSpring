package ca.homework.dto;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Game {
    /**
     * ID of the game
     */
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    /**
     * Game deck of this game
     */
    @OneToOne(targetEntity=Deck.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Deck gameDeck;
    /**
     * Players belonging to this game
     */
    @OneToMany(targetEntity=Player.class, mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    /**
     * Contructor of Game
     */
    public Game() {
        this.players = new ArrayList<Player>();
    }

    /**
     * Get the ID of the game
     * @return the ID of the game
     */
    public Long getId() {
        if (this.id != null) {return this.id;}

        return null;
    }

    /**
     * Get the ID of the game as a string
     * @return the ID of the game as a string
     */
    public String getIdAsString() {
        if (this.id != null) {return Long.toString(this.id);}

        return "-1";
    }

    /**
     * Create a deck
     * Only if it doesn't have one already
     * @param deck deck we want to add
     */
    public void createDeck(Deck deck) {
        if (!hasGameDeck()) {
            this.gameDeck = deck;
            deck.setGame(this);
        }
    }

    /**
     * Deal a card to each respective player of the game
     * @return the players that were dealt a card
     */
    public List<Player> dealCards() {
        for (Player player: this.players) {
            if (!this.gameDeck.isEmpty()) {
                Card card = this.gameDeck.pickFromDeck();
                card.setDeckHolder(null);
                player.receiveCard(card);
            }
        }

        return this.players;
    }

    /**
     * Get the game deck
     * @return the game deck
     */
    public Deck getGameDeck() {
        return this.gameDeck;
    }

    /**
     * Get the list of players associated to this game
     * @return the list of players associated with this game
     */
    public List<Player> getPlayers(){
        return this.players;
    }

    /**
     * Retrieve a specific player by its ID
     * @param id of the player
     * @return the player
     */
    public Player getPlayerById(int id) {
        return this.players.get(id);
    }

    /**
     * Verify if the game has a game deck
     * @return true if the game has a game deck
     */
    public boolean hasGameDeck() {
        if (this.gameDeck != null) { return true; }

        return false;
    }

    /**
     * Verify if there are players associated to this game
     * @return true if the game has players
     */
    public boolean hasPlayers() {
        if (!this.players.isEmpty()) { return true; }

        return false;
    }

    /**
     * Add the cards of another deck to the game deck
     */
    public void addDeckToGameDeck() {
        this.gameDeck.addDeck(new Deck());
    }

    /**
     * Add a player to the game
     * @param player specific player we want to add
     */
    public void addPlayer(Player player){
        this.players.add(player);
    }

    /**
     * Determing the count of each suit
     * @return mapping key value between the string referencing
     * the suit verbatim and the count of the cards associated with it
     */
    public Map<String, Integer> getSuitCount() {
        int hearts = 0;
        int spades = 0;
        int clubs = 0;
        int diamonds = 0;

        if (!this.gameDeck.isEmpty()) {
            for (Card card : this.gameDeck.getCards()) {
                switch (card.getSuit()) {
                    case 1 : hearts++;
                             break;
                    case 2 : spades++;
                             break;
                    case 3 : clubs++;
                             break;
                    case 4 : diamonds++;
                }
            }
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("hearts", hearts);
        map.put("spades", spades);
        map.put("clubs", clubs);
        map.put("diamonds", diamonds);

        return map;
    }

    /**
     * Get the count of all specific cards in the game deck
     * @return the count of all specific cards in the game deck
     */
    public int[][] getCardCount() {
        int[][] map = new int[4][13];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                map[i][j] = 0;
            }
        }

        if (!this.gameDeck.isEmpty()) {
            for (Card card : this.gameDeck.getCards()) {
                map[card.getSuit()-1][card.getFaceValue()-1]++;
            }
        }

        return map;
    }
}
