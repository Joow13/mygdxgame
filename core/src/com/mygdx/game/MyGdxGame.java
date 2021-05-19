package com.mygdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;


public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;
	BitmapFont font;
	Espace espace;
	Nave nave;
	List<Alien> enemigos = new ArrayList<>();
	List<Bala> disparosAEliminar = new ArrayList<>();
	List<Alien> enemigosAEliminar = new ArrayList<>();
	Sound sound;


	Temporizador temporizadorNuevoAlien = new Temporizador(120);



	private ScoreBoard scoreboard;
	private boolean gameover;


	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font.getData().setScale(2f);

		inicializarJuego();

		Sound sound = Gdx.audio.newSound(Gdx.files.internal("espace_alien.mp3"));
		sound.setLooping(sound.play(1.0f), true);


	}

	void inicializarJuego() {
		espace = new Espace();
		nave = new Nave();
		enemigos = new ArrayList<>();
		temporizadorNuevoAlien = new Temporizador(120);
		disparosAEliminar = new ArrayList<>();
		enemigosAEliminar = new ArrayList<>();
		scoreboard = new ScoreBoard();
		gameover = false;
	}

	void update() {
		Temporizador.framesJuego += 1;

		if (temporizadorNuevoAlien.suena()){
			enemigos.add(new Alien("alien"));
			enemigos.add(new Alien("alien2"));
			enemigos.add(new Alien("alien3"));
		}



		if (!gameover) nave.update();

		for (Alien enemigo : enemigos) enemigo.update();              // enemigos.forEach(Enemigo::update);


		for (Alien enemigo : enemigos) {
			for (Bala disparo : nave.disparos) {
				if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo.x, enemigo.y, enemigo.w, enemigo.h)) {
					disparosAEliminar.add(disparo);
					enemigosAEliminar.add(enemigo);
					nave.puntos++;
					break;
				}
			}

			if (!gameover && !nave.muerto && Utils.solapan(enemigo.x, enemigo.y, enemigo.w, enemigo.h, nave.x, nave.y, nave.w, nave.h)) {
				nave.morir();
				if (nave.vidas == 0) {
					gameover = true;
				}
			}

			if (enemigo.x < -enemigo.w) enemigosAEliminar.add(enemigo);
		}


			for (Bala disparo : nave.disparos)
				if (disparo.x > 640)
					disparosAEliminar.add(disparo);

			for (Bala disparo : disparosAEliminar)
				nave.disparos.remove(disparo);       // disparosAEliminar.forEach(disparo -> jugador.disparos.remove(disparo));
			for (Alien enemigo : enemigosAEliminar)
				enemigos.remove(enemigo);               // enemigosAEliminar.forEach(enemigo -> enemigos.remove(enemigo));
			disparosAEliminar.clear();
			enemigosAEliminar.clear();

			if (gameover) {
				int result = scoreboard.update(nave.puntos);
				if (result == 1) {
					inicializarJuego();
				} else if (result == 2) {
					Gdx.app.exit();
				}
			}
		}

		@Override
		public void render() {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			update();

			batch.begin();
			espace.render(batch);
			nave.render(batch);
			for (Alien enemigo : enemigos) enemigo.render(batch);  // enemigos.forEach(e -> e.render(batch));


			font.draw(batch, "" + nave.vidas, 590, 440);
			font.draw(batch, "" + nave.puntos, 30, 440);

			if (gameover) {
				scoreboard.render(batch, font);
			}
			batch.end();
		}
	}

