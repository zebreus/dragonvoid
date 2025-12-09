package tbs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class DebugLayer {
static HashMap<String,String> elements = new HashMap();
static boolean visible = Config.DEBUG ;
static LinkedList<String> errors = new LinkedList();
static private String error = "";
static private long init = 0;
public static void pushError(String msg){
	errors.add(msg);
	System.err.println(msg);
}
public static void draw(Graphics g){
	if(visible){

		if(error!=""){
			if(init+Config.ERROR_TIME<System.currentTimeMillis()){
				error="";
			}
		}else{
			if(errors.size()!=0){
				init = System.currentTimeMillis();
				error=errors.poll();
			}
		}
	Set<String> set = elements.keySet();

	int y = 20+(15*set.size());
	g.setFont(new Font("Courier", Font.PLAIN, 18)); 
	for(String key:set){
		
		g.setColor(Color.white);
		g.drawString(key+" : "+elements.get(key), 16, y);
		g.drawString(key+" : "+elements.get(key), 14, y);
		g.drawString(key+" : "+elements.get(key), 15, y+1);
		g.drawString(key+" : "+elements.get(key), 15, y-1);
		g.drawString(key+" : "+elements.get(key), 16, y-1);
		g.drawString(key+" : "+elements.get(key), 14, y+1);
		g.drawString(key+" : "+elements.get(key), 16, y+1);
		g.drawString(key+" : "+elements.get(key), 14, y-1);

		g.setColor(Color.black);
		g.drawString(key+" : "+elements.get(key), 15, y);
		y-=15;
		

	}
	g.setColor(Color.white);
	g.drawString(error, 16, 14);
	g.drawString(error, 15, 14);
	g.drawString(error, 14, 14);
	g.drawString(error, 16, 16);
	g.drawString(error, 15, 16);
	g.drawString(error, 14, 16);
	g.drawString(error, 16, 15);
	g.drawString(error, 14, 15);

	g.setColor(Color.red);
	g.drawString(error, 15, 15);
	}
}
}
