package tbs;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Animation implements Drawable{
	
	//Frames of the animation
	/**
	 * @uml.property  name="frames"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	Sprite[] frames;
	//sequences ; [sequence id][frame][0 for frameid ; 1 for time]
	/**
	 * @uml.property  name="sequences" multiplicity="(0 -1)" dimension="3"
	 */
	int[][][] sequences;
	//actual sequence state ; first var is for sequence, second var is for frame, third var is 0 for repeating and 1 for once
	/**
	 * @uml.property  name="actSequence" multiplicity="(0 -1)" dimension="1"
	 */
	int[] actSequence;
	//names associated with sequences
	/**
	 * @uml.property  name="names" multiplicity="(0 -1)" dimension="1"
	 */
	String[] names;
	//sprite to draw atm
	/**
	 * @uml.property  name="draw"
	 * @uml.associationEnd  readOnly="true"
	 */
	Sprite draw;
	/**
	 * @uml.property  name="sinceLast"
	 */
	int sinceLast;
	/**
	 * @uml.property  name="running"
	 */
	boolean running;
	/**
	 * @uml.property  name="speed"
	 */
	float speed;
	
	/**
	 * @uml.property name="name"
	 */
	String name;
	
	/** Creates new animation from given file. You have to call update for the animation to run
	 * @param file File in res/animations/ "here goes you input" .anim
	 * @throws IOException if .anim file does not exist or sprites do not exist
	 */
	public Animation(String file){
		try{
		Scanner s = new Scanner(new File("res/animations/"+file+".anim"));
		name = file.split("/")[file.split("/").length-1];
		speed = 1f;
		sinceLast = 0;
		running = false;
		actSequence = new int[3];
		actSequence[0] = 0;
		actSequence[1] = 0;
		actSequence[2] = 0;
		int frameNumber = s.nextInt();
		frames = new Sprite[frameNumber];
		String path = s.next();
		for(int x = 0;x<frameNumber;x++){
			frames[x] = new Sprite(path+s.next());
		}
		int sequenceNumber = s.nextInt();
		sequences = new int[sequenceNumber][][];
		names = new String[sequenceNumber];
		for(int x = 0;x<sequenceNumber;x++){
			names[x] = s.next();
			int sequenceFrameNumber = s.nextInt();
			sequences[x] = new int[sequenceFrameNumber][2];
			for(int y = 0;y<sequenceFrameNumber;y++){
				sequences[x][y][0] = s.nextInt();
				sequences[x][y][1] = s.nextInt();
			}
		}
		
		s.close();
		}catch(IOException e){
			System.out.println("Couldn't open .anim file res/animations/"+file+".anim");
			e.printStackTrace();
		}
		
		
	}
	/** Changes frames if needed
	 * @param delta time passed since last update
	 */
	public void update(long delta){
		if(running){
			sinceLast+=delta;
		}
		if(sinceLast*speed>=sequences[actSequence[0]][actSequence[1]][1]){
			sinceLast=0;
			if(actSequence[1]+1==sequences[actSequence[0]].length){
				if(actSequence[2]==1){
					running = false;
				}else{
					actSequence[1]=0;
				}
			}else{
				actSequence[1]++;
			}
		}
	}
	public void paint(Matrix mat,int x,int y){
		getActSprite().paint(mat, x, y);
	}
	/**	Returns the active frame
	 * @return the active frame
	 */
	public Sprite getActSprite(){
		return frames[sequences[actSequence[0]][actSequence[1]][0]];
		
	}
	/** Animates the given sequence once
	 * @param sequence the id of the sequence; if you dont know it call getSequenceByName
	 */
	public void animateOnce(int sequence){
		actSequence[0] = sequence;
		actSequence[1] = 0;
		actSequence[2] = 1;
		running = true;
	}
	/** Animates the given sequence until you pause
	 * @param sequence the id of the sequence; if you dont know it call getSequenceByName
	 */
	public void animateRepeating(int sequence){
		actSequence[0] = sequence;
		actSequence[1] = 0;
		actSequence[2] = 0;
		running = true;
	}
	/** Pauses the animation
	 * 
	 */
	public void pauseAnimation(){
		running = false;
	}
	
	/** Resumes the animation, if the previos animation was just run once it will runn once again
	 * 
	 */
	public void resumeAnimation(){
		running = true;
	}
	public String getActSequenceName(){
		return names[actSequence[0]];
	}
	public int getActSequence(){
		return actSequence[0];
	}
	/** Sets the frame, that is displayed and pauses; if you dont want the pause call resumeAnimation right after this
	 * @param sequ The sequence in which the frame is
	 * @param frame The number of the frame in the sequence
	 */
	public void setFrame(int sequ,int frame){
		actSequence[0] = sequ;
		actSequence[1] = frame;
	}
	/**
	 * @param speed
	 * @uml.property  name="speed"
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}
	/**
	 * @return
	 * @uml.property  name="speed"
	 */
	public float getSpeed(){
		return speed;
	}
	public int getSequenceByName(String name){
		for(int x = 0;x<names.length;x++){
			if(names[x].equals(name)){
				return x;
			}
		}
		return -1;
	}
	@Override
	public int[][] getAlpha() {
		return getActSprite().getAlpha();
	}
	@Override
	public Color[][] getColors() {
		return getActSprite().getColors();
	}
	@Override
	public int getWidth() {
		return getActSprite().getWidth();
	}
	@Override
	public int getHeight() {
		return getActSprite().getHeight();
	}
	@Override
	public String getName() {
		return name;
	}
}
