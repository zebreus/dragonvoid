package actions;

import tbs.Animation;
import tbs.Drawable;
import tbs.Sprite;

public interface PlayerAction{
	public Animation provideHudAnimation();
	public String provideDescription();
	public int provideAttackCosts();
}
