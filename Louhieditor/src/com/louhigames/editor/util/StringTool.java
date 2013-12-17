package com.louhigames.editor.util;

public class StringTool {

	public static int occurrences(String str, char c) {
		int occurrences = 0;
		
		for (int i = 0; i < str.length(); i++) {
			
			char ic = str.charAt(i);
			
			if (ic == c) {
				occurrences++;
			}
			
		}
		
		return occurrences;
	}
	
}
