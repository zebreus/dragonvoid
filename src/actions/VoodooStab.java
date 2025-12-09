package actions;

import tbs.DebugLayer;
import tbs.Drawable;
import tbs.Matrix;
import tbs.Sprite;

import java.io.IOException;

import tbs.Animation;
import tbs.Character;

public class VoodooStab implements PlayerAction,Action{
	final static int ATTACK_POWER = 5;
	final static int ANIMATION_SPEED = 50;
	final static int ATTACK_COST = 1;
	final String text = "Ein Schwertstoﬂ";
	Sprite[] sprites;
	boolean animated;
	int direction;
	int position;
	long last;
	Animation anim;
	public VoodooStab(){
		
		anim = new Animation("HUD/ability_default");
		
		sprites = new Sprite[4];
		animated = false;
		direction = 0;
		last = 0;
		try{
			sprites[0] = new Sprite("actions/voodoo_stab/voodoo_stab_right");
			sprites[1] = new Sprite("actions/voodoo_stab/voodoo_stab_down");
			sprites[2] = new Sprite("actions/voodoo_stab/voodoo_stab_left");
			sprites[3] = new Sprite("actions/voodoo_stab/voodoo_stab_up");
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
					{-1,3,-1},
					{2,-1,0},
					{-1,1,-1}
			};
		    return matrix;
	}

	@Override
	public void paint(Matrix mat, int x, int y) {
		if(animated){
			switch(direction){
			case Direction.LEFT:
				x+=position;
				break;
			case Direction.RIGHT:
				x-=position;
				break;
			case Direction.UP:
				y-=position;
				break;
			case Direction.DOWN:
				y+=position;
				break;
			}
			mat.drawDrawable(sprites[direction], x, y);
		}
	}

	@Override
	public void update(long delta) {
		if(animated){
			if(last+ANIMATION_SPEED<System.currentTimeMillis()){
				position++;
				last = System.currentTimeMillis();
				if(position==8){
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
					{0,0,0},
					{0,0,1},
					{0,0,0}
			};
		while(dir>0){
			int v = matrix.length;
		    int[][] ret = new int[v][v];
		    for (int r = 0; r < v; r++) {
		        for (int c = 0; c < v; c++) {
		            ret[c][v-1-r] = matrix[r][c];
		        }
		    }
		    matrix = ret;
			dir--;
		}
		    return matrix;
	}
	@Override
	public int provideAttackCosts() {
		// TODO Auto-generated method stub
		return ATTACK_COST;
	}

}
