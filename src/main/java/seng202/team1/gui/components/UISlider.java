package seng202.team1.gui.components;

import javafx.scene.control.Slider;


//@credit https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx
public class UISlider extends Slider {

    public UISlider(){

        // Style the fullness of the slider
        valueProperty().addListener((obs, oldVal, newVal) -> {

                double percentage = (Double)newVal;

                // Custom gradient that is active 0-x and default x-100
                lookup(".track").setStyle(
                    String.format("-fx-background-color: linear-gradient(to right, -active 0%%, -active %.1f%%, -text-secondary-half %.1f%%, -text-secondary-half 100%%);", percentage, percentage)
                );
            });
    }

}
