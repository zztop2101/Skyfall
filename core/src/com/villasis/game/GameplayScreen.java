package com.villasis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 1024;
    private static final float WORLD_HEIGHT = 768;
    private static final float CELL_SIZE = 32;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private final MyGdxGame game;

    Area[][] overWorld = new Area[10][10];
    int playerRow = 4;
    int playerCol = 4;
    Area currentArea;

    public static Array<Bullet> playerBullets = new Array<Bullet>();

    private TiledMap tiledMap;

    private Player player;

    // Keys
    boolean hasOpened = false;
    boolean hasWhiteKey = false;
    boolean hasSkyKey = false;
    boolean hasBlueKey = false;
    boolean hasBlackKey = false;
    boolean hasBloodKey = false;
    boolean hasGreenKey = false;
    boolean hasPinkKey = false;
    boolean hasRedKey = false;


    public GameplayScreen(MyGdxGame game) {
        this.game = game;
        makeOverWorld();
    }


    private void makeOverWorld() {
        overWorld[2][3] = new Area((TiledMap) game.getAssetManager().get("map1_32.tmx"));
        overWorld[3][3] = new Area((TiledMap) game.getAssetManager().get("map2_32.tmx"));
        overWorld[4][4] = new Area((TiledMap) game.getAssetManager().get("map3_32.tmx"));
        overWorld[3][4] = new Area((TiledMap) game.getAssetManager().get("map4_32.tmx"));
        overWorld[4][3] = new Area((TiledMap) game.getAssetManager().get("map5_32.tmx"));
        overWorld[5][4] = new Area((TiledMap) game.getAssetManager().get("map6_32.tmx"));
        overWorld[4][5] = new Area((TiledMap) game.getAssetManager().get("map7_32.tmx"));
        overWorld[5][5] = new Area((TiledMap) game.getAssetManager().get("map8_32.tmx"));
        overWorld[6][5] = new Area((TiledMap) game.getAssetManager().get("map9_32.tmx"));
        overWorld[5][6] = new Area((TiledMap) game.getAssetManager().get("map10_32.tmx"));
        overWorld[4][6] = new Area((TiledMap) game.getAssetManager().get("map11_32.tmx"));
        overWorld[3][6] = new Area((TiledMap) game.getAssetManager().get("map12_32.tmx"));
        overWorld[6][4] = new Area((TiledMap) game.getAssetManager().get("map13_32.tmx"));
        overWorld[6][3] = new Area((TiledMap) game.getAssetManager().get("map14_32.tmx"));
        overWorld[5][3] = new Area((TiledMap) game.getAssetManager().get("map15_32.tmx"));
        overWorld[2][4] = new Area((TiledMap) game.getAssetManager().get("map16_32.tmx"));
        overWorld[2][5] = new Area((TiledMap) game.getAssetManager().get("map17_32.tmx"));
        overWorld[3][5] = new Area((TiledMap) game.getAssetManager().get("map18_32.tmx"));
        overWorld[2][6] = new Area((TiledMap) game.getAssetManager().get("map19_32.tmx"));
        overWorld[6][6] = new Area((TiledMap) game.getAssetManager().get("mapExit_32.tmx"));
        currentArea = overWorld[4][4];
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        spriteBatch = new SpriteBatch();
        tiledMap = game.getAssetManager().get("map1_32.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(currentArea.getMap(), spriteBatch);
        tiledMapRenderer.setView(camera);

        Texture temp = game.getAssetManager().get("dragonDweeler.png");
        player = new Player(temp);
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();
        //System.out.println(whichCellsDoesPlayerCover());
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin();
        player.drawDebug(shapeRenderer);

        //for every bullet
        for(Bullet b: playerBullets) {
            b.drawDebug(shapeRenderer);
        }

        shapeRenderer.end();
    }

    private void draw() {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        tiledMapRenderer.render();
        spriteBatch.begin();
        player.draw(spriteBatch);
        spriteBatch.end();
    }

    private void stopPlayerLeavingTheScreen() {
        // bottom of Screen
        if(player.getY() < 5) {
            player.setPosition(player.getX(), WORLD_HEIGHT - 20);
            playerRow++;
            currentArea = overWorld[playerRow][playerCol];
            tiledMapRenderer.setMap(currentArea.getMap());
            playerBullets.clear();
        }
        // Top of the Screen
        if (player.getY() > WORLD_HEIGHT - 15){
            player.setPosition(player.getX(),5);
            playerRow --;
            currentArea = overWorld[playerRow][playerCol];
            tiledMapRenderer.setMap(currentArea.getMap());
            playerBullets.clear();
        }
        // Left of Screen
        if(player.getX() < 0) {
            player.setPosition(WORLD_WIDTH - Player.WIDTH, player.getY());
            playerCol--;
            currentArea = overWorld[playerRow][playerCol];
            tiledMapRenderer.setMap(currentArea.getMap());
            playerBullets.clear();
        }
        if(player.getX() + Player.WIDTH > WORLD_WIDTH) {
            player.setPosition(0, player.getY());
            playerCol++;
            currentArea = overWorld[playerRow][playerCol];
            tiledMapRenderer.setMap(currentArea.getMap());
            playerBullets.clear();
        }

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta) {
        player.update(delta);
        stopPlayerLeavingTheScreen();
        handlePlayerCollision();
        updateBullets(delta);
    }

    private void updateBullets(float delta) {
        for(Bullet b: playerBullets) {
            b.update(delta);
        }
    }

    public Array<CollisionCell> whichCellsDoesPlayerCover() {
        float x = player.getX();
        float y = player.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellRow = x / CELL_SIZE;
        float cellCol = y / CELL_SIZE;

        int bottomLeftCellRow = MathUtils.floor(cellRow);
        int bottomLeftCellCol = MathUtils.floor(cellCol);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)currentArea.getMap().getLayers().get(0);

        cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellRow,
                bottomLeftCellCol), bottomLeftCellRow, bottomLeftCellCol));

        if(cellRow % 1 != 0 && cellCol % 1 != 0) {
            int topRightCellRow = bottomLeftCellRow + 1;
            int topRightCellCol = bottomLeftCellCol + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellRow,
                    topRightCellCol), topRightCellRow, topRightCellCol));
        }
        if(cellRow % 1 != 0) {
            int bottomRightCellRow = bottomLeftCellRow + 1;
            int bottomRightCellCol = bottomLeftCellCol;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellRow,
                    bottomRightCellCol), bottomRightCellRow, bottomRightCellCol));

        }
        if(cellCol % 1 != 0) {
            int topLeftCellRow = bottomLeftCellRow;
            int topLeftCellCol = bottomLeftCellCol + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellRow,
                    topLeftCellCol), topLeftCellRow, topLeftCellCol));
        }

        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonCollisionCells(Array<CollisionCell> cells) {
        for(Iterator<CollisionCell> iter = cells.iterator(); iter.hasNext();) {
            CollisionCell collisionCell = iter.next();
            Input input = Gdx.input;

            if(collisionCell.isEmpty()) {
                iter.remove();
            }
            if(collisionCell.getId() == 1 ||
                    collisionCell.getId() == 2 ||
                    collisionCell.getId() == 91 ||
                    collisionCell.getId() == 92 ||
                    collisionCell.getId() == 93 ||
                    collisionCell.getId() == 94 ||
                    collisionCell.getId() == 31 ||
                    collisionCell.getId() == 71 ||
                    collisionCell.getId() == 72 ||
                    collisionCell.getId() == 73 ||
                    collisionCell.getId() == 74 ||
                    collisionCell.getId() == 81 ||
                    collisionCell.getId() == 82 ||
                    collisionCell.getId() == 83 ||
                    collisionCell.getId() == 84 ||
                    collisionCell.getId() == 10) {
                iter.remove();
            }
            if(collisionCell.getId() == 12) {
                iter.remove();
                player.setMaxSpeed(1, 1);
            }
            if(collisionCell.getId() == 20) {
                iter.remove();
            }
            else {
                player.setMaxSpeed(6, 6);
            }
            if(collisionCell.getId() == 13) {
            }

            }

        return cells;
    }

    public void handlePlayerCollision() {
        Array<CollisionCell> playerCells = whichCellsDoesPlayerCover();

        handleKey(playerCells);

        playerCells = filterOutNonCollisionCells(playerCells);
        for(CollisionCell cell: playerCells) {
            float cellLevelX = cell.getCellRow() * CELL_SIZE;
            float cellLevelY = cell.getCellCol() * CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(player.getCollisionRectangle(),
                    new Rectangle(cellLevelX,cellLevelY,CELL_SIZE,CELL_SIZE),
                    intersection);
            if(intersection.getHeight() < intersection.getWidth()) {

                if(intersection.getY() == player.getY()) {
                    player.setPosition(player.getX(), intersection.getY() + intersection.getHeight());
                }
                if(intersection.getY() > player.getY()) {
                    player.setPosition(player.getX(), intersection.getY() - player.HEIGHT);
                }
            }
            else if (intersection.getWidth() < intersection.getHeight()) {
                if(intersection.getX() == player.getX()) {
                    player.setPosition(intersection.getX() + intersection.getWidth(),
                            player.getY());
                }
                if(intersection.getX() > player.getX()) {
                    player.setPosition(intersection.getX() - player.WIDTH,
                            player.getY());
                }
            }
        }
    }

    private void handleKey(Array<CollisionCell> playerCells) {
        // Make Keys and Locks
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            for (CollisionCell c: playerCells){
                if (c.getId() == 10 ){

                    if (currentArea == overWorld[2][4] && !hasWhiteKey){
                            hasWhiteKey = true;
                            System.out.println("You have the white key");
                    }

                    if (currentArea == overWorld[4][4] && !hasBlueKey){
                        hasBlueKey = true;
                        System.out.println("You have the blue key");
                    }

                    if (currentArea == overWorld[3][6] && !hasRedKey){
                        hasRedKey = true;
                        System.out.println("You have the red key");
                    }

                    if (currentArea == overWorld[3][5] && !hasGreenKey){
                        hasGreenKey = true;
                        System.out.println("You have the green key");
                    }

                    if (currentArea == overWorld[2][3] && !hasBlackKey){
                        hasBlackKey = true;
                        System.out.println("You have the black key");
                    }

                    if (currentArea == overWorld[6][3] && !hasBloodKey){
                        hasBloodKey = true;
                        System.out.println("You have the blood key");
                    }

                    if (currentArea == overWorld[2][6] && !hasSkyKey){
                        hasSkyKey = true;
                        System.out.println("You have the sky key");
                    }

                    if (currentArea == overWorld[4][5] && !hasPinkKey){
                        hasPinkKey = true;
                        System.out.println("You have the pink key");
                    }
                }
                if (c.getId() == 20 && !hasOpened){
                    hasOpened = true;
                    System.out.println("nothing here");
                }
                if (c.getId() == 71 && hasWhiteKey) {                                //White Key
                    if (currentArea == overWorld[5][4]) {
                        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) currentArea.getMap().getLayers().get(0);
                        TiledMapTileLayer.Cell
                                cell = tiledMapTileLayer.getCell(24, 24 - 19); // Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(1)); // Set cell
                        cell = tiledMapTileLayer.getCell(24, 24 - 20);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(24, 24 - 21);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(24, 24 - 22);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(24, 24 - 23);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(1));// Set cell
                        if (currentArea == overWorld[2][6]) {
                            cell = tiledMapTileLayer.getCell(12, 24 - 10); // Get cell
                            cell.setTile(currentArea.getMap().getTileSets().getTile(1)); // Set cell
                            cell = tiledMapTileLayer.getCell(12, 24 - 11);// Get cell
                            cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                            cell = tiledMapTileLayer.getCell(12, 24 - 12);// Get cell
                            cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                            cell = tiledMapTileLayer.getCell(12, 24 - 13);// Get cell
                            cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    }
                }
                if (c.getId() == 72 && hasBlackKey && currentArea == overWorld[4][5]) {                                 //Black Key
                    TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)currentArea.getMap().getLayers().get(0);
                    TiledMapTileLayer.Cell
                            cell = tiledMapTileLayer.getCell(3,24-14); // Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2)); // Set cell
                    cell = tiledMapTileLayer.getCell(4,24-14);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(5,24-14);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(6,24-21);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(6,24-22);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(6,24-23);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                }
                if (c.getId() == 73 && hasSkyKey && currentArea == overWorld[5][5]) {                                 //Sky Key
                    TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)currentArea.getMap().getLayers().get(0);
                    TiledMapTileLayer.Cell
                            cell = tiledMapTileLayer.getCell(3,24-14); // Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(1)); // Set cell
                    cell = tiledMapTileLayer.getCell(24,24-3);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(25,24-3);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(26,24-3);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(27,24-3);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                    cell = tiledMapTileLayer.getCell(28,24-3);// Get cell
                    cell.setTile(currentArea.getMap().getTileSets().getTile(1));// Set cell
                }
                if (c.getId() == 84 && hasRedKey) {                                 //Red Key
                    if (currentArea == overWorld[6][5]) {
                        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) currentArea.getMap().getLayers().get(0);
                        TiledMapTileLayer.Cell
                                cell = tiledMapTileLayer.getCell(6, 24 - 16); // Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(1)); // Set cell
                        cell = tiledMapTileLayer.getCell(6, 24 - 17);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(6, 24 - 18);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(6, 24 - 19);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(2));// Set cell
                        cell = tiledMapTileLayer.getCell(6, 24 - 20);// Get cell
                        cell.setTile(currentArea.getMap().getTileSets().getTile(1));// Set cell
                    }
                }
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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