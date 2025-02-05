package ru.samsung.raidespacex;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class SpaceXButton {
    BitmapFont font;
    String text;
    float x, y;
    float width, height;

    public SpaceXButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    boolean hit(float tx, float ty){
        return x<tx && tx<x+width && y>ty && ty>y-height;
    }

    boolean hit(Vector3 t){
        return x<t.x && t.x<x+width && y>t.y && t.y>y-height;
    }
}
