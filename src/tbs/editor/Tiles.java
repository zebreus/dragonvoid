package tbs.editor;

public class Tiles {
	private String id;

	private String image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", image = " + image + "]";
	}
}