package connectfour.gui;

import connectfour.model.ConnectFourBoard;
import connectfour.model.Observer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This file is responsible for the view and controller layer
 *
 * @author Mayurreddy Sangepu
 */

public class ConnectFourGUI extends Application implements Observer<ConnectFourBoard> {
    /**vertical gaps between the boxes
     */
    private final static int VGAP = 10;
    /**horizontal gaps between the boxes
     */
    private final static int HGAP = 140;
    /*declaring default image
     */
    private static Image empty;
    /*declaring first player image
     */
    private static Image player1;
    /*declaring second player image
     */
    private static Image player2;
    /*declaring first label1
     */
    private Label label1;
    /*declaring second label2
     */
    private Label label2;
    /*declaring third label3
     */
    private Label label3;
    /*declaring gridPane layout
     */
    private GridPane gridPane;
    /*declaring board object
     */
    private ConnectFourBoard board;

    /**
     * initialize board and the images
     * to be placed on the board
     */
    public  ConnectFourGUI(){
        empty = new Image(getClass().getResourceAsStream(
                "empty.png"));
        player1 = new Image(getClass().getResourceAsStream(
                "p1black.png"));
        player2 = new Image(getClass().getResourceAsStream(
                "p2red.png"));
        this.board = new ConnectFourBoard();
    }

    /**
     * this is the main method responsible for
     * launching the application
     *
     * @param args: arguments taken from the user
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     *
     * *********************view layer*******************************
     *
     * this method adds the current object
     * to the observer
     */
    public void init(){
        board.addObserver(this);
    }

    /**
     * The update for the GUI updates the board and some state.
     *
     * @param connectFourBoard: the board on which updates take place
     */
    @Override
    public void update(ConnectFourBoard connectFourBoard) {

        this.label1.setText(""+connectFourBoard.getMovesMade() + " moves made");
        this.label2.setText("Current player: "+connectFourBoard.getCurrentPlayer());
        this.label3.setText("status: "+connectFourBoard.getGameStatus());
        if(connectFourBoard.hasWonGame())
        {
            stop();
        }
    }

    /**
     * this method creates a grid pane layout on
     * the scene and sets the event action on button
     * click
     *
     * @return gridPane : grid pane layout is returned.
     */
    private GridPane makeGridPane(){
        this.gridPane = new GridPane();
        //an array containing the constants of this enum type,
        // in the order they're declared.
        ConnectFourBoard.Player[] playerArr = ConnectFourBoard.Player.values();
        for (int row = 0; row< ConnectFourBoard.ROWS; ++row) {
            for (int col = 0; col< ConnectFourBoard.COLS; ++col) {
                // get the None type of player and create a button for it
                cellButton button = new cellButton(playerArr[2]);
                int finalCol = col;
                button.setOnAction(event -> run(finalCol));
                // JavaFX uses (x, y) pixel coordinates instead of
                // (row, col), so must invert when adding
                this.gridPane.add(button, col, row);

            }
        }
        return gridPane;
    }

    /**
     * this method checks if the cell is valid or not
     * for the player to make a move on that column.
     *
     * @param col: latest most column to be filled
     *
     * @return node: returns the button which should be
     *                updated.
     */
    private cellButton returnButton(int col)
    {
        int rows = ConnectFourBoard.ROWS-1;
        int counter =0;
        Node node = null;
        for(Node node1: this.gridPane.getChildren())
        {
            if(GridPane.getColumnIndex(node1)==col && counter<=rows)
            {
                if (String.valueOf(this.board.getContents(counter, col)).equals("NONE"))
                {
                    node = node1;
                }
                counter++;
            }
        }
        return (cellButton) node;
    }

    /**
     *
     * ************************ controller layer *****************************
     *
     *  this method is the process the data like getting
     *  current player, setting up the graphic image when
     *  the button is pressed.
     *
     * @param col: column number of the board
     */
    private void run(int col)
    {
        cellButton btt = returnButton(col);
        if(btt!=null) {
            String player = String.valueOf(this.board.getCurrentPlayer());
            if (player.equals("P1")) {
                btt.setGraphic(new ImageView(player1));
            } else if (player.equals("P2")) {
                btt.setGraphic(new ImageView(player2));
            }
            this.board.makeMove(col);
        }
    }



    private class cellButton extends Button {
        /** the type of this pokemon */
        private ConnectFourBoard.Player player;

        /**
         * Create the button with the image based on the players.
         *
         * @param player the None player
         */
        public cellButton(ConnectFourBoard.Player player) {
            this.player = player;
            Image image = switch (this.player) {
                case P1 -> player1;
                case P2 -> player2;
                default -> empty;
            };

            // set the graphic on the button
            this.setGraphic(new ImageView(image));
        }

    }

    /**
     * this method is responsible for setting layout,labels basically
     * all gui related initialization and stage show.
     * @param stage: the stage object where all the components will be showed
     * @throws Exception:
     */
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();
        this.gridPane = makeGridPane();
        borderPane.setCenter(this.gridPane);
        FlowPane flowPane = new FlowPane();
        setLabels();
        flowPane.getChildren().addAll(this.label1,this.label2,this.label3);
        flowPane.setVgap(VGAP);
        flowPane.setHgap(HGAP);
        borderPane.setBottom(flowPane);
        setSceneConditions(borderPane, stage);
        stage.show();

    }

    /**
     * this is the helper method which sets the labels
     * in the board.
     */
    private  void  setLabels(){
        this.label1 = new Label(""+this.board.getMovesMade() + " moves made");
        this.label2 = new Label("Current player: "+this.board.getCurrentPlayer());
        this.label3 = new Label("status: "+this.board.getGameStatus());
    }

    /**
     * this method sets the ending stage properties
     * such as title, scene and resizable.
     * @param borderPane : the boarderPane layout
     * @param stage: the stage for mounting scene and other
     *              components.
     */
    private void setSceneConditions(BorderPane borderPane,Stage stage){
        Scene scene = new Scene(borderPane);
        stage.setTitle("ConnectFourGUI");
        stage.setScene(scene);
        stage.setResizable(false);
    }

    /**
     * this method disables the grid pane after
     * the any one of the player wins
     */
    public void stop(){
        this.gridPane.setDisable(true);
    }

}