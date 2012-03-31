package interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Chef {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1)
			throw new Exception("No recipe found! Usage: Chef recipe.chef");
		Chef interpreter = new Chef(args[0]);
		//interpreter.execute();
	}
	
	File file;
	Scanner scan;
	HashMap<String, Recipe> recipes;
	
	public Chef(String filename) throws ChefException {
		recipes = new HashMap<String, Recipe>();
		System.out.print("Reading recipe(s) from '"+filename+"'.. ");
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
						progress = 1;
					}
					recipes.put(r.getTitle().toLowerCase(), r);
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
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected "+progressToExpected(4)+". Expecting "+progressToExpected(progress));
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
							}
							else
								throw new ChefException(ChefException.STRUCTURAL, "Read unexpected comments/title. Expecting "+progressToExpected(progress));
						}
					}
				}
				else
					throw new ChefException(ChefException.STRUCTURAL, "Title missing!");
			} while (scan.hasNext());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
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

	public void execute() {
		// TODO Auto-generated method stub 
		
	}

}
