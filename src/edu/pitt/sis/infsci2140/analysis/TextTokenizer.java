/**
 * @author Hong Zhang
 */
package edu.pitt.sis.infsci2140.analysis;

import java.util.LinkedList;

/**
 * TextTokenizer can split a sequence of text into individual word tokens.
 * This is for INFSCI 2140 of 2015
 */
public class TextTokenizer {
	private LinkedList<char[]> wordQueue = new LinkedList<char[]>(); // stored each token
	
	// YOU MUST IMPLEMENT THIS METHOD
	public TextTokenizer( char[] texts ) {
		// this constructor will tokenize the input texts (usually it is a char array for a whole document)
		int head = 0; // the head of a word
		int tail = 0; // the tail of a word
		while (head < texts.length && !Character.isLetterOrDigit(texts[head])) { // ignore whitespace or other special char in front
			head++;
			tail++;
		}
		
		for (int i = head; i < texts.length; i++) {			
			if (Character.isLetterOrDigit(texts[i])) tail++; // only care about letter
			else if (texts[i] == ',' && i >0 && Character.isDigit(texts[i-1]) && 
					i < texts.length-1 && Character.isDigit(texts[i+1])) tail++; // in case of number like 3,1818
			else if (texts[i] == '-' && i >0 && Character.isLetter(texts[i-1]) && 
					i < texts.length-1 && Character.isLetter(texts[i+1])) tail++;  // in case of word like i-i
			else if (tail-head > 0){ // ignore whitespace in the end
				char[] tmp = new char[tail-head];
				for (int ii = 0; ii < tail-head; ii++) 
					tmp[ii] = texts[head+ii];
				wordQueue.addLast(tmp); 
				head = tail + 1;
				while (head < texts.length && !Character.isLetterOrDigit(texts[head])) head++; // ignore whitespace or other special char
				tail = head;
				i = head - 1;
			}
		}
	}
	
	// YOU MUST IMPLEMENT THIS METHOD
	public char[] nextWord() {
		// read and return the next word of the document; or return null if it is the end of the document
		return wordQueue.poll(); // return the first word in the wordQueue
	}
}
