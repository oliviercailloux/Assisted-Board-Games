/*
 * The MIT License
 *
 * Copyright 2013 Thomas Schaffer <thomas.schaffer@epitech.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess;
import java.util.List;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public interface IChessPiece {
    public boolean color();
    
    public int getX();
    public int getY();
    
    public IChessPiece setX(int x);
    public IChessPiece setY(int y);
    
    public int getType();
    public int getValue();

    public void nextMoves(IChessBoard board);
    public List<ChessMove> getLegalMoves(IChessBoard board);
    
    public void addLegalMove(ChessMove move);
    
    public boolean hasMoved();
    public boolean isThreatened(IChessBoard board);
    
    public boolean threatens(IChessBoard board, int x, int y);
    
    public IChessPiece copyPiece();
    public boolean equals(IChessPiece other);
    
    final public static int PAWN = 0;
    final public static int KNIGHT = 1;
    final public static int BISHOP = 2;
    final public static int ROOK = 3;
    final public static int QUEEN = 4;
    final public static int KING = 5;
    
}
