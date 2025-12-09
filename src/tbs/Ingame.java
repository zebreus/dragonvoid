package tbs;

import java.awt.event.KeyEvent;
import java.io.IOException;

import characters.Player;

public class Ingame implements FrameHook, HUDFeedbackReceiver{

	World w;
	int[] chunk;
	int[] camera;
	SaveManager saves;
	HUD hud;
	Character act;
	int next = 0;
	int turnDelay;
	int actDelay;
	final int X_MID=Config.WIDTH/2;
	final int Y_MID=Config.HEIGHT/2;
	Player player;
	@SuppressWarnings("unused")
	private WorldLoadingScreen loadingscreen;
	
	public Ingame(World w,SaveManager saves,WorldLoadingScreen loadingscreen){
		this.loadingscreen = loadingscreen;
		this.saves = saves;
		this.w = w;
		int[] tempChunk = {(Integer)saves.getProperty("xchunk").getContent(), (Integer)saves.getProperty("ychunk").getContent()};
		chunk = tempChunk;
		int[] tempCamera = {0,0};
		camera = tempCamera;
		hud = new HUD(w,this, 100,100);
		player = (Player)w.getCharacter(characters.Player.class);
		centerCamera();
	}
	
	long last = 0;
	boolean down = true;
	private LoadingScreenOpener opener;
	
	@Override
	public void update(long delta) {
		for(int x = chunk[0]-World.SIGHT_RANGE;x<chunk[0]+World.SIGHT_RANGE;x++){
			for(int y = chunk[1]-World.SIGHT_RANGE;y<chunk[1]+World.SIGHT_RANGE;y++){
				if(chunk[0]+x>=0&&chunk[0]+x<w.width&&chunk[1]+y>=0&&chunk[1]+y<w.height){
					w.getLoadedChunk(chunk[0]+x, chunk[1]+y).update(delta);
				}
			}
		}
		hud.setHealth(player.getHealth());
		hud.setMaxHealth(player.getMaxHealth());
		if(act == null||act.finished()){
			if(next>=0&next<w.characters.size()){
				act = w.characters.get(next);
				act.startTurn();
				next++;
			}else{
				next = 0;
			}
		}
		if(act!=null){
		DebugLayer.elements.put("Has Turn", act.getClass().getSimpleName());
		}else{
		DebugLayer.elements.put("Has Turn", "null");
		}
		//TODO Zugreihenfolge verbessern
		
		for(Character c: w.characters){
			c.lowUpdate(delta);
		}
		if(opener!=null){
			opener.update(delta);
			if(opener.finished){
				opener = null;
			}
		}
		Character del = null;
		for(Character c: w.characters){
			if(c.getHealth()<=0){
				if(c instanceof Player){
					gameOver();
				}
				else{
				player.changeHealth(c.getBounty());
				w.loaded[c.getChunkX()][c.getChunkY()].characters[c.getX()][c.getY()]=null;
				del=c;
				}
			}
		}
		if(del!=null){
			w.characters.remove(del);
		}
	}
	public void gameOver(){
		DebugLayer.pushError("Player Died");
		w.resetAllChunks();
		try {
			saves.loadSave();
			// TODO Sauschlechten Code ausbessern
			final WorldLoadingScreen wls = new WorldLoadingScreen((String) saves.getProperty("worldname").getContent(),
					saves);
			GameFrame.getFrame().setHook(wls);
			new Thread() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						wls.loadWorld();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

		} catch (Exception e) {
			System.err.println("Unable to switch to world loading screen");
			e.printStackTrace();
		}
	}
	@Override
	public void repaint(Matrix mat) {
		for(int x = chunk[0]-World.SIGHT_RANGE;x<chunk[0]+World.SIGHT_RANGE;x++){
			for(int y = chunk[1]-World.SIGHT_RANGE;y<chunk[1]+World.SIGHT_RANGE;y++){
				if(chunk[0]+x>=0&&chunk[0]+x<w.width&&chunk[1]+y>=0&&chunk[1]+y<w.height){
					//System.out.println(w.loaded[playerChunkX+x][playerChunkY+y]);
					w.getLoadedChunk(chunk[0]+x, chunk[1]+y).paintFloor(mat, x*64+X_MID+camera[0], y*64+Y_MID+camera[1]);
				}
			}
		}
		for(int x = chunk[0]-World.SIGHT_RANGE;x<chunk[0]+World.SIGHT_RANGE;x++){
			for(int y = chunk[1]-World.SIGHT_RANGE;y<chunk[1]+World.SIGHT_RANGE;y++){
				if(chunk[0]+x>=0&&chunk[0]+x<w.width&&chunk[1]+y>=0&&chunk[1]+y<w.height){
					//System.out.println(w.loaded[playerChunkX+x][playerChunkY+y]);
					w.getLoadedChunk(chunk[0]+x, chunk[1]+y).paintItems(mat, x*64+X_MID+camera[0], y*64+Y_MID+camera[1]);
				}
			}
		}
		for(int x = chunk[0]-World.SIGHT_RANGE;x<chunk[0]+World.SIGHT_RANGE;x++){
			for(int y = chunk[1]-World.SIGHT_RANGE;y<chunk[1]+World.SIGHT_RANGE;y++){
				if(chunk[0]+x>=0&&chunk[0]+x<w.width&&chunk[1]+y>=0&&chunk[1]+y<w.height){
					//System.out.println(w.loaded[playerChunkX+x][playerChunkY+y]);
					w.getLoadedChunk(chunk[0]+x, chunk[1]+y).paintCharacters(mat, x*64+X_MID+camera[0], y*64+Y_MID+camera[1]);
				}
			}
		}
		hud.paint(mat);
		if(opener!=null){
			opener.paint(mat);
		}
	}

	@Override
	public void keyPressed(int keycode) {
		switch (keycode){
		case KeyEvent.VK_W:
			camera[1] --;
			break;
		case KeyEvent.VK_A:
			camera[0] --;
			break;
		case KeyEvent.VK_S:
			camera[1]++;
			break;
		case KeyEvent.VK_D:
			camera[0] ++;
			break;
		case KeyEvent.VK_ESCAPE:
			w.saveAllChunks();
			DebugLayer.pushError("World was saved");
			break;
		}
	}
	//
	/**Used to resolve chunk position on screen
	 * @param chunk Format: {chunkX, chunkY}
	 * @return Format: {screenX, Y}
	 */
	public int[] getScreenPosition(int[] chunk){
		//TODO Methode schreiben
		System.err.println("Unfinished Method getScreenPosition(int[] chunk) in Ingame got called");
		return null;
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
		if(!hud.onPress(x, y)){
		//TODO bessere Clickübergabe
		for(Character c: w.characters){
			int cx = (c.chunkX-chunk[0])*64+X_MID+camera[0]+c.x*8;
			int cy = (c.chunkY-chunk[1])*64+Y_MID+camera[1]+c.y*8;
			c.onClick(x - cx ,y - cy);
		}
		}
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		hud.onRelease(x, y);
		
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
		hud.onMove(x, y);
		
	}
	@Override
	public void mouseLeftClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseRightClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void centerCamera() {
		int px = (player.chunkX*8*8)+player.getX()*8;
		int py = (player.chunkY*8*8)+player.getY()*8;
		px -= (chunk[0]*8*8);
		py -= (chunk[1]*8*8);
		px=px*-1;
		py=py*-1;
		camera[0] = px;
		camera[1] = py;
		
	}
	@Override
	public void useAbility(int ability) {
		Player player = (Player)w.getCharacter(characters.Player.class);
		player.useAbility(ability);
	}
	@Override
	public void menuButton() {
		// TODO Auto-generated method stub
		
	}

	public void setLoadingScreenOpener(LoadingScreenOpener loadingScreenOpener) {
		opener = loadingScreenOpener;
		
	}
	
}
