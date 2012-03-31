package interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Matcher matcher;
		matcher = Pattern.compile("^Take ([a-zA-Z ]+) from refrigerator.$").matcher(line); 
		if (matcher.find()) {
			ingredient = matcher.group(1);
			type = Type.Take;
			return;
		}
		matcher = Pattern.compile("^(Put|Fold) ([a-zA-Z ]+) into( (\\d+)(nd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = matcher.group(1) == "Put" ? Type.Put : Type.Fold;
			ingredient = matcher.group(2);
			mixingbowl = matcher.group(4) == null ? 0 : Integer.parseInt(matcher.group(4));
			return;
		}
		matcher = Pattern.compile("^Add dry ingredients( to( (\\d+)(nd|th|st))? mixing bowl)?.$").matcher(line);
		if (matcher.find()) {
			type = Type.AddDry;
			mixingbowl = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
			return;
		}
		matcher = Pattern.compile("^(Add|Remove|Combine|Divide) ([a-zA-Z ]+)( (to|into|from)( (\\d+)(nd|th|st))? mixing bowl)?.$").matcher(line);
		if (matcher.find()) {
			type = matcher.group(1) == "Add" ? Type.Add : (matcher.group(1) == "Remove" ? Type.Remove : (matcher.group(1) == "Combine" ? Type.Combine : Type.Divide));
			ingredient = matcher.group(2);
			mixingbowl = matcher.group(6) == null ? 0 : Integer.parseInt(matcher.group(6));
			return;
		}
		matcher = Pattern.compile("^Liquefy contents of the( (\\d+)(nd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.LiquefyBowl;
			mixingbowl = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
			return;
		}
		matcher = Pattern.compile("^Liquefy ([a-zA-Z ]+).$").matcher(line);
		if (matcher.find()) {
			type = Type.Liquefy;
			ingredient = matcher.group(1);
			return;
		}
		//Stir [the [nth] mixing bowl] for number minutes.
		matcher = Pattern.compile("^Stir( the( (\\d+)(nd|th|st))? mixing bowl)? for (\\d+) minutes.$").matcher(line);
		if (matcher.find()) {
			type = Type.Stir;
			mixingbowl = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
			time = Integer.parseInt(matcher.group(5));
			return;
		}
		//Stir ingredient into the [nth] mixing bowl.
		matcher = Pattern.compile("^Stir ([a-zA-Z ]+) into the( (\\d+)(nd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.StirInto;
			ingredient = matcher.group(1);
			mixingbowl = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
			return;
		}
		matcher = Pattern.compile("^Mix( the( (\\d+)(nd|th|st))? mixing bowl)? well.$").matcher(line);
		if (matcher.find()) {
			type = Type.Mix;
			mixingbowl = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
			return;
		}
		matcher = Pattern.compile("^Clean( (\\d+)(nd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.Clean;
			mixingbowl = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
			return;
		}
		matcher = Pattern.compile("^Pour contents of the( (\\d+)(nd|th|st))? mixing bowl into the( (\\d+)(nd|th|st))? baking dish.$").matcher(line);
		if (matcher.find()) {
			type = Type.Pour;
			mixingbowl = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
			bakingdish = matcher.group(5) == null ? 0 : Integer.parseInt(matcher.group(5));
			return;
		}
		matcher = Pattern.compile("^Set aside.$").matcher(line);
		if (matcher.find()) {
			type = Type.SetAside;
			return;
		}
		matcher = Pattern.compile("^Refrigerate( for (\\d+) hours)?.$").matcher(line);
		if (matcher.find()) {
			type = Type.Refrigerate;
			time = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
			return;
		}
		matcher = Pattern.compile("^Serve with ([a-zA-Z ]+).$").matcher(line);
		if (matcher.find()) {
			type = Type.Serve;
			System.out.println(matcher.group(1));
			auxrecipe = matcher.group(1);
			return;
		}
		matcher = Pattern.compile("^([a-zA-Z]+)( the ([a-zA-Z ]+))? until ((?i)\\1)ed.$").matcher(line);
		if (matcher.find()) {
			type = Type.VerbUntil;
			verb = matcher.group(1);
			ingredient = matcher.group(3);
			return;
		}
		matcher = Pattern.compile("^([a-zA-Z]+) the ([a-zA-Z ]+).$").matcher(line);
		if (matcher.find()) {
			type = Type.Verb;
			verb = matcher.group(1);
			ingredient = matcher.group(2);
			return;
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
