package tbs;

import java.io.IOException;

public class TestScreen implements FrameHook{
	/**
	 * @uml.property  name="dirt"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Sprite dirt;
	/**
	 * @uml.property  name="player"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Sprite player;
	/**
	 * @uml.property  name="animatedPlayer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Animation animatedPlayer;
	/**
	 * @uml.property  name="sound"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	tbs.Sound sound;
	public TestScreen(){
		try {
			sound = new Sound("music/sbpfsp");
			dirt = new Sprite("floor/1");
			player = new Sprite("characters/player/player_up_1");
			animatedPlayer = new Animation("characters/player/player");
			animatedPlayer.animateRepeating(animatedPlayer.getSequenceByName("down"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void update(long delta) {
		animatedPlayer.update(delta);
	}
	/**
	 * @uml.property  name="x"
	 */
	int x = 50;
	/**
	 * @uml.property  name="y"
	 */
	int y = 50;
	@Override
	public void repaint(Matrix mat) {
		for(int x = 0;x<26;x++){
			for(int y = 0;y<26;y++){
				dirt.paint(mat,x*8, y*8);
			}
		}
		for(int x = 0;x<10;x++){
			for(int y = 0;y<10;y++){
				try{
					animatedPlayer.paint(mat, x*8+1, y*8+1);
					Thread.sleep(1);
				}catch(Exception e){
				}
			}
		}
		animatedPlayer.paint(mat, x, y);
		
	}
	/**
	 * @uml.property  name="seq"
	 */
	int seq = 0;
	@Override
	public void keyPressed(int keycode) {
		seq++;
		if(seq>3){
			seq=0;
		}
		animatedPlayer.animateRepeating(seq);
	}

	@Override
	public void keyReleased(int keycode) {
	}

	@Override
	public void keyTyped(int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseLeftPress(int x, int y) {
		sound.play();
		
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseRightPress(int x, int y) {
		sound.stopAll();
	}

	@Override
	public void mouseRightRelease(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMove(int x, int y) {
		System.out.println(x+" "+y);
		this.x=x;
		this.y=y;
		
	}
	@Override
	public void mouseLeftClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseRightClick(int i, int j) {
		// TODO Auto-generated method stub
		
	}

}
