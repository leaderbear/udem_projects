/**
 * @author
 * Abderrahim Tabta 20133680
 * Guilhem Robert 20125535
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;

public class FlappyGhostGUI extends Application  {


    //Variables utiles pour la vue
    private FlappyGhost flappy = new FlappyGhost();
    private Controller controller = new Controller(flappy);

    private boolean debug;
    private boolean pauseBool = false;

    @Override
    public void start(Stage primaryStage) throws Exception{


        //create stage
        VBox root = new VBox();
        primaryStage.setTitle("Flappy Ghost");
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(root, 640, 440);
        primaryStage.setScene(scene);

        //game
        VBox game = new VBox();
        Image background = new Image(new FileInputStream("images\\bg.png"));
        ImageView backgroundView = new ImageView(background);

        Canvas canvas = new Canvas(640, 400);
        GraphicsContext context = canvas.getGraphicsContext2D();
        game.getChildren().add(canvas);

        //Menu
        HBox menu = new HBox();
        CheckBox debugCheck = new CheckBox();
        debugCheck.setText("Mode debug");
        debugCheck.setSelected(false);
        Text score = new Text("0");
        Button pause = new Button("Pause");
        Separator separator = new Separator();
        Separator separator2 = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator2.setOrientation(Orientation.VERTICAL);

        //Placer node dans le menu
        menu.getChildren().add(pause);
        menu.getChildren().add(separator);
        menu.getChildren().add(debugCheck);
        menu.getChildren().add(separator2);
        menu.getChildren().add(score);
        menu.setAlignment(Pos.CENTER);

        //Set panes and show game
        borderPane.setCenter(game);
        borderPane.setBottom(menu);
        root.getChildren().add(borderPane);
        primaryStage.show();

        //mettre l'icone du jeu et ne pas permettre au joueur de modifier la fenetre de jeu
        Image ghost= new Image(new FileInputStream("images\\ghost.png"));
        primaryStage.getIcons().add(ghost);
        primaryStage.setResizable(false);

        /* Après l’exécution de la fonction, le
        focus va automatiquement au canvas */
        Platform.runLater(() -> {
            canvas.requestFocus();
        });

        /* Lorsqu’on clique ailleurs sur la scène,
        le focus retourne sur le canvas */
        scene.setOnMouseClicked((event) -> {
            canvas.requestFocus();
        });

        // Fermer le programme (tous les threads) lorsqu'on ferme la fenêtre
        primaryStage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });

        //Comportemtn des touches du clavier
        scene.setOnKeyPressed((keyEvent ->{
            if (keyEvent.getCode() == KeyCode.SPACE){
                controller.sauter();
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE){Platform.exit();}

        }));

        //Comportement du bouton pause
        pause.setOnAction((actionEvent -> {
           if(!pauseBool){pause.setText("resume");}
           else if (pauseBool){pause.setText("Pause");}

           pauseBool = !pauseBool;
           controller.setPause(pauseBool);
        }));


        //principe mécanique du jeu
        //S'occupe de scroller le background et dessiner le ghost et les obstacles en allant chercher les informations
        //avec le controlleur dans le modele
        AnimationTimer scrollingBg = new AnimationTimer() {

            private long lastTime = 0;
            private double x = 0;

            @Override
            public void handle(long now) {

                double bgSpeed = 120 + 15 * controller.getBgSpeed();

                if (!pauseBool) { //SI jeu en pause, ne rien mettre a jour

                    double deltaTime = (now - lastTime) * 1e-9;
                    x += deltaTime * bgSpeed;

                    controller.next();
                    controller.setdTime(now);
                    debug = debugCheck.isSelected();
                    controller.setDebug(debug);
                    score.setText("Score: "+controller.getScore());

                    context.drawImage(background, 640 - x, 0);
                    context.drawImage(background, -x, 0);
                }


                if(debug) { //Si jeu en mode debug dessiner des cercles

                    context.setFill(Color.rgb(0, 0, 0));
                    context.fillOval(640 / 2, flappy.getLvl().getGhost().getyPosition(), 60, 60);

                    for (int i = 0; i < controller.obstaclesCount(); i++) {
                        Obstacle obs = controller.getObsI(i);
                        if (controller.checkColision(i)) {
                            context.setFill(Color.rgb(255, 0, 0));
                        } else {
                            context.setFill(Color.rgb(255, 255, 0));
                        }
                        context.fillOval(obs.getxPosition(), obs.getyPosition(), obs.getRange() * 2, obs.getRange() * 2);
                    }
                }
                else { //Si jeu ni en pause, ni en mode debug dessiner ghost et obstacles

                    context.drawImage(ghost, 640/2, flappy.getLvl().getGhost().getyPosition(),60,60);
                    for (int i = 0; i< controller.obstaclesCount(); i++){

                        Obstacle obs = controller.getObsI(i);
                        String wayToApparence = "images\\obstacles\\"+obs.getApparence()+".png";
                        try{ Image apparence = new Image(new FileInputStream(wayToApparence));
                            context.drawImage(apparence, obs.getxPosition(), obs.getyPosition(),
                                    obs.getRange()*2, obs.getRange()*2);}
                        catch (IOException e){}
                    }
                }
                lastTime = now;
                if (x >= 635) { x = 0;}
                Platform.runLater(() -> { //Metre a jour le score
                    score.setText("Score: "+controller.getScore());
                });
            }
        };
        scrollingBg.start(); //recommencer jusqu'a fin du jeu
    }

    public static void main(String[] args) {
        launch(args);
    }
}
