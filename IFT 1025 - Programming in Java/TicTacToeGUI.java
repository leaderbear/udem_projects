
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Abderrahim Tabta 20133680
 */

public class TicTacToeGUI extends Application {

    private Text status;
    private Button buttons[][] = new Button[3][3];
    private Boolean o = false;

    public static void main(String[] args) {
        TicTacToeGUI.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {



        // Ouverture du Socket qui se connecte au serveur de TicTacToe
        Socket clientSocket = new Socket("127.0.0.1", 1337);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                clientSocket.getOutputStream()
        ));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()
        ));

        /**
         * Notez qu'on peut envoyer des messages au serveur en utilisant :
         *
         * writer.append("mon message" + "\n");
         * writer.flush();
         */

        // Création de l'interface graphique
        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 300);

        /**
         *
         * Lorsqu'on clique sur un des boutons, cela doit envoyer la coordonnée
         * du bouton au serveur dans le format :
         *
         * colonne:ligne
         */

        //Ajoute d'une grille 3par3 de 9 boutons.
        GridPane grid = new GridPane();
        addButtons(grid);
        root.getChildren().add(grid);

        // Ajout de la barre de status
        status = new Text();
        root.getChildren().add(status);

        //comportement des boutons qui s'ils sont vides , envoie leurs coord au serveur
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                Button button = buttons[j][i];
                String coords = "" + (j + 1) + ":" + (i + 1);

                button.setOnAction(actionEvent -> {
                    if (button.getText().equals("")) {
                        try {
                            writer.append(coords + "\n");
                            writer.flush();
                        } catch (IOException e){}
                    } else {}
                });
            }
        }


        /**
         * Crée un thread qui écoute les messages envoyés par le serveur et qui
         * met à jour l'interface graphique en conséquence
         */
        Thread listener = new Thread(() -> {
            try {
                String line;

                while ((line = reader.readLine()) != null) {
                    /**
                     *
                     * agir en conséquence
                     */


                    //Pour eviter une erreur, la taille des substring sont places en ordre croissant

                    //si le serveur envoie "tie", le match se termine sur une egalite et on met a jour le status
                    if(line.substring(0,3).equals("tie")){setStatus("Match nul");}

                    //Chaque fois que le joueur clique sur une case vide, la mettre a jour et met le status a null
                    else if(line.substring(0,4).equals("move")){

                        //D'abord, chercher les info ensuite modifier le bouton s'il n'est pas deja utilise
                        int i = Integer.valueOf(line.substring(7,8));
                        int j = Integer.valueOf(line.substring(9,10));
                        String turn = line.substring(5,6);

                        if(buttons[i-1][j-1].getText().equals("")){setButton(turn,i-1,j-1);}
                        setStatus(" ");
                    }

                    //Ici, savoir quel client joue les 'x' et quel client client joue les 'o'
                    //Si le client joue les 'o' definir la variable boolean "o" a true
                    else if(line.substring(0,5).equals("start")){
                        String turn = line.substring(6,7);
                        if(turn.equals("x")) {setStatus("Commencez!");}
                        else{setStatus("Les X commencent"); o = true;}
                    }

                    //Lors d'une victoire/defaite, selon le client(boolean o) mettre a jour le status
                    else if(line.substring(0,5).equals("xWins")){
                        if (o == false){setStatus("Vous avez gagné!");} else{setStatus("Vous avez perdu...");}
                    }

                    else if(line.substring(0,5).equals("oWins")){
                        if (o == true){setStatus("Vous avez gagné!");} else {setStatus("Vous avez perdu...");}
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        listener.start();


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tic Tac Toe");
        // Fermer le programme (tous les threads) lorsqu'on ferme la fenêtre
        primaryStage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    /**
     *
     * @param grid grille dans laquelle on met 9 boutons (3x3)
     *             de plus, stocker ces boutons dans une array afin de gerer leur comportement ensuite
     */
    public void addButtons(GridPane grid){
        for (int i = 0 ; i < 3; i++){
            for (int j = 0; j < 3; j++) {

                //bouton vide d'une taille fixe pour remplir fenetre de jeu
                Button button = new Button("");
                button.setMinSize(100,90);

                buttons[j][i] = button;
                grid.add(buttons[j][i],j,i);
            }
        }
    }

    /**
     *
     * Cette fonction change le texte dans un bouton qui se trouve dans l'array buttons
     * @param text qu'on doit mettre dans le bouton
     * @param i collone et ligne du bouton
     * @param j
     */
    private void setButton(String text, int i , int j){
        Platform.runLater(()-> {
            buttons[i][j].setText(text); }
        );
    }


    private void setStatus(String str) {
        /**
         * Important : toutes les modifications de l'interface graphique
         * **doivent** se faire sur le Thread d'application de JavaFX
         *
         * Si un autre thread souhaite modifier l'interface, on doit passer par
         * la méthode Platform.runLater(runnable);
         */
        Platform.runLater(() -> {
            status.setText(str);
        });
    }
}
