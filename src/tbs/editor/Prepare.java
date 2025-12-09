package tbs.editor;

import java.util.LinkedList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Prepare {

	public String prepare(String fileContent) {

		String[] splitedFileContent = fileContent.split("\"tiles\":|\"tilewidth\"");
		LinkedList<String> stringsToReplace = new LinkedList<String>();
		for (String a : splitedFileContent) {
			if (a.contains("image")) {
				stringsToReplace.add(a);
			}
		}

		JSONObject object = new JSONObject(fileContent);

		JSONArray tilesets = object.getJSONArray("tilesets");

		for (int a = 0; a < tilesets.length(); a++) {
			try {

				JSONObject tiles = ((JSONObject) tilesets.get(a)).getJSONObject("tiles");
				String[][] images = new String[tiles.length()][2];
				int last = 0;
				for (int b = 0; b < tiles.length(); b++) {
					for (int c = last; true; c++) {
						try {
							String imagePath = ((JSONObject) tiles.get(String.valueOf(c))).getString("image");
							images[b][1] = imagePath;
							images[b][0] = String.valueOf(c);
							last = c + 1;
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

				String array = "\n            [\n";

				for (int b = 0; b < images.length; b++) {

					array += "             {id:\"".concat(images[b][0]).concat("\",image:\"").concat(images[b][1])
							.concat("\"}").replaceAll("/", "\\/");
					if (b == images.length - 1) {
						array += "\n";
					} else {
						array += ",\n";
					}

				}

				array += "            ],\n";

				System.out.println(array);

				fileContent = fileContent.replaceAll(Pattern.quote(stringsToReplace.get(a)), array);
			} catch (org.json.JSONException e) {
				System.err.println(
						"ES DUERFEN KEINE LEEREN TEXTURENPAKETE VORHANDEN SEIN! FUEGE BITTE MINDESTENS EINE ANIMATION UND EINE TEXTUR HINZU!");
			}
		}

		String returnthis = fileContent;
		return returnthis;
	}

}
