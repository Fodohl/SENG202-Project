package seng202.team1.gui.components;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

/**
 * A UI Component to indicate something is laoding
 *
 * @credit reference example code on https://docs.oracle.com/javase/8/javafx/api/javafx/animation/RotateTransition.html
 */
public class UISpinner extends UIImage {

    /**
     * Constructs a new UISpinner
     *
     * @param size The size of the spinner image (in pixels)
     */
    public UISpinner(double size) {
        super("/media/images/loading.svg", size, size);

        // Spin anim
        RotateTransition rt = new RotateTransition(Duration.millis(3000), this);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }
}
