package characters;

import java.awt.Point;
import java.util.LinkedList;

import actions.Action;
import actions.MagmaShoot;
import actions.SwordStab;
import tbs.Animation;
import tbs.Character;
import tbs.Config;
import tbs.Matrix;
import tbs.World;

public class Magma extends tbs.Character {
	// um den Spieler herumlaufen 0 = oben 1 = rechts 2 = unten 3= links
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
	public Magma(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super(anim, world, chunkX, chunkY, x, y);
		System.out.println("Created Magma");
		health = 10;
		maxHealth = 10;
		lifebar = true;
		attack = new MagmaShoot();
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
				if(attack()){
					movesThisTurn+=movesPerTurn;
				}
				movesThisTurn++;
			}else{
				if(movable == false){
					movesThisTurn=0;
					endTurn();
				}
			}
			actTime = 0;
		}}
	}
	public boolean attack(){
		int[] target = world.getRelativeDistance(this, enemy);
		enemy = (Player)world.getCharacter(characters.Player.class);
		LinkedList<int[]> targets = new LinkedList();
		
		for(int x = ATTACK_RANGE;x>1;x--){
		int[] t1 = {target[0]-x,target[1],0};
		int[] t2 = {target[0],target[1]-x,1};
		int[] t3 = {target[0]+x,target[1],2};
		int[] t4 = {target[0],target[1]+x,3};
		targets.add(t1);
		targets.add(t2);
		targets.add(t3);
		targets.add(t4);
		}
		
		for(int[] p: targets){
			if(p[0]==0&&p[1]==0){
				Character[] playerArr = {enemy};
				int[] temp = {1};
				attack.start(p[2],playerArr,temp);
				return true;
			}
		}
		
		return false;
	}
	@Override
	public void startTurn(){
		moves.clear();
		movesThisTurn = 0;
		hasTurn = true;
		enemy = (Player)world.getCharacter(characters.Player.class);
		int[] target = world.getRelativeDistance(this, enemy);
		LinkedList<int[]> targets = new LinkedList();
		for(int x = ATTACK_RANGE;x>1;x--){
		int[] t1 = {target[0]-x,target[1],0};
		int[] t2 = {target[0],target[1]-x,1};
		int[] t3 = {target[0]+x,target[1],2};
		int[] t4 = {target[0],target[1]+x,3};
		targets.add(t1);
		targets.add(t2);
		targets.add(t3);
		targets.add(t4);
		}
		found:
		for(int[] p: targets){
			if(check(p[0],p[1])||(p[0]==0&p[1]==0)){
				int[] temp={p[0],p[1]};
				target = temp;
				side = p[2];
				break found;
			}
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
		return "magma";
	}
}
