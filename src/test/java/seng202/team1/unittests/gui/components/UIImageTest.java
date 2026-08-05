package seng202.team1.unittests.gui.components;

import org.girod.javafx.svgimage.SVGImage;
import org.junit.jupiter.api.Test;
import seng202.team1.gui.components.UIImage;
import javafx.scene.image.ImageView;
import seng202.team1.unittests.TrashAppTest;


import static org.junit.jupiter.api.Assertions.*;

class UIImageTest extends TrashAppTest {

    @Test
    public void shouldNotCrashIfImageDoesntExist(){

        // Should not add a image node if doesn't exist
        UIImage image = new UIImage("/xyz/this_wont_exist.notafile", 10,10);
        assertTrue(image.getChildren().isEmpty());

    }

    @Test
    public void shouldBeAbleToLoadSvg(){

        // Should not add a image node if doesn't exist
        UIImage image = new UIImage("/images/bin.svg", 10,10);
        assertInstanceOf(SVGImage.class, image.getChildren().getFirst());

    }

    @Test
    public void shouldLoadStandardImagesNativesly(){

        // Should not add a image node if doesn't exist
        UIImage image = new UIImage("/images/bin.png", 10,10);
        assertInstanceOf(ImageView.class, image.getChildren().getFirst());

    }
}