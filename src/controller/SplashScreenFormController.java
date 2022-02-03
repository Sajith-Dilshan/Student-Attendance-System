package controller;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class SplashScreenFormController {


    public ProgressBar pgb;
    public Label lblStatus;

    public void initialize(){
        pgb.setStyle("-fx-accent: orange;");

        new Thread(() -> {
            for (double i = 0; i <=100; i += 0.1) {

                pgb.setProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }






}
