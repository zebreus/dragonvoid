package tbs;

import java.util.LinkedList;

public class PropertyList {
	/**
	 * @uml.property  name="name"
	 */
	String name;
	/**
	 * @uml.property  name="property"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="tbs.SaveProperty"
	 */
	@SuppressWarnings("rawtypes")
	LinkedList<SaveProperty> property;
	@SuppressWarnings("rawtypes")
	public PropertyList(LinkedList<SaveProperty> property, String name){
		this.name = name;
		this.property = property;
	}
	public void addProperty(SaveProperty<?> prop){
		property.add(prop);
	}
	public SaveProperty<?> getProperty(String sname){
		for(SaveProperty<?> p:property){
			if(p.name.equals(sname)){
				return p;
			}
		}
		System.err.println("Couldn't find property "+sname+" in file "+name);
		return null;
	}
	public void removeProperty(String sname){
		for(SaveProperty<?> p:property){
			if(p.name.equals(sname)){
				property.remove(p);
			}
		}
		System.err.println("Couldn't find and remove property "+sname+" in file "+name);
	}
}
