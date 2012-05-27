package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Recipe {

	private String title;
	private HashMap<String,Ingredient> ingredients;
	@SuppressWarnings("unused")
	private String comment;
	@SuppressWarnings("unused")
	private int cookingtime;
	@SuppressWarnings("unused")
	private int oventemp;
	@SuppressWarnings("unused")
	private int gasmark;
	private ArrayList<Method> methods;
	private int serves;
	
	public Recipe(String title)
	{
		this.title = title;
	}
	
	public void setIngredients(String ingredients) throws ChefException
	{
		this.ingredients = new HashMap<String, Ingredient>();
		Scanner scanner = new Scanner(ingredients).useDelimiter("\n");
		//Clearing the 'Ingredients.' header
		scanner.next();
		while (scanner.hasNext()) {
			Ingredient ing = new Ingredient(scanner.next());
			this.ingredients.put(ing.getName().toLowerCase(), ing);
		}
	}
	
	public void setComments(String comment)
	{
		this.comment = comment;
	}
	
	public void setMethod(String method) throws ChefException
	{
		this.methods = new ArrayList<Method>();
		method = method.replaceAll("\n", "");
		method = method.replaceAll("\\. ",".");
		Scanner scanner = new Scanner(method).useDelimiter("\\.");
		//Clearing the 'Method.' header
		scanner.next();
		for (int i = 0; scanner.hasNext(); i++) {
			this.methods.add(new Method(scanner.next() + ".", i));
		}
	}
	
	public void setCookingTime(String cookingtime)
	{
		this.cookingtime = Integer.parseInt(cookingtime.split(" ")[2]);
	}
	
	public void setOvenTemp(String oventemp)
	{
		this.oventemp = Integer.parseInt(oventemp.split(" ")[3]);
		if (oventemp.matches("gas mark")) {
			String mark = oventemp.split(" ")[8];
			this.gasmark = Integer.parseInt(mark.substring(0, mark.length()-1));
		}
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
	
	public int getIngredientValue(String s) {
		return ingredients.get(s).getAmount();
	}
	
	public void setIngredientValue(String s, int n) {
		ingredients.get(s).setAmount(n);
	}
	
	public Method getMethod(int i) {
		return methods.get(i);
	}
	
	public ArrayList<Method> getMethods() {
		return methods;
	}
	
	public HashMap<String,Ingredient> getIngredients() {
		return ingredients;
	}
}
