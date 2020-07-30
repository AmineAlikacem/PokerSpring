package ca.homework;

import ca.homework.dto.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Game Repository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}