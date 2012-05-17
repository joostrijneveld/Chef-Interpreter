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
	public int n;
	
	public Method(String line, int n) throws ChefException {
		this.n = n;
		Matcher matcher;
		matcher = Pattern.compile("^Take ([a-zA-Z ]+) from refrigerator.$").matcher(line); 
		if (matcher.find()) {
			ingredient = matcher.group(1);
			type = Type.Take;
			return;
		}
		matcher = Pattern.compile("^(Put|Fold) ([a-zA-Z ]+) into( the)?( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = matcher.group(1).equals("Put") ? Type.Put : Type.Fold;
			ingredient = matcher.group(2);
			mixingbowl = (matcher.group(5) == null ? 1 : Integer.parseInt(matcher.group(5))) - 1;
			return;
		}
		matcher = Pattern.compile("^Add dry ingredients( to( (\\d+)(nd|rd|th|st))? mixing bowl)?.$").matcher(line);
		if (matcher.find()) {
			type = Type.AddDry;
			mixingbowl = (matcher.group(3) == null ? 1 : Integer.parseInt(matcher.group(3))) - 1;
			return;
		}
		matcher = Pattern.compile("^(Add|Remove|Combine|Divide) ([a-zA-Z ]+)( (to|into|from)( (\\d+)(nd|rd|th|st))? mixing bowl)?.$").matcher(line);
		if (matcher.find()) {
			type = matcher.group(1).equals("Add") ? Type.Add : (matcher.group(1).equals("Remove") ? Type.Remove : (matcher.group(1).equals("Combine") ? Type.Combine : Type.Divide));
			ingredient = matcher.group(2);
			mixingbowl = (matcher.group(6) == null ? 1 : Integer.parseInt(matcher.group(6))) - 1;
			return;
		}
		matcher = Pattern.compile("^Liquefy contents of the( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.LiquefyBowl;
			mixingbowl = (matcher.group(2) == null ? 1 : Integer.parseInt(matcher.group(2))) - 1;
			return;
		}
		matcher = Pattern.compile("^Liquefy ([a-zA-Z ]+).$").matcher(line);
		if (matcher.find()) {
			type = Type.Liquefy;
			ingredient = matcher.group(1);
			return;
		}
		//Stir [the [nth] mixing bowl] for number minutes.
		matcher = Pattern.compile("^Stir( the( (\\d+)(nd|rd|th|st))? mixing bowl)? for (\\d+) minutes.$").matcher(line);
		if (matcher.find()) {
			type = Type.Stir;
			mixingbowl = (matcher.group(3) == null ? 1 : Integer.parseInt(matcher.group(3))) - 1;
			time = Integer.parseInt(matcher.group(5));
			return;
		}
		//Stir ingredient into the [nth] mixing bowl.
		matcher = Pattern.compile("^Stir ([a-zA-Z ]+) into the( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.StirInto;
			ingredient = matcher.group(1);
			mixingbowl = (matcher.group(3) == null ? 1 : Integer.parseInt(matcher.group(3))) - 1;
			return;
		}
		matcher = Pattern.compile("^Mix( the( (\\d+)(nd|rd|th|st))? mixing bowl)? well.$").matcher(line);
		if (matcher.find()) {
			type = Type.Mix;
			mixingbowl = (matcher.group(3) == null ? 1 : Integer.parseInt(matcher.group(3))) - 1;
			return;
		}
		matcher = Pattern.compile("^Clean( (\\d+)(nd|rd|th|st))? mixing bowl.$").matcher(line);
		if (matcher.find()) {
			type = Type.Clean;
			mixingbowl = (matcher.group(2) == null ? 1 : Integer.parseInt(matcher.group(2))) - 1;
			return;
		}
		matcher = Pattern.compile("^Pour contents of the( (\\d+)(nd|rd|th|st))? mixing bowl into the( (\\d+)(nd|rd|th|st))? baking dish.$").matcher(line);
		if (matcher.find()) {
			type = Type.Pour;
			mixingbowl = (matcher.group(2) == null ? 1 : Integer.parseInt(matcher.group(2))) - 1;
			bakingdish = (matcher.group(5) == null ? 1 : Integer.parseInt(matcher.group(5))) - 1;
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
			auxrecipe = matcher.group(1);
			return;
		}
		matcher = Pattern.compile("^([a-zA-Z]+)( the ([a-zA-Z ]+))? until ([a-zA-Z]+).$").matcher(line);
		if (matcher.find()) {
			type = Type.VerbUntil;
			verb = matcher.group(4);
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
		throw new ChefException(ChefException.METHOD, n, line, "Unsupported method found!");
	}

}
