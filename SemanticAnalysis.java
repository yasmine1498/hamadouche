import java.util.ArrayList;
public final class SemanticAnalysis {
    private static String intsList[];
	private static String floatsList[];
	private static String stringsList[];
	private static String javaCode[];
	
	private SemanticAnalysis() {
		// Just to prevent the creation of the object
	}
	
	public static String analyse(String words[], String tokens[]) {
		getVariables(words);
		
		String output = "";
		output = checkingSet(words);
		output += isAllVariablesDeclared(words,tokens);
		toJava(words);
		
		if(output.equals("")) output += "Aucune errour rencontrer"+"\n\nProgramme java : \n"+toJava(words);
		return output;
	}
	
	// this functions separates the variables by type
	private static void getVariables(String words[]) {
		
		ArrayList<String> floats = new ArrayList<String>();
		ArrayList<String> ints  = new ArrayList<String>();
		ArrayList<String> strings = new ArrayList<String>();		
		
		int i=0;
		while(i<words.length) {
			if(words[i].equals("Snl_Int")) {
				i++;
				while(!words[i].equals("%.") && i<words.length ) {
					if(!words[i].equals(",")) {
						ints.add(words[i]);
					}
					i++;
				}
			}
			else if(words[i].equals("Snl_Real")) {
				i++;
				while(!words[i].equals("%.")  && i<words.length ) {
					if(!words[i].equals(",")) floats.add(words[i]);
					i++;
				}
			}
			else if(words[i].equals("Snl_Str")) {
				i++;
				while(!words[i].equals("%.") && i<words.length) {
					if(!words[i].equals(",")) strings.add(words[i]);
					i++;
				}
			}
			i++;
		}
		
		intsList = new String[ints.size()];
		for(int j=0;j<intsList.length;j++) {
			intsList[j] = ints.get(j);
		}
		
		floatsList = new String[floats.size()];
		for(int j=0;j<floatsList.length;j++) {
			floatsList[j] = floats.get(j);
		}
		
		stringsList = new String[strings.size()];
		for(int j=0;j<stringsList.length;j++) {
			stringsList[j] = strings.get(j);
		}	
	}
	
	// this function checks for SET instructions
	private static String checkingSet(String words[]) {
		
		int i=0;
		while(i<words.length) {
			if(words[i].equals("Set")) {
				String type = whatIsThis(words[++i]);
				i++;
				if(type.equals("INT") && !LexicalAnalysis.isInteger(words[i])) return words[i-2]+" "+words[i-1]+" "+words[i]+"* <-- error parsing";
				else if(type.equals("FLOAT") && !LexicalAnalysis.isFloat(words[i])) return words[i-2]+" "+words[i-1]+" "+words[i]+"* <-- error parsing";
				else if(type.equals("STR") && !LexicalAnalysis.isString(words[i])) return words[i-2]+" "+words[i-1]+" "+words[i]+"* <-- error parsing";
			}
			i++;
		}
		return "";
	}
	
	
	// this functions returns the type of the IDENT given
	private static String whatIsThis(String input) {
		for(int i=0;i<intsList.length;i++) {
			if(intsList[i].equals(input)) return "INT";
		}
		for(int i=0;i<floatsList.length;i++) {
			if(floatsList[i].equals(input)) return "FLOAT";
		}
		for(int i=0;i<stringsList.length;i++) {
			if(stringsList[i].equals(input)) return "STR";
		}
		return "";
	}
	
	// this function checks if all variables are decalared
	private static String isAllVariablesDeclared(String words[],String tokens[]) {
		int i=0;
		while(i<tokens.length) {
			if(tokens[i].equals("IDENT") && !isDeclared(words[i])) return words[i]+"* is undeclared";
			i++;
		}
		return "";
	}
	
	// this function checks if the given ident is declared
	private static boolean isDeclared(String input) {
		
		for(int i=0;i<intsList.length;i++) if(intsList[i].equals(input)) return true;
		for(int i=0;i<floatsList.length;i++) if(floatsList[i].equals(input)) return true;
		for(int i=0;i<stringsList.length;i++) if(stringsList[i].equals(input)) return true;
		
		
		return false;
	}
	
	// Convert to java code
	private static String toJava(String words[]) {
		String output = "public class test {\npublic static void main(String args[]) {\n";
		int i=0;
		while(i<words.length) {
			if(words[i].equals("Snl_Int")) {
				output += "int ";
				i++;
				while(!words[i].equals("%.")) {
					output += words[i];
					i++;
				}
				output += ";\n";
			}
			else if(words[i].equals("Snl_Real")) {
				output += "float ";
				i++;
				while(!words[i].equals("%.")) {
					output += words[i];
					i++;
				}
				output += ";\n";
			}
			else if(words[i].equals("Snl_Str")) {
				output += "String ";
				i++;
				while(!words[i].equals("%.")) {
					output += words[i];
					i++;
				}
				output += ";\n";
			}
			else if(words[i].equals("Set")) {
				output += words[++i] + " = " + words[++i] + ";\n";
			}
			else if(words[i].equals("If")) {
				output += "if("+words[i+=2]+words[++i]+words[++i]+") ";
			}
			else if(words[i].equals("Start")) output+="{\n";
			else if(words[i].equals("Finish")) output+="}\n";
			else if(words[i].equals("Else")) output+="else ";
			else if(words[i].equals("Get")) output+=words[++i]+" = "+words[i+=2]+";\n";
			else if(words[i].equals("Snl_Put")) output+="System.out.print("+words[++i]+"};\n";
			
			
			i++;
		}
		
		output+="\n\n}\n}";
		
		return output;
	}
    
}
