package tbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import characters.Magma;
import characters.Orc;
import characters.PinkPortal;
import characters.Player;
import characters.Voodoo;

public class Chunk {

	Drawable[][] characters;
	Drawable[][] floor;
	Drawable[][] items;
	Drawable[][] triggers;
	Drawable[][] walls;

	LinkedList<Animation> animations;

	String playerWorldpath;
	String defaultWorldPath;
	String chunkname;

	World w;
	int chunkX;
	int chunkY;

	private HashMap<String, HashMap<Integer, String[]>> id_lists = new HashMap<String, HashMap<Integer, String[]>>();

	public Chunk(World w, int chunkX, int chunkY, String defaultWorldPath, String playerWorldpath, String chunkname,
			HashMap<String, HashMap<Integer, String[]>> id_lists) throws IOException {

		this.id_lists = id_lists;

		this.playerWorldpath = playerWorldpath;
		this.defaultWorldPath = defaultWorldPath;
		this.chunkname = chunkname;

		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.w = w;
		// defaultWorldPath: res/worlds/world/chunks/
		// playerWorldPath: res/saves/<Name>/world/chunks/
		animations = new LinkedList<Animation>();
		// if(chunkname.equals("chunk900")){
		// System.err.println("Pause");
		// }
		if (Config.DEBUG) {
			DebugLayer.elements.put("Loading chunk", chunkname);
		}
		floor = loadTextures("floor");
		items = loadTextures("items");
		triggers = loadTextures("triggers");
		walls = loadTextures("walls");
		characters = loadCharacters();

	}

	private Drawable[][] loadCharacters() {
		File playerfile = new File(playerWorldpath + "/characters/" + chunkname);

		Drawable[][] sprites = new Drawable[8][8];

		Scanner s;
		try {
			if (playerfile.exists()) {

				s = new Scanner(playerfile);

			} else {
				s = new Scanner(new File(defaultWorldPath + "/characters/" + chunkname));
			}

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int value = 0;
					if (s.hasNext())
						value = Integer.parseInt(s.next());
					else
						break;

					if (value == 0) {
						sprites[x][y] = null;
					} else {
						if (id_lists.get("characters").get(value)[0].equals("a")) {

							if (this.id_lists.get("characters").get(value)[1].equals("player")) {
								sprites[x][y] = new Player(new Animation("characters/player/player"), w, chunkX, chunkY,
										x, y);
							} else if (this.id_lists.get("characters").get(value)[1].equals("pink_portal")) {
								sprites[x][y] = new PinkPortal(new Animation("characters/pink_portal/pink_portal"), w,
										chunkX, chunkY, x, y);
							} else if (this.id_lists.get("characters").get(value)[1].equals("orc")) {
								sprites[x][y] = new Orc(new Animation("characters/orc/orc"), w, chunkX, chunkY, x, y);
							} else if (this.id_lists.get("characters").get(value)[1].equals("magma")) {
								sprites[x][y] = new Magma(new Animation("characters/magma/magma"), w, chunkX, chunkY, x, y);
							} else if (this.id_lists.get("characters").get(value)[1].equals("voodoo")) {
								sprites[x][y] = new Voodoo(new Animation("characters/voodoo/voodoo"), w, chunkX, chunkY, x, y);
							}

						}
					}
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return sprites;
	}

	public void paintFloor(Matrix mat, int x, int y) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				if (floor[a][b] != null) {
					floor[a][b].paint(mat, x + a * 8, y + b * 8);
				}
				if (walls[a][b] != null) {
					walls[a][b].paint(mat, x + a * 8, y + b * 8);
				}
			}
		}
	}

	public void paintItems(Matrix mat, int x, int y) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				if (items[a][b] != null) {
					items[a][b].paint(mat, x + a * 8, y + b * 8);
				}
			}
		}
	}

	public void paintCharacters(Matrix mat, int x, int y) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 8; b++) {
				if (characters[a][b] != null) {
					characters[a][b].paint(mat, x + a * 8, y + b * 8);
				}
				if (triggers[a][b] != null) {
					triggers[a][b].paint(mat, x + a * 8, y + b * 8);
				}
			}
		}
	}

	private Drawable[][] loadTextures(String type) throws IOException {

		File playerfile = new File(playerWorldpath + "/" + type + "/" + chunkname);

		Drawable[][] sprites = new Drawable[8][8];

		Scanner s;
		try {
			if (playerfile.exists()) {

				s = new Scanner(playerfile);

			} else {
				s = new Scanner(new File(defaultWorldPath + "/" + type + "/" + chunkname));
			}

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {

					int value = 0;

					if (s.hasNext()) {
						value = Integer.parseInt(s.next());
					} else {
						break;
					}

					if (value == 0) {
						sprites[x][y] = null;
					} else {
						if (id_lists.get(type).get(value)[0].equals("a")) {
							Animation a = new Animation(type + "/" + this.id_lists.get(type).get(value)[1]);
							sprites[x][y] = a;
							a.animateRepeating(a.getSequenceByName("default"));
							animations.add(a);
						} else {
							sprites[x][y] = new Sprite(type + "/" + this.id_lists.get(type).get(value)[1]);
						}
					}
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sprites;

	}

	public boolean addCharacter(Character c, int x, int y) {

		if (checkPosition(x, y)) {
			characters[x][y] = c;
			return true;
		} else
			return false;
	}

	public boolean removeCharacter(Character c) {

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				if (characters[x][y] != null && characters[x][y].equals(c)) {

					characters[x][y] = null;
					return true;
				}
			}
		}
		return false;
	}

	public boolean moveCharacter(Character c, int x, int y) {

		boolean success = removeCharacter(c);
		if (success) {
			success = addCharacter(c, x, y);
		}
		return success;
	}

	public boolean checkPosition(int x, int y) {

		try {
			boolean free = false;

			if (walls[x][y] != null)
				free = false;
			else if (characters[x][y] != null)
				free = false;
			else
				free = true;

			return free;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.err.println("Hey, beim Pathfindig ist was schiefgegangen, kümmere dich darum");
			return false;

		}
	}

	public List<Character> getCharacters() {
		LinkedList<Character> chars = new LinkedList<Character>();

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				if (characters[x][y] != null)
					chars.add((Character) characters[x][y]);
			}
		}

		return chars;

	}

	/**
	 * 
	 * @param delta
	 *            Delta time since last update
	 */
	public void update(long delta) {
		for (Animation a : animations) {
			a.update(delta);
		}
	}

	/**
	 * Called before chunk is unloaded
	 */
	public void unload() {

	}
	
	public void saveChunkIfNeeded(){
		
		try {
			saveChunkType("characters");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private boolean saveChunkType(String type) throws IOException {

		if (IsFileSameAsIngame(type) == false) {
			File file = new File(playerWorldpath + "/" + type + "/" + chunkname);
			if (file.exists()) {
				file.delete();
			}

			file = fillChunkFile(type, new File(playerWorldpath + "/" + type + "/" + chunkname));

		}

		return false;
	}

	private File fillChunkFile(String type, File file) throws FileNotFoundException {

		Drawable[][] ingameSprites = null;

		if (type.equals("walls")) {
			ingameSprites = walls;
		} else if (type.equals("triggers")) {
			ingameSprites = triggers;
		} else if (type.equals("floor")) {
			ingameSprites = floor;
		} else if (type.equals("items")) {
			ingameSprites = items;
		} else if (type.equals("characters")) {
			ingameSprites = characters;
		}

		PrintWriter writer = new PrintWriter(file);

		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				
				if(ingameSprites[x][y] == null)
					writer.print("0 ");
				else
					writer.print(getValueFromIDListByName(ingameSprites[x][y].getName(), type) + " ");
			}
			writer.println("");
		}
		writer.close();
		return file;
	}

	private int getValueFromIDListByName(String name, String type) {

		for (int value = 0; value < id_lists.get(type).size(); value++) {

			if (id_lists.get(type).containsKey(value)) {
				if (id_lists.get(type).get(value)[1].equals(name))
					return value;
			}
		}
		return 0;
	}

	private File getChunkFile(String type) {

		File playerfile = new File(playerWorldpath + "/" + type + "/" + chunkname);

		if (playerfile.exists()) {
			return playerfile;
		} else {
			return new File(defaultWorldPath + "/" + type + "/" + chunkname);
		}

	}
	
	public void resetChunk(){
		
		if(new File(playerWorldpath + "/walls/" + chunkname).exists())
			new File(playerWorldpath + "/walls/" + chunkname).delete();
		if(new File(playerWorldpath + "/triggers/" + chunkname).exists())
			new File(playerWorldpath + "/triggers/" + chunkname).delete();
		if(new File(playerWorldpath + "/characters/" + chunkname).exists())
			new File(playerWorldpath + "/characters/" + chunkname).delete();
		if(new File(playerWorldpath + "/floor/" + chunkname).exists())
			new File(playerWorldpath + "/floor/" + chunkname).delete();
		if(new File(playerWorldpath + "/items/" + chunkname).exists())
			new File(playerWorldpath + "/items/" + chunkname).delete();
	
	}

	private boolean IsFileSameAsIngame(String type) throws IOException {

		try {
			Scanner s = new Scanner(getChunkFile(type));

			Drawable[][] ingameSprites = null;

			if (type.equals("walls")) {
				ingameSprites = walls;
			} else if (type.equals("triggers")) {
				ingameSprites = triggers;
			} else if (type.equals("floor")) {
				ingameSprites = floor;
			} else if (type.equals("items")) {
				ingameSprites = items;
			}

			if (ingameSprites != null) {

				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {

						Drawable current = ingameSprites[x][y];

						int value = 0;

						if (s.hasNext()) {
							value = s.nextInt();
						} else {
							s.close();
							return true;
						}
						if (value == 0) {

							if (current != null) {
								s.close();
								return false;
							}
						} else {
							if (id_lists.get(type).get(value)[0].equals("a")) {
								Animation a = new Animation(type + "/" + this.id_lists.get(type).get(value)[1]);

								if (!current.equals(a)) {
									s.close();
									return false;
								}
							} else {
								Sprite a = new Sprite(type + "/" + this.id_lists.get(type).get(value)[1]);

								if (!current.equals(a)) {
									s.close();
									return false;
								}
							}
						}
					}
				}
			} else if (type.equals("characters")) {
				ingameSprites = characters;

				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {

						Drawable current = ingameSprites[x][y];

						int value = 0;

						if (s.hasNext()) {
							value = s.nextInt();
						} else {
							s.close();
							return true;
						}
						if (value == 0) {

							if (current != null) {
								s.close();
								return false;
							}

						} else if (current == null && value != 0) {
							s.close();
							return false;
						}
					}
				}
			} else {
				System.err.println("There is something wrong with your Input: Save Chunk Type --> " + type);
			}

			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

}
