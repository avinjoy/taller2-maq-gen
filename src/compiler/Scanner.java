package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Scanner {

	private List<String> tokens;

	public List<String> getTokens() {
		return this.tokens;
	}

	public Scanner(String input) { 
		this.tokens = new ArrayList<String>();
		this.Scan(input); 
	}	
	
	public void Scan(String input){
		StringTokenizer token = new StringTokenizer(input, "\n");
		String instr = "";
    	while (token.hasMoreTokens()){
    		instr = token.nextToken();		
			this.tokens.add(instr);
    	}
	}
}
