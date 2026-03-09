package tbs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class SaveManager {

	public static String getSaveDir() {
		String dir = System.getProperty("dragonvoid.savedir");
		return dir != null ? dir : "res/saves";
	}

	public static String[] listSavegames() throws FileNotFoundException {
		Scanner s = new Scanner(new File(getSaveDir() + "/saves"));
		LinkedList<String> ll = new LinkedList<String>();
		while (s.hasNext()) {
			ll.add(s.next());
		}
		s.close();
		return (String[]) ll.toArray(new String[ll.size()]);
	}

	/**
	 * @uml.property name="sfr"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	SaveFeedbackReceiver sfr;
	/**
	 * @uml.property name="name"
	 */
	String name;
	/**
	 * @uml.property name="difficulty"
	 */
	int difficulty;
	/**
	 * @uml.property name="saveName"
	 */
	static String saveName;
	/**
	 * @uml.property name="property"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="tbs.PropertyList"
	 */
	LinkedList<PropertyList> property;

	public SaveManager(String saveName, SaveFeedbackReceiver sfr) throws FileNotFoundException {
		property = new LinkedList<PropertyList>();
		SaveManager.saveName = saveName;
		this.sfr = sfr;
	}

	public void loadSave() throws FileNotFoundException {
		sfr.saveLoadingProgress(0);
		Scanner s = new Scanner(new File(getSaveDir() + "/" + saveName + "/" + saveName + ".save"));
		name = s.next();
		difficulty = s.nextInt();
		sfr.saveLoadingProgress(50);
		while (s.hasNext()) {
			property.add(loadPropertyFile(s.next()));
		}
		sfr.saveLoadingProgress(100);
		sfr.saveLoaded();

		s.close();
	}

	private PropertyList loadPropertyFile(String filename) {
		@SuppressWarnings("rawtypes")
		LinkedList<SaveProperty> ll = new LinkedList<SaveProperty>();
		try {
			Scanner s = new Scanner(new File(getSaveDir() + "/" + saveName + "/" + filename + ".prop"));
			while (s.hasNext()) {
				SaveProperty<?> prop = lineToProperty(s.next(), s.next(), s.next());
				// ll.add(lineToProperty(s.next(),s.next(),s.next()));
				ll.add(prop);

				// System.out.println("Content: " + prop.getContent());
			}

			s.close();
			return new PropertyList(ll, filename);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load propertyFile " + getSaveDir() + "/" + saveName + "/" + filename + ".prop");
			e.printStackTrace();
			return null;
		}
	}

	public static void createSave(String name, int difficulty, SaveFeedbackReceiver sfr) throws IOException {
		InputStream masterStream = SaveManager.class.getClassLoader().getResourceAsStream("res/saves/" + Config.MASTER_SAVE + "/" + Config.MASTER_SAVE + ".msav");
		if (masterStream == null) throw new IOException("Master save template not found in JAR");
		BufferedReader master = new BufferedReader(new InputStreamReader(masterStream));
		File save = new File(getSaveDir() + "/" + name + "/" + name + ".save");
		if (save.exists()) {
			System.err.println("Couldn't create new savefile, because a save with the same name already exists");
			sfr.creatingError();
		} else {
			sfr.creatingProgress(0);
			new File(getSaveDir() + "/" + name).mkdirs();
			save.createNewFile();
			PrintWriter pw = new PrintWriter(new FileOutputStream(save));
			while (master.ready()) {
				String line = master.readLine();
				if (line.equals("#name#")) {
					pw.println(name);
				} else {
					if (line.equals("#difficulty#")) {
						pw.println(difficulty);
					} else {
						pw.println(line);
						File prop = new File(getSaveDir() + "/" + name + "/" + line + ".prop");
						prop.createNewFile();
						PrintWriter propWriter = new PrintWriter(new FileOutputStream(prop));
						InputStream propStream = SaveManager.class.getClassLoader().getResourceAsStream("res/saves/" + Config.MASTER_SAVE + "/" + line + ".prop");
						if (propStream == null) throw new IOException("Master prop template not found: " + line + ".prop");
						BufferedReader propReader = new BufferedReader(new InputStreamReader(propStream));
						while (propReader.ready()) {
							propWriter.println(propReader.readLine());
						}
						propWriter.flush();
						propWriter.close();
						propReader.close();
					}
				}
			}
			sfr.creatingProgress(80);
			master.close();
			pw.flush();
			pw.close();
			PrintWriter saves = new PrintWriter(new BufferedWriter(new FileWriter(new File(getSaveDir() + "/saves"), true)));
			saves.println(name);
			saves.close();
			sfr.creatingProgress(100);
			sfr.created();
			
			new File(getSaveDir() + "/" + name +"/chunks").mkdir();
			
			new File(getSaveDir() + "/" + name +"/chunks/walls").mkdir();
			new File(getSaveDir() + "/" + name +"/chunks/floor").mkdir();
			new File(getSaveDir() + "/" + name +"/chunks/triggers").mkdir();
			new File(getSaveDir() + "/" + name +"/chunks/items").mkdir();
			new File(getSaveDir() + "/" + name +"/chunks/characters").mkdir();
		}
	}

	public SaveProperty<?> lineToProperty(String type, String name, String content) {
		switch (Integer.valueOf(type)) {
		case Config.STRING:
			return new SaveProperty<String>(name, content);
		case Config.INT:
			return new SaveProperty<Integer>(name, Integer.valueOf(content));
		case Config.BOOLEAN:
			if (content.equals("true")) {
				return new SaveProperty<Boolean>(name, true);
			} else {
				return new SaveProperty<Boolean>(name, false);
			}
		case Config.FLOAT:
			return new SaveProperty<Float>(name, Float.valueOf(content));
		default:
			System.err.println("Couldn't parse save property: " + type + " " + name + " " + content);
			return null;
		}
	}

	public String propertyToLine(SaveProperty<?> sp) {
		int num = -1;
		if (sp.getContent() instanceof String) {
			num = Config.STRING;
		} else if (sp.getContent() instanceof Integer) {
			num = Config.INT;
		} else if (sp.getContent() instanceof Boolean) {
			num = Config.BOOLEAN;
		} else if (sp.getContent() instanceof Float) {
			num = Config.FLOAT;
		}
		if (num != -1) {
			return num + " " + sp.name + " " + sp.content;
		} else {
			System.err.println("Couldn't convert save property to line");
			return "";
		}
	}

	public PropertyList getPropertyList(String name) {
		for (PropertyList l : property) {
			if (l.name.equals(name)) {
				return l;
			}
		}
		System.err.println("Couldn't find propertylist " + name);
		return null;
	}

	public SaveProperty<?> getProperty(String name) {
		return getProperty("default", name);
	}

	public SaveProperty<?> getProperty(String listname, String name) {
		return getPropertyList(listname).getProperty(name);
	}

	public void addProperty(SaveProperty<?> sp) {
		addProperty("default", sp);
	}

	public void addProperty(String listname, SaveProperty<?> sp) {
		getPropertyList(listname).addProperty(sp);
	}

	public void removeProperty(String name) {
		removeProperty("default", name);
	}

	public void removeProperty(String listname, String name) {
		getPropertyList(listname).removeProperty(name);
	}

	public void saveSave() {

		for (PropertyList pl : property) {

			try {
				File file = new File(getSaveDir() + "/" + saveName + "/" + pl.name + ".prop");

				LinkedList<String> filetext = new LinkedList<String>();

				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					filetext.add(line);
				}

				fileReader.close();

				for (int i = 0; i < filetext.size(); i++) {

					String[] linesplit = filetext.get(i).split(" ");

					if (linesplit.length == 3) {
						linesplit[2] = "" + pl.getProperty(linesplit[1]);
						filetext.set(i, linesplit[0] + " " + linesplit[1] + " " + linesplit[2]);
					} else {
						System.out.println("There is an error in your " + getSaveDir() + "/" + saveName + "/" + pl.name
								+ ".prop" + " save in line: " + (i + 1));
					}

				}

				FileWriter fw = new FileWriter(file);
				BufferedWriter out = new BufferedWriter(fw);
				for (int j = 0; j < filetext.size(); j++) {
					System.out.println(j + "." + filetext.get(j));
					out.append(filetext.get(j));
					out.newLine();
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public String getSaveName() {
		return saveName;
	}

}
