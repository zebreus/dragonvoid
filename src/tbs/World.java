package tbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class World {
	public static final int LOAD_RANGE = 500;
	public static final int SIGHT_RANGE = 500;

	String[][] chunks;
	Chunk[][] loaded;

	int playerChunkX;
	int playerChunkY;
	int width;
	int height;

	String path;
	
	WorldFeedbackReceiver wfr;
	//TODO Listenart auswï¿½hlen, besser wï¿½re was mit threadsicherheit
	public LinkedList<Character> characters;

	HashMap<String, HashMap<Integer, String[]>> id_lists = new HashMap<String, HashMap<Integer, String[]>>();

	/*
	 * Aufbau der ID-List Map
	 * 
	 * Ebene 1: String--> characters,floor ... dazu eine Map zugeordnet mit
	 * einer hochzÃ¤hlenden Zahl beginnend bei 1 gehÃ¶rig dazu das ausgelesene in
	 * ein String[] <a/s> <name>
	 */
	public int[] getRelativeDistance(Character a, Character b){
		return getRelativeDistance(a.getChunkX(),b.getChunkX(),a.getChunkY(),b.getChunkY(),a.getX(),b.getX(),a.getY(),b.getY());
	}
	public int[] getRelativeDistance(int chunkxa,int chunkxb, int chunkya, int chunkyb,int xa, int xb,int ya, int yb){
		xb = (chunkxb-chunkxa)*8+xb;
		yb = (chunkyb-chunkya)*8+yb;
		int[] ret = {(xb-xa),(yb-ya)};
		return ret;
	}
	public Character getCharacter(Class<?> cls){
		for(Character c: characters){
			if(cls.isInstance(c)){
				return c;
			}
		}
		return null;
	}
	public World(WorldFeedbackReceiver wfr, String world, int playerChunkX,
			int playerChunkY) throws FileNotFoundException {

		
		
		path = "res/worlds/" + world + "/";
		loadIDLists();
		characters = new LinkedList<Character>();
		Scanner s = new Scanner(new File(path + "world"));
		this.wfr = wfr;
		width = s.nextInt();
		height = s.nextInt();
		loaded = new Chunk[width][height];
		chunks = new String[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				chunks[x][y] = s.next();
			}
		}
		
		s.close();
		
	}

	private void loadIDLists() throws FileNotFoundException {
		String path = this.path+"chunks/";

		id_lists.put("characters", loadIDList(path, "characters"));
		id_lists.put("floor", loadIDList(path, "floor"));
		id_lists.put("items", loadIDList(path, "items"));
		id_lists.put("triggers", loadIDList(path, "triggers"));
		id_lists.put("walls", loadIDList(path, "walls"));
		
		
	}

	public void loadWorld() throws IOException {
		//Herausfinden wie viele Chunks geladen werden müssen
		int amountToLoad = 0;
		for (int y = playerChunkY - LOAD_RANGE; y < playerChunkY + LOAD_RANGE; y++) {
			for (int x = playerChunkX - LOAD_RANGE; x < playerChunkX + LOAD_RANGE; x++) {
				if (x >= 0 && x < width && y >= 0 && y < height) {
					amountToLoad++;
				}
			}
		}
		
		//Chunks laden
		int yetLoaded = 0;
		for (int y = playerChunkY - LOAD_RANGE; y < playerChunkY + LOAD_RANGE; y++) {
			for (int x = playerChunkX - LOAD_RANGE; x < playerChunkX + LOAD_RANGE; x++) {
				if (x >= 0 && x < width && y >= 0 && y < height) {
					loadChunk(x,y);
					yetLoaded++;
				}
				
				//Prozentzahl berechnen und an World Feedback Receiver zurückgeben.
				int percent = ((int) ((100f * (float)((float)yetLoaded / (float)amountToLoad))));
				if(Config.DEBUG){
					DebugLayer.elements.put("Chunks too load",""+amountToLoad);
					DebugLayer.elements.put("Chunks yet loaded",""+yetLoaded);
					DebugLayer.elements.put("Loading Progress",percent+ " %");
				}
				wfr.WorldLoadingProgress(percent);
			}
		}
		
		if(Config.DEBUG){
			DebugLayer.elements.remove("Loading Progress");
		}
		wfr.WorldLoaded();
	}
	
	public Chunk loadChunk(int x, int y) throws IOException{
		Chunk c = loaded[x][y] = new Chunk(this,x,y,path + "chunks/","res/saves/" + SaveManager.saveName + "/chunks",
				chunks[x][y],id_lists);
		characters.addAll(c.getCharacters());
		Collections.sort(characters, new CharacterSpeedComparator());
		return c;
	}
	
	public void unloadChunk(int x, int y){
		Chunk c = loaded[x][y];
		if(c!=null){
		characters.removeAll(c.getCharacters());
		Collections.sort(characters, new CharacterSpeedComparator());
		c.unload();
		}
		c = null;
	}

	public Chunk getLoadedChunk(int x, int y) {
		if (loaded[x][y] != null) {
			return loaded[x][y];
		} else {
			try {
				if(Config.DEBUG){
					DebugLayer.pushError("Requested Chunk "+chunks[x][y]+" not loaded, loading it now");
				}
				loaded[x][y] = new Chunk(this,x,y,path+"chunks/","res/saves/" + SaveManager.saveName + "/chunks", chunks[x][y],id_lists);
				return loaded[x][y];
			} catch (IOException e) {
				if(Config.DEBUG){
					DebugLayer.pushError("Could not load Chunk "+chunks[x][y]);
				}
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private HashMap<Integer, String[]> loadIDList(String path,String type){
		
		Scanner s;
		try {
			s = new Scanner(new File(path + type +  "/id_list"));

		HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
		
		while(s.hasNext()){
			int id = s.nextInt();
			String[] values = {s.next(), s.next()};
			map.put(id, values);
		}
		
		
		
		s.close();
		return map;
		
		} catch (FileNotFoundException e1) {
			if(Config.DEBUG){
				DebugLayer.pushError("Failed to load IDList "+type);
			}
			e1.printStackTrace();
		}
		return null;
	}
	
	public void saveAllChunks(){
		
		for(int x = 0;x<width;x++){
			for(int y=0;y<height;y++){
				loaded[x][y].saveChunkIfNeeded();
			}
		}
		
	}
	
	public void resetAllChunks(){
		
		for(int x = 0;x<width;x++){
			for(int y=0;y<height;y++){
				loaded[x][y].resetChunk();
			}
		}
		
	}
	
}
