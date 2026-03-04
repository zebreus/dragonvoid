package tbs;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public abstract class Character implements Drawable{
	protected int health;
	protected int maxHealth;
	protected Animation anim;
	protected World world;
	protected int chunkX;
	protected int chunkY;
	protected int x;
	protected int y;
	protected  int speed;
	protected boolean hasTurn;
	protected LinkedList<Point> moves;
	protected boolean movable;
	protected boolean lifebar;
	protected Sprite[] lifebarSprites;
	public int getBounty(){
		return 0;
	}
	public int getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}

	public int getChunkX(){
		return chunkX;
	}
	public int getChunkY(){
		return chunkY;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean HasTurn() {
		return hasTurn;
	}
	public void endTurn(){
		hasTurn = false;
	}
	public void changeHealth(int change){
		 if(health+change<=maxHealth){
			 health+=change;
		 }else{
			 health = maxHealth;
		 }
	}
	boolean peace;
	public Character(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super();
		this.anim = anim;
		this.world = world;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.x = x;
		this.y = y;
		speed = 5;
		moves = new LinkedList<Point>();
		movable = false;
		lifebar = false;
		lifebarSprites = new Sprite[4];

		try {
			lifebarSprites[0] = new Sprite("HUD/enemy_lifebar_start");
			lifebarSprites[1] = new Sprite("HUD/enemy_lifebar_end");
			lifebarSprites[2] = new Sprite("HUD/enemy_lifebar_segment");
			lifebarSprites[3] = new Sprite("HUD/enemy_lifebar_part");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void lowUpdate(long delta){
		anim.update(delta);
		update(delta);
	}
	public void paintLifebar(Matrix mat, int x, int y){
		if(lifebar){
			int segments = (maxHealth+4)/5;
			int parts = health;
			x -=3;
			y -=5;
			while(segments>=2){
				mat.drawDrawable(lifebarSprites[0], x, y);
				mat.drawDrawable(lifebarSprites[2], x+2, y);
				mat.drawDrawable(lifebarSprites[2], x+7, y);
				mat.drawDrawable(lifebarSprites[1], x+12, y);
				for(int c = 0;c<10&&parts>0;c++){
					mat.drawDrawable(lifebarSprites[3], x+2+c, y+1);
					parts--;
				}
				y-=5;
				segments-=2;
			}
			if(segments==1){
				mat.drawDrawable(lifebarSprites[0], x+3, y);
				mat.drawDrawable(lifebarSprites[2], x+5, y);
				mat.drawDrawable(lifebarSprites[1], x+10, y);
				for(int c = 0;c<5&&parts>0;c++){
					mat.drawDrawable(lifebarSprites[3], x+5+c, y+1);
					parts--;
				}
			}
		}
	}
	public abstract void update(long delta);
	public boolean finished(){
		return !hasTurn;
	}
	public void startTurn(){
		hasTurn = true;
	}
	public void setPeace(boolean peaceful){
		peace = peaceful;
	}
	public int[][] getAlpha(){
		return anim.getAlpha();
	}
	public Color[][] getColors(){
		return anim.getColors();
	}
	public int getWidth(){
		return anim.getWidth();
	}
	public int getHeight(){
		return anim.getHeight();
	}
	public void paint(Matrix mat, int x, int y){
		anim.paint(mat, x, y);
	}
	public Character checkCharacter(int x, int y){
		int Xtemp = this.x+x;
		int Xmovement = Xtemp%8;
		int XchunkMovement = (int)(Xtemp/8);
		int Ytemp = this.y+y;
		int Ymovement = Ytemp%8;
		int YchunkMovement = (int)(Ytemp/8);
		if(Xmovement<0){
			Xmovement+=8;
			XchunkMovement-=1;
		}
		if(Ymovement<0){
			Ymovement+=8;
			YchunkMovement-=1;
		}
		try{
		Chunk c = world.loaded[chunkX+XchunkMovement][chunkY+YchunkMovement];
		if(c!=null){
			return (Character)c.characters[Xmovement][Ymovement];
		}else{
			System.err.println("Chunk not loaded : "+(chunkX+XchunkMovement)+" | "+(chunkY+YchunkMovement));
			return null;
		}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public boolean check(int x, int y){
		int Xtemp = this.x+x;
		int Xmovement = Xtemp%8;
		int XchunkMovement = (int)(Xtemp/8);
		int Ytemp = this.y+y;
		int Ymovement = Ytemp%8;
		int YchunkMovement = (int)(Ytemp/8);
		if(Xmovement<0){
			Xmovement+=8;
			XchunkMovement-=1;
		}
		if(Ymovement<0){
			Ymovement+=8;
			YchunkMovement-=1;
		}
		try{
		Chunk c = world.loaded[chunkX+XchunkMovement][chunkY+YchunkMovement];
		if(c!=null){
			return c.checkPosition(Xmovement, Ymovement);
		}else{
			System.err.println("Chunk not loaded : "+(chunkX+XchunkMovement)+" | "+(chunkY+YchunkMovement));
			return false;
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean move(int x,int y){
		try{
		int Xtemp = this.x+x;
		int Xmovement = Xtemp%8;
		int XchunkMovement = (int)(Xtemp/8);
		int Ytemp = this.y+y;
		int Ymovement = Ytemp%8;
		int YchunkMovement = (int)(Ytemp/8);
		if(Xmovement<0){
			Xmovement+=8;
			XchunkMovement-=1;
		}
		if(Ymovement<0){
			Ymovement+=8;
			YchunkMovement-=1;
		}
		System.out.println(XchunkMovement);
		if(XchunkMovement == 0 && YchunkMovement == 0){
			Chunk c = world.loaded[chunkX+XchunkMovement][chunkY+YchunkMovement];
			if (c!=null){
				if(world.loaded[chunkX][chunkY].moveCharacter(this, Xmovement, Ymovement)){
					this.x=Xmovement;
					this.y=Ymovement;
					return true;
				}else{
					return false;
				}
			}else {
				System.err.println("Own chunk ( "+chunkX+" | "+chunkY+" )not loaded");
				return false;
			}
		}else{
			Chunk oldChunk = world.loaded[chunkX][chunkY];
			Chunk newChunk = world.loaded[chunkX+XchunkMovement][chunkY+YchunkMovement];
			if(oldChunk!=null){
				if(newChunk!=null){
					if(newChunk.addCharacter(this, Xmovement, Ymovement)){
						oldChunk.removeCharacter(this);
						this.x=Xmovement;
						this.y=Ymovement;
						this.chunkX = chunkX+XchunkMovement;
						this.chunkY = chunkY+YchunkMovement;
						return true;
					}else{
						System.err.println("Couldn't add Character in new Chunk ( "+(chunkX+XchunkMovement)+" | "+(chunkY+YchunkMovement)+" )");
						return false;
					}
				}else{
					System.err.println("New chunk ( "+(chunkX+XchunkMovement)+" | "+(chunkY+YchunkMovement)+" )not loaded");
					return false;
				}
			}else {
				System.err.println("Own chunk ( "+chunkX+" | "+chunkY+" )not loaded");
				return false;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public int getSpeed(){
		return speed;
	}
	public LinkedList<Point> moveCharacter(int[][] range, int startX, int startY){
		int radius = (range.length-1)/2;
		boolean ret = (check(startX,startY))&startX<=radius&startY<=radius&startX>=-radius&startY>=-radius&&range[radius+startX][radius+startY]!=-1;
		if(ret){
			LinkedList<Point> temp = new LinkedList<Point>();
			//TODO Endlosschleife möglich
			int attempts=radius*4;
			while(attempts>0&&(startX!=0||startY!=0)){
				attempts--;
			if(radius+startX+1<radius*2+1&&range[radius+startX+1][radius+startY]!=-1&&range[radius+startX+1][radius+startY]<range[radius+startX][radius+startY]){
				temp.add(new Point(-1,0));
				startX++;
			}else if(radius+startX-1>-1&&range[radius+startX-1][radius+startY]!=-1&&range[radius+startX-1][radius+startY]<range[radius+startX][radius+startY]){
				temp.add(new Point(1,0));
				startX--;
			}else if(radius+startY-1>-1&&range[radius+startX][radius+startY-1]!=-1&&range[radius+startX][radius+startY-1]<range[radius+startX][radius+startY]){
				temp.add(new Point(0,1));
				startY--;
			}else if(radius+startY+1<radius*2+1&&range[radius+startX][radius+startY+1]!=-1&&range[radius+startX][radius+startY+1]<range[radius+startX][radius+startY]){
				temp.add(new Point(0,-1));
				startY++;
			}else{
				System.err.println("Pathfinding failed");
			}
			}
			if(attempts!=0){
			Collections.reverse(temp);
			return temp;
			}
		}
		return null;
	}
	public LinkedList<Point> findPath(int sx, int sy){
		int radius = Math.abs(sx);
		if(Math.abs(sy)>radius){
			radius = Math.abs(sy);
		}
		for(int bonus = Config.PATHFINDING_BASE_RADIUS_BONUS;bonus<=Config.PATHFINDING_MAX_RADIUS_BONUS;bonus++){
			int[][] grid = calcRange(0,0,radius+bonus);
			LinkedList<Point> points=moveCharacter(grid,sx,sy);
			if(points != null){
				return points;
			}
		}
		return null;
	}
	public int[][] calcRange(int sx, int sy, int radius){
		if(radius<=Config.PATHFINDING_MAX_RADIUS){
		boolean[][] temp = new boolean[1+(radius*2)][1+(radius*2)];
		int[][] tempint = new int[1+(radius*2)][1+(radius*2)];
		for(int x = 0;x<(1+(radius*2));x++){
			for(int y = 0;y<(1+(radius*2));y++){
				tempint[x][y] = -1;
				temp[x][y] = check(x-radius-sx,y-radius-sy);
			}
		}
		LinkedList<Point> points = new LinkedList<Point>();
		points.add(new Point(radius,radius));
		tempint[radius][radius] = 0;
		while(points.size()>0){
			Point p = points.poll();
			//TODO hier
			int act = tempint[p.x][p.y];
			if(p.x-1>=0&&tempint[p.x-1][p.y]==-1&&temp[p.x-1][p.y]){
				tempint[p.x-1][p.y] = act+1;
				points.add(new Point(p.x-1,p.y));
			}
			if(p.x+1<(1+(radius*2))&&tempint[p.x+1][p.y]==-1&&temp[p.x+1][p.y]){
				tempint[p.x+1][p.y] = act+1;
				points.add(new Point(p.x+1,p.y));
			}
			if(p.y-1>=0&&tempint[p.x][p.y-1]==-1&&temp[p.x][p.y-1]){
				tempint[p.x][p.y-1] = act+1;
				points.add(new Point(p.x,p.y-1));
			}
			if(p.y+1<(1+(radius*2))&&tempint[p.x][p.y+1]==-1&&temp[p.x][p.y+1]){
				tempint[p.x][p.y+1] = act+1;
				points.add(new Point(p.x,p.y+1));
			}
		}
		return tempint;
		}else{
			DebugLayer.pushError("Blocked Pathfinding with range "+radius);
			return new int[0][0];
		}
	}
	public boolean inRange(int[][] range,int sx, int sy, int radius){
		if(radius+sx>-1&&radius+sx<radius*2+1&&radius+sy>-1&&radius+sy<radius*2+1){
			if(range[radius+sx][radius+sy]<=radius){
				return true;
			}else {return false;}
		}else{
			if(Config.DEBUG){
				DebugLayer.errors.add("Checked Range of out of Bounds Pos");
				
			}
			return false;
		}
	}
	public void onClick(int x, int y){}
	public void onKey(KeyEvent ke){}
}
