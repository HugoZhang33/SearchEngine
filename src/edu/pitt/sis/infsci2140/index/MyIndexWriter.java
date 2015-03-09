/**
 * @author hongzhang
 */
package edu.pitt.sis.infsci2140.index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.pitt.sis.infsci2140.analysis.TextNormalizer;
import edu.pitt.sis.infsci2140.analysis.TextTokenizer;

/**
 * This is for INFSCI 2140 of 2015
 */
public class MyIndexWriter {
	
	private TreeMap<String, LinkedList<int[]>> treeMap = new TreeMap<String, LinkedList<int[]>>(); // inverted index
	private ArrayList<String> map_DocId = new ArrayList<String>(); // mapping from DocNo to DocId
	private int curDocId = 0; 
	
	protected File dir;
	
	public MyIndexWriter( File dir ) throws IOException {
		this.dir = dir;
	}
	
	public MyIndexWriter( String path_dir ) throws IOException {
		this.dir = new File(path_dir);
		if( !this.dir.exists() ) {
			this.dir.mkdir();
		}
	}
	
	/**
	 * This method build index for each document.
	 * NOTE THAT: in your implementation of the index, you should transform your string docnos into non-negative integer docids !!!
	 * In MyIndexReader, you should be able to request the integer docid for docnos.
	 * 
	 * @param docno Docno
	 * @param tokenizer A tokenizer that iteratively gives out each token in the document.
	 * @throws IOException
	 */
	public void index( String docno, TextTokenizer tokenizer ) throws IOException {
		// you should implement this method to build index for each document
		char[] word = null;
		while ( ( word = tokenizer.nextWord() ) != null ) {
			word = TextNormalizer.normalize(word);
			LinkedList<int[]> postingList = null;
			if ( ( postingList = treeMap.get( new String(word) ) ) == null ) { // dictionary does not contain this term
				postingList = new LinkedList<int[]>();
				int[] tuple = {curDocId, 1};
				postingList.addLast(tuple);
				treeMap.put( new String(word), postingList ); // add a new term into dictionary
			}
			else { // dictionary contains this term
				int[] curTuple = postingList.getLast();
				if ( curTuple[0] == curDocId ) curTuple[1] += 1; // the same doc, increase term frequency
				else {
					int[] tuple = {curDocId, 1};
					postingList.addLast(tuple);
				}
			}
		}
		curDocId++; // increase docId
		map_DocId.add(docno);
	}
	
	/**
	 * Close the index writer, and you should output all the buffered content (if any).
	 * @throws IOException
	 */
	public void close() throws IOException {
		// you should implement this method if necessary
		BufferedWriter dirOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dir.getAbsolutePath()+"/Dictionary.txt"), "UTF-8" ) );
		BufferedWriter postingOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dir.getAbsolutePath()+"/Posting.txt"), "UTF-8" ) );
		BufferedWriter docNoOutput = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dir.getAbsolutePath()+"/DocNo.txt"), "UTF-8" ) );

		// construct dictionary file and posting file
		int pointer = 0;
		Iterator<Entry<String, LinkedList<int[]>>> iterator = treeMap.entrySet().iterator();
		while ( iterator.hasNext() ) {
			Entry<String, LinkedList<int[]>> entry = ( Entry<String, LinkedList<int[]>> ) iterator.next();
			
			// write posting list
			postingOutput.write( String.valueOf(pointer) );
			postingOutput.write('-');
			int[] tuple = null;
			int cf = 0; // collection frequency
			int df = entry.getValue().size(); // document frequency
			while ( ( tuple = entry.getValue().poll() ) != null ) {
				cf += tuple[1]; // calculate collection frequency
				
				postingOutput.write( String.valueOf(tuple[0]) ); // docId
				postingOutput.write('=');
				postingOutput.write( String.valueOf(tuple[1]) ); // term frequency
				postingOutput.write(':');
			}
			postingOutput.write('\n');
			
			// write term
			dirOutput.write( String.valueOf(pointer) );
			dirOutput.write(':');
			dirOutput.write( entry.getKey() ); // term
			dirOutput.write(':');
			dirOutput.write( String.valueOf(df) ); // doc frequency
			dirOutput.write(':');
			dirOutput.write( String.valueOf(cf) ); // collection frequency
			dirOutput.write('\n');
			
			// increase pointer
			pointer++;
		}
		
		// construct mapping file, mapping DocNo to DocId
		for ( int i = 0; i < map_DocId.size(); i++ ) {
			docNoOutput.write( String.valueOf(i) ); // docId
			docNoOutput.write(':'); 
			docNoOutput.write(map_DocId.get(i)); // docNo
			docNoOutput.write('\n');
		}
		
		dirOutput.close();
		postingOutput.close();
		docNoOutput.close();
	}	
	
	
	
	
}
