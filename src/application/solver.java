package application;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;

class solver {

    private static int row = 0;
    private static int col = 0;
    private static boolean isBacktracking = false;


    static field[][] parseJSON(String filePath) throws IOException {

        Path path = Paths.get(filePath);

        String jsonString = Files.readString(path, StandardCharsets.UTF_8); //assign your JSON String here
        JSONObject jsonObject = new JSONObject(jsonString);

        int boardLength = jsonObject.getInt("boardLength");
        field[][] board = new field[boardLength][boardLength];

        JSONArray arr = jsonObject.getJSONArray("boardData");

        //Iteration of rows
        for (int i = 0; i < boardLength; i++) {
            //Iteration of cols
            for (int j = 0; j < boardLength; j++) {
                field parsedField = new field(arr.getJSONArray(i).getString(j));
                board[i][j] = parsedField;
            }
        }

        return board;
    }


    /**
     * @param board
     * @param row
     * @param col
     * @return a four digit code which represent the configuration of the colourable fields "41111" means 4 fields in total(empty and same color), 1 fields in each direction either empty or same color
     */
    private static void analyseSurroundingFields(field[][] board, int row, int col) {

        int totalColouredFields = 0;
        int emptyFieldsNorth = 0;
        int emptyFieldsEast = 0;
        int emptyFieldsSouth = 0;
        int emptyFieldsWest = 0;
        field selectedField = board[row][col];

        //traverse the fields north of the selected one
        for (int i = row; i >= 0; i--) {
            if (i == row)
                continue;

            if (board[i][col].getColor() == fieldColor.empty) {
                emptyFieldsNorth++;

            } else if (board[i][col].getNumber() != 99)
                break;
            else if (board[i][col].getColor() == selectedField.getColor())
                totalColouredFields++;

            else if (selectedField.getColor() == fieldColor.white && board[i][col].getColor() == fieldColor.black)
                break;
            else if (selectedField.getColor() == fieldColor.black && board[i][col].getColor() == fieldColor.white)
                break;
        }
        // traverse to the east
        for (int i = col; i < board.length; i++) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == fieldColor.empty) {
                emptyFieldsEast++;

            } else if (board[row][i].getNumber() != 99)
                break;
            else if (board[row][i].getColor() == selectedField.getColor())
                totalColouredFields++;

            else if (selectedField.getColor() == fieldColor.white && board[row][i].getColor() == fieldColor.black)
                break;
            else if (selectedField.getColor() == fieldColor.black && board[row][i].getColor() == fieldColor.white)
                break;
        }
        //traverse south
        for (int i = row; i < board.length; i++) {
            if (i == row)
                continue;

            if (board[i][col].getColor() == fieldColor.empty) {
                emptyFieldsSouth++;

            } else if (board[i][col].getNumber() != 99)
                break;
            else if (board[i][col].getColor() == selectedField.getColor())
                totalColouredFields++;

            else if (selectedField.getColor() == fieldColor.white && board[i][col].getColor() == fieldColor.black)
                break;
            else if (selectedField.getColor() == fieldColor.black && board[i][col].getColor() == fieldColor.white)
                break;
        }
        //traverse west
        for (int i = col; i >= 0; i--) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == fieldColor.empty) {
                emptyFieldsWest++;

            } else if (board[row][i].getNumber() != 99)
                break;
            else if (board[row][i].getColor() == selectedField.getColor())
                totalColouredFields++;
            else if (selectedField.getColor() == fieldColor.white && board[row][i].getColor() == fieldColor.black)
                break;
            else if (selectedField.getColor() == fieldColor.black && board[row][i].getColor() == fieldColor.white)
                break;
        }

        int totalEmptyFields = emptyFieldsNorth + emptyFieldsEast + emptyFieldsSouth + emptyFieldsWest;

        selectedField.setTotalEmpty(totalEmptyFields);
        selectedField.setTotalColoured(totalColouredFields);
        selectedField.setFieldsNorth(emptyFieldsNorth);
        selectedField.setFieldsEast(emptyFieldsEast);
        selectedField.setFieldsSouth(emptyFieldsSouth);
        selectedField.setFieldsWest(emptyFieldsWest);

        //return "" + totalColouredFields + totalEmptyFields + emptyFieldsNorth + emptyFieldsEast + emptyFieldsSouth + emptyFieldsWest;
    }

    private static boolean isSatisfied(field[][] board, int row, int col) {
        //traverse the fields north of the selected one
        fieldColor selectedColor = board[row][col].getColor();
        int selectedNumber = board[row][col].getNumber();
        int numberOfValidFields = 0;

        for (int i = row; i >= 0; i--) {
            if (i == row)
                continue;
            if (board[i][col].getColor() == selectedColor && board[i][col].getNumber() == 99)
                numberOfValidFields++;
            else
                break;

        }
        // traverse to the east
        for (int i = col; i < board.length; i++) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == selectedColor && board[row][i].getNumber() == 99)
                numberOfValidFields++;

            else
                break;

        }
        //traverse south
        for (int i = row; i < board.length; i++) {
            if (i == row)
                continue;

            if (board[i][col].getColor() == selectedColor && board[i][col].getNumber() == 99)
                numberOfValidFields++;

            else
                break;
        }
        //traverse west
        for (int i = col; i >= 0; i--) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == selectedColor && board[row][i].getNumber() == 99)
                numberOfValidFields++;

            else
                break;

        }
        if (selectedNumber == numberOfValidFields)
            return true;
        else
            return false;

    }

    /**
     * colours all visible empty fields in the colour of the given field, is only called when there is only one valid move
     *
     * @param board
     * @param row
     * @param col
     */
    private static void colourEmptyFields(field[][] board, int row, int col) {

        fieldColor destinationColor = board[row][col].getColor();

        //traverse north
        for (int i = row; i >= 0; i--) {
            if (i == row)
                continue;

            if (board[i][col].getColor() == fieldColor.empty)
                board[i][col].setColor(destinationColor);
            else if (board[i][col].getColor() == destinationColor && board[i][col].getNumber() == 99)
                continue;
            else
                break;
        }
        // traverse to the east
        for (int i = col; i < board.length; i++) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == fieldColor.empty)
                board[row][i].setColor(destinationColor);
            else if (board[row][i].getColor() == destinationColor && board[row][i].getNumber() == 99)
                continue;
            else
                break;
        }
        //traverse south
        for (int i = row; i < board.length; i++) {
            if (i == row)
                continue;

            if (board[i][col].getColor() == fieldColor.empty)
                board[i][col].setColor(destinationColor);
            else if (board[i][col].getColor() == destinationColor && board[i][col].getNumber() == 99)
                continue;
            else
                break;
        }
        //traverse west
        for (int i = col; i >= 0; i--) {
            if (i == col)
                continue;

            if (board[row][i].getColor() == fieldColor.empty)
                board[row][i].setColor(destinationColor);
            else if (board[row][i].getColor() == destinationColor && board[row][i].getNumber() == 99)
                continue;
            else
                break;
        }
    }

    private static void colourDirectNeighboursOppositeColour(field[][] board, int row, int col) {
        int[] top = {row - 1, col};
        int[] right = {row, col + 1};
        int[] bottom = {row + 1, col};
        int[] left = {row, col - 1};

        int[][] directions = {top, right, bottom, left};
        field selectedField = board[row][col];


        for (int[] direction : directions) {
            if ((direction[0] >= 0 && direction[0] < board.length) && (direction[1] >= 0 && direction[1] < board.length)) {
                if (board[direction[0]][direction[1]].getColor() == fieldColor.empty) {
                    if (selectedField.getColor() == fieldColor.black)
                        board[direction[0]][direction[1]].setColor(fieldColor.white);
                    else if (selectedField.getColor() == fieldColor.white)
                        board[direction[0]][direction[1]].setColor(fieldColor.black);
                }
            }
        }

    }

    private static void colourMissingFieldsInOneDirection(field[][] board, int row, int col) {
        field selectedField = board[row][col];
        int missingFields = selectedField.getNumber() - selectedField.getTotalColoured();

        //traverse to the north
        if (selectedField.getTotalEmpty() == selectedField.getFieldsNorth()) {
            for (int i = row; i >= 0; i--) {
                if (i == row)
                    continue;
                if (missingFields == 0)
                    break;

                if (board[i][col].getColor() == fieldColor.empty) {
                    board[i][col].setColor(selectedField.getColor());
                    missingFields--;
                }


            }
        }
        // traverse to the east
        if (selectedField.getTotalEmpty() == selectedField.getFieldsEast()) {
            for (int i = col; i < board.length; i++) {
                if (i == col)
                    continue;

                if (missingFields == 0)
                    break;

                if (board[row][i].getColor() == fieldColor.empty) {
                    board[row][i].setColor(selectedField.getColor());
                    missingFields--;
                }

            }
        }
        //traverse south
        if (selectedField.getTotalEmpty() == selectedField.getFieldsSouth()) {
            for (int i = row; i < board.length; i++) {
                if (i == row)
                    continue;

                if (missingFields == 0)
                    break;

                if (board[i][col].getColor() == fieldColor.empty) {
                    board[i][col].setColor(selectedField.getColor());
                    missingFields--;
                }
            }
        }
        //traverse west
        if (selectedField.getTotalEmpty() == selectedField.getFieldsWest()) {
            for (int i = col; i >= 0; i--) {
                if (i == col)
                    continue;

                if (missingFields == 0)
                    break;

                if (board[row][i].getColor() == fieldColor.empty) {
                    board[row][i].setColor(selectedField.getColor());
                    missingFields--;
                }
            }
        }

    }

    private static void colourSpecifiedFields(field[][] board, int row, int col, fieldColor destinationColor, String move) {
        // field selectedField = board[row][col];
        int numberOfFieldsNorth = Character.getNumericValue(move.charAt(0));
        int numberOfFieldsEast = Character.getNumericValue(move.charAt(1));
        int numberOfFieldsSouth = Character.getNumericValue(move.charAt(2));
        int numberOfFieldsWest = Character.getNumericValue(move.charAt(3));

        for (int i = row; i >= 0; i--) {
            if (i == row)
                continue;
            if (numberOfFieldsNorth == 0)
                break;

            if (board[i][col].getColor() == fieldColor.empty) {
                board[i][col].setColor(destinationColor);
                board[i][col].setColouredBy("" + row + col);
                numberOfFieldsNorth--;
            }
        }

        for (int i = col; i < board.length; i++) {
            if (i == col)
                continue;

            if (numberOfFieldsEast == 0)
                break;

            if (board[row][i].getColor() == fieldColor.empty) {
                board[row][i].setColor(destinationColor);
                board[row][i].setColouredBy("" + row + col);
                numberOfFieldsEast--;
            }

        }

        for (int i = row; i < board.length; i++) {
            if (i == row)
                continue;

            if (numberOfFieldsSouth == 0)
                break;

            if (board[i][col].getColor() == fieldColor.empty) {
                board[i][col].setColor(destinationColor);
                board[i][col].setColouredBy("" + row + col);
                numberOfFieldsSouth--;
            }
        }

        for (int i = col; i >= 0; i--) {
            if (i == col)
                continue;

            if (numberOfFieldsWest == 0)
                break;

            if (board[row][i].getColor() == fieldColor.empty) {
                board[row][i].setColor(destinationColor);
                board[row][i].setColouredBy("" + row + col);
                numberOfFieldsWest--;
            }
        }

    }

    private static Stack<String> generatePossibleMoves(field[][] board, int row, int col) {
        field selectedField = board[row][col];
        int missingFields = selectedField.getNumber() - selectedField.getTotalColoured();
        // Stack<String> alreadyTestedMoves = selectedField.getAlreadyTestedMoves();

        Stack<String> possibleMoves = new Stack<>();

        for (int i = 0; i <= selectedField.getFieldsNorth(); i++) {
            for (int j = 0; j <= selectedField.getFieldsEast(); j++) {
                for (int k = 0; k <= selectedField.getFieldsSouth(); k++) {
                    for (int l = 0; l <= selectedField.getFieldsWest(); l++) {

                        if ((i + j + k + l) == missingFields)
                            if (selectedField.alreadyTestedMoves.empty() || !selectedField.alreadyTestedMoves.contains("" + i + j + k + l))
                                possibleMoves.push("" + i + j + k + l);

                    }
                }
            }
        }

        return possibleMoves;
    }

    private static void undoLastTestedMove(field[][] board, int row, int col) {
        String colouredBy = "" + row + col;

        for (int i = row; i >= 0; i--) {
            if (i == row)
                continue;


            if (board[i][col].getColouredBy().equals(colouredBy)) {
                board[i][col].setColor(fieldColor.empty);
                board[i][col].setColouredBy("");

            }
        }

        for (int i = col; i < board.length; i++) {
            if (i == col)
                continue;

            if (board[row][i].getColouredBy().equals(colouredBy)) {
                board[row][i].setColor(fieldColor.empty);
                board[row][i].setColouredBy("");
            }

        }

        for (int i = row; i < board.length; i++) {
            if (i == row)
                continue;

            if (board[i][col].getColouredBy().equals(colouredBy)) {
                board[i][col].setColor(fieldColor.empty);
                board[i][col].setColouredBy("");
            }
        }

        for (int i = col; i >= 0; i--) {
            if (i == col)
                continue;

            if (board[row][i].getColouredBy().equals(colouredBy)) {
                board[row][i].setColor(fieldColor.empty);
                board[row][i].setColouredBy("");
            }
        }

    }

    /**
     * This function checks if a given board can be solved, A board is not solvable if a field has not enough empty fields to satisfy its requirement.
     *
     * @param board
     * @return if the board is solvable return true, if not return false
     */
    private static boolean checkBoard(field[][] board) {

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                //int emptyFields = countEmptyFields(board, row, col);
                analyseSurroundingFields(board, row, col);
                if (board[row][col].getTotalEmpty() < board[row][col].getNumber())
                    return true;
                if (board[row][col].getTotalEmpty() == 0 && board[row][col].getNumber() == 0)
                    return false;
            }
        }
        return false;
    }

    private static boolean isBoardSolved(field[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (board[row][col].getNumber() != 99) {
                    if (!isSatisfied(board, row, col))
                        return false;
                }
            }
        }
        return true;
    }

    private static void fillRemainingEmptyFields(field[][] board){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if(board[row][col].getNumber() != 99){
                    if (isSatisfied(board, row, col)){
                        fieldColor destinationColor;

                        if(board[row][col].getColor() == fieldColor.white)
                            destinationColor = fieldColor.black;
                        else
                            destinationColor = fieldColor.white;



                        //traverse north
                        for (int i = row; i >= 0; i--) {
                            if (i == row)
                                continue;

                            if (board[i][col].getColor() == fieldColor.empty)
                                board[i][col].setColor(destinationColor);
                            else if (board[i][col].getColor() == destinationColor && board[i][col].getNumber() == 99)
                                continue;
                            else
                                break;
                        }
                        // traverse to the east
                        for (int i = col; i < board.length; i++) {
                            if (i == col)
                                continue;

                            if (board[row][i].getColor() == fieldColor.empty)
                                board[row][i].setColor(destinationColor);
                            else if (board[row][i].getColor() == destinationColor && board[row][i].getNumber() == 99)
                                continue;
                            else
                                break;
                        }
                        //traverse south
                        for (int i = row; i < board.length; i++) {
                            if (i == row)
                                continue;

                            if (board[i][col].getColor() == fieldColor.empty)
                                board[i][col].setColor(destinationColor);
                            else if (board[i][col].getColor() == destinationColor && board[i][col].getNumber() == 99)
                                continue;
                            else
                                break;
                        }
                        //traverse west
                        for (int i = col; i >= 0; i--) {
                            if (i == col)
                                continue;

                            if (board[row][i].getColor() == fieldColor.empty)
                                board[row][i].setColor(destinationColor);
                            else if (board[row][i].getColor() == destinationColor && board[row][i].getNumber() == 99)
                                continue;
                            else
                                break;
                        }
                    }

                }
            }
        }


    }

    static void solveIrasutoBoard(field[][] board) {

        if (checkBoard(board))
            System.out.println("This board is not solvable! Please submit another board");

        while (!isBoardSolved(board)) {
            row = 0;
            while (row < board.length) {
                col = 0;
                while (col < board.length) {
                    // if the field is a colour field do nothing

                    if (board[row][col].getNumber() == 99) {
                        col++;
                        continue;
                    }

                    if (board[row][col].getNumber() == 0) {
                        colourDirectNeighboursOppositeColour(board, row, col);
                        col++;
                        continue;
                    }

                    // If a number field can only just enough empty fields and fields of the need colour to be satisfied, we can savely colour all those fields

                    if (isSatisfied(board, row, col)) {
                        colourDirectNeighboursOppositeColour(board, row, col);
                        col++;
                        continue;

                    }

                    analyseSurroundingFields(board, row, col);

                    if (board[row][col].getNumber() == (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                        colourEmptyFields(board, row, col);
                        col++;
                        continue;
                    }
                    /* else if (board[row][col].getNumber() < (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                        //generate all possible moves that were not tested before, and only try out the latest one
                        String possibleMove = generatePossibleMoves(board, row, col).peek();
                        //colour the sorroundings fields in the generated layout
                        colourSpecifiedFields(board, row, col, board[row][col].getColor(), possibleMove);
                        // after painting the fields the tried out move gets pushed to the already tested moves stack
                        board[row][col].alreadyTestedMoves.push(possibleMove);
                        col++;
                        continue;

                    } else if (board[row][col].getNumber() > (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                        isBacktracking = true;
                    }
                } else {

                    if (board[row][col].getNumber() == 99) {
                        col--;
                        continue;
                    }
                    //undo the last made move
                    colourSpecifiedFields(board, row, col, fieldColor.empty);

                }
*/
                    colourMissingFieldsInOneDirection(board, row, col);

                    if (isBoardSolved(board))
                        break;

                    col++;
                }

                if (isBoardSolved(board))
                    break;
                row++;
            }
        }
        fillRemainingEmptyFields(board);

    }

    static field[][] solveIrasutoBoardStepped(field[][] board, int row, int col) {
        if (checkBoard(board))
            System.out.println("This board is not solvable! Please submit another board");

        if (board[row][col].getNumber() == 0) {
            colourDirectNeighboursOppositeColour(board, row, col);
        }

        // If a number field can only just enough empty fields and fields of the need colour to be satisfied, we can savely colour all those fields

        if (isSatisfied(board, row, col)) {
            colourDirectNeighboursOppositeColour(board, row, col);
        }

        analyseSurroundingFields(board, row, col);

        if (board[row][col].getNumber() == (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
            colourEmptyFields(board, row, col);
        }
                    /* else if (board[row][col].getNumber() < (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                        //generate all possible moves that were not tested before, and only try out the latest one
                        String possibleMove = generatePossibleMoves(board, row, col).peek();
                        //colour the sorroundings fields in the generated layout
                        colourSpecifiedFields(board, row, col, board[row][col].getColor(), possibleMove);
                        // after painting the fields the tried out move gets pushed to the already tested moves stack
                        board[row][col].alreadyTestedMoves.push(possibleMove);
                        col++;
                        continue;

                    } else if (board[row][col].getNumber() > (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                        isBacktracking = true;
                    }
                } else {

                    if (board[row][col].getNumber() == 99) {
                        col--;
                        continue;
                    }
                    //undo the last made move
                    colourSpecifiedFields(board, row, col, fieldColor.empty);

                }
*/
        colourMissingFieldsInOneDirection(board, row, col);

        return board;
    }

    static void solveIrasutoBacktracking(field[][] board) {

        row = 0;
        while (row < board.length) {
            col = 0;
            while (col < board.length) {
                if (!isBacktracking) {
                    if (board[row][col].getNumber() != 99) {
                        analyseSurroundingFields(board, row, col);
                        if (board[row][col].getNumber() == board[row][col].getTotalColoured()) {
                            col++;
                            continue;
                        } else if (board[row][col].getNumber() == (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                            colourEmptyFields(board, row, col);
                            col++;
                            continue;
                        } else if (board[row][col].getNumber() < board[row][col].getTotalColoured()) {
                            col++;
                            continue;
                        } else if (board[row][col].getNumber() < (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                            //generate all possible moves that were not tested before, and only try out the latest one
                            Stack<String> possibleMoves = generatePossibleMoves(board, row, col);
                            //colour the sorroundings fields in the generated layout
                            colourSpecifiedFields(board, row, col, board[row][col].getColor(), possibleMoves.peek());
                            // after painting the fields the tried out move gets pushed to the already tested moves stack
                            board[row][col].alreadyTestedMoves.push(possibleMoves.peek());
                            col++;
                            continue;

                        } else if (board[row][col].getNumber() > (board[row][col].getTotalColoured() + board[row][col].getTotalEmpty())) {
                            isBacktracking = true;

                            if (col > 0)
                                col--;
                            else if (col == 0) {
                                col = board.length - 1;
                                if (row == 0)
                                    System.out.println("Cant go beyond the first field 0,0");
                                if (row > 0)
                                    row--;
                            }
                            continue;
                        }
                    }
                    col++;
                }
                else {

                    //undo the last made move
                    if (board[row][col].getNumber() != 99) {
                        //                  System.out.println("Undoing the follow move for " + row + col +"move: "+ board[row][col].alreadyTestedMoves.peek());
                        undoLastTestedMove(board, row, col);
                        //optional should not be needed
                        analyseSurroundingFields(board, row, col);
                        Stack<String> possibleMoves = generatePossibleMoves(board, row, col);

                        if (!possibleMoves.empty()) {
                            if (possibleMoves.peek().equals("0000"))
                                possibleMoves.pop();
                            if (!possibleMoves.empty()) {
                                colourSpecifiedFields(board, row, col, board[row][col].getColor(), possibleMoves.peek());
                                board[row][col].alreadyTestedMoves.push(possibleMoves.peek());
                                isBacktracking = false;
                                if (col < board.length - 1) {
                                    col++;
                                    continue;
                                } else if (col == board.length - 1) {
                                    col = 0;
                                    if (row == board.length - 1)
                                        System.out.println("Cant go further than the last field");
                                    if (row < board.length - 1) {
                                        row++;
                                        continue;
                                    }
                                }
                            }
                        } else if (possibleMoves.empty()) {
                            board[row][col].alreadyTestedMoves.clear();
                        }


                    }

                    if (col == 0) {
                        col = board.length - 1;
                        if (row == 0)
                            System.out.println("This puzzle cannot be solved. column error");
                        if (row > 0)
                            row--;
                        continue;
                    }
                    if (col > 0)
                        col--;
                }//end else
                //end col while loop
            }

            if (!isBacktracking)
                row++;
            if (isBacktracking) {
                if (row == 0)
                    System.out.println("This puzzle cannot be solved, row error");
                if (row > 0)
                    row--;
            }
            if (isBoardSolved(board))
                break;

            //end row while loop
        }
        fillRemainingEmptyFields(board);

    }
}


