package actions;

import tbs.Animation;
import tbs.Character;
import tbs.DebugLayer;
import tbs.Matrix;
import tbs.Sprite;

public class UberAttack implements Action, PlayerAction {

	final static int ATTACK_POWER = 50;
	final static int ANIMATION_SPEED = 12;
	final static int ATTACK_COST = 22;
	final static int RANGE = 3;

	private boolean animated;
	private int direction;
	private Animation anim;
	private int position;
	private Sprite[] sprites;
	private long last;

	public UberAttack() {
		anim = new Animation("HUD/ability_uber");
		sprites = new Sprite[4];
		try {
			sprites[0] = new Sprite("actions/uber_attack/uberattack_right");
			sprites[1] = new Sprite("actions/uber_attack/uberattack_down");
			sprites[2] = new Sprite("actions/uber_attack/uberattack_left");
			sprites[3] = new Sprite("actions/uber_attack/uberattack_up");
		} catch (Exception e) {
			DebugLayer.pushError("Konnte bei Action SwordStab nicht alle Bilder laden");
			System.err.println("Konnte Bilder nicht laden");
			e.printStackTrace();
		}
	}

	@Override
	public Animation provideHudAnimation() {
		return anim;
	}

	@Override
	public String provideDescription() {
		return "Die Spezialattacke";
	}

	@Override
	public int provideAttackCosts() {
		return ATTACK_COST;
	}

	@Override
	public int[][] getPossibleTargets() {
		int range = 1 + (RANGE * 2);
		int[][] matrix = new int[range][range];
		for (int x = 0; x < range; x++) {
			for (int y = 0; y < range; y++) {
				int num = -1;
				if (x == RANGE && y < RANGE) {
					num = 2;
				}
				if (x == RANGE && y > RANGE) {
					num = 0;
				}
				if (y == RANGE && x < RANGE) {
					num = 3;
				}
				if (y == RANGE && x > RANGE) {
					num = 1;
				}
				matrix[x][y] = num;
			}
			System.out.println();
		}
		return matrix;
	}

	@Override
	public int[][] getPossibleTargets(int dir) {
		int range = 1 + (RANGE * 2);
		int[][] matrix = new int[range][range];
		for (int x = 0; x < range; x++) {
			for (int y = 0; y < range; y++) {
				int num = 0;
				if (dir == 2 && x == RANGE && y < RANGE) {
					num = 1;
				}
				if (dir == 0 && x == RANGE && y > RANGE) {
					num = 1;
				}
				if (dir == 3 && y == RANGE && x < RANGE) {
					num = 1;
				}
				if (dir == 1 && y == RANGE && x > RANGE) {
					num = 1;
				}
				matrix[x][y] = num;
			}
		}
		return matrix;
	}

	@Override
	public void paint(Matrix mat, int x, int y) {
		if (animated) {
			switch (direction) {
			case Direction.LEFT:
				x += position;
				break;
			case Direction.RIGHT:
				x -= position;
				break;
			case Direction.UP:
				y -= position;
				break;
			case Direction.DOWN:
				y += position;
				break;
			}
			mat.drawDrawable(sprites[direction], x, y);
		}
	}

	@Override
	public void update(long delta) {
		if (animated) {
			if (last + ANIMATION_SPEED < System.currentTimeMillis()) {
				position++;
				last = System.currentTimeMillis();
				if (position == (RANGE*8)-2) {
					position = 0;
					animated = false;
				}
			}
		}
	}

	@Override
	public void start(int dir, Character[] victims, int[] victimFields) {
		direction = dir;
		animated = true;
		for (Character v : victims) {
			v.changeHealth(-ATTACK_POWER);
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean getRunning() {
		return animated;
	}

}
