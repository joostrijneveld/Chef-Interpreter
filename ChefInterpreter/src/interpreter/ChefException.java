package interpreter;

@SuppressWarnings("serial")
public class ChefException extends Exception {

	public static final int STRUCTURAL = 0;
	public static final int LOCAL = 1;
	public static final int INGREDIENT = 2;
	public static final int METHOD = 3;
	
	public ChefException(int type, String text) {
		super(typeToText(type)+text);
	}
	
	public ChefException(int type, String[] a, String error) {
		super("Ingredient wrongly formatted: '"+arrayToString(a," ")+"' ("+error+")");
	}
	
	public ChefException(int type, int step, String method, String error) {
		super("Method error, step "+(step+1)+": "+method+" ("+error+")");
	}
	
	public ChefException(int type, Recipe recipe, int step, String method,
			String error) {
		super("Method error, recipe "+recipe.getTitle()+", step "+(step+1)+": "+method+" ("+error+")");
	}

	private static String typeToText(int type)
	{
		return type == STRUCTURAL ? "Structural error: " : "Local error: ";
	}
	
	public static String arrayToString(Object[] a, String separator) {
	    String result = "";
	    if (a.length > 0) {
	        result = a[0].toString();
	        for (int i=1; i<a.length; i++) {
	            result += separator + a[i].toString();
	        }
	    }
	    return result;
	}
}
