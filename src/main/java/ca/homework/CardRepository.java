package ca.homework;

import ca.homework.dto.Card;
import ca.homework.dto.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Card Repository
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}