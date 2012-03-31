package interpreter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		Chef interpreter = new Chef(args[0]);
		interpreter.execute();
	}
	
	FileReader file;
	Scanner scan;
	HashMap<String, Recipe> recipes;

	public Chef(String filename) {
		try {
			file = new FileReader(filename);
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void execute() {
		// TODO Auto-generated method stub 
		
	}

}
