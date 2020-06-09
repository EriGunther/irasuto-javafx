package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    Button button_step;
    @FXML
    Button button_solve;
    @FXML
    Button button_close;
    @FXML
    Canvas canvas;
    @FXML
    VBox dragVBox;

    private File droppedFile;
    private int stepRow = 0;
    private int stepCol = 0;
    private boolean isFirstStep = true;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        field[][] dummyBoard = new field[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field dummy = new field(99, fieldColor.empty);
                dummyBoard[i][j] = dummy;
            }
        }
        drawOnCanvas(context, dummyBoard);
    }

    public void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dragVBox
                && event.getDragboard().hasFiles()) {

            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    public void handleDragDropped(DragEvent event) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        Dragboard db = event.getDragboard();
        File file = db.getFiles().get(0);
        droppedFile = file;
        field[][] parsedBoard = new field[0][];
        try {
            parsedBoard = solver.parseJSON(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawOnCanvas(context, parsedBoard);
    }

    void keyPressed(KeyEvent keyEvent){
        GraphicsContext context = canvas.getGraphicsContext2D();
        field[][] parsedBoard = new field[0][];
        try {
            parsedBoard = solver.parseJSON(droppedFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (keyEvent.getCode() == KeyCode.F8){
            Stage stage = (Stage) canvas.getScene().getWindow();
            stage.close();
        }
        if (keyEvent.getCode() == KeyCode.F6) {

            //solver.solveIrasutoBoard(parsedBoard);
            solver.solveIrasutoBacktracking(parsedBoard);
            drawOnCanvas(context, parsedBoard);
        }
        if (keyEvent.getCode() == KeyCode.F5){
            /*field[][] steppedBoard = new field[0][];
            if(isFirstStep) {
                steppedBoard = solver.solveIrasutoBoardStepped(parsedBoard, stepRow, stepCol);
                isFirstStep = false;
            } else if (!isFirstStep)
                steppedBoard = solver.solveIrasutoBoardStepped(steppedBoard, stepRow, stepCol);

            if(stepRow < parsedBoard.length) {
                if(stepCol < parsedBoard.length - 1)
                    stepCol++;
                else if(stepCol == parsedBoard.length - 1){
                    stepCol = 0;
                    stepRow++;
                }
            }*/
            solver.solveIrasutoBoard(parsedBoard);
            drawOnCanvas(context, parsedBoard);
        }
    }

    private void drawOnCanvas(GraphicsContext context, field[][] board) {
        int boardLength = board.length;
        for (int row = 0; row < boardLength; row++) {
            for (int col = 0; col < boardLength; col++) {
                // finds the y positbion of the cell, by multiplying the row number by 50, which is the height of a row 					// in pixels
                // then adds 2, to add some offset
                int position_y = row * 40 + 2;

                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a 					// column in pixels
                // then add 2, to add some offset
                int position_x = col * 40 + 2;

                // defines the width of the square as 46 instead of 50, to account for the 4px total of blank space 					// caused by the offset
                // as we are drawing squares, the height is going to be the same
                int width = 34;

                // set the fill color to white (you could set it to whatever you want)


                // draw a rounded rectangle with the calculated position and width. The last two arguments specify the 					// rounded corner arcs width and height.
                // Play around with those if you want.
                if (board[row][col].getColor() == fieldColor.white) {
                    // draw the number
                    context.setFill(Color.WHITE);
                    context.fillRoundRect(position_x, position_y, width, width, 10, 10);
                } else if (board[row][col].getColor() == fieldColor.black) {
                    context.setFill(Color.BLACK);
                    context.fillRoundRect(position_x, position_y, width, width, 10, 10);
                } else {
                    context.setFill(Color.GRAY);
                    context.fillRoundRect(position_x, position_y, width, width, 10, 10);
                }

            }
        }


        // draw the initial numbers from our GameBoard instance
        for (int row = 0; row < boardLength; row++) {
            for (int col = 0; col < boardLength; col++) {
                // finds the y position of the cell, by multiplying the row number by 50, which is the height of a row in pixels
                // then adds 2, to add some offset
                int position_y = row * 40 + 25;
                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a column in pixels
                // then add 2, to add some offset
                int position_x = col * 40 + 15;
                // set the fill color to black (you could set it to whatever you want)
                // set the font, from a new font, constructed from the system one, with size 20
                context.setFont(new Font(20));
                // check if value of coressponding array position is not 0
                if (board[row][col].getNumber() != 99) {

                    if (board[row][col].getColor() == fieldColor.white) {
                        // draw the number
                        context.setFill(Color.BLACK);
                        context.fillText(board[row][col].getNumber() + "", position_x, position_y);
                    } else if (board[row][col].getColor() == fieldColor.black) {
                        context.setFill(Color.WHITE);
                        context.fillText(board[row][col].getNumber() + "", position_x, position_y);
                    }
                }
            }
        }
    }
}