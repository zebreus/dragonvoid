package tbs;

import java.io.IOException;

public class WorldLoadingScreen implements FrameHook, WorldFeedbackReceiver{
	/**
	 * @uml.property  name="percentage"
	 */
	int percentage;
	/**
	 * @uml.property  name="background"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Sprite background_left;
	Sprite background_right;
	Sprite[] loadingbars;
	Sprite[] leftbars;
	Sprite[] rightbars;
	int dif = 120;
	boolean loaded = false;
	/**
	 * @uml.property  name="w"
	 * @uml.associationEnd  
	 */
	World w;
	/**
	 * @uml.property  name="world"
	 */
	String world;
	/**
	 * @uml.property  name="chunkx"
	 */
	int chunkx;
	/**
	 * @uml.property  name="chunky"
	 */
	int chunky;
	/**
	 * @uml.property  name="saves"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	SaveManager saves;
	
	public WorldLoadingScreen(String world,SaveManager saves) throws IOException{
		percentage = 0;
		this.saves = saves;
		background_left = new Sprite("HUD/loadingscreen_background_left");
		background_right = new Sprite("HUD/loadingscreen_background_right");
		loadingbars = new Sprite[4];
		loadingbars[0] = new Sprite("HUD/loadingscreen_bar_1");
		loadingbars[1] = new Sprite("HUD/loadingscreen_bar_2");
		loadingbars[2] = new Sprite("HUD/loadingscreen_bar_3");
		loadingbars[3] = new Sprite("HUD/loadingscreen_bar_4");
		leftbars=new Sprite[50];
		rightbars = new Sprite[50];
		this.world = world;
		this.chunkx = (Integer)saves.getProperty("xchunk").getContent();
		this.chunky = (Integer)saves.getProperty("ychunk").getContent();
		
	}
	public void loadWorld() throws IOException{
		w = new World(this,world,chunkx,chunky);
		w.loadWorld();
	}
	@Override
	public void WorldLoaded() {
		loaded = true;
	}

	@Override
	public void WorldLoadingProgress(int percentage) {
		this.percentage = percentage;
		
	}
	long last = 0;
	int speed = 10;
	boolean leftFull = false;
	
	@Override
	public void update(long delta) {
		if(loaded&&dif<=0){
			Ingame in = new Ingame(w,saves,this);
			in.setLoadingScreenOpener(new LoadingScreenOpener(background_left,background_right,leftbars,rightbars));
			GameFrame.getFrame().hook = in;
		}
		if(dif>0){
		if(last+speed<System.currentTimeMillis()){
			dif-=2;
			speed-=speed/10;
			last = System.currentTimeMillis();
		}
		}
		if(percentage==0){
			
		}else if(percentage<=50){
			for(int x = percentage-1;x!=-1&&leftbars[x]==null;x--){
				leftbars[x] = loadingbars[((int)(Math.random()*4))];
			}
		}
		else if(percentage>50){
			if(!leftFull){
				for(int x = 49;x!=-1&&leftbars[x]==null;x--){
					leftbars[x] = loadingbars[((int)(Math.random()*4))];
				}
			}
			for(int x = percentage-51;x!=-1&&rightbars[x]==null;x--){
				rightbars[x] = loadingbars[((int)(Math.random()*4))];
			}
		}
		
	}
	
	@Override
	public void repaint(Matrix mat) {
		
		background_left.paint(mat, -dif, 0);
		background_right.paint(mat, 104+dif, 0);
		for(int x=0;x<50;x++){
			if(leftbars[x]!=null){
				leftbars[x].paint(mat, 54+x-dif, 61);
			}
			if(rightbars[x]!=null){
				rightbars[x].paint(mat, 104+x+dif, 61);
			}
		}
	}

	@Override
	public void keyPressed(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseLeftPress(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseRightPress(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseRightRelease(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMove(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseLeftClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseRightClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}

}
