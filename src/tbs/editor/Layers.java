package tbs.editor;

public class Layers {
	private String height;

	private String visible;

	private String width;

	private String name;

	private String opacity;

	private String[] data;

	private String type;

	private String y;

	private String x;

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpacity() {
		return opacity;
	}

	public void setOpacity(String opacity) {
		this.opacity = opacity;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return "ClassPojo [height = " + height + ", visible = " + visible + ", width = " + width + ", name = " + name
				+ ", opacity = " + opacity + ", data = " + data + ", type = " + type + ", y = " + y + ", x = " + x
				+ "]";
	}
}