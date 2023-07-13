package com.example.ift2905_h21_devoir1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/* date : 2/2/2021
 * noms : Aleksandra Maric (1049140), Abderrahim Tabta (20133680), Christophe Nadeau (20180393),
 *        Guillaume Gagnon (20191696), Hanz Schepens (20189679)
 *
 * Application Android simple visant à effectuet un test du "Model Human Processor".
 * L'application invite l'usager à cliquer sur un bouton le plus rapidement possible et
 * affiche le temps de réaction moyen après 5 essais.
 */

public class MainActivity extends AppCompatActivity {

    // Properties declaration goes here
    Button main_button;
    TextView counter; // "Essai x de 5"
    TextView reaction; // TextView to update with the timer
    Timer timer;
    Boolean state; // False = initial state; True = game in progress

    Boolean result; // False = player clicked too fast; True = Player clicked in the right margin of time
    int gameCounter; // Global counter 0-4
    Handler handler; // Reusing the same handler throughout the code.
    int min, max; // Used for generating a random number between min-max seconds (both are inclusive)
    long t1;

    double[] responseTimes;
    double moyenne;

    // Handler used to update the chronometer display, uses separate thread to keep UI responsive
    private Handler chronometerHandler;
    // timer handling
    private Timer chronometerTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        main_button = findViewById(R.id.main_button);
        counter = findViewById(R.id.counter_Text);
        reaction = findViewById(R.id.timer_Text);

        buttonInitializer();
        counterInitializer();
        chronometerInitializer();

        timer = new Timer();
        chronometerHandler = new Handler();
        handler = new Handler();

        state = false;
        result = false;
        min = 3;
        max = 10;
        responseTimes = new double[5];
    }

    private void counterInitializer() {
        changeTextViewVisibility(counter); // counter visible au 1er clique
        counter.setText(R.string.counter);
    }

    private void chronometerInitializer(){
        changeTextViewVisibility(reaction);
        reaction.setText(R.string.timer_time);
    }

    // default state (invisible) or the active state (visible)
    private void changeTextViewVisibility(TextView textView){
        if (textView.getVisibility() == View.INVISIBLE){
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    // Function to play
    private void game() {
        // Create a handler with random time and changes the color after a set amount of time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                result = true; // Meaning: If the player clicks now, it will be registered as a success
                changeColor(R.color.colorPressButton); // Yellow color -> Pressing time

                // Logic to update the chronometer
                if(chronometerTimer != null)
                    chronometerTimer.cancel();

                chronometerTimer = new Timer();

                // save current time
                t1 = SystemClock.elapsedRealtime();
                // schedule task
                chronometerTimer.scheduleAtFixedRate(new UpdateChronometerTask(), 0, 50);
            }
        },generateRandom()*1000); //adding 3-10 seconds delay
    }

    // Main Button listener
    private final View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Timer logic should go somewhere in here, since it is dependent on when the click is performed.
            // If the counter is under 4 -> proceeds with the tries (5 tries)

            // game is not started yet
            if(!state){
                gameCounter = 0; // Set/reset new game counter
                state = true;
                main_button.setText(R.string.button_wait);

                changeTextViewVisibility(counter); // display # tries
                changeTextViewVisibility(reaction); // display chronometer

                game();
                updateCounter();
            } else {
                // game is already started --> continue current game
                stopTimer();
                if (!result) {
                    handler.removeCallbacksAndMessages(null); // Clearing the queue from other potential calls.
                    changeColor(R.color.colorErrorButton); // Red color
                    main_button.setText(R.string.button_error);

                    // Resetting the view without incrementing
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeColor(R.color.colorInitialButton); // Returning to original color
                            main_button.setText(R.string.button_wait);
                        }
                    },1500); //adding 1.5 second delay
                    game(); // Relaunches the game
                }
                if (result) {
                    handler.removeCallbacksAndMessages(null);
                    changeColor(R.color.colorSuccessButton); // Green color
                    main_button.setText(R.string.button_right); // Success message
                    result = false; // Preparing for the next run

                    if(gameCounter == 4) { // Game end
                        moyenne = 0.0; // reinitialize
                        for (double responseTime : responseTimes) {
                            moyenne += (responseTime / 5);
                        }
                        Arrays.fill(responseTimes, 0.0); // delete previous times

                        Dialog popup = onCreateDialog(null); // Shows at end of the game
                        popup.show();

                    } else { // Next round
                        // Resetting the view with increment
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Reset chronometer display
                                reaction.setText(R.string.timer_time);
                                gameCounter++;
                                changeColor(R.color.colorInitialButton); // original color
                                main_button.setText(R.string.button_wait);
                                updateCounter();
                            }
                        },1500); //adding 1.5 second delay
                        game();
                    }
                }
            }
        }
    };

    // Link the listener to the button and put the starting color (Reset)
    private void buttonInitializer() {
        changeColor(R.color.colorInitialButton);
        main_button.setOnClickListener(buttonListener);
        main_button.setText(R.string.button_start);
    }

    // Takes as an argument R.color.your_color_here (located in the colors.xml)
    private void changeColor(int colorId) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, colorId);
        main_button.setBackgroundTintList(colorStateList);
    }

    private void resetGame() {
        buttonInitializer();
        counterInitializer();
        chronometerInitializer();
        state = false;
        result = false;
    }

    private void updateCounter(){
        int temp = gameCounter + 1;
        counter.setText("Essai " + temp + " de 5");
    }

    // Create AlertDialog
    private Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Le temps moyen de réaction est de " + String.format("%.3f", moyenne)
                + " secondes!").setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetGame();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Stops the timer and saves the value to the array
    private void stopTimer(){
        // Save current response time
        responseTimes[gameCounter] = (double)(SystemClock.elapsedRealtime() - t1) / 1000;

        // In case the timer is not active
        if (chronometerTimer == null) {
            return;
        }
        chronometerTimer.cancel(); // Cancel timer
    }

    // Function to generate a new random number
    private int generateRandom() {
        return new Random().nextInt(max - min + 1) + min;
    }

    class UpdateChronometerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread to avoid blocking UI
            chronometerHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Update the chronometer's value
                    reaction.setText(Double.toString((double)(SystemClock.elapsedRealtime() - t1) / 1000));
                }
            });
        }
    }
}