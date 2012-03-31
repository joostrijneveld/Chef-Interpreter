package interpreter;

public class Method {

	public enum Type {
		Take,
		Put,
		Fold,
		Add,
		Remove,
		Combine,
		Divide,
		AddDry,
		Liquefy,
		LiquefyBowl,
		Stir,
		StirInto,
		Mix,
		Clean,
		Pour,
		Verb,
		VerbUntil,
		SetAside,
		Serve,
		Refrigerate
	}
	
	public String ingredient;
	public Integer mixingbowl;
	public Integer bakingdish;
	public String auxrecipe;
	public Integer time; 		//'Refrigerate for number of hours' / 'Stir for number of minutes'
	public String verb;
	public Type type;
	
	public Method(String line, int n) throws ChefException {
		if (line.matches("^Take ([a-z][A-Z] )* from refrigerator.$")) {
			
		}
	}
	
	/*public Method(String line, int n) throws ChefException {
		String[] parts = line.split(" ");
		if (parts[0] == "Take") {
			ingredient = arrayToString(parts, 1, -2);
			type = Type.Take;
		}
		else if (parts[0] == "Put" || parts[0] == "Fold") {
			if (parts[parts.length-5] == "into") {
				mixingbowl = nthToInt(parts[parts.length-3]);
				ingredient = arrayToString(parts, 1, parts.length-5);
			}
			else {
				mixingbowl = 0;
				ingredient = arrayToString(parts, 1, parts.length-4);
			}
			type = parts[0] == "Put" ? Type.Put : Type.Fold;
		}
		else if (arrayToString(parts, 0, 3) == "Add dry ingredients") {
			switch (parts.length) {
				case 3 :
				case 6 : mixingbowl = 0; break;
				case 7 : mixingbowl = nthToInt(parts[4]);
				default : throw new ChefException(ChefException.METHOD, n, line, "wrong amount of words");
			}
			type = Type.AddDry;
		}
		else if (parts[0] == "Add" || parts[0] == "Remove" || parts[0] == "Combine" || parts[0] == "Divide") {
			if (parts[parts.length-1] != "bowl") {
				mixingbowl = 0;
				ingredient = arrayToString(parts, 1);
			}
			else if	(parts[parts.length-3] == "from" || parts[parts.length-3] == "to" || parts[parts.length-3] == "into") {
				mixingbowl = 0;
				ingredient = arrayToString(parts, 1, parts.length-3);
			}
			else {
				mixingbowl = nthToInt(parts[parts.length-3]);
				ingredient = arrayToString(parts, 1, parts.length-4);
			}
			type = (parts[0] == "Add" ? Type.Add : (parts[0] == "Remove" ? Type.Remove : (parts[0] == "Combine" ? Type.Combine : Type.Divide)));
		}
		else if (parts[0] == "Liquefy") {
			if (parts[1] == "contents"){
				if (parts.length == 6) {
					mixingbowl = 0;
				}
				else {
					mixingbowl = nthToInt(parts[parts.length-3]);
				}
				type = Type.LiquefyBowl;
			}
			else {
				ingredient = arrayToString(parts, 1, parts.length);
				type = Type.Liquefy;
			}
		}
		else if (parts[0] == "Stir") {
			if (parts[parts.length-1] == "minutes") {
				time = Integer.parseInt(parts[parts.length-2]);
				switch(parts.length) {
					case 4 :
					case 7 : mixingbowl = 0; break;
					case 8 : mixingbowl = nthToInt(parts[4]);
				}
			}
			else {
				if (parts[parts.length-4] == "the") {
					mixingbowl = nthToInt(parts[parts.length-3]);
					ingredient = arrayToString(parts, 1, parts.length-5);
				}
				else {
					mixingbowl = 0;
					ingredient = arrayToString(parts, 1, parts.length-4);
				}
			}
		}	 
	}*/
	
	private Integer nthToInt(String string) {
		return Integer.parseInt(string.substring(0));
	}

	public static String arrayToString(Object[] a, String separator) {
		return arrayToString(a, separator, 0, a.length);
	}
	
	public static String arrayToString(Object[] array, int a, int b) {
		if (b < 0)
			b = array.length + b;
		return arrayToString(array, " ", a, b);
	}
	
	public static String arrayToString(Object[] array, int a) {
		return arrayToString(array, " ", a, array.length);
	}
	
	public static String arrayToString(Object[] array, String separator, int a, int b) {
		if (b < 0)
			b = array.length + b;
	    String result = "";
	    if (array.length > a) {
	        result = array[a].toString();
	        for (int i=a+1; i<b; i++) {
	            result += separator + array[i].toString();
	        }
	    }
	    return result;
	}

}
