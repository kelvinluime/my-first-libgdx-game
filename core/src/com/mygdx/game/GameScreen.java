package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {

    private final MyGdxGame game;
    private Texture ballTexture;
    private Texture boardTexture;
    private Texture numbersTexture;
    private TextureRegion[][] numbers;
    private Rectangle board;
    private Rectangle ball;
    private float gameStartCounter;
    private boolean isGameBegun;
    private final int DEFAULT_BOARD_VEL = 300;
    private final int DEFAULT_BOARD_FAST_VEL = 3 * DEFAULT_BOARD_VEL;
    private int boardVel;
    private int ballVerticalVel;
    private int ballHorizontalVel;
    private final int AIR_RESISTANCE = 150;
    private BoardDirection boardDirection;
    private OrthographicCamera camera;

    public GameScreen(final MyGdxGame game) {
        this.game = game;

        ballTexture = new Texture(Gdx.files.internal("ball.png"));
        boardTexture = new Texture(Gdx.files.internal("board.png"));
        numbersTexture = new Texture(Gdx.files.internal("numbers.png"));

        numbers = TextureRegion.split(numbersTexture, numbersTexture.getWidth() / 3,
                numbersTexture.getHeight() / 5);

        ball = new Rectangle();
        board = new Rectangle();

        ball.width = 32;
        ball.height = 32;
        ball.x = 384;
        ball.y = 300;

        board.width = 128;
        board.height = 16;
        board.x = 336;
        board.y = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        gameStartCounter = 0f;
        ballVerticalVel = -300;
        ballHorizontalVel = 0;
        boardVel = 0;
        isGameBegun = false;
        boardDirection = BoardDirection.STATIONARY;

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!isGameBegun || isGameBegun && gameStartCounter < 1) {
            gameStartCounter += delta;
        }

        game.batch.begin();
        game.batch.draw(ballTexture, ball.x, ball.y);
        game.batch.draw(boardTexture, board.x, board.y);
        drawGameCounter();
        game.batch.end();

        if(gameStartCounter >= 3){
            isGameBegun = true;
            gameStartCounter = 0;
        }

        if(isGameBegun && gameStartCounter > 1){
            reactToPlayerInput(delta);
        }

        boundObjectsInGame();

    }

    private void boundObjectsInGame(){
        // makes sure the board would not go out the game's frame
        if(board.x < 0){
            board.x = 0;
            boardDirection = BoardDirection.STATIONARY;
        } else if(board.x > 800 - board.width) {
            board.x = 800 - board.width;
            boardDirection = BoardDirection.STATIONARY;
        }

        // makes the ball rebound when it collides with the walls or the board
        if(ball.overlaps(board) && ball.y >= board.y) {
            switch(boardDirection){
                case LEFT:
                    ballHorizontalVel = -DEFAULT_BOARD_VEL;
                    break;
                case RIGHT:
                    ballHorizontalVel = DEFAULT_BOARD_VEL;
                    break;
                case FAST_LEFT:
                    ballHorizontalVel = -DEFAULT_BOARD_FAST_VEL;
                    break;
                case FAST_RIGHT:
                    ballHorizontalVel = DEFAULT_BOARD_FAST_VEL;
                    break;
                case STATIONARY: // do nothing when the board is stationary
                    break;
            }
            ballVerticalVel = 300;
        }
        if(ball.y > 480 - ball.height){
            ballVerticalVel = -ballVerticalVel;
        } else if(ball.y < -ball.width){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(ball.x < 0 || ball.x > 800 - ball.width){
            ballHorizontalVel = -ballHorizontalVel;
        }
    }

    private void drawGameCounter(){
        if(gameStartCounter >= 3 && !isGameBegun || isGameBegun && gameStartCounter <= 1){
            game.batch.draw(numbers[0][0], 0, 432);
        } else if(gameStartCounter >= 2 && !isGameBegun){
            game.batch.draw(numbers[0][1], 0, 432);
        } else if(gameStartCounter >= 1 && !isGameBegun){
            game.batch.draw(numbers[0][2], 0, 432);
        }
    }

    private void reactToPlayerInput(float delta){
        // board's movement based on player's input
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                boardDirection = BoardDirection.FAST_LEFT;
                boardVel = -DEFAULT_BOARD_FAST_VEL;
            } else {
                boardDirection = BoardDirection.LEFT;
                boardVel = -DEFAULT_BOARD_VEL;
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                boardDirection = BoardDirection.FAST_RIGHT;
                boardVel = DEFAULT_BOARD_FAST_VEL;
            } else {
                boardDirection = BoardDirection.RIGHT;
                boardVel = DEFAULT_BOARD_VEL;
            }
        } else {
            boardDirection = BoardDirection.STATIONARY;
            boardVel = 0;
        }

        board.x += boardVel * delta;

        // ball slows down due to air resistance if it moves faster than the speed of 300 pixels/s
        if(ballHorizontalVel > 300){
            ballHorizontalVel -= AIR_RESISTANCE * delta;
        } else if(ballHorizontalVel < -300){
            ballHorizontalVel += AIR_RESISTANCE * delta;
        }
        // ball's movement
        ball.x += ballHorizontalVel * delta;
        ball.y += ballVerticalVel * delta;
    }

    private enum BoardDirection { STATIONARY, LEFT, RIGHT, FAST_LEFT, FAST_RIGHT }

    @Override
    public void show() {

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
    public void dispose(){
        ballTexture.dispose();
        boardTexture.dispose();
        numbersTexture.dispose();
    }
}
