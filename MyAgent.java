import java.util.Random;

public class MyAgent extends Agent // Extends abstract class Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     *
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed) // Construct MyAgent class
    {
        super(game, iAmRed); // Extends variables from Agent class
        r = new Random(); // generates new Random variable r
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     *
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     *
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     *
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     *
     */
 
    public void move()
    {
    	{
    		int columnIndex = canWin(!iAmRed);
    		if (columnIndex >= 0)
    		{
    			moveOnColumn(columnIndex);
    		}
    		else
    		{
    			columnIndex = canConnectTheMost(iAmRed);
    			if (columnIndex >= 0)
    			{
    				moveOnColumn(columnIndex);
    			}
    			else
    			{
    				columnIndex = canWin(iAmRed);
    				if (columnIndex >= 0)
    				{
    					moveOnColumn(columnIndex);
    				}
    				else
    				{
    					moveFromMiddle();
    				}
    			}
    		}
    	}
    }
    
    /**
     * Checks which column/row combination would allow for the most possible moves
     * @param redPlayer Determine if player or opponent
     * @return column that allows most connections possible
    */


    public int canConnectTheMost(boolean redPlayer)
    {
        int numOfColumns = myGame.getColumnCount(); // Total columns on board
        int winningColumn = -1; // initialize winningColumn (check if method enters loop)
        int maxConnects = 0; // initialize maxConnects
        for (int i = 0; i <= numOfColumns/2; i++) // for i is on left side of board
        {
            int slotIndex = getLowestEmptyIndex(myGame.getColumn(numOfColumns/2 + i));
            if (slotIndex != -1)  // if column is not full
            {
                int connects = canConnectUpTo(numOfColumns/2 + i, slotIndex, redPlayer);
                if (connects > maxConnects)
                {
                    maxConnects = connects;
                    winningColumn = numOfColumns/2 + i;
                }
            }
            if (i > 0)
            {
                slotIndex = getLowestEmptyIndex(myGame.getColumn(numOfColumns/2 - i));
                if (slotIndex != -1)
                {
                    int connects = canConnectUpTo(numOfColumns/2 - i, slotIndex, redPlayer);
                    if (connects > maxConnects)
                    {
                        maxConnects = connects;
                        winningColumn = numOfColumns/2 - i;
                    }
                }
            }
        }
        return winningColumn;
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     *
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     *
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     *
     * @return a random valid move.
     */
     public int randomMove()
     {
         int i = r.nextInt(myGame.getColumnCount());
         while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
         {
             i = r.nextInt(myGame.getColumnCount());
         }
         return i;
     }

    /**
     * Determines what move would allow agent to win - allows move method to forego two separate methods (iCanWin & theyCanWin)
     * Uses different parameters for iCanWin and theyCanWin
     * @param redPlayer determine whether or not is player or is opponent
     * @return the column that would be a winning move
    */

    public int canWin(boolean redPlayer)
    {
        //Find out myColor; this.iAmREd
        int winningColumn = -1;
        for (int i = 0; i < myGame.getColumnCount(); i++)
        {
            int slotIndex = getLowestEmptyIndex(myGame.getColumn(i));
            if ( slotIndex != -1 )  // if column is not full
                {
                    if ( possibleSlot(i, slotIndex, redPlayer) )  // if we can connect 4 slots

                    {
                        winningColumn = i;
                        break;
                    }
                }
        }
        return winningColumn;
    }

    /**
     * Checks all slots in the field that would allow a piece to be placed 4 in a row
     * If not 4 in a row, checks all other options
     * @param columnIndex Get current column
     * @param slotIndex Get current row (slot)
     * @param redPlayer Determine if player or opponent
     * @return possible winning slot @ current position
    */

    public boolean possibleSlot(int columnIndex, int slotIndex, boolean redPlayer)
    {
        int numColumns = myGame.getColumnCount();
        int numRows = myGame.getRowCount();

        if ( !((0 <= columnIndex && columnIndex < numColumns) && (0 <= slotIndex && slotIndex < numRows)) ) // Check if slot is on board
            return false;

        // Check horizontally and left side
        int numConnectedslots = 1;
        for (int x = columnIndex - 1; 0 <= x; x--) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(slotIndex);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
        }
        // Continue to check horizontally and right side
        for (int x = columnIndex + 1; x < numColumns; x++) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(slotIndex);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
        }

        // Check vertically and down side
        numConnectedslots = 1;
        for (int y = slotIndex + 1; y < numRows; y++) {
            Connect4Slot slot = myGame.getColumn(columnIndex).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
        }
        // Continue to check vertically and up side
        for (int y = slotIndex - 1; 0 <= y; y--) {
            Connect4Slot slot = myGame.getColumn(columnIndex).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
        }

        // Check diagonally left side and up side
        numConnectedslots = 1;
        int x = columnIndex - 1;
        int y = slotIndex - 1;
        while (0 <= x && 0 <= y) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
            x--;
            y--;
        }
        // Continue to check diagonally right side and down side
        x = columnIndex + 1;
        y = slotIndex + 1;
        while (x < numColumns && y < numRows) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
            x++;
            y++;
        }

        // Check diagonally left side and down side
        numConnectedslots = 1;
        x = columnIndex - 1;
        y = slotIndex + 1;
        while (0 <= x && y < numRows) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
            x--;
            y++;
        }
        // Continue to check diagonally right side and up side
        x = columnIndex + 1;
        y = slotIndex - 1;
        while (x < numColumns && 0 <= y) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                numConnectedslots ++;
                if (numConnectedslots == 4) {
                    return true;
                }
            }
            else break;
            x++;
            y--;
        }

        return false;
    }
	 /** 
     * Returns the maximum out of all the possible connections, given the slot at 
     * the given column.
     * @param columnIndex index of the column the slot belongs to.
     * @param slotIndex index of the given slot 
     * @return the maximum number.
     */

    public int canConnectUpTo(int columnIndex, int slotIndex, boolean redPlayer)
    {
        int numColumns = myGame.getColumnCount();
        int numRows = myGame.getRowCount();
        int maxConnects = 0;

        if ( !((0 <= columnIndex && columnIndex < numColumns) && (0 <= slotIndex && slotIndex < numRows)) )
            return maxConnects;

        // Check horizonatally and left side
        int nbrConnectedTokens = 0;
        for (int x = columnIndex - 1; 0 <= x; x--) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(slotIndex);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
        }
        // Continue checking horizonatally but right side
        for (int x = columnIndex + 1; x < numColumns; x++) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(slotIndex);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
                }
            else break;
        }
        maxConnects = (nbrConnectedTokens > maxConnects) ? nbrConnectedTokens : maxConnects;

        // Check vertically and down side
        nbrConnectedTokens = 0;
        for (int y = slotIndex + 1; y < numRows; y++) {
            Connect4Slot slot = myGame.getColumn(columnIndex).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
        }
        // Continue checkinh vertically but up side
        for (int y = slotIndex - 1; 0 <= y; y--) {
            Connect4Slot slot = myGame.getColumn(columnIndex).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
        }
        maxConnects = (nbrConnectedTokens > maxConnects) ? nbrConnectedTokens : maxConnects;

        // Chek diagonally left side and up side
        nbrConnectedTokens = 0;
        int x = columnIndex - 1;
        int y = slotIndex - 1;
        while (0 <= x && 0 <= y) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
            x--;
            y--;
        }
        // Continue checking diagonally but right side and down side
        x = columnIndex + 1;
        y = slotIndex + 1;
        while (x < numColumns && y < numRows) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
            x++;
            y++;
        }
        maxConnects = (nbrConnectedTokens > maxConnects) ? nbrConnectedTokens : maxConnects;

        // Check diagonally left side and down side
        nbrConnectedTokens = 0;
        x = columnIndex - 1;
        y = slotIndex + 1;
        while (0 <= x && y < numRows) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
            x--;
            y++;
        }
        // Continue checking diagonally but right side and up side
        x = columnIndex + 1;
        y = slotIndex - 1;
        while (x < numColumns && 0 <= y) {
            Connect4Slot slot = myGame.getColumn(x).getSlot(y);
            if (slot.getIsFilled() && slot.getIsRed() == redPlayer) {
                nbrConnectedTokens ++;
            }
            else break;
            x++;
            y--;
        }
        maxConnects = (nbrConnectedTokens > maxConnects) ? nbrConnectedTokens : maxConnects;

        return maxConnects;
    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName() // return string
    {
        return "My Agent"; // returns Name of agent
    }

    /**
     * Attempts to drop a token from the middle column of the game panel.
     * If all the columns are full, nothing will change.
     */

    public void moveFromMiddle()
    {
    int numColumns = myGame.getColumnCount();
    for (int i = 0; i <= numColumns/2; i++) // while i <= middle column of board; Move from left side of board
      {
          int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(numColumns/2 + i)); // Lowest empty row on right half of board
          if (lowestEmptySlotIndex != -1) // If getLowestEmptyIndex did not enter for loop, move on column to the right of middle
          {
              moveOnColumn(numColumns/2 + i);
              break; // exit loop
          }
          if (i > 0) // If it's inside left side of board
          {
              lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(numColumns/2 - i)); // Lowest empty row on left half of board
              if (lowestEmptySlotIndex != -1) // If getLowestEmptyIndex did not enter fer loop, move on column to left of middle
              {
                  moveOnColumn(numColumns/2 - i); // move on column to left of middle
                  break; // exit loop
              }
          }
      }
  }
}
