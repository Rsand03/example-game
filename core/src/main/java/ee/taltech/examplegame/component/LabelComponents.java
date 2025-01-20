package ee.taltech.examplegame.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import static ee.taltech.examplegame.util.Font.getPixelFont;

public class LabelComponents {

    private static final int LABEL_BACKGROUND_PADDING = 10;
    private static final int MIN_LABEL_BACKGROUND_WIDTH = 75;


    public static Label createLabel(String text, Color textColor, int size) {
        BitmapFont font = getPixelFont(size);
        LabelStyle labelStyle = new LabelStyle(font, textColor);

        return new Label(text, labelStyle);
    }

    public static Label createLabelWithBackground(String text, Color textColor, int size) {
        BitmapFont font = getPixelFont(size);
        LabelStyle labelStyle = new LabelStyle(font, textColor);

        Label label = new Label(text, labelStyle);
        label.setAlignment(Align.center); // Align text to the center of the background

        Pixmap labelBackground = new Pixmap(
            Math.max(MIN_LABEL_BACKGROUND_WIDTH, (int) label.getWidth() + LABEL_BACKGROUND_PADDING),
            (int) label.getHeight(),
            Pixmap.Format.RGB888
        );

        labelBackground.setColor(Color.DARK_GRAY);
        labelBackground.fill();
        label.getStyle().background = new Image(new Texture(labelBackground)).getDrawable();
        labelBackground.dispose();  // Dispose to prevent unnecessary RAM usage (small leaks will accumulate quickly)
        return label;
    }

}
