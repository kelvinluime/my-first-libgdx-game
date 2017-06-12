package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class GameOverScreen implements Screen {

    private final MyGdxGame game;
    private Texture backgroundTexture;
    private Texture redPlay;
    private Texture whitePlay;
    private Queue<Texture> playQueue;
    private OrthographicCamera camera;
    private float buttonStateTime;

    public GameOverScreen(final MyGdxGame game){

        this.game = game;

        backgroundTexture = new Texture(Gdx.files.internal("gameover.png"));
        redPlay = new Texture(Gdx.files.internal("play-red.png"));
        whitePlay = new Texture(Gdx.files.internal("play-white.png"));
        playQueue = new LinkedBlockingDeque<Texture>();
        playQueue.add(whitePlay);
        playQueue.add(redPlay);

        buttonStateTime = 0f;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        buttonStateTime += delta;
        if(buttonStateTime >= 0.5){
            playQueue.add(playQueue.remove());
            buttonStateTime = 0f;
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.disableBlending();
        game.batch.draw(backgroundTexture, 0 ,0);
        game.batch.enableBlending();
        game.batch.draw(playQueue.peek(), 272, 100);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        redPlay.dispose();
        whitePlay.dispose();
    }
}
