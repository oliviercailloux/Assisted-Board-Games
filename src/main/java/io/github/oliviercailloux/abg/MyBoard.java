package io.github.oliviercailloux.abg;

import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveException;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
public abstract class MyBoard {

  @Id
  @GeneratedValue
  int id;
  Side sideToMove;
  String fen;

  public abstract Side getSideToMove();
  public abstract MyBoard doMoves(List<MoveEntity> moves) throws MoveException;
  public abstract String getFen();
}
