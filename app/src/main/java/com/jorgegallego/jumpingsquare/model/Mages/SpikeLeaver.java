package com.jorgegallego.jumpingsquare.model.Mages;

import android.graphics.Bitmap;

import com.jorgegallego.framework.util.Painter;
import com.jorgegallego.framework.util.RandomNumberGenerator;
import com.jorgegallego.jumpingsquare.main.Assets;
import com.jorgegallego.jumpingsquare.model.Spike;
import com.jorgegallego.jumpingsquare.model.Squaredy;
import com.jorgegallego.jumpingsquare.state.PlayState;

import java.util.ArrayList;

/**
 * Created by Jorge on 29/04/2017.
 */
public class SpikeLeaver extends Mage {

    private boolean spiked;
    private int spikeNum, spikeNum2;
    private ArrayList<Spike> spikes, spikes2;
    private Squaredy hero;
    private PlayState game;

    public SpikeLeaver(Bitmap mageImage, Bitmap hurtMageImage, Bitmap angryMageImage, Bitmap angryHurtMageImage, int width, int height, float xConstant, int HP, Squaredy ball, PlayState game, String ID) {
        super(mageImage, hurtMageImage, angryMageImage, angryHurtMageImage, width, height, xConstant, HP, game, ID);
        hero = ball;
        this.game = game;
    }

    @Override
    public void renderExtras(Painter g, float xConstant) {
        if (spiked) {
            for (int i = 0; i < spikeNum; i++) {
                spikes.get(i).render(g);
            }
            if (spikeNum2 != 0) {
                for (int i = 0; i < spikeNum2; i++) {
                    spikes2.get(i).render(g);
                }
            }
        }
    }

    public void update(double delta) {
        super.update(delta);
        if (angry) {
            getAngry(500000000);
        }
        if (spiked) {
            for (int i = 0; i < spikeNum; i++) {
                spikes.get(i).update(delta);
                if (hero.getRect().intersect(spikes.get(i).getSpikeRect())) {
                    game.die();
                }
            }
            if (spikeNum2 != 0) {
                for (int i = 0; i < spikeNum2; i++) {
                    spikes2.get(i).update(delta);
                    if (hero.getRect().intersect(spikes2.get(i).getSpikeRect())) {
                        game.die();
                    }
                }
            }
        }
    }

    @Override
    public void whenHit(int damage){
        super.whenHit(damage);
        if (HP % 2 == 1) {
            leaveSpike();
            getAngry(1000000000);
        }
    }

    public void leaveSpike() {
        Assets.playSound(Assets.evilLaugh, 0);
        if (spiked) {
            spikeNum2 = RandomNumberGenerator.getRandInt(3);
            spikes2 = new ArrayList<>();
            for (int i = 0; i < spikeNum2; i++) {
                spikes2.add(new Spike((int) (100 * X_CONSTANT), this));
            }
        } else {
            spiked = true;
            spikeNum = RandomNumberGenerator.getRandInt(3);
            spikes = new ArrayList<>();
            for (int i = 0; i < spikeNum; i++) {
                spikes.add(new Spike((int) (100 * X_CONSTANT), this));
            }
        }
    }

    public void spikeEnd() {
        if (spikeNum2 == 0) {
            spiked = false;
        } else {
            spikes = spikes2;
            spikeNum = spikeNum2;
            spikeNum2 = 0;
        }
    }
}
