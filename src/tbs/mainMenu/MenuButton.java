package tbs.mainMenu;

import java.io.IOException;
import java.util.LinkedList;

import tbs.Matrix;
import tbs.Sprite;

public abstract class MenuButton extends MenuItem {

	protected Sprite unhoveredSprite;
	protected Sprite hoveredSprite;
	protected Sprite selectedSprite;
	protected Sprite currentSprite;
	protected String text;
	protected LinkedList<Sprite> textSprites;

	public MenuButton(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent,
			Sprite unhoveredSprite, Sprite hoveredSprite, Sprite clickedSprite, String text) {
		super(x, y, width, height, visible, active, parent);
		this.unhoveredSprite = unhoveredSprite;
		this.hoveredSprite = hoveredSprite;
		this.selectedSprite = clickedSprite;
		this.currentSprite = unhoveredSprite;
		this.text = text;
		textSprites = new LinkedList<Sprite>();
		for (int a = 0; a < text.length(); a++) {
			String sub = text.substring(a, a + 1).toLowerCase();
			if (sub.equals(" ")) sub = "space";
			try {
				Sprite s = new Sprite("fonts/" + sub);
				textSprites.add(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawSprite(currentSprite, rect.x, rect.y, 1);
		for (int a = 0; a < textSprites.size(); a++) {
			mat.drawDrawable(textSprites.get(a), rect.x + a * (textSprites.getFirst().getWidth() + 1) + 5, rect.y + 1);
		}
	}

	public MenuButton(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent,
			String text) {
		this(x, y, width, height, visible, active, parent, getButtonUnhovered(), getButtonHovered(),
				getButtonSelected(), text);
	}

	public Sprite getUnhoveredSprite() {
		return unhoveredSprite;
	}

	public void setUnhoveredSprite(Sprite unhoveredSprite) {
		this.unhoveredSprite = unhoveredSprite;
	}

	public Sprite getHoveredSprite() {
		return hoveredSprite;
	}

	public void setHoveredSprite(Sprite hoveredSprite) {
		this.hoveredSprite = hoveredSprite;
	}

	public Sprite getClickedSprite() {
		return selectedSprite;
	}

	public void setClickedSprite(Sprite clickedSprite) {
		this.selectedSprite = clickedSprite;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void hover() {
		this.currentSprite = this.hoveredSprite;
	}

	public void unhover() {
		this.currentSprite = this.unhoveredSprite;
	}

	public void select() {
		this.currentSprite = this.selectedSprite;
	}

	private static Sprite getButtonUnhovered() {
		Sprite s = null;
		try {
			s = new Sprite("mainMenu/buttonUnhovered");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	private static Sprite getButtonHovered() {
		Sprite s = null;
		try {
			s = new Sprite("mainMenu/buttonHovered");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	private static Sprite getButtonSelected() {
		Sprite s = null;
		try {
			s = new Sprite("mainMenu/buttonSelected");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}
