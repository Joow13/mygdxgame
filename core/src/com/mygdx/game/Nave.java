package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Nave {

    Animacion animacion = new Animacion(16,
            new Texture("nave_luces.png"),
            new Texture("nave_luces2.png"),
            new Texture("nave_luces3.png")
    );


    float x, y, w, h, v;
    List<Bala> disparos = new ArrayList<>();
    int vidas = 3;
    int puntos = 0;
    boolean muerto = false;
    Temporizador temporizadorFireRate = new Temporizador(20);
    Temporizador temporizadorRespawn = new Temporizador(120, false);
    public Sound laser = Gdx.audio.newSound(Gdx.files.internal("disparo.mp3"));

    Nave() {
        x = 100;
        y = 100;
        w = 43 * 3;
        h = 21 * 3;
        v = 5;
    }

    void update() {
        for (Bala disparo : disparos) disparo.update();

        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += v;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= v;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) y += v;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= v;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && temporizadorFireRate.suena() && !muerto) {
            disparos.add(new Bala(x + w / 2, y + h/2));
            laser.play();
        }

        if (x < 0) x = 0;

        if (temporizadorRespawn.suena()) {
            muerto = false;
        }
    }

    void render(SpriteBatch batch) {
        batch.draw(animacion.obtenerFrame(), x, y, w, h);
        for (Bala disparo : disparos) disparo.render(batch);
    }

    public void morir() {
        vidas--;
        muerto = true;
        temporizadorRespawn.activar();
    }
}

