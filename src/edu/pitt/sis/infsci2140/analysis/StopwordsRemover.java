package edu.pitt.sis.infsci2140.analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * This is for INFSCI 2140 of 2015
 */
public class StopwordsRemover {
	private HashSet<String> set = new HashSet<String>(); // same element only can appear once in a set
	
	// YOU MUST IMPLEMENT THIS METHOD
	public StopwordsRemover( FileInputStream instream ) {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		try {
			String stopword = reader.readLine();
			while (stopword != null) { // not reach the end of file
				set.add(stopword); // add stopword to set
				stopword = reader.readLine();
			}
			
			reader.close(); // close resource
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public boolean isStopword( char[] word ) {
		// return true if the input word is a stopword, or false if not
		String str = new String(word);
		return set.contains(str) ? true : false; // a word is a stopword if it is contained in the set 
	}
}
