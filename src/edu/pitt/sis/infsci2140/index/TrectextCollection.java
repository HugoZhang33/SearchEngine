/**
 * @author Hong Zhang
 */
package edu.pitt.sis.infsci2140.index;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * This is for INFSCI 2140 in 2015
 *
 */
public class TrectextCollection implements DocumentCollection {
	private ArrayList<Map<String, Object>> collection = null; // store each document
	int cur = 0; // this pointer point to current document in collection
	
	// YOU SHOULD IMPLEMENT THIS METHOD
	public TrectextCollection( FileInputStream instream ) throws IOException {
		// This constructor should take an inputstream of the collection file as the input parameter.
		
		collection = new ArrayList<Map<String, Object>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		boolean findDOCNO = true;
		String line = reader.readLine();
		while (line != null) {
			if (findDOCNO) { // still need to find DcoNo.
				int start = 0;
				if ((start = line.indexOf("<DOCNO>")) != -1) { // find <DOCNO> tag
					int end = line.indexOf('<', start+1);
					String str = line.substring(start+7, end).trim(); // get DocNo from <DOCNO> tag and delete unnecessary withespace 
					map.put("DOCNO", str);
					findDOCNO = false;
				}	
			}
			else { // still need to find content
				if (line.indexOf("<TEXT>") != -1) { // find <TEXT> tag
					line = reader.readLine();
					StringBuilder str = new StringBuilder();
					while (line.indexOf("</TEXT>") == -1) { // not find </TEXT> tag, so append those content to str val
						str.append(line);
						str.append("\n"); // readline method would omit '\n' char, so must add this 
						line = reader.readLine();
					}
					char[] tmp = new char[str.length()];  // copy char[] from str val
					str.getChars(0, str.length(), tmp, 0);
					map.put("CONTENT", tmp);
					collection.add(map);
					map = new HashMap<String, Object>();
					findDOCNO = true; // now it's time to collect DocNo
				}
			}
			
			line = reader.readLine();
		}
		reader.close(); // close resource when reach the end		
	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD
	public Map<String, Object> nextDocument() throws IOException {
		// Read the definition of this method from edu.pitt.sis.infsci2140.index.DocumentCollection interface 
		// and follow the assignment instructions to implement this method.		
		if (cur >= collection.size()) // reach the end of collection
			return null;
		else {
			Map<String, Object> document = collection.get(cur);
			cur++;
			return document;
		}
	}
}
