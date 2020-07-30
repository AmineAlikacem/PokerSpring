package  ca.homework.dto;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Deck {
    /**
     * ID of the deck
     */
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    /**
     * Cards of the deck
     */
    @OneToMany(targetEntity=Card.class, mappedBy = "deck", cascade = CascadeType.ALL)
    private List<Card> cards;
    /**
     * The game associated with this deck
     */
    @OneToOne(targetEntity=Game.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Game game;

    /**
     * Constructor of the deck
     * We fill the deck and shuffle
     */
    public Deck() {
        this.cards = new ArrayList<Card>();
        fillTheDeck();
        shuffle();
    }

    /**
     * Set the gamm for which the deck belongs to
     * @param game possessing this deck
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the ID of the deck
     * @return the ID of the deck
     */
    public Long getId() {
        if (this.id != null) {return this.id;}

        return null;
    }

    /**
     * Get the ID as a string
     * @return the ID as a string
     */
    public String getIdAsString() {
        if (this.id != null) {return Long.toString(this.id);}

        return "-1";
    }

    /**
     * Fill the deck with all its respective cards
     */
    private void fillTheDeck(){
        for (int suit = 1; suit <= 4; suit++) {
            for (int faceValue = 1; faceValue <= 13; faceValue++) {
                Card card = new Card(suit,faceValue);
                this.cards.add( card );
                card.setDeckHolder(this);
            }
        }
    }

    /**
     *  Shuffle the deck with random permutations
     */
    public void shuffle(){
        int indexOne, indexTwo;
        Random generator = new Random();
        Card helper;

        for (int i=0; i<100; i++) {
            indexOne = generator.nextInt( this.cards.size() - 1 );
            indexTwo = generator.nextInt( this.cards.size() - 1 );

            helper = this.cards.get( indexTwo );
            this.cards.set( indexTwo, this.cards.get( indexOne ) );
            this.cards.set( indexOne, helper );
        }
    }

    /**
     *  Add the cards of a deck to the current deck
     * @param deck deck we want to add
     */
    public void addDeck(Deck deck) {
        this.cards.addAll(deck.getCards());
        for (Card card : deck.getCards()) {
            card.setDeckHolder(this);
        }
        shuffle();
    }

    /**
     * Get the number of cards
     * @return the number of cards
     */
    public int getTotalCards() {
        return this.cards.size();
    }

    /**
     * Remove a card from the deck
     * @return this specific card
     */
    public Card pickFromDeck() {
        return this.cards.remove( 0 );
    }

    /**
     *  Get the cards associated with this deck
     * @return the list of cards
     */
    public List<Card> getCards() {
        return this.cards;
    }

    /**
     * Verify if the deck is empty
     * @return true if the deck is empty
     */
    public boolean isEmpty(){
        return this.cards.isEmpty();
    }
}
