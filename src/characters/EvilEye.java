package characters;

import java.awt.Point;
import java.util.LinkedList;

import actions.Action;
import actions.LaserShoot;
import actions.MagmaShoot;
import actions.SwordStab;
import tbs.Animation;
import tbs.Character;
import tbs.Config;
import tbs.Matrix;
import tbs.World;

public class EvilEye extends tbs.Character {
	static final int ATTACK_RANGE = 5;
	int side = 0;
	long actTime;
	int movesThisTurn;
	int movesPerTurn = 5;
	static final int MOVEMENT_SPEED = 40;
	Action attack;
	Player enemy;
	static final int BOUNTY = 5;
	@Override
	public int getBounty(){
		return BOUNTY;
	}
	public EvilEye(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super(anim, world, chunkX, chunkY, x, y);
		System.out.println("Created aug");
		health = 10;
		maxHealth = 10;
		lifebar = true;
		attack = new LaserShoot();
		side = (int)(Math.random()*4);
	}
	@Override
	public void paint(Matrix mat, int x, int y){
		anim.paint(mat, x, y);
		paintLifebar(mat,x,y);
		attack.paint(mat, x, y);
	}
	@Override
	public void update(long delta) {
		attack.update(delta);
		actTime+=delta;
		if(hasTurn){
			int baseTime = MOVEMENT_SPEED;
			int[] rel = world.getRelativeDistance(this, enemy);
			if(Math.abs(rel[0])>Config.FAR_MOVEMENT_RANGE|Math.abs(rel[1])>Config.FAR_MOVEMENT_RANGE){
				baseTime=Config.FAR_MOVEMENT_SPEED;
			}
			if(actTime>baseTime){
			if(moves.size()!=0&&movesThisTurn<movesPerTurn){
				Point p = moves.poll();
				move(p.x,p.y);
				movesThisTurn++;
			}else{
				if(movesThisTurn<movesPerTurn){
					attack();
					side = (int)(Math.random()*4);
				}
				if(movable == false){
					movesThisTurn=0;
					endTurn();
				}
			}
			actTime = 0;
		}}
	}
	public void attack(){
//		int[][] dmg = attack.getPossibleTargets(side);
//		LinkedList<Character> victims = new LinkedList<Character>();
//		LinkedList<Integer> victimFields = new LinkedList<Integer>();
//		for(int c = 0;c<dmg.length;c++){
//			for(int d = 0;d<dmg.length;d++){
//				if(dmg[c][d]!=0){
//					Character e = checkCharacter(d-(dmg.length/2),c-(dmg.length/2));
//					if(e!=null){
//						victims.add(e);
//						victimFields.add(dmg[c][d]);
//					}
//				}
//			}
//		}
//		Character[] tempChar = new Character[victims.size()];
//		int[] tempInt = new int[victims.size()];
//		for(int f = 0;f<tempChar.length;f++){
//			tempChar[f]=victims.poll();
//			tempInt[f]=victimFields.poll();
//		}
		int[] target = world.getRelativeDistance(this, enemy);
		switch(side){
		case 0:
			target[0]-=ATTACK_RANGE;
			break;
		case 1:
			target[1]-=ATTACK_RANGE;
			break;
		case 2:
			target[0]+=ATTACK_RANGE;
			break;
		case 3:
			target[1]+=ATTACK_RANGE;
			break;
		}
		if(target[1]==0&&target[0]==0){
		enemy = (Player)world.getCharacter(characters.Player.class);
		Character[] playerArr = {enemy};
		int[] temp = {1};
		attack.start(side,playerArr,temp);
		}
	}
	@Override
	public void startTurn(){
		moves.clear();
		movesThisTurn = 0;
		hasTurn = true;
		enemy = (Player)world.getCharacter(characters.Player.class);
		int[] target = world.getRelativeDistance(this, enemy);
		switch(side){
		case 0:
			target[0]-=ATTACK_RANGE;
			break;
		case 1:
			target[1]-=ATTACK_RANGE;
			break;
		case 2:
			target[0]+=ATTACK_RANGE;
			break;
		case 3:
			target[1]+=ATTACK_RANGE;
			break;
		}
		LinkedList<Point> path = findPath(target[0],target[1]);
		if(path!=null){
		moves.addAll(path);
		if(path.size()==0){
			side = (int)(Math.random()*4);
		}
		}else{
			side = (int)(Math.random()*4);
		}

		movable = false;
		
	}
	@Override
	public boolean finished(){
		return !hasTurn;
	}

	private void action() {
		
	}
	@Override
	public String getName() {
		return "evil_eye";
	}
}
