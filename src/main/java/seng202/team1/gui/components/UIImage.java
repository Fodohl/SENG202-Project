package seng202.team1.gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * A UI Component to handle rendering images
 *
 * Supports loading the default set of javafx supported images, aswell as svgs. Safely loads such that a empty node will
 * be returned if file doesn't exist, and will log an warning.
 */
public class UIImage extends StackPane {

    /// UImage specific logger
    private static final Logger log = LogManager.getLogger(UIImage.class);

    /**
     * Constructs a new UI Image
     *
     * @param path The path to the image file
     * @param width How many pixels across the image should be rendered as
     * @param height How many pixels tall the image should be rendered as
     */
    public UIImage(String path, double width, double height){

        // Ensure stack pane is same dimensions
        setPrefSize(width, height);
        setAlignment(Pos.CENTER);

        // Check that the image exists
        InputStream is = UIImage.class.getResourceAsStream(path);
        if(is == null){
            log.warn("Invalid image path:  {}", path);
            return;
        }

        // Load image
        getChildren().add(path.endsWith(".svg") ? loadSvgImage(path, width, height)
                                                : loadImage(is, width, height));

    }

    /**
     * Load a Scalable Vector Graphics (SVG) image file
     *
     * @param path The path to the svg
     * @param width How many pixels across the svg should be scaled to
     * @param height How many pixels tall the svg should be scaled to
     * @return A scaled SVG container
     */
    private Node loadSvgImage(String path, double width, double height) {

        // Load image
        URL fullpath = getClass().getResource(path);
        SVGImage img = SVGLoader.load(fullpath);

        // Preserve raito
        double scaleX = width / img.getLayoutBounds().getWidth();
        double scaleY = height / img.getLayoutBounds().getHeight();
        double scale = Math.min(scaleX, scaleY);

        // Scale image to size
        img.setScaleX(scale);
        img.setScaleY(scale);

        // @todo change to svg-icon & svg-icon-fill
        img.getStyleClass().add("svg-icon");
        return img;

    }

    /**
     * Load a standard image file from a stream
     *
     * @param stream The opened image file stream
     * @param width How many pixels across the image should be rendered as
     * @param height How many pixels tall the image should be rendered as
     * @return The resized image view for the image
     */
    private ImageView loadImage(InputStream stream, double width, double height){

        // Load image
        Image img = new Image(stream);
        ImageView imageView = new ImageView(img);

        // Resize to dims
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return imageView;
    }
}
