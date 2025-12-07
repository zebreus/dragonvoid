package actions;

import tbs.DebugLayer;
import tbs.Drawable;
import tbs.Matrix;
import tbs.Sprite;

import java.io.IOException;

import tbs.Animation;
import tbs.Character;

public class SwordSwish implements PlayerAction,Action{
	final static int ATTACK_POWER = 7;
	final static int ANIMATION_SPEED = 50;
	final static int ATTACK_COST = 7;
	final static int STRAIGHT_MOVE = 7;
	final static int DIAGONAL_MOVE = 4;
	final String text = "Ein Schwertstoß";
	Sprite[] sprites;
	boolean animated;
	int direction;
	int position;
	long last;
	Animation anim;
	public SwordSwish(){
		
		anim = new Animation("HUD/ability_swish");
		
		sprites = new Sprite[9];
		animated = false;
		direction = 0;
		last = 0;
		try{
			sprites[0] = new Sprite("actions/sword_swish/sword_swish_1");
			sprites[1] = new Sprite("actions/sword_swish/sword_swish_2");
			sprites[2] = new Sprite("actions/sword_swish/sword_swish_3");
			sprites[3] = new Sprite("actions/sword_swish/sword_swish_4");
			sprites[4] = new Sprite("actions/sword_swish/sword_swish_5");
			sprites[5] = new Sprite("actions/sword_swish/sword_swish_6");
			sprites[6] = new Sprite("actions/sword_swish/sword_swish_7");
			sprites[7] = new Sprite("actions/sword_swish/sword_swish_8");
			sprites[8] = sprites[0];
		}catch(Exception e){
			DebugLayer.pushError("Konnte bei Action SwordStab nicht alle Bilder laden");
			System.err.println("Konnte Bilder nicht laden");
			e.printStackTrace();
		}
	}
	@Override
	public int[][] getPossibleTargets() {
		int[][] matrix =
			{
					{3,3,3},
					{3,-1,3},
					{3,3,3}
			};
		    return matrix;
	}

	@Override
	public void paint(Matrix mat, int x, int y) {
		if(animated){
			switch(position){
			case 0:
				y-=STRAIGHT_MOVE;
				break;
			case 1:
				y-=DIAGONAL_MOVE;
				x-=DIAGONAL_MOVE;
				break;
			case 2:
				x-=STRAIGHT_MOVE;
				break;
			case 3:
				y+=DIAGONAL_MOVE;
				x-=DIAGONAL_MOVE;
				break;
			case 4:
				y+=STRAIGHT_MOVE;
				break;
			case 5:
				y+=DIAGONAL_MOVE;
				x+=DIAGONAL_MOVE;
				break;
			case 6:
				x+=STRAIGHT_MOVE;
				break;
			case 7:
				y-=DIAGONAL_MOVE;
				x+=DIAGONAL_MOVE;
				break;
			case 8:
				y-=STRAIGHT_MOVE;
				break;
			}
			mat.drawDrawable(sprites[position], x, y);
		}
	}

	@Override
	public void update(long delta) {
		if(animated){
			if(last+ANIMATION_SPEED<System.currentTimeMillis()){
				position++;
				last = System.currentTimeMillis();
				if(position==9){
					position = 0;
					animated=false;
				}
			}
		}
	}

	@Override
	public void start(int dir, Character[] victims, int[] victimField) {
			direction = dir;
			animated = true;
			for(Character v:victims){
					v.changeHealth(-ATTACK_POWER);
			}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getRunning() {
		// TODO Auto-generated method stub
		return animated;
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
		int[][] matrix =
			{
					{1,1,1},
					{1,0,1},
					{1,1,1}
			};
		    return matrix;
	}
	@Override
	public int provideAttackCosts() {
		// TODO Auto-generated method stub
		return ATTACK_COST;
	}

}
