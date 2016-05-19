package com.villasis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;



public class StartScreen implements Screen {

    private static final float WORLD_WIDTH = 1024;
    private static final float WORLD_HEIGHT = 768;

    private Stage stage;
    private Texture back;
    private Texture playUp;
    private Texture playDown;
    private Texture exitUp;
    private Texture exitDown;
    private MyGdxGame game;

    public StartScreen(MyGdxGame game){
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        back = game.getAssetManager().get("Title.png");
        Image background = new Image(back);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////// Creating a Button /////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        playUp = game.getAssetManager().get("playButtonUp.png");
        playDown = game.getAssetManager().get("playButtonDown.png");
        ImageButton playButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playUp)),
                new TextureRegionDrawable(new TextureRegion(playDown)));

        playButton.setPosition(WORLD_WIDTH/2, 350, Align.center);

        playButton.addListener(new ActorGestureListener(){
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameplayScreen(game));
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////// Creating a Button /////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////// Creating a Button /////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        exitUp = new Texture(Gdx.files.internal("exitButtonUp.png"));
        exitDown = new Texture(Gdx.files.internal("exitButtonDown.png"));
        ImageButton exitButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(exitUp)),
                new TextureRegionDrawable(new TextureRegion(exitDown)));

        exitButton.setPosition(WORLD_WIDTH/2, 207, Align.center);

        exitButton.addListener(new ActorGestureListener(){
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                Gdx.app.exit();
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////// Creating a Button /////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        stage.addActor(background);
        stage.addActor(playButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);

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

    }
}
