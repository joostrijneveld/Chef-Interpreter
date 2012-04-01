package interpreter;

import interpreter.Ingredient.State;

import java.util.ArrayList;
import java.util.Collections;

public class Container {

	ArrayList<Component> contents;
	
	public Container() {
		contents = new ArrayList<Component>();
	}
	
	public void push(Component c) {
		contents.add(c);
	}
	
	public Component peek() {
		return contents.get(contents.size()-1);
	}
	
	public Component pop() throws ChefException {
		if (contents.size() == 0)
			throw new ChefException(ChefException.LOCAL, "Folded from empty container");
		return contents.remove(contents.size()-1);
	}
	
	public void combine(Container c) {
		contents.addAll(c.contents);
	}

	public void liquefy() {
		for (Component c : contents)
			c.liquefy();
	}

	public void clean() {
		contents = new ArrayList<Component>();
	}
	
	public String serve() {
		String result = "";
		for (int i = contents.size()-1; i > 0; i--) {
			if (contents.get(i).getState() == State.Dry) {
				result += contents.get(i).getValue();
			}
			else {
				result += Character.toChars(contents.get(i).getValue())[0];
			}
		}
		return result;
	}

	public void shuffle() {
		Collections.shuffle(contents);
	}
}
