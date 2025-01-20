package ee.taltech.examplegame.component;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static ee.taltech.examplegame.util.Font.getPixelFont;

public class ButtonComponents {

    public static TextButton getButton(int fontSize, String text, OnClickHandler onClickHandler) {
        // styling the button
        var style = new TextButton.TextButtonStyle();
        style.fontColor = BLACK;
        style.font = getPixelFont(fontSize);
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button/up.png")));
        style.over = new TextureRegionDrawable(new TextureRegion(new Texture("button/down.png")));

        // adding padding
        var button = new TextButton(text, style);
        button.pad(4);
        button.padLeft(8);
        button.padRight(8);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClickHandler.handleClick();
                super.clicked(event, x, y);
            }
        });
        return button;
    }

    public interface OnClickHandler {
        void handleClick();
    }
}
