package tbs.editor;

public class Chunks {

	private String name;
	private Chunk[][] chunks;

	public Chunks() {

	}

	public Chunks(String name, Chunk[][] chunks) {
		super();
		this.name = name;
		this.chunks = chunks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Chunk[][] getChunks() {
		return chunks;
	}

	public void setChunks(Chunk[][] chunks) {
		this.chunks = chunks;
	}
	
}
