package characters;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

import actions.Action;
import actions.ArrowShoot;
import actions.DefaultAction;
import actions.EndTurn;
import actions.LaserShoot;
import actions.PlayerAction;
import actions.SwordStab;
import actions.SwordSwish;
import actions.UberAttack;
import tbs.Animation;
import tbs.Config;
import tbs.DebugLayer;
import tbs.Matrix;
import tbs.Sprite;
import tbs.World;
import tbs.Character;

public class Player extends Character{
	static final int MOVEMENT_COST = 1;
	static final int MOVEMENT_SPEED = 20;
	static final int END_TURN_BONUS = 20;
	int actTime = 0;
	int radius = 6;
	int[][] range;
	Sprite freeFloor;
	Sprite attackFloor;
	boolean first = true;
	Action[] actions;
	int[][] targets;
	int attacking;
	boolean waitForActionEnd;
	public PlayerAction[] playerActions;
	@Override
	public void startTurn(){
		super.startTurn();
		range = calcRange(0,0,radius);
		movable = true;
	}
	public Player(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super(anim, world, chunkX, chunkY, x, y);
		if(Config.DEBUG){
		System.out.println("Created Player");
		}
		maxHealth = 80;
		health = 80;
		waitForActionEnd = false;
		attacking = -1;
		lifebar = false;
		actions = new Action[6];
		playerActions = new PlayerAction[6];
		targets=new int[0][0];
		for(int s = 0;s<6;s++){
			Object o = new DefaultAction();
			actions[s] = (Action)o;
			playerActions[s] = (PlayerAction)o;
		}
		Object atk1 = new SwordStab();
		Object atk2 = new SwordSwish();
		Object atk3 = new ArrowShoot();
		Object atk4 = new UberAttack();
		playerActions[5] = new EndTurn();
		actions[0] = (Action)atk1;
		playerActions[0] = (PlayerAction)atk1;
		actions[1] = (Action)atk2;
		playerActions[1] = (PlayerAction)atk2;
		actions[2] = (Action)atk3;
		playerActions[2] = (PlayerAction)atk3;
		actions[4] = (Action)atk4;
		playerActions[4] = (PlayerAction)atk4; 
		speed = 1;
		try{
		freeFloor = new Sprite("HUD/movable");
		attackFloor = new Sprite("HUD/attack");
		}catch(Exception e){
			DebugLayer.pushError("Konnte Grafiken im Spieler nicht laden");
		}
	}
	@Override
	public void paint(Matrix mat, int x,int y){
		anim.paint(mat,x,y);
		for(Action a: actions){
			a.paint(mat,x,y);
		}
		if(movable&attacking==-1&hasTurn){
			for(int a = 0;a<(1+(radius*2));a++){
				for(int b = 0;b<(1+(radius*2));b++){
					if(range[a][b]>0&&range[a][b]<=radius){
						freeFloor.paint(mat, x+(a-radius)*8, y+(b-radius)*8);
					}
				}
			}
		}
		
		if(attacking!=-1){
			int attackRadius = (targets.length/2);
			for(int a = 0;a<(1+(attackRadius*2));a++){
				for(int b = 0;b<(1+(attackRadius*2));b++){
					if(targets[a][b]!=-1){
						attackFloor.paint(mat, x+(a-attackRadius)*8, y+(b-attackRadius)*8);
					}
				}
			}
		}
		paintLifebar(mat,x,y);
	}
	@Override
	public void update(long delta) {
		if(first){
			initiate();
			first = false;
		}
		for(Action a: actions){
			a.update(delta);
		}
		if(attacking!=-1&&waitForActionEnd){
			if(actions[attacking].getRunning()==false){
				attacking = -1;
				waitForActionEnd=false;
				range = calcRange(0,0,radius);
			}
		}
		actTime+=delta;
		if(actTime>MOVEMENT_SPEED){
			if(moves.size()!=0){
				Point p = moves.poll();
				move(p.x,p.y);
			}else if(!movable){
				range = calcRange(0,0,radius);
				movable = true;
			}
			actTime = 0;
		}
	}
	public void initiate(){
	}

	
	
	@Override
	public void onClick(int x, int y){
		if(movable&&attacking==-1){
		for(int a = 0;a<(1+(radius*2));a++){
			for(int b = 0;b<(1+(radius*2));b++){
				if(range[a][b]>0&&range[a][b]<=radius){
					if((1+a-radius)*8>=x&&(1+a-radius)*8<x+8){
						if((1+b-radius)*8>=y&&(1+b-radius)*8<y+8){
							System.out.println("tile = "+(a-radius)+" : "+(b-radius));
							moves.addAll(findPath((a-radius),(b-radius)));
							movable=false;
							health-= MOVEMENT_COST;
						}
					}
				}
			}
		}
		}
		if(attacking!=-1){
			int attackRadius = (targets.length/2);
			for(int a = 0;a<(1+(attackRadius*2));a++){
				for(int b = 0;b<(1+(attackRadius*2));b++){
					if(targets[a][b]!=-1){
						if((1+a-attackRadius)*8>=x&&(1+a-attackRadius)*8<x+8){
							if((1+b-attackRadius)*8>=y&&(1+b-attackRadius)*8<y+8){
								int[][] dmg = actions[attacking].getPossibleTargets(targets[b][a]);
								LinkedList<Character> victims = new LinkedList<Character>();
								LinkedList<Integer> victimFields = new LinkedList<Integer>();
								for(int c = 0;c<dmg.length;c++){
									for(int d = 0;d<dmg.length;d++){
										if(dmg[c][d]!=0){
											Character e = checkCharacter(d-attackRadius,c-attackRadius);
											if(e!=null){
												victims.add(e);
												victimFields.add(dmg[c][d]);
											}
										}
									}
								}
								Character[] tempChar = new Character[victims.size()];
								int[] tempInt = new int[victims.size()];
								for(int f = 0;f<tempChar.length;f++){
									tempChar[f]=victims.poll();
									tempInt[f]=victimFields.poll();
								}
								actions[attacking].start(targets[b][a],tempChar,tempInt);
								health-=playerActions[attacking].provideAttackCosts();
								waitForActionEnd = true;
							}
						}
					}
				}
			}
		}
	}
	
	public void useAbility(int id){
		DebugLayer.pushError("Player use action "+id);
		if(playerActions[id] instanceof EndTurn){
			movable = false;
			changeHealth(END_TURN_BONUS);
			endTurn();
		}
		if(!(actions[id] instanceof DefaultAction)){
			attacking = id;
			targets = actions[id].getPossibleTargets();
		}
	}
	@Override
	public String getName() {
		return "player";
	}
}
