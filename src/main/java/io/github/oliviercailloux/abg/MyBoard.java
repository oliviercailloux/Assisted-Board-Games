package io.github.oliviercailloux.abg;

import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveException;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class MyBoard {

	@Id
	@GeneratedValue
	int id;

	protected abstract ChessBoard doMoves(List<MoveEntity> moves) throws MoveException;

	protected abstract Side getSideToMove();
}
