package ca.homework.controller;
import ca.homework.CardRepository;
import ca.homework.DeckRepository;
import ca.homework.PlayerRepository;
import ca.homework.dto.Card;
import ca.homework.dto.Deck;
import ca.homework.dto.Game;
import ca.homework.GameRepository;
import ca.homework.dto.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping(PokerController.POKER_BASE_URI)
@EnableJpaRepositories("ca.homework")
public class PokerController {
    /**
     * Game Repository
     */
    private final GameRepository gameRepository;
    /**
     *  Deck Repository
     */
    private final DeckRepository deckRepository;
    /**
     * Card Repository
     */
    private final CardRepository cardRepository;
    /**
     * Player repository
     */
    private final PlayerRepository playerRepository;

    /**
     * Base URI of the routing
     */
    public static final String POKER_BASE_URI = "/poker";

    /**
     * Constructor of the rest controller in which the repositories
     * are fed and injected
     *
     * @param gameRepository
     * @param deckRepository
     * @param cardRepository
     * @param playerRepository
     */
    PokerController(@Autowired GameRepository gameRepository,
                    @Autowired DeckRepository deckRepository,
                    @Autowired CardRepository cardRepository,
                    @Autowired PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Get all games IDs
     * @return the IDs of all the games
     */
    @GetMapping("/games")
    public String all() {
        StringBuilder str = new StringBuilder("");
        List<Game> list = gameRepository.findAll();

        if (!list.isEmpty()) {
            for (Game game : list) {
                str.append(game.getIdAsString() + ", ");
            }
            str.delete(str.length() - 2, str.length() - 1);
        }

        return str.toString();
    }

    /**
     * Create a new game
     * @return Game ID that was added
     */
    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    public String newGame() {
        Game newGame = new Game();

        return gameRepository.save(newGame).getIdAsString();
    }

    /**
     * Delete a game
     * @param id of the game we want to delete
     * @return http status to signal that it worked
     */
    @DeleteMapping("/game/{id}")
    public HttpStatus deleteGame(@PathVariable final Long id) {
        gameRepository.deleteById( id);

        return HttpStatus.OK;
    }

    /**
     * Create a deck for a game only if that game doesn't have one
     * @param id of the game for which we want to create a deck
     * @return http status to signal that it worked
     */
    @PostMapping("/create/deck/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus createDeck(@PathVariable final Long id) {
        Game game = gameRepository.findById(id).get();

        //It already has a game deck, so the request is not accepted
        if (game.hasGameDeck()) {
            return HttpStatus.NOT_ACCEPTABLE;
        }

        Deck deck = new Deck();
        game.createDeck(deck);
        cardRepository.saveAll(deck.getCards());
        deckRepository.save(deck);
        gameRepository.save(game);

        return HttpStatus.OK;
    }

    /**
     * Add a deck for a game if it has already a game deck
     * or create a game deck if it doesn't have one
     * @param id of the game for which we want to add a deck
     * @return http status to signal that it worked
     */
    @PostMapping("/add/deck/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus addDeck(@PathVariable final Long id) {
        Game game = gameRepository.findById(id).get();
        Deck newDeck = new Deck();

        if (game.hasGameDeck()) {
            game.getGameDeck().addDeck(newDeck);
        } else {
            game.createDeck(newDeck);
        }

        cardRepository.saveAll(game.getGameDeck().getCards());
        deckRepository.save(game.getGameDeck());
        gameRepository.save(game);

        return HttpStatus.OK;
    }

    /**
     * Add a player to a game
     * @param id of the game for which we want to add a player
     * @param name of the player we want to add
     * @return http status to signal that it worked
     */
    @PostMapping("/add/player/{id}/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus addPlayer(@PathVariable final Long id, @PathVariable final String name) {
        Game game = gameRepository.findById(id).get();
        Player player = new Player(name);
        game.addPlayer(player);
        player.setGame(game);
        playerRepository.save(player);
        gameRepository.save(game);

        return HttpStatus.OK;
    }

    /**
     * Deal a card to each player belonging to the game
     * @param id of the game in which we want to deal cards
     * @return http status to signal that it worked
     */
    @PostMapping("/dealcards/{id}")
    public HttpStatus dealCards(@PathVariable final Long id) {
        Game game = gameRepository.findById(id).get();

        if (!game.getGameDeck().isEmpty()) {
            game.getGameDeck().shuffle();
            List<Player> players = game.dealCards();

            for (Player player : players) {
                cardRepository.saveAll(player.getCards());
                playerRepository.save(player);
            }
            gameRepository.save(game);
        }

        return HttpStatus.OK;
    }

    /**
     * Get the list of cards associated to a player
     * @param id of the player
     * @return String containing list of cards verbatim
     */
    @GetMapping("/cardlistplayer/{id}")
    public String getCardListOfThePlayer(@PathVariable final Long id) {
        Player player = playerRepository.findById(id).get();
        StringBuilder str = new StringBuilder("");

        //to display the holded cards of one player
        if (!player.getCards().isEmpty()) {
            for (Card card : player.getCards()) {
                str.append(card.toString() + ", ");
            }
            str.delete(str.length() - 2, str.length() - 1);
        }

        return str.toString();
    }

    /**
     *  Get all the IDs of the players belonging to a game
     * @param id of the game
     * @return String containing all the IDs of the players
     */
    @GetMapping("/get/players/{id}")
    public String getAllPlayers(@PathVariable final Long id) {
        StringBuilder str = new StringBuilder("");
        Game game = gameRepository.findById(id).get();

        //To display the id of all the players
        if (!game.getPlayers().isEmpty()) {
            for (Player player : game.getPlayers()) {
                str.append(player.getIdAsString() + ", ");
            }
            str.delete(str.length() - 2, str.length() - 1);
        }

        return str.toString();
    }

    /**
     * Get the players total value of their cards and sort them in descending order
     * @param id of the game
     * @return String containing the players of a game sorted by value in descending order
     */
    @GetMapping("/get/playersvalue/{id}")
    public String getSortedPlayersValue(@PathVariable final Long id) {
        StringBuilder str = new StringBuilder("");
        Game game = gameRepository.findById(id).get();
        Map<String, Integer> valueMap = new HashMap<>();

        for (Player player : game.getPlayers()) {
            valueMap.put(player.getName(), player.getTotalValue());
        }

        //Sort in descending order the the mapping between the player and its player's value
        Map<String, Integer> sortedValueMap = valueMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        sortedValueMap.forEach((key,value) -> str.append(key + " = " + value+ "\n\n"));

        return str.toString();
    }

    /**
     * Get the count of each suit for the remaining cards in the deck
     * @param id of the game
     * @return the count for each suit
     */
    @GetMapping("/get/suitcount/{id}")
    public String getSuitCount(@PathVariable final Long id) {
        StringBuilder str = new StringBuilder("");
        Game game = gameRepository.findById(id).get();
        game.getSuitCount().forEach((key,value) -> str.append(key + " = " + value+ "\n\n"));

        return str.toString();
    }

    /**
     * Get the card count of each specific card (suit and face value) remaining in the game deck
     * @param id of the game
     * @return containing the card count of each card
     */
    @GetMapping("/get/cardcount/{id}")
    public String getCardCount(@PathVariable final Long id) {
        StringBuilder str = new StringBuilder("");
        Game game = gameRepository.findById(id).get();
        int[][] map = game.getCardCount();

        //In order to display the card count of each card
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
               str.append(Card.helperToString(i,12-j)+ ": Count = "+ map[i][12-j] + "\n");
            }
            str.append("\n");
        }

        return str.toString();
    }

    /**
     * Shuffle the game deck of a given game
     * @param id of the game we want to shuffle
     * @return http status to signal that it worked
     */
    @PostMapping("/shuffle/{id}")
    public HttpStatus shuffleDeck(@PathVariable final Long id) {
        StringBuilder str = new StringBuilder("");
        Game game = gameRepository.findById(id).get();

        if (game.hasGameDeck()) {
            game.getGameDeck().shuffle();

            cardRepository.saveAll(game.getGameDeck().getCards());
            deckRepository.save(game.getGameDeck());
            gameRepository.save(game);
        }

        return HttpStatus.OK;
    }
}
