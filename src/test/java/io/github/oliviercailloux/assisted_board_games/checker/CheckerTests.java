package io.github.oliviercailloux.assisted_board_games.checker;
/**
 * @author Dahuiss and Marina
 *
 */
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.oliviercailloux.assisted_board_games.checkers.Square;

class CheckerTests {

	@Test
	void test() {
		Square s1 = Square.givenCoordonate(4, 5);
		boolean actual = s1.isWhite();
		Assertions.assertEquals(false, actual);
	}

}
