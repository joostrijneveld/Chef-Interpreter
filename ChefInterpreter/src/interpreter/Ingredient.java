package interpreter;

public class Ingredient {

	public enum State {
		Dry, Liquid
	}
	
	private String name;
	private Integer amount;
	private State state;
	
	public Ingredient(String name) {
		this.name = name;
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
}
