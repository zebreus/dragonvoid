package tbs;

import java.awt.Color;

public class LoadingScreenOpener{
	Sprite background_left;
	Sprite background_right;
	Sprite[] leftbars;
	Sprite[] rightbars;
	int dif = 0;
	int speed = 20;
	long last = 0;
	boolean finished = false;
	public LoadingScreenOpener(Sprite background_left, Sprite background_right, Sprite[] leftbars, Sprite[] rightbars) {
		super();
		this.background_left = background_left;
		this.background_right = background_right;
		this.leftbars = leftbars;
		this.rightbars = rightbars;
	}
	public void update(long delta){
		if(last+speed<System.currentTimeMillis()){
			dif+=2;
			speed-=speed/10;
			last = System.currentTimeMillis();
		}
		if(dif>200){finished=true;}
	}
	public void paint(Matrix mat) {
		background_left.paint(mat, -dif, 0);
		background_right.paint(mat, 104+dif, 0);
		for(int v=0;v<50;v++){
			if(leftbars[v]!=null){
				leftbars[v].paint(mat, 54+v-dif, 61);
			}
			if(leftbars[v]!=null){
				leftbars[v].paint(mat, 104+v+dif, 61);
			}
		}
		
	}

}
