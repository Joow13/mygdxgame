package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Espace {
    Texture texture = new Texture("space.jpg");

    public void render(SpriteBatch batch) {
        batch.draw(texture, 0, 0, 640, 480);
    }
}
