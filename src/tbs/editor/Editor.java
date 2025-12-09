package tbs.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import tbs.editor.Chunk;

public class Editor {

	public static final int CHUNK_SIZE = 8;

	/**
	 * @uml.property name="input"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private JFileChooser input;
	/**
	 * @uml.property name="output"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private JFileChooser output;

	private InputJson inputJson;
	private LinkedList<Sprites> sprites;
	private LinkedList<Animations> animations;
	private String idList;
	private LinkedList<Chunks> chunks;
	private int chunkWidth;
	private int chunkHeight;

	public Editor() {
		// create in and output windows
		input = new JFileChooser(System.getProperty("user.dir"));
		output = new JFileChooser(System.getenv("user.dir"));
		input.setFileSelectionMode(JFileChooser.FILES_ONLY);
		output.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		sprites = new LinkedList<Sprites>();
		animations = new LinkedList<Animations>();
		chunks = new LinkedList<Chunks>();

		// show them and give error feedback
		if (JFileChooser.APPROVE_OPTION == input.showOpenDialog(null)
				&& JFileChooser.APPROVE_OPTION == output.showSaveDialog(null)) {

			// transform the input
			transform();

		} else {
			JOptionPane.showMessageDialog(null, "Error getting input and output folder\nProgram will exit",
					"error".toUpperCase(), JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("finished");
		input = null;
		output = null;
		System.exit(0);
	}

	private void transform() {

		Gson gson = new Gson();

		try {
			String fileContent = new String(Files.readAllBytes(Paths.get(input.getSelectedFile().getAbsolutePath())),
					StandardCharsets.UTF_8);
			String preparedFileContent = prepareFileContent(fileContent);
			// System.err.println("THIS IS THE FILECONTENT:\n" +
			// preparedFileContent + "||||||||||||||||||||||");
			inputJson = gson.fromJson(preparedFileContent, InputJson.class);
			// System.out.println(inputJson.getHeight() + "\n" +
			// inputJson.getWidth() + "\n"
			// + Arrays.toString(inputJson.getLayers()[0].getData()));

			generateIdLists();
			readData();
			writeOutput();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String prepareFileContent(String fileContent) {
		return new Prepare().prepare(fileContent);
	}

	private void writeOutput() {
		String path = output.getSelectedFile().getAbsolutePath();
		new File(path.concat("/world")).mkdirs();
		new File(path.concat("/world/chunks")).mkdirs();

		PrintWriter out;
		try {
			out = new PrintWriter(path.concat("/world/world"));

			String output = chunkWidth + " " + chunkHeight + "\n";

			for (int a = 0; a < chunkHeight; a++) {
				for (int b = 0; b < chunkWidth; b++) {
					output += "chunk".concat(String.valueOf(a * chunkWidth + b)).concat(" ");
				}
				output = output.trim() + "\n";
				output = output.replaceAll("(?m)^[ \t]*\r?\n", "");

			}
			out.print(output);
			out.close();

			for (Chunks c : chunks) {
				new File(path.concat("/world/chunks/".concat(c.getName()))).mkdirs();
				out = new PrintWriter(path.concat("/world/chunks/".concat(c.getName().concat("/id_list"))), "UTF-8");
				out.print(idList);
				out.close();

				for (int a = 0; a < c.getChunks().length; a++) {
					for (int b = 0; b < c.getChunks()[a].length; b++) {
						// System.out.println("a: " + a + " || b: " + b);
						out = new PrintWriter(
								path.concat("/world/chunks/".concat(c.getName()
										.concat("/chunk".concat(String.valueOf(a * c.getChunks().length + b))))),
								"UTF-8");
						out.print(c.getChunks()[a][b]);
						out.close();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void readData() {

		chunkWidth = Integer.valueOf(inputJson.getWidth()) / CHUNK_SIZE;
		chunkHeight = Integer.valueOf(inputJson.getHeight()) / CHUNK_SIZE;

		for (Layers l : inputJson.getLayers()) {

			// parse input string to array

			String[][] inputData = new String[Integer.valueOf(inputJson.getWidth())][Integer
					.valueOf(inputJson.getHeight())];

			for (int a = 0; a < Integer.valueOf(inputJson.getHeight()); a++) {
				for (int b = 0; b < Integer.valueOf(inputJson.getWidth()); b++) {
					// System.out.println(a + " " + b);
					inputData[a][b] = l.getData()[a * Integer.valueOf(inputJson.getWidth()) + b];
				}
			}

			Chunks chunks = new Chunks();
			chunks.setName(l.getName());
			Chunk[][] chunksToAdd = new Chunk[chunkWidth][chunkHeight];

			// parse array to chunks

			for (int a = 0; a < chunkHeight; a++) {
				for (int b = 0; b < chunkWidth; b++) {
					String[][] tempData = new String[CHUNK_SIZE][CHUNK_SIZE];
					for (int c = 0; c < CHUNK_SIZE; c++) {
						for (int d = 0; d < CHUNK_SIZE; d++) {
							tempData[c][d] = inputData[b * CHUNK_SIZE + c][a * CHUNK_SIZE + d];
						}
					}
					chunksToAdd[b][a] = new Chunk(tempData);
				}
			}

			chunks.setChunks(chunksToAdd);
			this.chunks.add(chunks);

		}

	}

	private void generateIdLists() {
		for (Tilesets tileSets : inputJson.getTilesets()) {
			int add = Integer.valueOf(tileSets.getFirstgid());
			System.out.println("firstgid " + add);
			if (tileSets.getName().toLowerCase().contains("anim")) {
				
				for (Tiles tiles : tileSets.getTiles()) {
					// System.out.println("TileID: " + tiles.getId() + " " +
					// tiles.getImage());
					animations.add(new Animations(Integer.valueOf(tiles.getId()) + add,
							tiles.getImage().replaceAll(".*?(?=/)", "").replaceAll("/", "").replaceAll(".png", "")));
				}

			} else {

				for (Tiles tiles : tileSets.getTiles()) {
					sprites.add(new Sprites(Integer.valueOf(tiles.getId()) + add,
							tiles.getImage().replaceAll(".*?(?=/)", "").replaceAll("/", "").replaceAll(".png", "")));
				}

			}
		}

		idList = "";

		// System.out.println("Animations");
		String temp = "";
		for (Animations a : animations) {
			temp = a.getId() + " a " + a.getName();
			idList += "\n".concat(temp);
		}
		// System.out.println("Sprites");
		for (Sprites a : sprites) {
			temp = a.getId() + " s " + a.getName();
			idList += "\n".concat(temp);
		}
		idList = idList.replaceAll("(?m)^[ \t]*\r?\n", "").replaceAll("Ã¤", "ä");
		// System.out.println(idList);
	}

	public static void main(String[] args) {

		new Editor();

	}

}
