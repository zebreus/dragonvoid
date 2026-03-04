package tbs;

import characters.Player;

public class HUD {
/**
 * @uml.property  name="hfr"
 * @uml.associationEnd  multiplicity="(1 1)"
 */
HUDFeedbackReceiver hfr;
/**
 * @uml.property  name="abilities"
 * @uml.associationEnd  multiplicity="(0 -1)"
 */
/**
 * @uml.property  name="abilityCount"
 */
int abilityCount;
/**
 * @uml.property  name="focus"
 * @uml.associationEnd  multiplicity="(1 1)"
 */
Animation focus;
/**
 * @uml.property  name="abilityX"
 */
int abilityX;
Sprite[] bloodbar;
Sprite[] bloodSprites;
Sprite lifebarTop;
Sprite lifebarBackground;
Sprite lifebarBottom;
static final int LIFEBAR_X = 10;
static final int LIFEBAR_Y = 3;
int health;
int maxHealth;
Player player;
	public HUD(World w,HUDFeedbackReceiver hfr, int maxHealth, int health){
		player = (Player)w.getCharacter(characters.Player.class);
		this.health = health;
		this.maxHealth = maxHealth;
		abilityCount = player.playerActions.length;
		this.hfr = hfr;
		focus = new Animation("HUD/focus");
		abilityX = (Config.WIDTH/2)-(((abilityCount*16)+((abilityCount-1)*dist))/2);
		try{
		lifebarTop = new Sprite("HUD/lifebar_top");
		lifebarBottom = new Sprite("HUD/lifebar_bot");
		lifebarBackground= new Sprite("HUD/lifebar_mid");
		bloodSprites = new Sprite[4];
		bloodSprites[0] = new Sprite("HUD/lifebar_blood_1");
		bloodSprites[1] = new Sprite("HUD/lifebar_blood_2");
		bloodSprites[2] = new Sprite("HUD/lifebar_blood_3");
		bloodSprites[3] = new Sprite("HUD/lifebar_blood_4");
		}catch(Exception e){
			e.printStackTrace();
			DebugLayer.pushError("Error loading Lifebar Textures");
		}
		setLifebar();
	}
	public void setHealth(int health){
		this.health = health;
		setLifebar();
	}
	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
		setLifebar();
	}
	public void setLifebar(){
		if(maxHealth>100){
			DebugLayer.pushError("Too much maxHealth too display");
			maxHealth = 100;
		}
		if(health>100){
			DebugLayer.pushError("Too much health too display");
			health = 100;
		}
		if(bloodbar==null){
			bloodbar=new Sprite[maxHealth];
		}
		if(bloodbar.length!=maxHealth){
			Sprite[] temp = new Sprite[maxHealth];
			int small = maxHealth;
			if(bloodbar.length<small){
				small = bloodbar.length;
			}
			for(int x = 0;x<small;x++){
				temp[x]=bloodbar[x];
			}
			bloodbar=temp;
		}
		if(health <= 0){
			for(int x = 0;x<bloodbar.length;x++){
				bloodbar[x]=null;
			}
		}else if(bloodbar[health-1]==null){
			for(int x = health;x!=0&&bloodbar[x-1]==null;x--){
				bloodbar[x-1] = bloodSprites[((int)(Math.random()*4))];
			}
		}else if(health<bloodbar.length&&bloodbar[health-1]!=null){
			for(int x = health+1;x<bloodbar.length;x++){
				bloodbar[x] = null;
			}
		}
	}
	/**
	 * @uml.property  name="dist"
	 */
	int dist = 4;
	public void paint(Matrix mat){
		for(int x = 0;x<abilityCount;x++){
			player.playerActions[x].provideHudAnimation().paint(mat, abilityX+x*(16+dist), 4);
		}
		focus.paint(mat,188, 4);
		mat.drawDrawable(lifebarTop, 10, LIFEBAR_Y);
		int x = LIFEBAR_Y+5;
		while(x<maxHealth+LIFEBAR_Y+5){
			mat.drawDrawable(lifebarBackground, 10, x);
			x+=4;
		}
		mat.drawDrawable(lifebarBottom, 10, x);
		for(int y = 0;y<bloodbar.length;y++){
			if(bloodbar[y]!=null){
				mat.drawDrawable(bloodbar[y], 11, x-y);
			}
		}
	}
	public boolean onPress(int mouseX, int mouseY){
		if(checkClick(188,4,16,16,mouseX,mouseY)){
			focus.animateOnce(focus.getSequenceByName("click"));
			return true;
		}
		for(int x = 0;x<abilityCount;x++){
			if(checkClick(abilityX+x*(16+dist), 4,16,16,mouseX,mouseY)){
				player.playerActions[x].provideHudAnimation().animateOnce(player.playerActions[x].provideHudAnimation().getSequenceByName("click"));
				return true;
			}
		}
		return false;
	}
	public void onMove(int mouseX, int mouseY){
		if(checkClick(188,4,16,16,mouseX,mouseY)){
			if(!focus.getActSequenceName().equals("click")){
				focus.animateOnce(focus.getSequenceByName("hover"));
			}
		}else{
			if(!focus.getActSequenceName().equals("click")){
				focus.animateOnce(focus.getSequenceByName("normal"));
			}
		}
		for(int x = 0;x<abilityCount;x++){
			if(checkClick(abilityX+x*(16+dist), 4,16,16,mouseX,mouseY)){
				if(!player.playerActions[x].provideHudAnimation().getActSequenceName().equals("click")){
					player.playerActions[x].provideHudAnimation().animateOnce(player.playerActions[x].provideHudAnimation().getSequenceByName("hover"));
				}
			}else{
				if(!player.playerActions[x].provideHudAnimation().getActSequenceName().equals("click")){
					player.playerActions[x].provideHudAnimation().animateOnce(player.playerActions[x].provideHudAnimation().getSequenceByName("normal"));
				}
			}
		}
	}
	public void onRelease(int mouseX, int mouseY){
		if(checkClick(188,4,16,16,mouseX,mouseY)){
			if(focus.getActSequenceName().equals("click")){
				focus.animateOnce(focus.getSequenceByName("hover"));
				hfr.centerCamera();
			}
		}else{
			if(!focus.getActSequenceName().equals("hover")){
				focus.animateOnce(focus.getSequenceByName("normal"));
			}
		}
		for(int x = 0;x<abilityCount;x++){
			if(checkClick(abilityX+x*(16+dist), 4,16,16,mouseX,mouseY)){
				if(player.playerActions[x].provideHudAnimation().getActSequenceName().equals("click")){
					player.playerActions[x].provideHudAnimation().animateOnce(player.playerActions[x].provideHudAnimation().getSequenceByName("hover"));
					hfr.useAbility(x);
				}
			}else{
				if(!player.playerActions[x].provideHudAnimation().getActSequenceName().equals("hover")){
					player.playerActions[x].provideHudAnimation().animateOnce(player.playerActions[x].provideHudAnimation().getSequenceByName("normal"));
				}
			}
		}
	}
	public boolean checkClick(int x,int y,int sizex,int sizey,int mousex,int mousey){
		if(mousex>=x&&mousex<x+sizex&&mousey>=y&&mousey<y+sizey){
			return true;
		}else{
			return false;
		}
	}
	public void pressedEsc() {
		// TODO Auto-generated method stub
		
	}
}
