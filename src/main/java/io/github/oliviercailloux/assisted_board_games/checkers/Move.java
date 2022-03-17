package io.github.oliviercailloux.assisted_board_games.checkers;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * <p>
 * The class is used to describe the movement of piece during a checkers game.
 * 
 */

public class Move {

  /**
   * <p>
   * Square is defined using row and column index, for example: 12
   * <p>
   * to: where the piece is being promoted to from: where the piece is being promoted from
   * <p>
   * A piece is defined by it's type and by it's color.
   * 
   */
  Square from;
  Square to;
  Piece promoted;

  /**
   * <p>
   * To initialize a move without any given piece, to and from
   */
  Move() {
    this.from = Square.given(0);
    this.to = Square.given(0);
    this.promoted = Piece.given(null, null);
  }

  /**
   * <p>
   * Checks that the start and finish squares are not null if the piece is null, then a new piece is
   * initialized
   */
  Move(Square to, Square from, Piece promoted) {
    checkArgument(from != null);
    checkArgument(to != null);
    this.to = to;
    this.from = from;
    this.promoted = promoted == null ? Piece.given(null, null) : promoted;
  }

  public static Move empty() {
    return new Move();
  }

  /**
   * <p>
   * Creates a Move using a piece, where and from it's being promoted
   */
  public static Move given(Square to, Square from, Piece promoted) {
    checkArgument(from != null);
    checkArgument(to != null);
    checkArgument(promoted != null);
    return new Move(to, from, promoted);
  }

  public Square getFrom() {
    return from;
  }

  public Square getTo() {
    return to;
  }

  public Piece getPromoted() {
    return promoted;
  }
}
