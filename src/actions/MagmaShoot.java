package actions;

import tbs.DebugLayer;
import tbs.Drawable;
import tbs.Matrix;
import tbs.Sprite;

import java.io.IOException;

import tbs.Animation;
import tbs.Character;

public class MagmaShoot implements PlayerAction,Action{
	final static int ATTACK_POWER = 5;
	final static int ANIMATION_SPEED = 20;
	final static int ATTACK_COST = 5;
	final static int RANGE = 9;
	final static int FLY_SPEED = 3;
	final String text = "Schieﬂt einen Magmaball";
	Sprite magma;
	boolean animated;
	int direction;
	int position;
	long last;
	Animation anim;
	public MagmaShoot(){
		
		anim = new Animation("HUD/ability_default");
		animated = false;
		direction = 0;
		last = 0;
		try{
			magma = new Sprite("actions/magma_shoot/magma_shoot");
		}catch(Exception e){
			DebugLayer.pushError("Konnte bei Action MagmaShoot nicht alle Bilder laden");
			System.err.println("Konnte Bilder nicht laden");
			e.printStackTrace();
		}
	}
	@Override
	public int[][] getPossibleTargets() {
		int range = 1+(RANGE*2);
		int[][] matrix = new int[range][range];
		for(int x = 0;x<range;x++){
			for(int y = 0;y<range;y++){
				int num = -1;
				if(x==RANGE&&y<RANGE){
					num = 2;
				}
				if(x==RANGE&&y>RANGE){
					num = 0;
				}
				if(y==RANGE&&x<RANGE){
					num = 3;
				}
				if(y==RANGE&&x>RANGE){
					num = 1;
				}
				matrix[x][y]=num;
			}
			System.out.println();
		}
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
			mat.drawDrawable(magma, x, y);
		}
	}

	@Override
	public void update(long delta) {
		if(animated){
			if(last+ANIMATION_SPEED<System.currentTimeMillis()){
				position+=FLY_SPEED;
				last = System.currentTimeMillis();
				if(position>=8*(RANGE+2)){
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
		int range = 1+(RANGE*2);
		int[][] matrix = new int[range][range];
		for(int x = 0;x<range;x++){
			for(int y = 0;y<range;y++){
				int num = 0;
				if(dir==2&&x==RANGE&&y<RANGE){
					num = 1;
				}
				if(dir==0&&x==RANGE&&y>RANGE){
					num = 1;
				}
				if(dir==3&&y==RANGE&&x<RANGE){
					num = 1;
				}
				if(dir==1&&y==RANGE&&x>RANGE){
					num = 1;
				}
				matrix[x][y]=num;
			}
		}
		    return matrix;
	}
	@Override
	public int provideAttackCosts() {
		// TODO Auto-generated method stub
		return ATTACK_COST;
	}

}
