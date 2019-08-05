package com.jorgegallego.jumpingsquare.state;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

import com.jorgegallego.framework.util.Painter;
import com.jorgegallego.framework.util.UIButton;
import com.jorgegallego.jumpingsquare.main.Assets;
import com.jorgegallego.jumpingsquare.main.DataManager;
import com.jorgegallego.jumpingsquare.main.GameMainActivity;

import java.util.Locale;

public class GameOverState extends State {

	private String gameOverString = "GAMEOVER";

	private int textSize = 30;
	
	private String currentScoreString, bestScoreString;

	private UIButton homeBut;

	AssetManager am = GameMainActivity.sGame.getActivity().getApplicationContext().getAssets();
	private Typeface font = Typeface.createFromAsset(am,
			String.format(Locale.US, "fonts/%s", "pricedown.ttf"));

	@Override
	public void init() {
		System.out.println("Entered GameOverState");
		homeBut = new UIButton(GameMainActivity.GAME_WIDTH/2 - (int) (72 * Y_CONSTANT), GameMainActivity.GAME_HEIGHT - (int) (244 * Y_CONSTANT), (int) (144 * Y_CONSTANT), (int) (126 * Y_CONSTANT), Assets.homeButton, Assets.homeButtonDown);
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
		switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				homeBut.onTouchDown(scaledX, scaledY);
				return true;
			case MotionEvent.ACTION_UP:
				if (homeBut.onTouchUp(scaledX, scaledY)) {
					setCurrentState(new MenuState());
					return true;
				}
				return true;
		}
		return true;
	}

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
	public void render(Painter g) {
		//background
		//g.setColor(Assets.backgroundColor);
		//g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		g.drawImage(Assets.background, 0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
		
		//current score + GAMEOVER + best score
		g.setColor(Assets.ballBorderColor);
		g.setFont(font, textSize * X_CONSTANT);
		int textWidth = g.measureText(gameOverString, (int) (textSize * X_CONSTANT));
		g.drawString(gameOverString, GameMainActivity.GAME_WIDTH/2 - textWidth/2, GameMainActivity.GAME_HEIGHT/2);
		
		currentScoreString = "current score: " + DataManager.getCurrentScore();
		textWidth = g.measureText(currentScoreString, (int) (textSize * X_CONSTANT));
		g.drawString(currentScoreString, GameMainActivity.GAME_WIDTH/2 -textWidth/2,GameMainActivity.GAME_HEIGHT/2 - (int) (60 * X_CONSTANT));
		
		bestScoreString = "Best score: " + DataManager.getBestScore();
		textWidth = g.measureText(bestScoreString, (int) (textSize * X_CONSTANT));
		g.drawString(bestScoreString, GameMainActivity.GAME_WIDTH/2 -textWidth/2, GameMainActivity.GAME_HEIGHT/2 + (int) (60 * X_CONSTANT));

		g.setColor(Color.WHITE);
		textWidth = g.measureText(gameOverString, (int) (textSize * X_CONSTANT));
		g.drawString(gameOverString, GameMainActivity.GAME_WIDTH/2 - textWidth/2 + (int) (5 * X_CONSTANT), GameMainActivity.GAME_HEIGHT/2 - (int) (5 * X_CONSTANT));

		currentScoreString = "current score: " + DataManager.getCurrentScore();
		textWidth = g.measureText(currentScoreString, (int) (textSize * X_CONSTANT));
		g.drawString(currentScoreString, GameMainActivity.GAME_WIDTH/2 -textWidth/2 + (int) (5 * X_CONSTANT), GameMainActivity.GAME_HEIGHT/2 - (int) (65 * X_CONSTANT));

		bestScoreString = "Best score: " + DataManager.getBestScore();
		textWidth = g.measureText(bestScoreString, (int) (textSize * X_CONSTANT));
		g.drawString(bestScoreString, GameMainActivity.GAME_WIDTH/2 -textWidth/2 + (int) (5 * X_CONSTANT), GameMainActivity.GAME_HEIGHT/2 + (int) (55 * X_CONSTANT));

		homeBut.render(g);
	}

	public String name() {
		return "GameOverState";
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}
}
