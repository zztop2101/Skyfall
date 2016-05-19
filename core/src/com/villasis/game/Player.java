package com.villasis.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private static float MAX_X_SPEED = 6;
    private static float MAX_Y_SPEED = 6;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private static final float MAX_JUMP_DISTANCE = 4 * HEIGHT;
    private final Rectangle collisionRectangle = new Rectangle(0,0,WIDTH,HEIGHT);
    private float x = 400;
    private float y = 300;
    private float xSpeed = 0;
    private float ySpeed = 0;
    private boolean blockJump = false;
    private float jumpYDistance = 0;

    private float animationTimer = 0;
    private final Animation walkRight;
    private final Animation walkLeft;
    private final Animation walkUp;
    private final Animation walkDown;
    private final TextureRegion standing;

    public Player(Texture texture) {
       TextureRegion[] regions = TextureRegion.split(texture,WIDTH,HEIGHT)[0];
        walkRight = new Animation(0.1f, regions[3], regions[4]);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);
        walkLeft = new Animation(0.1f, regions[1], regions[2]);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        walkUp = new Animation(0.1f, regions[5], regions[6], regions[7]);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);
        walkDown = new Animation(0.1f, regions[0], regions[8], regions[9]);
        walkDown.setPlayMode(Animation.PlayMode.LOOP);
    }


    public void update(float delta) {
        animationTimer += delta;
        Input input = Gdx.input;

        //x movement
        if(input.isKeyPressed(Input.Keys.D)) {
            xSpeed = MAX_X_SPEED;
        }
        else if(input.isKeyPressed(Input.Keys.A)) {
            xSpeed = -MAX_X_SPEED;
        }
        else {
            xSpeed = 0;
        }

        //y movement
        if(input.isKeyPressed(Input.Keys.W)) {
            ySpeed = MAX_Y_SPEED;
        }
        else if(input.isKeyPressed(Input.Keys.S)) {
            ySpeed = -MAX_Y_SPEED;
        }
        else {
            ySpeed = 0;
        }

        x += xSpeed;
        y += ySpeed;
        updateCollisionRectangle();

        //System.out.println("Speed: " + xSpeed + "," + ySpeed);

        /*if(input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(xSpeed == 0 && ySpeed == 0) {
                xSpeed = (float) (-5 + Math.random() * 11);
                ySpeed = (float) (-5 + Math.random() * 11);
            }
            GameplayScreen.playerBullets.add(new Bullet(x,y,xSpeed*3,ySpeed*3));
        }*/
    }

    public void landed() {
        blockJump = false;
        jumpYDistance = 0;
        ySpeed = 0;
    }

    public void draw(SpriteBatch batch) {

        TextureRegion toDraw = standing;

        if(xSpeed > 0) {
            toDraw = walkRight.getKeyFrame(animationTimer);
        }
        if(xSpeed < 0) {
            toDraw = walkLeft.getKeyFrame(animationTimer);
        }
        if(ySpeed > 0) {
            toDraw = walkUp.getKeyFrame(animationTimer);
        }
        if(ySpeed < 0) {
            toDraw = walkDown.getKeyFrame(animationTimer);
        }


        batch.draw(toDraw, x, y);
    }


    public void drawDebug(ShapeRenderer shapeRenderer) {
        //shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width,
               // collisionRectangle.height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void setMaxSpeed (float MAX_X_SPEED, float MAX_Y_SPEED){
        this.MAX_X_SPEED = MAX_X_SPEED;
        this.MAX_Y_SPEED = MAX_Y_SPEED;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private void updateCollisionRectangle() {
        collisionRectangle.setPosition(x,y);
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public static float getMaxXSpeed() {
        return MAX_X_SPEED;
    }

    public static float getMaxYSpeed() {
        return MAX_Y_SPEED;
    }
}

