package ee.taltech.examplegame.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import static ee.taltech.examplegame.util.Font.getPixelFont;

public class TextLabel {

    public static Label createLabel(String text, Color textColor, int size) {
        BitmapFont font = getPixelFont(size);
        LabelStyle labelStyle = new LabelStyle(font, textColor);
        return new Label(text, labelStyle);
    }

}
