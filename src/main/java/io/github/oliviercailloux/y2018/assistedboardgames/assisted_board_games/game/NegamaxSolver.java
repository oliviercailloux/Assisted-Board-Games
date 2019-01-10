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
package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.game;

import java.util.List;

/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class NegamaxSolver implements IGameTreeSolver {
    
    public int solve(IGameTree node, int depth) {
        return negamax(node, depth, -1000000, 1000000, 1);
    }
    
    private int negamax(IGameTree node, int depth, int alpha, int beta, int color) {
        if (depth <= 0) {
            return color * node.evalNodeValue();
        }
        
        List<IGameTree> moves = node.nextPly();
        if (moves == null) {
            // end game found, arbitrary large score
            return -color * 100000;
        }
        
        for (IGameTree move : moves) {
            // Zero sum game -- score == -opponent_score
            int score = -negamax(move, depth - 1, -beta, -alpha, -color);
            move.setNodeValue(score);

            if (score > alpha) {
                alpha = score;
            }
            if (beta <= alpha) {
                // Alpha-beta pruning -- current branch is either too good
                // or bad to be picked by parent, discard it
                break;
            }
        }

        return alpha;
    }
}
