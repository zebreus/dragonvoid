package characters;

import tbs.Animation;
import tbs.World;

public class Default extends tbs.Character{

	public Default(Animation anim, World world, int chunkX, int chunkY, int x, int y) {
		super(anim, world, chunkX, chunkY, x, y);
		System.err.println("Created default Character, probably not what you wanted");
		speed = 5;
	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getName() {
		return "default";
	}

}
