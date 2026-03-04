package tbs;

import java.util.Comparator;

public class CharacterSpeedComparator implements Comparator<Object>{

	@Override
	public int compare(Object arg0, Object arg1) {
		return ((((Character)arg0).getSpeed())-(((Character)arg1).getSpeed()));
	}

}
