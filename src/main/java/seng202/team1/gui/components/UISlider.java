package seng202.team1.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng202.team1.gui.GraphicsCommon;

// @credit https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx

public class UISlider extends VBox implements UIInputComponent {

    private boolean isErroring = false;
    private double percentage;

    private Slider slider = new Slider();
    private Label percetageDsiplay = new Label();

    /// The label to show invalid input messages in
    private Label errorText = new Label();

    public UISlider() {
        super(5);

        // Style the fullness of the slider
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {

            // Clear the error
            setError("");

            // Show the percentage
            percentage = (Double) newVal;
            updateTrackStyle();

        });

        // Combine
        HBox sliderComponent = new HBox(10, slider, percetageDsiplay);
        getChildren().addAll(sliderComponent, errorText);

        slider.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(slider, Priority.ALWAYS);
    }

    private void updateTrackStyle() {
        percetageDsiplay.setText(String.format("%.2f%%", percentage));

        String colour = isErroring ? "-text-error" : "-active";

        // Ensure track has rendered
        Node track = lookup(".track");
        if (track == null)
            return;

        // Custom gradient that is active 0-x and default x-100
        track.setStyle(
                String.format("-fx-background-color: linear-gradient(to right, %s 0%%, %s %.1f%%, -text-secondary-half %.1f%%, -text-secondary-half 100%%);", colour, colour, percentage, percentage)
        );
    }

    /**
     * Get the value of the slider
     *
     * @return The selected value
     */
    @Override
    public Double getInput() {
        return slider.getValue();
    }

    /**
     * Set the input to a desired value
     *
     * @param value The new float input value, must bet 0-1
     */
    @Override
    public void setInput(Object value) {

        if (value instanceof Double)
            slider.setValue((Double) value);
    }

    /**
     * Get whether a user has entered input yet
     * <p>
     * Note: if true then getInput() will not be null
     *
     * @return True if the user has inputted a value
     */
    @Override
    public boolean hasInput() {
        return true;
    }

    /**
     * Reset the value of the input to default, clearing any errors
     */
    @Override
    public void clear() {
        slider.setValue(0.0);
        setError("");
    }

    /**
     * Check if the current input has resulted in an error
     *
     * @return True if there is an error set
     */
    @Override
    public boolean isErroring() {
        return isErroringInternal(errorText);
    }

    /**
     * Set the error for this input
     *
     * @param error The error message
     */
    @Override
    public void setError(String error) {

        // Extra erroring style
        isErroring = !error.isEmpty();
        updateTrackStyle();

        setIsErrorInternal(error, this, errorText);
    }
}
