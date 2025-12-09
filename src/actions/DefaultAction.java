package actions;

import java.io.IOException;

import tbs.Animation;
import tbs.Character;
import tbs.DebugLayer;
import tbs.Drawable;
import tbs.Matrix;
import tbs.Sprite;

public class DefaultAction implements PlayerAction,Action{
	Animation anim;
	String text = "Default Action";
	public DefaultAction(){

		anim = new Animation("HUD/ability_default");
	}
	@Override
	public int[][] getPossibleTargets() {
	int[][] ret = {{0}};
		return ret;
	}

	@Override
	public void paint(Matrix mat, int x, int y) {
	}

	@Override
	public void update(long delta) {
	}

	@Override
	public void start(int dir, Character[] victims, int[] victimFields) {
		DebugLayer.pushError("Tried to use default Action");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean getRunning() {
		return false;
	}

	@Override
	public String provideDescription() {
		// TODO Auto-generated method stub
		return text;
	}
	@Override
	public Animation provideHudAnimation() {
		return anim;
	}
	@Override
	public int[][] getPossibleTargets(int dir) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int provideAttackCosts() {
		// TODO Auto-generated method stub
		return 0;
	}

}
