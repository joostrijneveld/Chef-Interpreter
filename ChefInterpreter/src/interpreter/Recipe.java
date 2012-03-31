package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Recipe {

	private String title;
	private HashMap<String,Ingredient> ingredients;
	private String comment;
	private String cookingtime;
	private String oventemp;
	private ArrayList<Method> methods;
	private int serves;
	
	public Recipe(String title)
	{
		this.title = title;
	}
	
	public void setIngredients(String ingredients) throws ChefException
	{
		this.ingredients = new HashMap<String, Ingredient>();
		Scanner scanner = new Scanner(ingredients);
		//Clearing the 'Ingredients.' header
		scanner.next();
		while (scanner.hasNext()) {
			String[] tokens = scanner.next().split(" ");
			Integer amount = null;
			Ingredient.State state = null;
			int i = 0;
			if (tokens[i].matches("^\\d*$"))
			{
				amount = Integer.parseInt(tokens[i]);
				i++;
				if (tokens[i].equals("heaped") || tokens[i].equals("level")) {
					state = tokens[i].equals("heaped") ? Ingredient.State.Dry : Ingredient.State.Liquid;
					i++;
				}
				if (tokens[i].matches("^g|kg|pinch(es)?")) {
					state = Ingredient.State.Dry;
					i++;
				}
				else if (tokens[i].matches("^ml|l|dash(es)?")) {
					state = Ingredient.State.Liquid;
					i++;
				}
				else if (tokens[i].matches("^cup(s)?|teaspoon(s)?|tablespoon(s)?")) {
					i++;
				}
				else {
					throw new ChefException(ChefException.LOCAL, "Ingredient "+tokens+" is wrongly formatted (unit missing)");
				}
			}
			String name = "";
			while (i < tokens.length) {
				name += tokens[i] + " ";
			}
			if (name.equals("")) {
				throw new ChefException(ChefException.LOCAL, "Ingredient "+tokens+" is wrongly formatted (ingredient missing)");
			}
			this.ingredients.put(name.toLowerCase(),new Ingredient(amount, state, name));
		}
	}
	
	public void setComments(String comment)
	{
		this.comment = comment;
	}
	
	public void setMethod(String method)
	{
		this.methods = parseMethods(method);
	}
	
	public void setCookingTime(String cookingtime)
	{
		this.cookingtime = cookingtime;
	}
	
	public void setOvenTemp(String oventemp)
	{
		this.oventemp = oventemp;
	}
	
	public void setServes(String serves) {
		this.serves = Integer.parseInt(serves.substring(("Serves ").length(), serves.length()-1));
	}

	public int getServes()
	{
		return serves;
	}
	
	public String getTitle() {
		return title;
	}
	
	private ArrayList<Method> parseMethods(String methods2) {
		// TODO Auto-generated method stub
		return new ArrayList<Method>();
	}
	
}
