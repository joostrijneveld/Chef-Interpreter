package interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Chef {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1)
			throw new Exception("No recipe found! Usage: Chef recipe.chef");
		//new Method("Verb the potatoe until verbed.", 0);
		//new Ingredient("100 g dijon mustard");
		//String line = "Take pot atoe from refrigerator.";
		//System.out.println(line.matches("^Take ([a-zA-Z])* from refrigerator.$"));
		//new Method("Put potatoes into the mixing bowl.",0);
		Chef interpreter = new Chef(args[0]);
		interpreter.bake();
	}
	
	File file;
	Scanner scan;
	HashMap<String, Recipe> recipes;
	Recipe mainrecipe;
	
	public Chef(String filename) throws ChefException {
		recipes = new HashMap<String, Recipe>();
		//System.out.print("Reading recipe(s) from '"+filename+"'.. ");
		try {
			file = new File(filename);
			scan = new Scanner(file);
			scan.useDelimiter("\n\n");
			int progress = 0;
			Recipe r = null;
			do {
				if (scan.hasNext()) {
					if (progress == 0) {
						String title = scan.next();
						r = new Recipe(title);
						if (mainrecipe == null)
							mainrecipe = r;
						progress = 1;
					}
					recipes.put(parseTitle(r.getTitle()), r);
					while (scan.hasNext()) {
						String line = scan.next();
						if (line.startsWith("Ingredients")) {
							if (progress > 3)
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(2)+". Expecting "+progressToExpected(progress));
							progress = 3;
							r.setIngredients(line);
						}
						else if (line.startsWith("Cooking time")) {
							if (progress > 4)
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(3)+". Expecting "+progressToExpected(progress));
							progress = 4;
							r.setCookingTime(line);
						}
						else if (line.startsWith("Pre-heat oven")) {
							if (progress > 5)
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(4)+". Expecting "+progressToExpected(progress));
							progress = 5;
							r.setOvenTemp(line);
						}
						else if (line.startsWith("Method")) {
							if (progress > 5)
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(5)+". Expecting "+progressToExpected(progress));
							progress = 6;
							r.setMethod(line);
						}
						else if (line.startsWith("Serves")) {
							if (progress != 6)
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(6)+". Expecting "+progressToExpected(progress));
							progress = 0;
							r.setServes(line);
							break;
						}
						else {
							if (progress == 1) {
								progress = 2;
								r.setComments(line);
							}
							else if (progress >= 6) {
								progress = 1;
								r = new Recipe(line);
								break;
							}
							else {
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected comments/title. Expecting "+progressToExpected(progress)+" Hint:"+structHint(progress));
							}
						}
					}
				}
				else
					throw new ChefException(ChefException.STRUCTURAL, "Title missing!");
			} while (scan.hasNext());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("Done!");
	}
	
	private String parseTitle(String title) {
		if (title.charAt(title.length() - 1) == '.') {
			title = title.substring(0,title.length()-1);
		}
		return title.toLowerCase();
	}

	private String structHint(int progress) {
		switch (progress) {
			case 2 : return "did you specify 'Method.' above the methods?";
		}
		return "no hint available";
	}

	private String progressToExpected(int progress) {
		switch (progress) {
			case 0 : return "title";
			case 1 : return "comments";
			case 2 : return "ingredient list";
			case 3 : return "cooking time";
			case 4 : return "oven temperature";
			case 5 : return "methods";
			case 6 : return "serve amount";
		}
		return null;
	}

	public void bake() throws ChefException {
		Kitchen k = new Kitchen(recipes, mainrecipe);
		k.cook();
	}

}
