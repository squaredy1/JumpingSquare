package com.jorgegallego.jumpingsquare.model.Mages;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;

import com.jorgegallego.framework.util.Painter;
import com.jorgegallego.framework.util.RandomNumberGenerator;
import com.jorgegallego.jumpingsquare.main.Assets;
import com.jorgegallego.jumpingsquare.main.GameMainActivity;
import com.jorgegallego.jumpingsquare.state.PlayState;

/**
 * Created by Jorge y Juan on 10/09/2016.
 */
public abstract class Mage {

    PlayState game;
    String ID;
    double redStartTime, angryStartTime;
    final int START_X, START_Y;
    final float X_CONSTANT;
    int x, y, width, height, HP, textSize, textWidth;
    double velY, velX, accelX, accelY;
    boolean hitProcessOn, visible = true, hurt, dead, entering, angry;
    Bitmap mageImage, hurtMageImage, angryMageImage, hurtAngryMageImage;
    Typeface font = Typeface.create("Arial", Typeface.BOLD);


    public Mage(Bitmap mageImage, Bitmap hurtMageImage, Bitmap angryMageImage, Bitmap hurtAngryMageImage, int width, int height, float xConstant, int HP, PlayState game, String ID) {
        this.width = width;
        this.height = height;

        START_X = GameMainActivity.GAME_WIDTH/2 - width/ 2;
        START_Y = GameMainActivity.GAME_HEIGHT/2 - height/ 2;

        X_CONSTANT = xConstant;

        x = START_X;
        y = START_Y;

        this.mageImage = mageImage;
        this.hurtMageImage = hurtMageImage;
        this.angryMageImage = angryMageImage;
        this.hurtAngryMageImage = hurtAngryMageImage;

        accelX = RandomNumberGenerator.getRandSign() * RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT /1000, 200 * GameMainActivity.GAME_HEIGHT /1000);
        accelY = RandomNumberGenerator.getRandSign() * RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT /1000, 200 * GameMainActivity.GAME_HEIGHT /1000);

        this.HP = HP;

        this.game = game;

        this.ID = ID;

        textSize = Math.round(50 * X_CONSTANT);

        textWidth = new Painter(new Canvas()).measureText("" + HP, textSize/2);
    }

    public int getY(){return y;}

    public int getX(){return x;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}

    public void whenKilled() {
        accelY = 1000 * GameMainActivity.GAME_HEIGHT /1000;
        dead = true;
    }

    public void whenHit(int damage) {
        if (!visible) {
            return;
        }
        Assets.playSound(Assets.hit, 0);

        Painter g = new Painter(new Canvas());
        HP -= damage;
        textWidth = g.measureText("" + HP, textSize/2);

        getRed();
        if (HP <= 0) {
            HP = 0;
            whenKilled();
        }
    }

    public void getRed() {
        if (!hurt) {
            redStartTime = System.nanoTime();
            hurt = true;
        } else {
            if (System.nanoTime() - redStartTime > 50000000) {
                hurt = false;
            }
        }
    }

    void getAngry(double duration) {
        if (!angry) {
            angryStartTime = System.nanoTime();
            angry = true;
        } else {
            if (System.nanoTime() - angryStartTime > duration) {
                angry = false;
            }
        }
    }

    public void enter() {
        entering = true;
        y = - height;
        velY = 450 * X_CONSTANT;
    }

    public void update(double delta){
        if (!visible) {
            return;
        }

        randomMov(delta);

        if (hurt){
            getRed();
        }
    }

    public void randomMov(double delta){
        if (!dead) {
            if (x > START_X + 10 * X_CONSTANT && accelX > 0) {
                accelX = -RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT / 1000, 200 * GameMainActivity.GAME_HEIGHT / 1000);
            }
            if (x < START_X - 10 * X_CONSTANT && accelX < 0) {
                accelX = RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT / 1000, 200 * GameMainActivity.GAME_HEIGHT / 1000);
            }
            if (y > START_Y + 10 * X_CONSTANT && accelY > 0) {
                if (entering) {
                    entering = false;
                }
                accelY = -RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT / 1000, 200 * GameMainActivity.GAME_HEIGHT / 1000);
            }
            if (y < START_Y - 10 * X_CONSTANT && accelY < 0) {
                accelY = RandomNumberGenerator.getRandIntBetween(100 * GameMainActivity.GAME_HEIGHT / 1000, 200 * GameMainActivity.GAME_HEIGHT / 1000);
            }
        } else {
            if (x > START_X + 5 * X_CONSTANT && accelX > 0) {
                accelX = -RandomNumberGenerator.getRandIntBetween(200 * GameMainActivity.GAME_HEIGHT / 1000, 400 * GameMainActivity.GAME_HEIGHT / 1000);
            }
            if (x < START_X - 5 * X_CONSTANT && accelX < 0) {
                accelX = RandomNumberGenerator.getRandIntBetween(200 * GameMainActivity.GAME_HEIGHT / 1000, 400 * GameMainActivity.GAME_HEIGHT / 1000);
            }
        }

        velX += accelX * delta;

        if (Math.abs(velY) < 200 * X_CONSTANT || dead) {
            velY += accelY * delta;
        } else if (velY < 0) {
            velY += 3 * X_CONSTANT;
        } else {
            velY -= 3 * X_CONSTANT;
        }

        if (velX > 500 * X_CONSTANT || velX < -500 * X_CONSTANT) {
            accelX = 0;
        }
        if ((velY > 500 * X_CONSTANT || velY < -500 * X_CONSTANT) && !dead) {
            accelY = 0;
        }

        y += velY * delta;
        x += velX * delta;

        if (y > GameMainActivity.GAME_HEIGHT && dead) {
            visible = false;
            game.mageKilled(ID);
        }
    }

    public void render(Painter g) {
        if (!visible) {
            return;
        }
        g.drawImage(hurt || dead ? hurtMageImage : mageImage, x, y, width, height);

        g.setFont(font, textSize);
        g.setColor(Assets.mageLifeFontColor);
        g.drawImage(Assets.heart, x + width / 2 + (int) (20 * X_CONSTANT), y - textSize + (int) (13 * X_CONSTANT), (int) (45 * X_CONSTANT), (int) (40 * X_CONSTANT));
        g.drawString("" + HP, x + width / 2 - (textWidth*3/4), y - (int) (3 * X_CONSTANT));
    }

    public abstract void renderExtras(Painter g, float xConstant);
}
