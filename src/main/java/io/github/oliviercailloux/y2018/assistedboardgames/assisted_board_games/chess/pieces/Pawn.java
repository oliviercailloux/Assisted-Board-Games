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
package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessBoard;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessPiece;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class Pawn extends AChessPiece {
    final private static int[] _posMatrix = {
        0, 0, 0, 0, 0, 0, 0, 0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5, 5, 10, 25, 25, 10, 5, 5,
        0, 0, 0, 20, 20, 0, 0, 0,
        5, -5, -10, 0, 0, -10, -5, 5,
        5, 10, 10, -20, -20, 10, 10, 5,
        0, 0, 0, 0, 0, 0, 0, 0
    };
    
    @Override
    protected int[] getPositionMatrix() {
        return _posMatrix;
    }

    public Pawn(int x, int y, boolean color) {
        super(x, y, color, IChessPiece.PAWN, 100);
    }
    
    private Pawn(Pawn other) {
        super(other);
    }
    
    @Override
    public void nextMoves(IChessBoard board) {
        int step = 1;

        if (color()) {
            step = -1;
        }
        
        if (getY() > 0 && getY() < 7) {
            if (board.pieceAtXY(getX(), getY() + step) == null) {
                board.potentialFutureMove(this, getX(), getY() + step);
                if (((color() == false && getY() == 1)
                        || (color() == true && getY() == 6))
                        && board.pieceAtXY(getX(), getY() + step * 2) == null) {
                    board.potentialFutureMove(this, getX(), getY() + step * 2);
                }
            }
        }
        
        IPositionExec functor = potentialMovesFunctor(board);
        
        if (board.pieceAtXY(getX() + 1, getY() + step) != null) {
            validStep(getX() + 1, getY() + step, functor);
        }
        if (board.pieceAtXY(getX() - 1, getY() + step) != null) {
            validStep(getX() - 1, getY() + step, functor);
        }

        //TODO en passant

        //TODO pawn promotion
    }
    
    @Override
    public boolean threatens(IChessBoard board, int x, int y) {
        
        int step = 1;
        if (color()) {
            step = -1;
        }
        
        return ((x == getX() + 1 || x == getX() - 1) && y == getY() + step);
    }

    @Override
    public IChessPiece copyPiece() {
        return new Pawn(this);
    }
    
}
