package seng202.team1.gui.components;

//@credit https://docs.oracle.com/javase/8/javafx/api/javafx/animation/RotateTransition.html


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class UISpinner extends UIImage {

    public UISpinner(double width, double height) {
        super("/media/images/loading.svg", width, height);

        // Spin anim
        RotateTransition rt = new RotateTransition(Duration.millis(3000), this);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }


}
