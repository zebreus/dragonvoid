package characters;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

import tbs.Animation;
import tbs.Character;
import tbs.CharacterSpeedComparator;
import tbs.Matrix;
import tbs.World;

public class PinkPortal extends Character{
	static final int CHANCE_NOTHING = 100;
	static final int CHANCE_MAGMA = 50;
	static final int CHANCE_VOODOO = 50;
	static final int BOUNTY = 20;
	@Override
	public int getBounty(){
		return BOUNTY;
	}
	public PinkPortal(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super(anim, world, chunkX, chunkY, x, y);
		anim.animateRepeating(anim.getSequenceByName("active"));
		health = 35;
		maxHealth = 35;
		lifebar = true;
	}
	// um den Spieler herumlaufen 0 = oben 1 = rechts 2 = unten 3= links
	Character next;
	long actTime;
	long baseTime = 100;

	@Override
	public void update(long delta) {
	}
	@Override
	public void startTurn(){
		int ran = (int)(Math.random()*(CHANCE_NOTHING+CHANCE_MAGMA+CHANCE_VOODOO));
		int relX = ((int)(Math.random()*3))-1;
		int relY = ((int)(Math.random()*3))-1;
		int tempX = x+relX;
		int tempY = y+relY;
		if(check(relX,relY)){
		int tempCX = chunkX;
		int tempCY = chunkY;
		if(tempX>=8){
			tempCX++;
			tempX-=8;
		}else if(tempX<0){
			tempCX--;
			tempX+=8;
		}
		if(tempY>=8){
			tempCY++;
			tempY-=8;
		}else if(tempY<0){
			tempCY--;
			tempY+=8;
		}
		if(ran>=0&&ran<CHANCE_NOTHING){
			//nix
		}else if(ran>=CHANCE_NOTHING&&ran<CHANCE_NOTHING+CHANCE_MAGMA){
			next = new Voodoo(new Animation("characters/voodoo/voodoo"), world, tempCX, tempCY, tempX, tempY);
		}else if(ran>=CHANCE_NOTHING+CHANCE_MAGMA&&ran<CHANCE_NOTHING+CHANCE_MAGMA+CHANCE_VOODOO){
			next = new Magma(new Animation("characters/magma/magma"), world, tempCX, tempCY, tempX, tempY);
		}
		if(next!=null){
		world.getLoadedChunk(next.getChunkX(), next.getChunkY()).addCharacter(next, next.getX(), next.getY());
		world.characters.add(next);
		Collections.sort(world.characters, new CharacterSpeedComparator());
		}
		}
	}
	@Override
	public boolean finished(){
		return true;
	}
	@Override
	public void paint(Matrix mat,int x, int y){
		anim.paint(mat, x, y);
		paintLifebar(mat,x,y);
	}

	private void action() {
		
	}
	@Override
	public String getName() {
		return "pink_portal";
	}
	}
