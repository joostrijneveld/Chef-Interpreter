package interpreter;

public class Ingredient {

	public enum State {
		Dry, Liquid
	}
	
	private String name;
	private Integer amount;
	private State state;
	
	public Ingredient(String ingredient) throws ChefException {
		String[] tokens = ingredient.split(" ");
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
		}
		name = "";
		while (i < tokens.length) {
			name += tokens[i] + " ";
			i++;
		}
		if (name.equals("")) {
			throw new ChefException(ChefException.INGREDIENT, tokens, "ingredient name missing");
		}
	}
	
	public Ingredient(Integer n, State s, String name) {
		this.amount = n;
		this.state = s;
		this.name = name;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int n) {
		amount = n;
	}
	
	public State getstate() {
		return state;
	}
	
	public void liquefy() {
		state = State.Liquid;
	}
	
	public void dry() {
		state = State.Dry;
	}

	public String getName() {
		return name;
	}
}
