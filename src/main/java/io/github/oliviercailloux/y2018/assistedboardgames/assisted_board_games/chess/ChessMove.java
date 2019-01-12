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

import org.json.simple.JSONObject;


/**
 *
 * @author Thomas Schaffer <thomas.schaffer@epitech.eu>
 */
public class ChessMove {

    int _origX, _origY;
    private int _destX, _destY;
    
    public ChessMove(int origX, int origY, int destX, int destY) {
        _origX = origX;
        _origY = origY;
        _destX = destX;
        _destY = destY;
    }
    
    public int getOrigX() {
        return _origX;
    }
    
    public int getOrigY() {
        return _origY;
    }

    public int getDestX() {
        return _destX;
    }

    public int getDestY() {
        return _destY;
    }
    
    /**Megan Brassard feature "ChessMoveTXT **/
    public static ChessMove getMove(ChessState state1 , ChessState state2) {
    	IChessPiece[][] board1 = state1.get_board();
    	IChessPiece[][] board2 = state2.get_board();
    	int origX = -1;
    	int origY = -1;
    	int destX = -1;
    	int destY = -1; 
    	
    	for (int i =0;i<board1.length ; i++) {
    		for (int j =0;j< board1[0].length; j++) {
    				if (board1[i][j] == null && board2[i][j] != null) {
    					
    					destX=i;
    					destY=j;
    				}
    				if (board1[i][j] != null && board2[i][j] == null) {
    					origX=i;
    					origY=j;
    					
    				}
    			}
    		}
    	if (origX != -1)
    		return new ChessMove (origX, origY, destX, destY);
    	else return null;
    }
    /**Megan Brassard feature "ChessMoveTXT **/
    @SuppressWarnings("unchecked")
	public JSONObject encode ( ) {
    	JSONObject j = new JSONObject();
		j.put("OrigX", getOrigX());
		j.put("OrigY", getOrigY());
    	j.put("DestX", getDestX());
    	j.put("DestY", getDestY());	
    	return j; 
    	
    }
    /**Megan Brassard feature "ChessMoveTXT **/
    public static ChessMove decode (JSONObject json) {	
		int origx=(int)json.get("OrigX");
		int origy=(int)json.get("OrigY");
		int destx=(int)json.get("DestX");
		int desty=(int)json.get("DestY");
	    return new ChessMove ( origx, origy, destx, desty);
   	
    }
    
    @Override
    public boolean equals (Object o) {
    	if ( o instanceof ChessMove) {
    		ChessMove move=((ChessMove)o);
    		return move.getOrigX()==_origX 
    				&&move.getOrigY()==_origY
    						&&move.getDestX()==_destX
    								&&move.getDestY()==_destY;
    	}
    	return false;
    }
  
}
