package ca.homework.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Card implements  Serializable{
    /**
     * ID of the Card
     */
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    /**
     * Face Value
     */
    private int faceValue;
    /**
     *  Suit
     */
    private int suit;

    /**
     * The deck for which the card is associated
     */
    @ManyToOne
    private Deck deck;

    /**
     * The player for which it is associated
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    /**
     *  Static list of suits displayed verbatim
     */
    private static String[] suits = { "hearts", "spades", "clubs", "diamonds" };
    /**
     *  Static list of face value displayed verbatim
     */
    private static String[] faceValues  = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };

    /**
     *  Constructor the card
     * @param suit Assign the suit to the card
     * @param faceValue Assign the face value of the card
     */
    public Card(int suit, int faceValue ){
        this.suit = suit;
        this.faceValue = faceValue;
    }

    /**
     * Default constructor
     */
    public Card(){
    }

    /**
     * Get ID
     * @return ID of the card
     */
    public Long getId() {
        if (id != null) {return id;}

        return null;
    }

    /**
     *  Get ID as string
     * @return String ID of the card
     */
    public String getIdAsString() {
        if (id != null) {return Long.toString(id);}

        return "-1";
    }

    /**
     *  Get Face value as String in a static context
     * @param faceValue Face Value
     * @return the String containing the face value verbatim
     */
    public static String faceValueAsString( int faceValue ) {
        return faceValues[faceValue];
    }

    /**
     *  Get the Face value of the card
     * @return the face value of the card
     */
    public int getFaceValue() {
        return this.faceValue;
    }

    /**
     *  Get the suit of the card
     * @return the suit of the card
     */
    public int getSuit() {
        return this.suit;
    }

    /**
     * Set the player that holds this card
     * @param player the specific player holder
     */
    public void setPlayerHolder(Player player) {
        this.player = player;
    }

    /**
     *  Set the deck that holds this card
     * @param deck the specific deck holder
     */
    public void setDeckHolder(Deck deck) {
        this.deck = deck;
    }

    /**
     * Get a string describing the specific card
     * @return the specific face value and suit displayed verbatim as a string
     */
    public @Override String toString() {
        return faceValues[this.faceValue-1] + " of " + suits[this.suit-1];
    }

    /**
     * Get a string describing the specific card in a static context
     * @param suit suit of the card
     * @param faceValue face value of the card
     * @return string describing the card verbatim
     */
    public static String helperToString(int suit, int faceValue) {
        return faceValues[faceValue] + " of " + suits[suit];
    }
}
