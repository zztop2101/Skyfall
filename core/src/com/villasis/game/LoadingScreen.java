package com.villasis.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private final MyGdxGame game;
    private float progress = 0;

    public LoadingScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        game.getAssetManager().load("map1_32.tmx", TiledMap.class);
        game.getAssetManager().load("map3_32.tmx", TiledMap.class);
        game.getAssetManager().load("map2_32.tmx", TiledMap.class);
        game.getAssetManager().load("map4_32.tmx", TiledMap.class);
        game.getAssetManager().load("map5_32.tmx", TiledMap.class);
        game.getAssetManager().load("map6_32.tmx", TiledMap.class);
        game.getAssetManager().load("map7_32.tmx", TiledMap.class);
        game.getAssetManager().load("map8_32.tmx", TiledMap.class);
        game.getAssetManager().load("map9_32.tmx", TiledMap.class);
        game.getAssetManager().load("map10_32.tmx", TiledMap.class);
        game.getAssetManager().load("map11_32.tmx", TiledMap.class);
        game.getAssetManager().load("map12_32.tmx", TiledMap.class);
        game.getAssetManager().load("map13_32.tmx", TiledMap.class);
        game.getAssetManager().load("map14_32.tmx", TiledMap.class);
        game.getAssetManager().load("map15_32.tmx", TiledMap.class);
        game.getAssetManager().load("map16_32.tmx", TiledMap.class);
        game.getAssetManager().load("map17_32.tmx", TiledMap.class);
        game.getAssetManager().load("map18_32.tmx", TiledMap.class);
        game.getAssetManager().load("map19_32.tmx", TiledMap.class);
        game.getAssetManager().load("mapExit_32.tmx", TiledMap.class);
        game.getAssetManager().load("dragonDweeler.png", Texture.class);
        game.getAssetManager().load("playButtonUp.png", Texture.class);
        game.getAssetManager().load("playButtonDown.png", Texture.class);
        game.getAssetManager().load("Title.png", Texture.class);
        game.getAssetManager().load("guy.png", Texture.class);

    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin();
        shapeRenderer.rect(WORLD_WIDTH - PROGRESS_BAR_WIDTH/2,
                WORLD_HEIGHT/2 - PROGRESS_BAR_HEIGHT/2,
                progress * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_WIDTH);
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update() {
        if(game.getAssetManager().update()) {
            game.setScreen(new StartScreen(game));
        }
        else {
            progress = game.getAssetManager().getProgress();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        shapeRenderer.dispose();
    }
}