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



import java.util.LinkedList;
import java.util.List;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessMove;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessBoard;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessPiece;



/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public abstract class AChessPiece implements IChessPiece {
    private int _x;
    private int _y;
    private boolean _color;
    
    
    private int _type;
    private boolean _moved;
    protected List<ChessMove> _legalMoves;
    private int _value;
    
    protected AChessPiece(int x, int y, boolean color, int type, int value) {
        _x = x;
        _y = y;
        _color = color;
        _type = type;
        _moved = false;
        _value = value;
    }
    
    protected AChessPiece(IChessPiece other) {
        _x = other.getX();
        _y = other.getY();
        _color = other.color();
        _type = other.getType();
        _moved = other.hasMoved();
        _value = other.getValue();
    }
    
    protected boolean validStep(int x, int y, IPositionExec functor) {
        // If the given coords are valid, call the functor with them
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return (functor.exec(x, y) == IPositionExec.TRUE);
        }
        return false;
    }

    private int stepThroughBoard(int xStep, int yStep, int n, IPositionExec functor) {
        // Step through the board with a pattern determined by the parameter x/y steps
        // For each position, call the functor with the coords until it returns TRUE or STOP
        for (int y = getY() + yStep, x = getX() + xStep;
                y >= 0 && y < 8 && x >= 0 && x < 8 && n > 0;
                y += yStep, x += xStep, n--) {
            int ret = functor.exec(x, y);
            if (ret != IPositionExec.FALSE) {
                return ret;
            }
        }
        return IPositionExec.FALSE;
    }
    
    protected IPositionExec potentialMovesFunctor(final IChessBoard board) {
        // Generate a functor to be passed to the above functions, to notify
        // the chess board of each potential future move
        final IChessPiece self = this;
        return new IPositionExec() {
            @Override
            public int exec(int x, int y) {
                if (x == self.getX() && y == self.getY()) {
                    return IPositionExec.FALSE;
                }
                IChessPiece p = board.pieceAtXY(x, y);
                if (p == null || p.color() != self.color()) {
                    // No piece or capturable piece, legal move
                    board.potentialFutureMove(self, x, y);
                }
                if (p != null) {
                    // Hit a piece, future moves are blocked
                    return IPositionExec.STOP;
                }
                return IPositionExec.FALSE;
            }
        };
    }
    
    protected IPositionExec threatenFunctor(final IChessBoard board, final int threatX, final int threatY) {
        // Generate a functor to be passed to the above functions, to check
        // if the given coords are threatened by this piece
        return new IPositionExec() {
            @Override
            public int exec(int x, int y) {
                if (x == threatX && y == threatY) {
                    // Current move being considered matches the coords to be checked,
                    // the position is indeed threatened
                    return IPositionExec.TRUE;
                }
                if (board.pieceAtXY(x, y) != null) {
                    // Hit a piece, future moves are blocked
                    return IPositionExec.STOP;
                }
                return IPositionExec.FALSE;
            }
        };
    }
    
    protected boolean stepDiagonally(int n, IPositionExec functor) {
        // Pass parameters to the step function for a diagonal progression with any functor
        if (stepThroughBoard(1, 1, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(1, -1, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(-1, 1, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(-1, -1, n, functor) == IPositionExec.TRUE) {
            return true;
        }
        return false;
    }

    protected boolean stepLine(int n, IPositionExec functor) {
        // Pass parameters to the step function for a line progression with any functor
        if (stepThroughBoard(1, 0, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(-1, 0, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(0, 1, n, functor) == IPositionExec.TRUE
                || stepThroughBoard(0, -1, n, functor) == IPositionExec.TRUE) {
            return true;
        }
        return false;
    }
    
    protected void moveNDiagonally(IChessBoard board, int n) {
        // Diagonal progression with a functor checking future moves
        stepDiagonally(n, potentialMovesFunctor(board));
    }
    
    protected void moveNLine(IChessBoard board, int n) {
        // Line progression with a functor checking future moves
        stepLine(n, potentialMovesFunctor(board));
    }
    
    protected boolean threatenNDiagonally(IChessBoard board, int n, int x, int y) {
        // Diagonal progression with a functor checking coords threat
        return stepDiagonally(n, threatenFunctor(board, x, y));
    }
    
    protected boolean threatenNLine(IChessBoard board, int n, int x, int y) {
        // Line progression with a functor checking coords threat
        return stepLine(n, threatenFunctor(board, x, y));
    }
    
    @Override
    public int getX() {
        return _x;
    }
    
    @Override
    public int getY() {
        return _y;
    }
    
    @Override
    public boolean color() {
        return _color;
    }
    
    @Override
    public int getType() {
        return _type;
    }
    
    @Override
    public int getValue() {
        // Return material piece value + estimated position value
        return _value + getPositionValue();
    }
    
    @Override
    public IChessPiece setX(int x) {
        _x = x;
        _moved = true;
        return this;
    }
    
    @Override
    public IChessPiece setY(int y) {
        _y = y;
        _moved = true;
        return this;
    }
    
    protected IChessPiece setType(int type) {
        _type = type;
        return this;
    }

    @Override
    public boolean hasMoved() {
        // Keep track of whether a piece has moved (castling, etc.)
        return _moved;
    }
    
    @Override
    public boolean isThreatened(IChessBoard board) {
        // Ask the chess board if the piece's position is safe
        return (!board.safeSquare(color(), getX(), getY()));
    }
    

    @Override
    public boolean equals(IChessPiece other) {
        return (color() == other.color()
                && getX() == other.getX()
                && getY() == other.getY()
                && getType() == other.getType()
                && hasMoved() == other.hasMoved());
    }
    
    @Override
    public void addLegalMove(ChessMove move) {
        if (_legalMoves != null) {
            _legalMoves.add(move);
        }
    }
    
    @Override
    public List<ChessMove> getLegalMoves(IChessBoard board) {
        // Collect legal moves list
        List<ChessMove> list = new LinkedList<>();
        _legalMoves = list;
        nextMoves(board);
        _legalMoves = null;
        return list;
    }
    
    public int getPositionValue() {
        // Get piece's position matrix to compute its current position value
        int[] matrix = getPositionMatrix();
        
        int x, y;
        x = (color() ? 7 - getX() : getX());
        y = (color() ? 7 - getY() : getY());
        return matrix[y * 8 + x];
    }
    
    // Concrete pieces will implement a position matrix + accessor
    abstract int[] getPositionMatrix();

	public void nextMoves(IChessBoard board) {	
	}

	public boolean threatens(IChessBoard board, int x, int y) {
		return false;
	}

	public IChessPiece copyPiece() {
		return null;
	}
}
