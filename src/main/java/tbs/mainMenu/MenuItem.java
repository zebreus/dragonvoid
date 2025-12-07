package tbs.mainMenu;

import java.awt.Rectangle;
import java.util.LinkedList;

import tbs.Matrix;

public abstract class MenuItem {

	protected Rectangle rect;
	protected MenuItem parent;
	protected boolean visible;
	protected boolean active;
	protected LinkedList<MenuItem> menuItems;

	public MenuItem(int x, int y, int width, int height, boolean visible, boolean active) {
		this(x, y, width, height, visible, active, null);
	}

	public MenuItem(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent) {
		rect = new Rectangle(x, y, width, height);
		this.visible = visible;
		this.active = active;
		this.menuItems = new LinkedList<MenuItem>();
		this.parent = parent;
	}

	public void udate(long delta) {
		update(delta);
		for (MenuItem i : menuItems) {
			i.udate(delta);
		}
	}

	public abstract void update(long delta);

	public void rpaint(Matrix mat) {
		if (visible) {
			repaint(mat);
			for (MenuItem i : menuItems) {
				i.rpaint(mat);
			}
		}
	}

	public abstract void repaint(Matrix mat);

	public void kPressed(int keycode) {
		if (active) {
			keyPressed(keycode);
			for (MenuItem i : menuItems) {
				i.kPressed(keycode);
			}
		}

	}

	public abstract void keyPressed(int keycode);

	public void kReleased(int keycode) {
		if (active) {
			keyReleased(keycode);
			for (MenuItem i : menuItems) {
				i.kReleased(keycode);
			}
		}

	}

	public abstract void keyReleased(int keycode);

	public void kTyped(int keycode) {
		if (active) {
			keyTyped(keycode);
			for (MenuItem i : menuItems) {
				i.kTyped(keycode);
			}
		}

	}

	public abstract void keyTyped(int keycode);

	public void mLeftPressed(int x, int y) {
		if (active) {
			mouseLeftPress(x, y);
			for (MenuItem i : menuItems) {
				i.mLeftPressed(x, y);
			}
		}

	}

	public abstract void mouseLeftPress(int x, int y);

	public void mLeftRelease(int x, int y) {
		if (active) {
			mouseLeftRelease(x, y);
			for (MenuItem i : menuItems) {
				i.mLeftRelease(x, y);
			}
		}

	}

	public abstract void mouseLeftRelease(int x, int y);

	public void mRightPress(int x, int y) {
		if (active) {
			mouseRightPress(x, y);
			for (MenuItem i : menuItems) {
				i.mRightPress(x, y);
			}
		}

	}

	public abstract void mouseRightPress(int x, int y);

	public void mRightRelease(int x, int y) {
		if (active) {
			mouseRightRelease(x, y);
			for (MenuItem i : menuItems) {
				i.mRightRelease(x, y);
			}
		}

	}

	public abstract void mouseRightRelease(int x, int y);

	public void mMove(int x, int y) {
		if (active) {
			mouseMove(x, y);
			for (MenuItem i : menuItems) {
				i.mMove(x, y);
			}
		}

	}

	public abstract void mouseMove(int x, int y);

	public void mLeftClick(int x, int y) {
		if (active) {
			mouseLeftClick(x, y);
			for (MenuItem i : menuItems) {
				i.mLeftClick(x, y);
			}
		}

	}

	public abstract void mouseLeftClick(int x, int y);

	public void mRightClick(int x, int y) {
		if (active) {
			mouseRightClick(x, y);
			for (MenuItem i : menuItems) {
				i.mRightClick(x, y);
			}
		}

	}

	public abstract void mouseRightClick(int x, int y);

	// GETTER SETTER
	public void addItem(MenuItem item) {
		menuItems.add(item);
	}

	public void removeItem(MenuItem item) {
		menuItems.remove(item);
	}

	public void show() {
		visible = true;
		for (MenuItem i : menuItems) {
			i.show();
		}
	}

	public void hide() {
		visible = false;
		for (MenuItem i : menuItems) {
			i.hide();
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void activate() {
		active = true;
		for (MenuItem i : menuItems) {
			i.activate();
		}
	}

	public void deactivate() {
		active = false;
		for (MenuItem i : menuItems) {
			i.deactivate();
		}
	}

	public boolean isActive() {
		return active;
	}

	public Rectangle getRect() {
		return rect;
	}
}