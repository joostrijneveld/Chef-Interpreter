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
		Refrigerate,
		Remember
	}
	
	public String ingredient;
	public Integer mixingbowl;
	public Integer bakingdish;
	public String auxrecipe;
	public Integer time; 		//'Refrigerate for number of hours' / 'Stir for number of minutes'
	public String verb;
	public Type type;
	public int n;
	
	public Method(String line, int n) throws ChefException {
		line = line.trim();
		this.n = n;
		Matcher[] matchers = {
				Pattern.compile("^Take ([a-zA-Z ]+) from refrigerator.$").matcher(line),
				Pattern.compile("^(Put|Fold) ([a-zA-Z ]+) into( the)?( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line),
				Pattern.compile("^Add dry ingredients( to( (\\d+)(nd|rd|th|st))? mixing bowl)?.$").matcher(line),
				Pattern.compile("^(Add|Remove|Combine|Divide) ([a-zA-Z ]+)( (to|into|from)( (\\d+)(nd|rd|th|st))? mixing bowl)?.$").matcher(line),
				Pattern.compile("^Liquefy contents of the( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line),
				Pattern.compile("^Liquefy ([a-zA-Z ]+).$").matcher(line),
				Pattern.compile("^Stir( the( (\\d+)(nd|rd|th|st))? mixing bowl)? for (\\d+) minutes.$").matcher(line),
				Pattern.compile("^Stir ([a-zA-Z ]+) into the( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line),
				Pattern.compile("^Mix( the( (\\d+)(nd|rd|th|st))? mixing bowl)? well.$").matcher(line),
				Pattern.compile("^Clean( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line),
				Pattern.compile("^Pour contents of the( (\\d+)(nd|rd|th|st))? mixing bowl into the( (\\d+)(nd|rd|th|st))? baking dish.$").matcher(line),
				Pattern.compile("^Set aside.$").matcher(line),
				Pattern.compile("^Refrigerate( for (\\d+) hours)?.$").matcher(line),
				Pattern.compile("^Serve with ([a-zA-Z ]+).$").matcher(line),
				Pattern.compile("^Suggestion: (.*).$").matcher(line),
				Pattern.compile("^([a-zA-Z]+)( the ([a-zA-Z ]+))? until ([a-zA-Z]+).$").matcher(line),
				Pattern.compile("^([a-zA-Z]+) the ([a-zA-Z ]+).$").matcher(line)
				}; 
		if (matchers[0].find()) {
			ingredient = matchers[0].group(1);
			type = Type.Take;
		}
		else if (matchers[1].find()) {
			type = matchers[1].group(1).equals("Put") ? Type.Put : Type.Fold;
			ingredient = matchers[1].group(2);
			mixingbowl = (matchers[1].group(5) == null ? 1 : Integer.parseInt(matchers[1].group(5))) - 1;
		}
		else if (matchers[2].find()) {
			type = Type.AddDry;
			mixingbowl = (matchers[2].group(3) == null ? 1 : Integer.parseInt(matchers[2].group(3))) - 1;
		}
		else if (matchers[3].find()) {
			type = matchers[3].group(1).equals("Add") ? Type.Add : (matchers[3].group(1).equals("Remove") ? Type.Remove : (matchers[3].group(1).equals("Combine") ? Type.Combine : Type.Divide));
			ingredient = matchers[3].group(2);
			mixingbowl = (matchers[3].group(6) == null ? 1 : Integer.parseInt(matchers[3].group(6))) - 1;
		}
		else if (matchers[4].find()) {
			type = Type.LiquefyBowl;
			mixingbowl = (matchers[4].group(2) == null ? 1 : Integer.parseInt(matchers[4].group(2))) - 1;
		}
		else if (matchers[5].find()) {
			type = Type.Liquefy;
			ingredient = matchers[5].group(1);
		}
		else if (matchers[6].find()) {
			type = Type.Stir;
			mixingbowl = (matchers[6].group(3) == null ? 1 : Integer.parseInt(matchers[6].group(3))) - 1;
			time = Integer.parseInt(matchers[6].group(5));
		}
		//Stir ingredient into the [nth] mixing bowl.
		else if (matchers[7].find()) {
			type = Type.StirInto;
			ingredient = matchers[7].group(1);
			mixingbowl = (matchers[7].group(3) == null ? 1 : Integer.parseInt(matchers[7].group(3))) - 1;
		}
		else if (matchers[8].find()) {
			type = Type.Mix;
			mixingbowl = (matchers[8].group(3) == null ? 1 : Integer.parseInt(matchers[8].group(3))) - 1;
		}
		else if (matchers[9].find()) {
			type = Type.Clean;
			mixingbowl = (matchers[9].group(2) == null ? 1 : Integer.parseInt(matchers[9].group(2))) - 1;
		}
		else if (matchers[10].find()) {
			type = Type.Pour;
			mixingbowl = (matchers[10].group(2) == null ? 1 : Integer.parseInt(matchers[10].group(2))) - 1;
			bakingdish = (matchers[10].group(5) == null ? 1 : Integer.parseInt(matchers[10].group(5))) - 1;
		}
		else if (matchers[11].find()) {
			type = Type.SetAside;
		}
		else if (matchers[12].find()) {
			type = Type.Refrigerate;
			time = matchers[12].group(2) == null ? 0 : Integer.parseInt(matchers[12].group(2));
		}
		else if (matchers[13].find()) {
			type = Type.Serve;
			auxrecipe = matchers[13].group(1);
		}
		else if (matchers[14].find()) {
			type = Type.Remember;
		}
		else if (matchers[15].find()) {
			type = Type.VerbUntil;
			verb = matchers[15].group(4);
			ingredient = matchers[15].group(3);
		}
		else if (matchers[16].find()) {
			type = Type.Verb;
			verb = matchers[16].group(1);
			ingredient = matchers[16].group(2);
		}
		else
			throw new ChefException(ChefException.METHOD, n, line, "Unsupported method found!");
	}

}
