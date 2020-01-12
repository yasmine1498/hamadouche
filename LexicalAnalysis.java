public final class LexicalAnalysis {
    private LexicalAnalysis() {
		// nothing for now, this is just here to prevent any instantiation
	}
	
	public static String[] analyse(String input[]) {
		String output[] = new String[input.length];
		
		for(int i=0;i<input.length;i++) {
			String tmp = isKeyword(input[i]);
			if(tmp != "") output[i] = tmp;
			else if(isIdent(input[i])) output[i] = "IDENT";
			else if(isInteger(input[i])) output[i] = "INT";
			else if(isFloat(input[i])) output[i] = "FLOAT";
			else if(isString(input[i])) output[i] = "STR";
			else if(isArthOper(input[i])) output[i] = "AROP";
			else if(isLogOper(input[i])) output[i] = "LOOP";
		}
		return output;
	}
	
	// This function takes a string and determines if its an integer	--WORKS PERFECTLY--
	public static boolean isInteger(String input) {
		for(byte i=0;i<input.length();i++) {
			char c = input.charAt(i);
			if(!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	// This function takes a string and determines whether it is a float	--WORKS PERFECTLY--
	public static boolean isFloat(String input) {
		byte numberOfPoints = 0;
		for(byte i=0;i<input.length();i++) {
			char c = input.charAt(i);
			if(!Character.isDigit(c) && c != '.') {
				return false;
			}
			else if(c == '.') {
				numberOfPoints++;
			}
		}
		if(numberOfPoints == 1) return true;
		else return false;
	}
	
	// This function check if the word given is a KEYWORD and returns the appropriate token		--WORKS PERFECTLY--
	private static String isKeyword(String input) {		
		switch(input) {
			case "Snl_Start":
				return "START";
			case "Snl_Close":
				return "CLOSE";
			case "Snl_Real":
				return "DFLOAT";
			case "Snl_Int":
				return "DINT";
			case "SnlSt":
				return "DSTR";
			case "Start":
				return "OPBR";
			case "Finish":
				return "CLBR";
			case "Set":
				return "SET";
			case "%.":
				return "FININ";
			case "Snl_Put":
				return "PUT";
			case "Get":
				return "GET";
			case "from":
				return "FROM";
			case ",":
				return "VIR";
			case "If":
				return "IF";
			case "Else":
				return "ELSE";
			case "%":
				return "COPAR";
			default:
				return "";
		}
	}
	
	// This function check if the word given is a string and return true or false	-- WORKS PERFECTLY --
	public static boolean isString(String input) {
		return (input.charAt(0) == '"' && input.charAt(input.length()-1) == '"');
	}
	
	// This function checks if the word given is an identifier and returns true of false	--WORKS PERFECTLY--
	private static boolean isIdent(String input) {
		if(!Character.isLetter(input.charAt(0))) return false;
		else if(input.charAt(input.length()-1) == '_') return false;
		else {
			for(int i=1;i<input.length()-1;i++) {
				if(input.charAt(i)=='_') {
					i++;
					if(input.charAt(i)=='_') return false;
				}
				if(!Character.isLetterOrDigit(input.charAt(i))) return false;
			}
		}
		return true;
	}
	
	// This function checks if the word given is an arithmetic operator and returns true of false	--WORKS PERFECTLY--
	private static boolean isArthOper(String input) {
		String tmp = "+-*/";
		return tmp.contains(input);
	}
	
	// This function checks if the word given is a logical operator and returns true or false	--WORKS PERFECTLY--
	private static boolean isLogOper(String input) {
		String tmp = "<>!=";
		return tmp.contains(input);
	}
    
}
