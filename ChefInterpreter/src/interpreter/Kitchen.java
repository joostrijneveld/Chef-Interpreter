package interpreter;

import interpreter.Ingredient.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Kitchen {

	Container[] mixingbowls;
	Container[] bakingdishes;
	HashMap<String, Recipe>	recipes;
	Recipe recipe;
	
	public Kitchen(HashMap<String, Recipe> recipes, Recipe mainrecipe) {
		this.recipes = recipes;
		int maxbowl = -1, maxdish = -1;
		recipe = mainrecipe;
		for (Method m : recipe.getMethods()) {
			if (m.bakingdish != null && m.bakingdish > maxdish)
				maxdish = m.bakingdish;
			if (m.mixingbowl != null && m.mixingbowl > maxbowl)
				maxbowl = m.mixingbowl;
		}
		mixingbowls = new Container[maxbowl + 1];
		bakingdishes = new Container[maxdish + 1];
		for (int i = 0; i < mixingbowls.length; i++)
			mixingbowls[i] = new Container();
		for (int i = 0; i < bakingdishes.length; i++)
			bakingdishes[i] = new Container();
	}

	public Container cook() throws ChefException {
		HashMap<String,Ingredient> ingredients = recipe.getIngredients();
		ArrayList<Method> methods = recipe.getMethods();
		Scanner input = new Scanner(System.in);
		Component c, c2;
		int i = 0;
		boolean deepfrozen = false;
		while (i < methods.size() && !deepfrozen) {
			Method m = methods.get(i);
			switch (m.type) {
				case Take : 
					ingredients.get(m.ingredient).setAmount(input.nextInt());
					break;
				case Put :
					mixingbowls[m.mixingbowl].push(new Component(ingredients.get(m.ingredient)));
					break;
				case Fold :
					c = mixingbowls[m.mixingbowl].pop();
					ingredients.get(m.ingredient).setAmount(c.getValue());
					ingredients.get(m.ingredient).setState(c.getState());
					break;
				case Add :
					c2 = mixingbowls[m.mixingbowl].peek();
					c = new Component(c2.getValue() + ingredients.get(m.ingredient).getAmount(), c2.getState());
					break;
				case Remove :
					c2 = mixingbowls[m.mixingbowl].peek();
					c = new Component(c2.getValue() - ingredients.get(m.ingredient).getAmount(), c2.getState()); 
					break;
				case Combine :
					c2 = mixingbowls[m.mixingbowl].peek();
					c = new Component(c2.getValue() * ingredients.get(m.ingredient).getAmount(), c2.getState()); 
					break;
				case Divide :
					c2 = mixingbowls[m.mixingbowl].peek();
					c = new Component(c2.getValue() / ingredients.get(m.ingredient).getAmount(), c2.getState()); 
					break;
				case AddDry :
					int sum = 0;
					for(Entry<String, Ingredient> s : ingredients.entrySet())
						if (s.getValue().getstate() == State.Dry)
							sum += s.getValue().getAmount();
					mixingbowls[m.mixingbowl].push(new Component(sum, State.Dry));
					break;
				case Liquefy :
					ingredients.get(m.ingredient).liquefy();
					break;
				case LiquefyBowl :
					mixingbowls[m.mixingbowl].liquefy();
					break;
				case Stir :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case StirInto :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case Mix :
					mixingbowls[m.mixingbowl].shuffle();
				case Clean :
					mixingbowls[m.mixingbowl].clean();
					break;
				case Pour :
					bakingdishes[m.bakingdish].combine(mixingbowls[m.mixingbowl]);
					break;
				case Verb :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case VerbUntil :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case SetAside :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case Serve :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
				case Refrigerate :
					serve(m.time);
					deepfrozen = true;
					break;
				default :
					throw new ChefException(ChefException.METHOD, m.n, m.type.toString(), "Unsupported method found!");
			}
			i++;
		}
		if (recipe.getServes() > 0 && !deepfrozen)
			serve(recipe.getServes());
		return mixingbowls[0];
	}

	private void serve(int n) {
		for (int i = 0; i < n && i < bakingdishes.length; i++) {
			System.out.println(bakingdishes[i].serve());
		}
	}
	
}
