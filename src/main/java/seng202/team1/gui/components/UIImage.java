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

public class UIImage extends StackPane {

    private static final Logger log = LogManager.getLogger(UIImage.class);

    public UIImage(String path, double width, double height){
        super();

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

    private Node loadSvgImage(String path, double width, double height) {

        // Load image
        URL fullpath = getClass().getResource(path);
        SVGImage img = SVGLoader.load(fullpath);
        Image fxImage = img.toImage((int) width);

        ImageView imageView = new ImageView(fxImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return img;

    }

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
