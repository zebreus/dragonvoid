package tbs.mainMenu;

import java.io.IOException;
import java.util.LinkedList;

import tbs.Matrix;
import tbs.Sprite;

public class Label extends MenuItem {

	private String text;
	private Sprite background;
	private LinkedList<Sprite> textSprites;

	public Label(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent, String text) {
		super(x, y, width, height, visible, active, parent);
		try {
			background = new Sprite("mainMenu/label");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.text = text;

		textSprites = new LinkedList<Sprite>();
		for (int a = 0; a < text.length(); a++) {
			String sub = text.substring(a, a + 1).toLowerCase();
			try {
				Sprite s = new Sprite("fonts/" + sub);
				textSprites.add(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawDrawable(background, rect.x, rect.y);
		for (int a = 0; a < textSprites.size(); a++) {
			mat.drawDrawable(textSprites.get(a), rect.x + a * (textSprites.getFirst().getWidth() + 1) + 5, rect.y + 1);
		}
	}

	@Override
	public void keyPressed(int keycode) {

	}

	@Override
	public void keyReleased(int keycode) {

	}

	@Override
	public void keyTyped(int keycode) {

	}

	@Override
	public void mouseLeftPress(int x, int y) {

	}

	@Override
	public void mouseLeftRelease(int x, int y) {

	}

	@Override
	public void mouseRightPress(int x, int y) {
	}

	@Override
	public void mouseRightRelease(int x, int y) {

	}

	@Override
	public void mouseMove(int x, int y) {

	}

	@Override
	public void mouseLeftClick(int x, int y) {

	}

	@Override
	public void mouseRightClick(int x, int y) {

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
