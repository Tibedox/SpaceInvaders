package ru.samsung.spaceinvaders;

import static ru.samsung.spaceinvaders.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class SpaceButton {
    BitmapFont font;
    String text;
    float x, y;
    float width, height;

    public SpaceButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public SpaceButton(BitmapFont font, String text, float y) {
        this.font = font;
        this.text = text;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = SCR_WIDTH/2-width/2;
    }

    public void setText(String text){
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        this.x = SCR_WIDTH/2-width/2;
    }

    public boolean hit(float tx, float ty){
        return x<tx && tx<x+width && y>ty && ty>y-height;
    }

    public boolean hit(Vector3 t){
        return x<t.x && t.x<x+width && y>t.y && t.y>y-height;
    }
}
