package ca.homework;

import ca.homework.dto.Deck;
import ca.homework.dto.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Deck Repository
 */
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

}