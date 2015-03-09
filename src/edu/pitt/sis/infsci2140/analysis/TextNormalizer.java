/**
 * @author Hong Zhang
 */
package edu.pitt.sis.infsci2140.analysis;

/**
 * This is for INFSCI 2140 of 2015
 */
public class TextNormalizer {
	
	// YOU MUST IMPLEMENT THIS METHOD
	public static char[] normalize( char[] chars ) {
		// return the normalized version of the word characters (replacing all uppercase characters into the corresponding lowercase characters)
		for (int i = 0; i < chars.length; i++) 
			chars[i] = Character.toLowerCase(chars[i]); // convert each letter to lower case
		return chars;
	}
}
