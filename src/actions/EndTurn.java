package actions;

import tbs.Animation;

public class EndTurn implements PlayerAction{
	Animation anim;
	String text = "Zug beenden";
	public EndTurn(){
		anim = new Animation("HUD/end_turn");
	}
	@Override
	public Animation provideHudAnimation() {
		return anim;
	}

	@Override
	public String provideDescription() {
		return text;
	}

	@Override
	public int provideAttackCosts() {
		return 0;
	}
	
}
