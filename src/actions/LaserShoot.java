package actions;

import tbs.DebugLayer;
import tbs.Drawable;
import tbs.Matrix;
import tbs.Sprite;

import java.io.IOException;

import tbs.Animation;
import tbs.Character;

public class LaserShoot implements PlayerAction,Action{
	final static int ATTACK_POWER = 5;
	final static int ANIMATION_DURATION = 200;
	final static int ATTACK_COST = 5;
	final static int RANGE = 9;
	final static int FLY_SPEED = 3;
	final String text = "Der Bosslaser";
	Sprite[] sprites;
	boolean animated;
	int direction;
	int position;
	long last;
	Animation anim;
	public LaserShoot(){
		
		anim = new Animation("HUD/ability_default");
		
		sprites = new Sprite[6];
		animated = false;
		direction = 0;
		last = 0;
		try{
			sprites[0] = new Sprite("actions/laser_shoot/laser_right");
			sprites[1] = new Sprite("actions/laser_shoot/laser_down");
			sprites[2] = new Sprite("actions/laser_shoot/laser_left");
			sprites[3] = new Sprite("actions/laser_shoot/laser_up");
			sprites[4] = new Sprite("actions/laser_shoot/laser_left_right");
			sprites[5] = new Sprite("actions/laser_shoot/laser_up_down");
		}catch(Exception e){
			DebugLayer.pushError("Konnte bei Action LaserShoot nicht alle Bilder laden");
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
				int xx = 0;
				while(xx<RANGE*8){
					mat.drawDrawable(sprites[4], x+xx, y);
					xx+=8;
				}
				mat.drawDrawable(sprites[direction], x+xx, y);
				break;
			case Direction.RIGHT:
				int xxx = 0;
				while(xxx<RANGE*8){
					mat.drawDrawable(sprites[4], x-xxx, y);
					xxx+=8;
				}
				mat.drawDrawable(sprites[direction], x-xxx, y);
				break;
			case Direction.UP:
				int yy = 0;
				while(yy<RANGE*8){
					mat.drawDrawable(sprites[5], x, y-yy);
					yy+=8;
				}
				mat.drawDrawable(sprites[direction], x, y-yy);
				break;
			case Direction.DOWN:
				int yyy = 0;
				while(yyy<RANGE*8){
					mat.drawDrawable(sprites[5], x, y+yyy);
					yyy+=8;
				}
				mat.drawDrawable(sprites[direction], x, y+yyy);
				break;
			}
			
		}
	}

	@Override
	public void update(long delta) {
		if(animated){
			if(last+ANIMATION_DURATION<System.currentTimeMillis()){
					position = 0;
					animated=false;
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
			last=System.currentTimeMillis();
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
