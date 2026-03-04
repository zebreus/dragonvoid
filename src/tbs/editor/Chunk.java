package tbs.editor;

public class Chunk {

	private String[][] data;

	public Chunk(String[][] data) {
		super();
		this.data = data;
	}

	public Chunk() {
	};

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	@Override
	public String toString() {

		String returnThis = "";

		for (String[] a : data) {
			for (String b : a) {
				returnThis += b.concat(" ");
			}
			returnThis = returnThis.trim();
			returnThis += "\n";
		}

		return returnThis;
	}

}
