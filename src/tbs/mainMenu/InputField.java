package tbs.mainMenu;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

import tbs.Matrix;
import tbs.Sprite;

public class InputField extends MenuItem {

	protected String text;
	protected LinkedList<Sprite> textSprites;
	protected int maxlength;
	protected Sprite unhoveredSprite;
	protected Sprite hoveredSprite;
	protected Sprite selectedSprite;
	protected Sprite currentSprite;
	protected boolean backspace;
	protected boolean write;

	private long delta;

	public InputField(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent,
			String text, int maxlength) {
		super(x, y, width, height, visible, active, parent);
		this.text = text;
		this.maxlength = maxlength;
		try {
			unhoveredSprite = new Sprite("mainMenu/inputUnhovered");
			hoveredSprite = new Sprite("mainMenu/inputHovered");
			selectedSprite = new Sprite("mainMenu/inputSelected");
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentSprite = unhoveredSprite;
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
	public void update(long delta) {
		if (this.delta > 100) {
		} else {
			this.delta += delta;
		}
	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawDrawable(currentSprite, rect.x, rect.y);
		for (int a = 0; a < textSprites.size(); a++) {
			mat.drawDrawable(textSprites.get(a), rect.x + a * (textSprites.getFirst().getWidth() + 1) + 5, rect.y + 1);
		}
	}

	@Override
	public void keyPressed(int keycode) {
		if (write) {
			// DANKE MERKEL!!!! :@ ( oder lennart )
			System.out.println("Keycode " + keycode);
			switch (keycode) {
			case KeyEvent.VK_BACK_SPACE:
				setText(text.substring(0, text.length() - 1));
				break;
			case KeyEvent.VK_A:
				setText(text.concat("a"));
				break;
			case KeyEvent.VK_B:
				setText(text.concat("b"));
				break;
			case KeyEvent.VK_C:
				setText(text.concat("c"));
				break;
			case KeyEvent.VK_D:
				setText(text.concat("d"));
				break;
			case KeyEvent.VK_E:
				setText(text.concat("e"));
				break;
			case KeyEvent.VK_F:
				setText(text.concat("f"));
				break;
			case KeyEvent.VK_G:
				setText(text.concat("g"));
				break;
			case KeyEvent.VK_H:
				setText(text.concat("h"));
				break;
			case KeyEvent.VK_I:
				setText(text.concat("i"));
				break;
			case KeyEvent.VK_J:
				setText(text.concat("j"));
				break;
			case KeyEvent.VK_K:
				setText(text.concat("k"));
				break;
			case KeyEvent.VK_L:
				setText(text.concat("l"));
				break;
			case KeyEvent.VK_M:
				setText(text.concat("m"));
				break;
			case KeyEvent.VK_N:
				setText(text.concat("n"));
				break;
			case KeyEvent.VK_O:
				setText(text.concat("o"));
				break;
			case KeyEvent.VK_P:
				setText(text.concat("p"));
				break;
			case KeyEvent.VK_Q:
				setText(text.concat("q"));
				break;
			case KeyEvent.VK_R:
				setText(text.concat("r"));
				break;
			case KeyEvent.VK_S:
				setText(text.concat("s"));
				break;
			case KeyEvent.VK_T:
				setText(text.concat("t"));
				break;
			case KeyEvent.VK_U:
				setText(text.concat("u"));
				break;
			case KeyEvent.VK_V:
				setText(text.concat("v"));
				break;
			case KeyEvent.VK_W:
				setText(text.concat("w"));
				break;
			case KeyEvent.VK_X:
				setText(text.concat("x"));
				break;
			case KeyEvent.VK_Y:
				setText(text.concat("y"));
				break;
			case KeyEvent.VK_Z:
				setText(text.concat("z"));
				break;
			case KeyEvent.VK_SPACE:
				setText(text.concat(" "));
				break;
			default:
				System.out.println("default");
				break;
			}
		}
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
		if (!write) {
			if (rect.contains(x, y)) {
				currentSprite = hoveredSprite;
			} else {
				currentSprite = unhoveredSprite;
			}
		}
	}

	@Override
	public void mouseLeftClick(int x, int y) {
		if (rect.contains(x, y)) {
			startWriting();
		} else {
			stopWriting();
		}
	}

	@Override
	public void mouseRightClick(int x, int y) {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		System.out.println("text changed " + text);
		if (text.length() >= maxlength) {
			text = text.substring(0, maxlength);
		}
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

	public void startWriting() {
		write = true;
		currentSprite = selectedSprite;
	}

	public void stopWriting() {
		write = false;
		currentSprite = unhoveredSprite;
	}

}
