package tbs.editor;

public class Tilesets {
	private String tileheight;

	private String tilecount;

	private Tiles[] tiles;

	private String firstgid;

	private String tilewidth;

	private String name;

	private String columns;

	private String margin;

	private String spacing;

	public String getTileheight() {
		return tileheight;
	}

	public void setTileheight(String tileheight) {
		this.tileheight = tileheight;
	}

	public String getTilecount() {
		return tilecount;
	}

	public void setTilecount(String tilecount) {
		this.tilecount = tilecount;
	}

	public Tiles[] getTiles() {
		return tiles;
	}

	public void setTiles(Tiles[] tiles) {
		this.tiles = tiles;
	}

	public String getFirstgid() {
		return firstgid;
	}

	public void setFirstgid(String firstgid) {
		this.firstgid = firstgid;
	}

	public String getTilewidth() {
		return tilewidth;
	}

	public void setTilewidth(String tilewidth) {
		this.tilewidth = tilewidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getSpacing() {
		return spacing;
	}

	public void setSpacing(String spacing) {
		this.spacing = spacing;
	}

	@Override
	public String toString() {
		return "ClassPojo [tileheight = " + tileheight + ", tilecount = " + tilecount + ", tiles = " + tiles
				+ ", firstgid = " + firstgid + ", tilewidth = " + tilewidth + ", name = " + name + ", columns = "
				+ columns + ", margin = " + margin + ", spacing = " + spacing + "]";
	}
}