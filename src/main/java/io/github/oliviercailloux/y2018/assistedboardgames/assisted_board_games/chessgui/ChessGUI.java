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
package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chessgui;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessGame;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessMove;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessPiece;


/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class ChessGUI extends JFrame implements MouseMotionListener, MouseListener {
    private JLayeredPane _surface;
    private JPanel[][] _squares;
    private boolean _moving;
    private JPanel _chessBoard;
    private JLabel _chessPiece;
    private int _offsetX;
    private int _offsetY;
    private IChessPiece _curPiece;
    private List<ChessMove> _legalMoves;
    private ChessGame _game;
    private ChessMove _lastMove;
    private boolean _AIturn;
    private boolean _gameStarted;
    final private static String _iconPath = "../img/";
    final private static String[] _icons = { "pawn", "knight", "bishop", "rook", "queen", "king" };
    final private static String _iconExt = "png";
    
    public ChessGUI() {
        // Init chess engine (human plays white)
        _game = new ChessGame(true);
        // Swing components init -- surface for default/drag layers,
        // GridLayout panel as the board
        _surface = new JLayeredPane();
        getContentPane().add(_surface);
        Dimension boardSize = new Dimension(720, 720);
        _surface.setPreferredSize(boardSize);
        _surface.addMouseMotionListener(this);
        _surface.addMouseListener(this);
        _chessBoard = new JPanel();
        _surface.add(_chessBoard, JLayeredPane.DEFAULT_LAYER);
        _chessBoard.setLayout(new GridLayout(8, 8));
        _chessBoard.setPreferredSize(boardSize);
        _chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        // Init 8 * 8 squares
        _squares = new JPanel[8][8];
        for (int i = 0; i < 64; i++) {
            JPanel sq = new JPanel(new BorderLayout());
            _chessBoard.add(sq);
            _squares[i % 8][i / 8] = sq;
        }
        defaultSqColors();
        
        defaultPieces();
        
        final ChessGUI self = this;
        
        // Timed check to trigger AI play
        Timer _t = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                self.checkAIPlay();
            }
        });
        _t.start();
    }
    
    private void defaultPieces() {
        // Draw all the pieces existing in the game engine
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                IChessPiece p = _game.pieceAtXY(i, j);

                if (p != null) {
                    loadPiece(p.getType(), p.color(), i, j);
                }
            }
        }
    }
    
    private void loadPiece(int type, boolean color, int x, int y) {
        // recompose icon path with color naming convention + piece name table + extension
        String icon = _iconPath + (color ? "w" : "b") + _icons[type] + "." + _iconExt;
        
        // Add piece to square
        _squares[x][y].add(new JLabel(new ImageIcon(icon)));
    }
    
    private void defaultSqColors() {
        // Reset default square colors
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j % 2 == 0) {
                    _squares[i][j].setBackground(i % 2 == 0 ? Color.WHITE : Color.GRAY);
                } else {
                    _squares[i][j].setBackground(i % 2 == 0 ? Color.GRAY : Color.WHITE);
                }
            }
        }
    }

    private void colorSquares(List<ChessMove> moves, Color col) {
        for (ChessMove move : moves) {
            colorOneSq(move.getDestX(), move.getDestY(), col);
        }
    }

    private void colorOneSq(int x, int y, Color col) {
        _squares[x][y].setBackground(col);
    }
    
    private void checkKingStatus() {
        // Check if Kings are in check and color them red
        if (_game.getKing(_game.AIColor()).isThreatened(_game)) {
            colorOneSq(_game.getKing(_game.AIColor()).getX(),
                    _game.getKing(_game.AIColor()).getY(),
                    Color.red);
        }
        if (_game.getKing(!_game.AIColor()).isThreatened(_game)) {
            colorOneSq(_game.getKing(!_game.AIColor()).getX(),
                    _game.getKing(!_game.AIColor()).getY(),
                    Color.red);
        }
    }
    
    private void highlightLastMove() {
        // Color the last move as a yellow square
        if (_lastMove != null) {
            colorOneSq(_lastMove.getDestX(), _lastMove.getDestY(), Color.YELLOW);
        }
    }
    
    private void checkEndGame() {
        // Make sure the game is started to prevent message from appearing at the start
        if (_gameStarted) {
            if (_lastMove == null) {
                JOptionPane.showMessageDialog(this, "Congratulations, you have won!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
            } else if (!_game.humanCanMove()) {
                JOptionPane.showMessageDialog(this, "You have lost... try again!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                return;
            }
            // Change state so the message does not keep coming up
            _gameStarted = false;
        }
    }
    
    private void lockMoves() {
        _moving = true;
    }
    
    private void unlockMoves() {
        _moving = false;
    }
    
    public void mousePressed(MouseEvent event) {
        if (_moving) {
            // A move is currently being done, return to prevent races
            return;
        }
        
        _chessPiece = null;

        lockMoves();
        
        Component c = _chessBoard.findComponentAt(event.getX(), event.getY());

        if (c instanceof JPanel) {
            // No chess piece at event coordinates
            return;
        }
        
        int pX, pY;
        pX = event.getX() / _squares[0][0].getWidth();
        pY = event.getY() / _squares[0][0].getHeight();
        // Acquire game instance of the selected piece
        IChessPiece p = _game.pieceAtXY(pX, pY);
        
        if (p.color() == _game.AIColor()) {
            // Do not move the AI pieces
            return;
        } 
        _curPiece = p;
        _legalMoves = _curPiece.getLegalMoves(_game);
        
        Point parentLoc = c.getParent().getLocation();
        _offsetX = parentLoc.x - event.getX();
        _offsetY = parentLoc.y - event.getY();
        _chessPiece = (JLabel) c;
        // Calculate offsets to drag piece from the point where user clicked
        _chessPiece.setLocation(event.getX() + _offsetX, event.getY() + _offsetY);
        _chessPiece.setSize(_chessPiece.getWidth(), _chessPiece.getHeight());
        // Move to drag layer
        _surface.add(_chessPiece, JLayeredPane.DRAG_LAYER);
        
        // Highlight the squares where it is legal to move this piece
        colorSquares(_legalMoves, Color.BLUE);
    }
    
    public void mouseDragged(MouseEvent event) {
        if (_chessPiece == null) {
            // No piece is being moved
            return;
        }
        
        // Update the coords (with offset)
        _chessPiece.setLocation(event.getX() + _offsetX, event.getY() + _offsetY);
    }
    
    private int toGUICoords(int x) {
        // Transform game square coords to window point coords
        return (x * _squares[0][0].getWidth() + _squares[0][0].getWidth() / 2);
    }
    
    public void mouseReleased(MouseEvent event) {
        if (_chessPiece == null) {
            // No piece being moved
            unlockMoves();
            return;
        }
        
        defaultSqColors();
        
        // Keep track of whether the user actually performed a move
        boolean moved = false;

        int pX, pY;
        pX = event.getX() / _squares[0][0].getWidth();
        pY = event.getY() / _squares[0][0].getHeight();
        if (pX != _curPiece.getX() || pY != _curPiece.getY()) {
            for (ChessMove move : _legalMoves) {
                if (move.getDestX() == pX && move.getDestY() == pY) {
                    // Update game engine and references
                    _game.doMove(move);
                    _curPiece = _game.pieceAtXY(pX, pY);
                    moved = true;
                    _lastMove = move;
                    break;
                }
            }
        }
        
        GUIExecMove(_chessPiece, _curPiece.getX(), _curPiece.getY());
        
        if (!moved) {
            // No move, piece was returned to its original coord
            return;
        }
        
        _AIturn = true;
    }

    private void makeAIPlay() {
        lockMoves();
        _gameStarted = true;
        // Ask AI solver to choose a move
        ChessMove AIMove = _game.AIMove();
        _lastMove = AIMove;
        _AIturn = false;

        if (AIMove == null) {
            // AI found no legal move
            return;
        }

        defaultSqColors();
        
        Component c = _chessBoard.findComponentAt(toGUICoords(AIMove.getOrigX()),
                toGUICoords(AIMove.getOrigY()));

        // Bad anyway? No visual piece found where AI instructed
        if (c instanceof JPanel) {
            return;
        }

        GUIExecMove(c, AIMove.getDestX(), AIMove.getDestY());
    }
    
    public void checkAIPlay() {
        if (_AIturn) {
            makeAIPlay();
        } else {
            checkEndGame();
        }
    }

    private void GUIExecMove(Component toMove, int x, int y) {
        toMove.setVisible(false);

        Component dest = _chessBoard.findComponentAt(toGUICoords(x), toGUICoords(y));

        if (dest instanceof JLabel) {
            // dest is another piece, get parent (square) and remove all children
            Container parent = dest.getParent();
            while (parent.getComponentCount() > 0) {
                parent.remove(0);
            }
            parent.add(toMove);
        } else {
            // dest is a square
            ((Container) dest).add(toMove);
        }

        toMove.setVisible(true);
        highlightLastMove();
        checkKingStatus();
        unlockMoves();
    }

    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseMoved(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public static void main(String[] args) {
        JFrame gameWindow = new ChessGUI();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.pack();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
    }
}
