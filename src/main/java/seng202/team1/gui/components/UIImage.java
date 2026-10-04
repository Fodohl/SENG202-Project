package seng202.team1.gui.components;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A UI Component to handle rendering images
 * <p>
 * Supports loading the default set of javafx supported images, aswell as svgs. Safely loads such that a empty node will
 * be returned if file doesn't exist, and will log an warning.
 */
public class UIImage extends StackPane {

    /// UImage specific logger
    private static final Logger log = LogManager.getLogger(UIImage.class);

    /// How many pixels across the image should be rendered as
    private double width;

    /// How many pixels tall the image should be rendered as
    private double height;

    /// Where downloaded web images are kept (never expire)
    private static final Path CACHE_DIR = Path.of(System.getProperty("user.home"), ".trashapp/cache/images");


    /**
     * Constructs a new UI Image
     *
     * @param path   The path to the image file
     * @param width  How many pixels across the image should be rendered as
     * @param height How many pixels tall the image should be rendered as
     */
    public UIImage(String path, double width, double height) {

        // Ensure stack pane is same dimensions
        setPrefSize(width, height);
        setAlignment(Pos.CENTER);

        this.width = width;
        this.height = height;

        renderImage(path);
    }

    /**
     * Load a image from a path onto this component
     *
     * @param path The image to load, if it exsists
     */
    private void renderImage(String path) {

        // NO image to load
        if(path == null)
            return;

        // Image not on disk
        if (path.startsWith("http")) {
            loadWebImage(path);
            return;
        }

        // Check that the image exists
        InputStream is = UIImage.class.getResourceAsStream(path);
        if (is == null) {
            log.warn("Invalid image path:  {}", path);
            return;
        }

        // Load image
        getChildren().setAll(path.endsWith(".svg") ? loadSvgImage(path) : loadImage(is));
    }

    /**
     * Load a Scalable Vector Graphics (SVG) image file
     *
     * @param path The path to the svg
     * @return A scaled SVG container
     */
    private Node loadSvgImage(String path) {

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

        // Allow for the svg to CSS targetable
        img.getStyleClass().add("svg-icon");
        if (hasFill(img))
            img.getStyleClass().add("svg-icon-fill");

        return img;

    }

    /**
     * Get whether this SVG element has fill or is an outline
     *
     * @param parent The svg node group to check
     * @return True if this is a filled svg icon
     */
    private boolean hasFill(Parent parent) {

        for (Node child : parent.getChildrenUnmodifiable()) {

            // libSVG converts to shapes with corresponding tags
            if (child instanceof Shape)
                if (((Shape) child).getFill() != null)
                    return true;

            // Not shape so check children
            if (child instanceof Parent)
                if (hasFill((Parent) child))
                    return true;
        }

        return false;
    }

    /**
     * Load a standard image file from a stream
     *
     * @param stream The opened image file stream
     * @return The resized image view for the image
     */
    private ImageView loadImage(InputStream stream) {

        // Load image
        Image img = new Image(stream);
        ImageView imageView = new ImageView(img);

        return setupImageView(imageView);
    }

    /**
     * Resizes a image view to fit the desired dimensions
     *
     * @param imageView The image to size
     * @return The resized view
     */
    private ImageView setupImageView(ImageView imageView) {

        // Resize to dims
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return imageView;

    }

    /**
     * Load and cache a image from a online URL
     *
     * @param path The image to load
     * @return The loaded image
     */
    private void loadWebImage(String path) {

        // Show loader
        String webPath = path.trim().replace(" ", "%20");
        getChildren().setAll(new UISpinner(Math.min(width, height) / 2));

        Thread thread = new Thread(() -> {

            try {

                // Load image
                Image img = new Image(fetchImage(webPath));
                ImageView imageView = new ImageView(img);

                Platform.runLater(() -> getChildren().setAll(setupImageView(imageView)));

            } catch (Exception e) {
                log.warn("Invalid image path:  {}", webPath);
            }

        });
        thread.start();
    }

    /**
     * Load an image from the cache, downloading it to the cahce if it doesnt exist
     *
     * @param path The image to download/load
     * @return The image
     */
    private InputStream fetchImage(String path) throws IOException {

        // Parse the URL
        URI url = URI.create(path);
        String filename = Paths.get(url.getPath()).getFileName().toString();
        Path file = CACHE_DIR.resolve(filename);

        // Make sure cache exists
        Files.createDirectories(CACHE_DIR);

        // Image not yet cached
        if(!Files.exists(file)){
            InputStream is = url.toURL().openStream();
            Files.copy(is, file);
        } else{
            log.info("Cache hit for image: {} -> {}", path, file);
        }

        return Files.newInputStream(file);
    }

    /**
     * Convert the image to a raw image buffer
     *
     * @see #toRawImage()
     */
    public Image toRawImage() {
        return toRawImage(Color.TRANSPARENT);
    }

    /**
     * Convert the image to a raw image buffer
     *
     * @param colour The background colour to place the image on
     * @return The snapshot of this svg image
     */
    public Image toRawImage(Color colour) {

        // Ensure transparency is kept
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(colour);

        // Write the component to an image
        return this.snapshot(params, null);
    }

    /**
     * Update this image to show a different image
     *
     * @param path The image to show
     */
    public void setSource(String path) {
        renderImage(path);
    }
}
