package interpreter;

public class ChefException extends Exception {

	public static final int STRUCTURAL = 0;
	public static final int LOCAL = 1;
	
	public ChefException(int type, String text) {
		super(typeToText(type)+text);
	}
	
	private static String typeToText(int type)
	{
		return type == STRUCTURAL ? "Structural error: " : "Local error: ";
	}
}
