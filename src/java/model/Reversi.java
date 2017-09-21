/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**

 @author Peer-Anders
 */
public class Reversi implements Serializable
{

    private final int COLUMN_SIZE = 8; //sets column size to 8
    private final int ROW_SIZE = 8; //sets row size to 8
    private final int GRID_SIZE = ROW_SIZE * COLUMN_SIZE; //sets the grid size based on row and column size
    private final int TOP_LEFT = -ROW_SIZE - 1; //the upper left diagonal is always minus the row size - 1 of the given index
    private final int TOP = -ROW_SIZE; //the cell directly above is always minus the row size of the given index
    private final int TOP_RIGHT = -ROW_SIZE + 1; //the upper right diagonal is always minus the row size + 1 of the given index
    private final int LEFT = -1; //the left adjacent cell is always -1 of the given index
    private final int RIGHT = +1; //the right adjacent cell is alway +1 of the given index
    private final int BOTTOM_LEFT = ROW_SIZE - 1; //the lower left diagonal is always plus the row size - 1 of the given index
    private final int BOTTOM = ROW_SIZE; //the cell directly below is always plus the row size of the given index
    private final int BOTTOM_RIGHT = ROW_SIZE + 1; //the lower right cell is alway plus the row size + 1 of the given index
    
    private int blackScore; //for holding the black score
    private int whiteScore; //for holding the white score
    private int turn; //for holding the current turn
    private int passes; //for holding the number of passes
    private Disk[] board; //for holding the board array

    /**
     Constructor calls the reset method to initialize a game
     */
    public Reversi()
    {
        reset();
    }

    /**
     Enumeration of black and white disks
     */
    public enum Disk
    {
        BLACK, WHITE;
    }

    /**
     Gets the current turn for the game

     @return The current turn
     */
    public int getTurn()
    {
        return turn;
    }

    /**
     Gets a board for the game

     @return The game board
     */
    public Disk[] getBoard()
    {
        return board.clone();
    }

    /**
     Gets the current score for the black player

     @return The black player's score
     */
    public int getBlackScore()
    {
        return blackScore;
    }

    /**
     Gets the current score for the white player

     @return The white player's score
     */
    public int getWhiteScore()
    {
        return whiteScore;
    }

    /**
     Sets the scores for each player
     */
    public void setScores()
    {
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < GRID_SIZE; i++) //itterate through the board array
        {
            if (board[i] != null && board[i] == Disk.BLACK) //if current space at i is black
            {
                blackScore++;
            }
            else if (board[i] != null && board[i] == Disk.WHITE) //if current space at i is white
            {
                whiteScore++;
            }
        }
    }

    /**
     Quits the current game
     */
    public void quitGame()
    {
        passes = 2; //set the number of passes to 2 to trigger game over
    }

    /**
     Checks to see if the game is over

     @return True if the game is over, false if not
     */
    public boolean isGameOver()
    {
        boolean gameOver = false;
        int filled = 0;

        if (passes == 2) //if both players have passed the game ends
        {
            gameOver = true;
        }
        else
        {
            for (int i = 0; i < GRID_SIZE; i++) //iterate through the array
            {
                if (board[i] != null) //increment filled if the space has a disk on it
                {
                    filled++;
                }
            }
            if (filled == GRID_SIZE) //if all spaces have been filled then the game ends
            {
                gameOver = true;
            }
        }
        return gameOver;
    }

    /**
     Initializes and resets the board.
     */
    public final void reset()
    {
        blackScore = 2;
        whiteScore = 2;
        turn = 0;
        passes = 0;
        board = new Disk[GRID_SIZE];
        board[27] = Disk.WHITE;
        board[28] = Disk.BLACK;
        board[36] = Disk.WHITE;
        board[35] = Disk.BLACK;
    }

    /**
    Places a disk in the cell specified by the player input
     @param cellIndex The space that the player would like to place a disk
     @return True if the disk was placed, false if the space was an invalid move
     */
    public boolean placeDisk(int cellIndex)
    {
        boolean placed = false;
        if (isValid(cellIndex) && board[cellIndex] == null) //if move is valid and space is not occupied
        {
            board[cellIndex] = getCurrentPlayer(); //place disk on board
            flipValid(cellIndex); //flip over disks that are valid moves
            placed = true; 
            setScores(); //set the scores
            if (!isGameOver()) //if the game is not over increment the turn
            {
                turn++;
            }
        }
        if (placed == true && passes == 1) // reset passes if one player passed  
        {                                  // but the second player was able to play
            passes = 0;
        }
        return placed;
    }

    /**
    Gets the current player based on the current turn
     @return The current player
     */
    public Disk getCurrentPlayer()
    {
        return turn % 2 == 0 ? Disk.BLACK : Disk.WHITE;
    }

    /**
    Passes play over to the next player
     */
    public void pass()
    {
        turn++; //increment turn to the next player so getCurrentPlayer() method returns correctly
        passes++; //increment passes
    }

    //This method flips over the any valid moves
    private void flipValid(int cellIndex)
    {

        int row = cellIndex / COLUMN_SIZE; //gets the row number
        int column = cellIndex % COLUMN_SIZE; //gets the column number

        if (row == 0 && column == 0) //if the cell is the top left corner
        {
            if (checkLine(cellIndex, RIGHT)) //check if alignment is valid (right side in this case) 
            {                                
                flip(cellIndex, RIGHT); //call flip alignment if its valid
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
            if (checkLine(cellIndex, BOTTOM_RIGHT))
            {
                flip(cellIndex, BOTTOM_RIGHT);
            }
        }
        else if (row == 0 && column == (COLUMN_SIZE - 1)) //if cell is in top right corner
        {

            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }

            if (checkLine(cellIndex, BOTTOM_LEFT))
            {
                flip(cellIndex, BOTTOM_LEFT);
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
        }
        else if (row == (ROW_SIZE - 1) && column == (COLUMN_SIZE - 1)) //if cell is in the bottom right corner
        {
            if (checkLine(cellIndex, TOP_LEFT))
            {
                flip(cellIndex, TOP_LEFT);
            }
            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
        }
        else if (row == (ROW_SIZE - 1) && column == 0) //if cell is in the bottom left corner
        {

            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, TOP_RIGHT))
            {
                flip(cellIndex, TOP_RIGHT);
            }
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
        }
        else if (row == 0) //if cell is on the top row
        {
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
            if (checkLine(cellIndex, RIGHT))
            {
                flip(cellIndex, RIGHT);
            }
            if (checkLine(cellIndex, BOTTOM_LEFT))
            {
                flip(cellIndex, BOTTOM_LEFT);
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
            if (checkLine(cellIndex, BOTTOM_RIGHT))
            {
                flip(cellIndex, BOTTOM_RIGHT);
            }
        }
        else if (column == 0) //if cell is in the left-most column
        {

            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, TOP_RIGHT))
            {
                flip(cellIndex, TOP_RIGHT);
            }
            if (checkLine(cellIndex, RIGHT))
            {
                flip(cellIndex, RIGHT);
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
            if (checkLine(cellIndex, BOTTOM_RIGHT))
            {
                flip(cellIndex, BOTTOM_RIGHT);
            }
        }
        else if (row == (ROW_SIZE - 1)) //if cell is in bottom row
        {

            if (checkLine(cellIndex, TOP_LEFT))
            {
                flip(cellIndex, TOP_LEFT);
            }
            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, TOP_RIGHT))
            {
                flip(cellIndex, TOP_RIGHT);
            }
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
            if (checkLine(cellIndex, RIGHT))
            {
                flip(cellIndex, RIGHT);
            }

        }
        else if (column == (COLUMN_SIZE - 1)) //if cell is in the right-most column
        {
            if (checkLine(cellIndex, TOP_LEFT))
            {
                flip(cellIndex, TOP_LEFT);
            }
            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
            if (checkLine(cellIndex, BOTTOM_LEFT))
            {
                flip(cellIndex, BOTTOM_LEFT);
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
        }
        else //if cell is in the middle
        {
            if (checkLine(cellIndex, TOP_LEFT))
            {
                flip(cellIndex, TOP_LEFT);
            }
            if (checkLine(cellIndex, TOP))
            {
                flip(cellIndex, TOP);
            }
            if (checkLine(cellIndex, TOP_RIGHT))
            {
                flip(cellIndex, TOP_RIGHT);
            }
            if (checkLine(cellIndex, LEFT))
            {
                flip(cellIndex, LEFT);
            }
            if (checkLine(cellIndex, RIGHT))
            {
                flip(cellIndex, RIGHT);
            }
            if (checkLine(cellIndex, BOTTOM_LEFT))
            {
                flip(cellIndex, BOTTOM_LEFT);
            }
            if (checkLine(cellIndex, BOTTOM))
            {
                flip(cellIndex, BOTTOM);
            }
            if (checkLine(cellIndex, BOTTOM_RIGHT))
            {
                flip(cellIndex, BOTTOM_RIGHT);
            }
        }
    }

    //this method goes through an alignment and flips over the disks
    //parameters are the cellIndex and the adjacent cell of the aligment to be flipped
    private void flip(int cellIndex, int adjacent)
    {
        int nextIndex = getAdjacentIndex(cellIndex, adjacent); //gets the index of the adjacent cell
        while (board[nextIndex] != getCurrentPlayer()) //while the adjacent index is the opposing player
        {
            board[nextIndex] = getCurrentPlayer(); //set to current player 
            cellIndex = nextIndex; //set the cell index to the adjacent index
            nextIndex = getAdjacentIndex(cellIndex, adjacent); //get the next adjacent index in the line
        }
    }

    //this method checks if the move made by the player is a valid move
    //parameter is the cell index of the move
    private boolean isValid(int cellIndex)
    {
        boolean valid = false;
        int row = cellIndex / COLUMN_SIZE; //the row number of the cell
        int column = cellIndex % COLUMN_SIZE; //the column number of the cell

        if (row == 0 && column == 0) //if the cell is in the top left corner
        {

            if (checkLine(cellIndex, RIGHT) || checkLine(cellIndex, BOTTOM) //check all potential aligments
                    || checkLine(cellIndex, BOTTOM_RIGHT))
            {
                valid = true; //if any are true its a valid move
            }
        }
        else if (row == 0 && column == (COLUMN_SIZE - 1)) //if the cell is in the top right corner
        {
            if (checkLine(cellIndex, LEFT) || checkLine(cellIndex, BOTTOM_LEFT)
                    || checkLine(cellIndex, BOTTOM))
            {
                valid = true;
            }
        }
        else if (row == (ROW_SIZE - 1) && column == (COLUMN_SIZE - 1)) //if the cell is in the bottom right corner
        {
            if (checkLine(cellIndex, TOP_LEFT) || checkLine(cellIndex, TOP)
                    || checkLine(cellIndex, LEFT))
            {
                valid = true;
            }
        }
        else if (row == (ROW_SIZE - 1) && column == 0) //if the cell is in the bottom left corner
        {
            if (checkLine(cellIndex, TOP) || checkLine(cellIndex, TOP_RIGHT)
                    || checkLine(cellIndex, LEFT))
            {
                valid = true;
            }
        }
        else if (row == 0) //if the cell is in the top row
        {
            if (checkLine(cellIndex, LEFT) || checkLine(cellIndex, RIGHT)
                    || checkLine(cellIndex, BOTTOM_LEFT) || checkLine(cellIndex, BOTTOM)
                    || checkLine(cellIndex, BOTTOM_RIGHT))
            {
                valid = true;
            }

        }
        else if (column == 0) //if the cell is in the left-most column
        {

            if (checkLine(cellIndex, TOP) || checkLine(cellIndex, TOP_RIGHT)
                    || checkLine(cellIndex, RIGHT) || checkLine(cellIndex, BOTTOM)
                    || checkLine(cellIndex, BOTTOM_RIGHT))
            {
                valid = true;
            }

        }
        else if (row == (ROW_SIZE - 1)) //if the cell is in the bottom row
        {
            if (checkLine(cellIndex, TOP_LEFT) || checkLine(cellIndex, TOP)
                    || checkLine(cellIndex, TOP_RIGHT) || checkLine(cellIndex, LEFT)
                    || checkLine(cellIndex, RIGHT))
            {
                valid = true;
            }
        }
        else if (column == (COLUMN_SIZE - 1)) //if the cell is in the right-most column
        {
            if (checkLine(cellIndex, TOP_LEFT) || checkLine(cellIndex, TOP)
                    || checkLine(cellIndex, LEFT) || checkLine(cellIndex, BOTTOM_LEFT)
                    || checkLine(cellIndex, BOTTOM))
            {
                valid = true;
            }
        }
        else //if the cell is in the middle
        {
            if (checkLine(cellIndex, TOP_LEFT) || checkLine(cellIndex, TOP)
                    || checkLine(cellIndex, TOP_RIGHT) || checkLine(cellIndex, LEFT)
                    || checkLine(cellIndex, RIGHT) || checkLine(cellIndex, BOTTOM_LEFT)
                    || checkLine(cellIndex, BOTTOM) || checkLine(cellIndex, BOTTOM_RIGHT))
            {
                valid = true;
            }

        }
        return valid;
    }

    //retrieves the adjacent index of a specified cell
    //paramters are the cell index and the location of the adjacent cell 
    private int getAdjacentIndex(int cellIndex, int adjacent)
    {
        int adj = cellIndex + adjacent; //the index of the adjacent cell
        int cellRow = cellIndex / COLUMN_SIZE; //the row of the cell index
        int cellColumn = cellIndex % COLUMN_SIZE; //the column of the cell index
        int adjacentColumn = (cellIndex + adjacent) % COLUMN_SIZE; //used for checking the adjacent column
        
        //check to see if adjacent cell is off the board
        if ((cellRow == 0 && adj < 0) || (cellRow == (ROW_SIZE - 1) && adj >= GRID_SIZE)
                || (cellColumn == (COLUMN_SIZE - 1) && adjacentColumn == 0)
                || (cellColumn == 0 && adjacentColumn == (COLUMN_SIZE - 1))) 
        {
            return -1; //returns a semiphore value if the next index is off the board
        }
        else
        {
            return adj; //otherwise return the adjacent index
        }
    }

    //checks a single alignment based on the cell index and the adjacent cell to be checked
    //parameters are the cell index to be checked and the direction of the adjacent cell
    private boolean checkLine(int cellIndex, int adjacent)
    {
        boolean validLine = false;
        int nextIndex = getAdjacentIndex(cellIndex, adjacent); //get the index of the adjacent cell
        
        //if the next index is on the board, the opposing player, and not null
        while (nextIndex >= 0 && board[nextIndex] != getCurrentPlayer() && board[nextIndex] != null)                   
        {
            cellIndex = nextIndex; //set the cell index to the next index
            nextIndex = getAdjacentIndex(cellIndex, adjacent); //get the next adjacent index

            //if the next index is on the board and is the current player the move is valid
            if (nextIndex >= 0 && board[nextIndex] == getCurrentPlayer()) 
            {
                validLine = true;
            }
        }

        return validLine;
    }

}
