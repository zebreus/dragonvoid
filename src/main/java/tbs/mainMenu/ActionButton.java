package tbs.mainMenu;

import java.io.IOException;

import tbs.Matrix;
import tbs.Sprite;

public class ActionButton extends MenuItem {

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int BACK = 2;
	public static final int FORWARD = 3;
	private int type;

	private Sprite unhoveredSprite;
	private Sprite hoveredSprite;
	private Sprite selectedSprite;
	private Sprite currentSprite;

	private ActionListener listener;
	private boolean pressed;

	private int delta;

	public ActionButton(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent, int type,
			ActionListener listener) {
		super(x, y, width, height, visible, active, parent);
		this.type = type;
		switch (type) {
		case 0:
			try {
				unhoveredSprite = new Sprite("mainMenu/scrollupUnhovered");
				hoveredSprite = new Sprite("mainMenu/scrollupHovered");
				selectedSprite = new Sprite("mainMenu/scrollupSelected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				unhoveredSprite = new Sprite("mainMenu/scrolldownUnhovered");
				hoveredSprite = new Sprite("mainMenu/scrolldownHovered");
				selectedSprite = new Sprite("mainMenu/scrolldownSelected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				unhoveredSprite = new Sprite("mainMenu/backUnhovered");
				hoveredSprite = new Sprite("mainMenu/backHovered");
				selectedSprite = new Sprite("mainMenu/backSelected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				unhoveredSprite = new Sprite("mainMenu/forwardUnhovered");
				hoveredSprite = new Sprite("mainMenu/forwardHovered");
				selectedSprite = new Sprite("mainMenu/forwardSelected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		currentSprite = unhoveredSprite;
		this.listener = listener;
	}

	@Override
	public void update(long delta) {
		if (this.delta > 100) {
			if (pressed) {
				listener.action(type);
				pressed = false;
			}
			this.delta = 0;
		} else {
			this.delta += delta;
		}
	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawDrawable(currentSprite, this.rect.x, this.rect.y);
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
		if (rect.contains(x, y)) {
			pressed = true;
		}
		checkSprite(x, y);
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		checkSprite(x, y);
	}

	@Override
	public void mouseRightPress(int x, int y) {
	}

	@Override
	public void mouseRightRelease(int x, int y) {
	}

	@Override
	public void mouseMove(int x, int y) {
		checkSprite(x, y);
	}

	private void checkSprite(int x, int y) {
		if (rect.contains(x, y)) {
			if (pressed) {
				currentSprite = selectedSprite;
			} else {
				currentSprite = hoveredSprite;
			}
		} else {
			currentSprite = unhoveredSprite;
		}
	}

	@Override
	public void mouseLeftClick(int x, int y) {
	}

	@Override
	public void mouseRightClick(int x, int y) {
	}

}
