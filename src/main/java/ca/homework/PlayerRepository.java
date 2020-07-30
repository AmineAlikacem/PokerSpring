package ca.homework;

import ca.homework.dto.Game;
import ca.homework.dto.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Player repository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}