package tbs.editor;

public class InputJson {
	private String tileheight;

	private String orientation;

	private String nextobjectid;

	private String renderorder;

	private String height;

	private String tilewidth;

	private String width;

	private Layers[] layers;

	private Tilesets[] tilesets;

	private String version;

	public String getTileheight() {
		return tileheight;
	}

	public void setTileheight(String tileheight) {
		this.tileheight = tileheight;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getNextobjectid() {
		return nextobjectid;
	}

	public void setNextobjectid(String nextobjectid) {
		this.nextobjectid = nextobjectid;
	}

	public String getRenderorder() {
		return renderorder;
	}

	public void setRenderorder(String renderorder) {
		this.renderorder = renderorder;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getTilewidth() {
		return tilewidth;
	}

	public void setTilewidth(String tilewidth) {
		this.tilewidth = tilewidth;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Layers[] getLayers() {
		return layers;
	}

	public void setLayers(Layers[] layers) {
		this.layers = layers;
	}

	public Tilesets[] getTilesets() {
		return tilesets;
	}

	public void setTilesets(Tilesets[] tilesets) {
		this.tilesets = tilesets;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ClassPojo [tileheight = " + tileheight + ", orientation = " + orientation + ", nextobjectid = "
				+ nextobjectid + ", renderorder = " + renderorder + ", height = " + height + ", tilewidth = "
				+ tilewidth + ", width = " + width + ", layers = " + layers + ", tilesets = " + tilesets
				+ ", version = " + version + "]";
	}
}