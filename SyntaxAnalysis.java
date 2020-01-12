import java.util.ArrayList;

public final class SyntaxAnalysis {
    private SyntaxAnalysis() {
		// Just to make that there won't be any objects created from this
	}
	
	// This function takes a String array of tokens and returns a String array of each instruction's description
	public static String[] analyse(String input[]) {
		
		if(isSnail(input)) {
			ArrayList<String> outputList = new ArrayList<String>();
			
			int i[] = {0};
			while(i[0]<input.length-2) {
				i[0]++;
				String tmp[] = _splitInstruction(input,i);
				outputList.add(analyseInstruction(tmp));
			}
			
			String output[] = new String[outputList.size()];
			for(int j=0;j<output.length;j++) {
				output[j] = outputList.get(j);
			}
			
			return output;
		}
		else {
			String error[] = {"No snail code"};
			return error;
		}
	}
	
	// This functions checks whether the file given is a Snail Code by looking for at the first word	--WORKS PERFECTLY--
	private static boolean isSnail(String input[]) {
		return input[0].equals("START") && input[input.length-1].equals("CLOSE");
	}
	
	// This function takes the tokens array code and returns ONE instruction that starts from the index given	--WORKS PERFECTLY--
	private static String[] _splitInstruction(String input[],int i[]) {
		
		ArrayList<String> instructions = new ArrayList<String>();
		
		while(i[0]<input.length && !input[i[0]].equals("FININ")) {
			instructions.add(input[i[0]]);
			i[0]++;
			if(input[i[0]].equals("OPBR")) {
				while(!input[i[0]].equals("CLBR") && i[0]<input.length-1) {
					instructions.add(input[i[0]]);
					i[0]++;
				}
				break;
			}
		}
		instructions.add(input[i[0]]);
		
		String output[] = new String[instructions.size()];
		for(int j=0;j<output.length;j++) {
			output[j] = instructions.get(j);
		}
		return output;
	}
	
	// These three functions checks whether the given instruction is a declaration of any type (DINT/DFLOAT/DSTR)	--WORKS PERFECTLY--
	private static boolean isDeclarationINT(String input[]) {
		if(input[0].equals("DINT")) {
			int i=0;
			while(i<input.length-2) {
				if(!input[++i].equals("IDENT")) return false;
				if(input[++i].equals("FININ")) return true;
				if(!input[i].equals("VIR")) return false;
			}
			return true;
		}
		else return false;
	}
	
	private static boolean isDeclarationFLOAT(String input[]) {
		if(input[0].equals("DFLOAT")) {
			int i=0;
			while(i<input.length-2) {
				if(!input[++i].equals("IDENT")) return false;
				if(input[++i].equals("FININ")) return true;
				if(!input[i].equals("VIR")) return false;
			}
			return true;
		}
		else return false;
	}
	
	private static boolean isDeclarationSTR(String input[]) {
		if(input[0].equals("DSTR")) {
			int i=0;
			while(i<input.length-2) {
				if(!input[++i].equals("IDENT")) return false;
				if(input[++i].equals("FININ")) return true;
				if(!input[i].equals("VIR")) return false;
			}
			return true;
		}
		else return false;
	}
	
	// This function checks whether the given instruction is an "affectation" of type (SET)		--WORKS PERFECLY--
	private static boolean isSet(String input[]) {
		return (input[0].equals("SET") && input[1].equals("IDENT") && (input[2].equals("INT") || input[2].equals("FLOAT") || input[2].equals("STR")) && input[3].equals("FININ"));
	}
	
	// This function checks whether the given instruction is a display of type (PUT)	--WORKS PERFECTLY--
	private static boolean isPut(String input[]) {
		return (input[0].equals("PUT") && (input[1].equals("IDENT") || input[1].equals("INT") || input[1].equals("FLOAT") || input[1].equals("STR")) && input[2].equals("FININ"));
	}
	
	// This function checks whether the given instruction is a value transmission of type (GET)		--WORKS PERFECTLY--
	private static boolean isGet(String input[]) {
		return (input[0].equals("GET") && input[1].equals("IDENT") && input[2].equals("FROM") && input[3].equals("IDENT") && input[4].equals("FININ"));
	}
	
	// This function checks whether the given instruction is a condition of type (IF)		--WORKS PERFECTLY--
	private static String isIFCondition(String input[]) {
		
		String output = "";
		
		if(input[0].equals("IF")) {
			if(input[1].equals("COPAR") && input[2].equals("IDENT") && input[3].equals("LOOP") && input[4].equals("IDENT") && input[5].equals("COPAR")) {
				
				output+="IF Condition";
				
				if(input[6].equals("OPBR") && input[input.length-1].equals("CLBR")) {
					//separating the instructions (multiple) within the condition from the condition structure
					int i=7;
					String all_Instructions_Within_Brackets[] = new String[input.length-7];	/*this String variable will hold ALL the instructions inside the condition*/
					while(i<input.length-1) {
						all_Instructions_Within_Brackets[i-7] = input[i];
						i++;
					}
					
					//splitting the instructions and testing them one at a time while adding the result to output
					int k[] = {0};
					while(k[0]<all_Instructions_Within_Brackets.length-6) {
						String one_Fecthed_Instruction[] = _splitInstruction(all_Instructions_Within_Brackets,k);
						output += " - "+analyseInstruction(one_Fecthed_Instruction);
					}
					return output;
				}
				else {
					//separating the ONE instruction within the condition
					int i=6;
					String one_instruction[] = new String[input.length-6];
					while(i<input.length) {
						one_instruction[i-6] = input[i];
						i++;
					}
					
					// no need for splitting here because supposedly there is only one instruction inside the condition
					output += " - "+analyseInstruction(one_instruction);
					return output;
				}
			}
			return "error";
		}
		else return "error";		
	}
	
	// This function checks whether the given instruction is a condition of type (ELSE)		--WORKS PERFECTLY--
	private static String isELSECondition(String input[]) {
		
		String output = "";
		
		if(input[0].equals("ELSE")) {
			
			output = "ELSE condition";
			
			if(input[1].equals("OPBR") && input[input.length-1].equals("CLBR")) {
				// separate the instructions (multiple) within the brackets
				int i=2;
				String all_Instructions_Within_Brackets[] = new String[input.length-2];	/*this String variable will hold ALL the instructions inside the condition*/
				while(i<input.length) {
					all_Instructions_Within_Brackets[i-2] = input[i];
					i++;
				}
				
				//splitting the instructions and testing them one at a time while adding the result to output
				int k[] = {-1};
				while(k[0]<all_Instructions_Within_Brackets.length-5) {
					k[0]++;
					String one_Fecthed_Instruction[] = _splitInstruction(all_Instructions_Within_Brackets,k);
					output += " - "+analyseInstruction(one_Fecthed_Instruction);
				}	
			}
			else {
				//separating the ONE instruction within the condition
				int i=2;
				String one_instruction[] = new String[input.length-2];
				while(i<input.length) {
					one_instruction[i-2] = input[i];
					i++;
				}
				
				// no need for splitting here because supposedly there is only one instruction inside the condition
				output += " - "+analyseInstruction(one_instruction);
			}
			
			
			return output;
		}
		else return "error";
	}
	
	// This function analyzes the instruction given and		-WORKS PERFECTLY--
	private static String analyseInstruction(String input[]) {
		
		String cond1Result = isIFCondition(input);
		String cond2Result = isELSECondition(input);
		
		if(!cond1Result.equals("error")) return cond1Result;
		else if(!cond2Result.equals("error")) return cond2Result;
		else if(isDeclarationINT(input)) return "Declaration d'un/plusieur entier(s)";
		else if(isDeclarationFLOAT(input)) return "Declaration d'un/plusieur réel";
		else if(isDeclarationSTR(input)) return "Declaration d'une/plusier chaine de caractère(s)";
		else if(isSet(input)) return "une affectation";
		else if(isPut(input)) return "affichage d'un message à l'écran";
		else if(isGet(input)) return "transmission de valeur entre variable";
		else return "non reconuue";
	}
	
    
}
