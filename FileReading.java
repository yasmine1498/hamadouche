import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class FileReading {
	
	private String sourceCode;
	private String[] linesCode;
	private String[] wordsCode;
	private String[] tokens;
	private int errorCounter;
	
	public FileReading(String path) {
		
		this.sourceCode=readFile(path);
		linesCode = _splitLines(sourceCode);
		wordsCode = _splitWords(linesCode);
		
	}
	
	//this functions reads the snail file given	--WORKS PERFECTLY--
	private String readFile(String path) {
		String tmp = "";
		try {
			tmp = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	//this function splits the text into a String array of lines & deletes comments	--WORKS PERFECTLY
	private String[] _splitLines(String input) {
		
		String line = "";
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i=0;i<input.length();i++) {
			Character c = input.charAt(i);
			
			//----------------------------------------------------------
			if(c.equals('%')) {
				Character c1 = input.charAt(i+2);
				if(c1.equals('.')) {
					int k=i+3;
					do {							/* This condition & loop ignores comments */
						c1 = input.charAt(k);
						k++;
					} while(!c1.equals('\n') && !c.equals('\r'));
					i=k; // this is the actual ignoring because it's here where the index is moved forward
					c = input.charAt(i);
				}
			}
			//----------------------------------------------------------
			if(!c.equals('\n') && !c.equals('\r')) {
				line+=c;
			}
			else if(!line.equals("\n") && !line.equals("\r") && line.length()!=0){
				result.add(line);
				line="";
			}
		}
		result.add(line);
		line="";
		
		String output[] = new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
		}
		
		return output;
	}

	// This function takes a String array of source code lines and return a String array of words
	// cases handled : logical/arithmetic operators, commas, strings							
	// -- WORKS PERFECTLY --	-- BUT IT LOOKS HIDEOUS, YOU DON'T WANNA LOOK AT IT! --	-- TO GET BACK TO --
	private String[] _splitWords(String input[]){
		
		ArrayList<String> result = new ArrayList<String>();
		String temp = "";
		String arithLogOps = "+-*/<>!=,"; //for a more pleasant condition
		
		
		for(int i=0;i<input.length;i++) {
			for(int j=0;j<input[i].length();j++) {
				char c = input[i].charAt(j);
				if(!Character.isWhitespace(c) && c!=',' && !arithLogOps.contains(String.valueOf(c)) && c!='"') {
					temp+=c;
				}
				else if(c=='"' || arithLogOps.contains(String.valueOf(c))) {
					if(!temp.equals("")) {
						result.add(temp);
						temp="";
					}
					if(arithLogOps.contains(String.valueOf(c))) {
						result.add(String.valueOf(c));
					}
					else if(c=='"'){
						do {
							temp+=c;
							j++;
							c = input[i].charAt(j);
						} while(c != '"' && j<input[i].length());
						temp+=c;
						result.add(temp);
						temp="";
					}
				}
				else if(temp.length()!=0) {
					result.add(temp);
					temp="";
				}
			}
			result.add(temp);
			temp="";
		}
		
		String output[] = new String[result.size()];
		for(int i=0;i<output.length;i++) {
			output[i]=result.get(i);
		}
		return output;		
	}
	
	// This function format the result of the lexical analysis and returns it as a simple String
	public String lexicalAnalysisFunction() {
		tokens = LexicalAnalysis.analyse(wordsCode);
		String output = "";
			for(int i=0;i<wordsCode.length;i++) {
				if(tokens[i] != "VIR") {
					String comment;
					switch(tokens[i]) {
						case "START":
							comment = "Mot réservé pour le debut du programme";
							break;
						case "DFLOAT":
							comment = "Déclaration d'un réel";
							break;
						case "DINT":
							comment = "Déclaration d'un entier";
							break;
						case "IDENT":
							comment = "Identificateur";
							break;
						case "OPBR":
							comment = "croché ouvrant";
							break;
						case "CLBR":
							comment = "croché fermant";
							break;
						case "SET":
							comment = "Mot réservé pour affectation d'une valeur";
							break;
						case "FININ":
							comment = "Fin d'une instruction";
							break;
						case "AROP":
							comment = "Opérateur arithimetique";
							break;
						case "LOOP":
							comment = "Opérateur logique";
							break;
						case "PUT":
							comment = "Mot réserve pour l'affichage d'un string";
							break;
						case "CLOSE":
							comment = "Mot réservé pour la fin du programme";
							break;
						case "GET":
							comment = "Mot réservé pour la transmission de valeur";
							break;
						case "FROM":
							comment = "Mot réservé pour la spécification de source";
							break;
						case "INT":
							comment = "Un entier";
							break;
						case "FLOAT":
							comment = "Un Réel";
							break;
						case "STR":
							comment = "Un String";
							break;
						case "IF":
							comment = "Mot réservé pour tester";
							break;
						case "ELSE":
							comment = "Mot réservé pour la condition alternative";
							break;
						case "DSTR":
							comment = "Mot réservé pour la déclaration d'un String";
							break;
						case "COPAR":
							comment = "Mot réservé pour une condition";
							break;
						default:
							comment = "";
					}		
					output += wordsCode[i]+"   : "+comment+"\n";
				}
		}
		return output;
	}
	
	// This function format the result of the syntax analysis and returns it as a Simple String
	public String syntaxAnalysisFunction() {
		String descriptions[] = SyntaxAnalysis.analyse(tokens);
		String output = "";
		
		errorCounter=0;
		
		for(int i=0;i<descriptions.length;i++) {
			if(descriptions[i].equals("non reconuue")) errorCounter++;
			output += String.valueOf(i+1)+". "+descriptions[i]+"\n";
		}
		
		return output+"\n\n\nNombre d'erreur recontré : "+errorCounter;
	}
	
	// this function is for determining whether the semantic button should be enabled or not
	public int getErrorCount() {
		return errorCounter;
	}
	
	// this function format the result of the semantic analysis and returns it as a String
	public String SemanticAnalysisFunction() {
		String output = SemanticAnalysis.analyse(wordsCode, tokens);
		return output;
	}
	
}
