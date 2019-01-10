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
public class Knight extends AChessPiece {
    final private static int[] _posMatrix = {
        -50, -40, -30, -30, -30, -30, -40, -50,
        -40, -20, 0, 0, 0, 0, -20, -40,
        -30, 0, 10, 15, 15, 10, 0, -30,
        -30, 5, 15, 20, 20, 15, 5, -30,
        -30, 0, 15, 20, 20, 15, 0, -30,
        -30, 5, 10, 15, 15, 10, 5, -30,
        -40, -20, 0, 5, 5, 0, -20, -40,
        -50, -40, -30, -30, -30, -30, -40, -50
    };
    
    @Override
    protected int[] getPositionMatrix() {
        return _posMatrix;
    }

    public Knight(int x, int y, boolean color) {
        super(x, y, color, IChessPiece.KNIGHT, 320);
    }
    
    private Knight(Knight other) {
        super(other);
    }

    private boolean LStep(IPositionExec functor) {
        return (validStep(getX() + 1, getY() + 2, functor)
        || validStep(getX() - 1, getY() + 2, functor)
        || validStep(getX() - 2, getY() + 1, functor)
        || validStep(getX() - 2, getY() - 1, functor)
        || validStep(getX() + 1, getY() - 2, functor)
        || validStep(getX() - 1, getY() - 2, functor)
        || validStep(getX() + 2, getY() - 1, functor)
        || validStep(getX() + 2, getY() + 1, functor));
    }

    @Override
    public void nextMoves(IChessBoard board) {
        LStep(potentialMovesFunctor(board));
    }
    
    @Override
    public boolean threatens(IChessBoard board, int x, int y) {
        return LStep(threatenFunctor(board, x, y));
    }

    @Override
    public IChessPiece copyPiece() {
        return new Knight(this);
    }
}
