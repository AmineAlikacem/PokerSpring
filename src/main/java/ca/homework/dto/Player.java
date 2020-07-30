package ca.homework.dto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {
    /**
     * ID of the player
     */
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    /**
     * Name of the player
     */
    private String name;

    /**
     * Game for which the player belongs to
     */
    @ManyToOne
    private Game game;
    /**
     * Cards that the player holds
     */
    @OneToMany(targetEntity=Card.class, mappedBy = "player", cascade = CascadeType.ALL)
    private List<Card> cards;

    /**
     * Constuctor of the player
     * @param name we want to give to the new player
     */
    public Player(String name){
        this.name = name;
        this.cards = new ArrayList<Card>();
    }

    /**
     * Default constructor
     */
    public Player(){
    }

    /**
     * Get the ID as a string
     * @return the ID as a string
     */
    public String getIdAsString() {
        if (this.id != null) {return Long.toString(id);}

        return "-1";
    }

    /**
     * Set the game the player belongs to
     * @param game game the player belongs to
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get this ID of the player
     * @return the ID of the player
     */
    public Long getId() {
        if (this.id != null) {return this.id;}

        return null;
    }

    /**
     * Add a card to the hand of the player
     * @param card Card that the player receives
     */
    public void receiveCard(Card card) {
        this.cards.add(card);
        card.setPlayerHolder(this);
    }

    /**
     * Get the total value of all the cards the player posesses
     * @return the total value of all the cards the player posesses
     */
    public int getTotalValue() {
        int value = 0;
        for (Card card : this.cards) {
            value += card.getFaceValue();
        }

        return value;
    }

    /**
     * Get the cards the player holds
     * @return the cards the player holds
     */
    public List<Card> getCards() {
        return this.cards;
    }
}
