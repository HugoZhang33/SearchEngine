/**
 * @author hongzhang
 */
package edu.pitt.sis.infsci2140.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class for reading your index.
 * 
 * This is for INFSCI 2140 of 2015
 */
public class MyIndexReader {
	
	protected File dir;
	
	public MyIndexReader( File dir ) throws IOException {
		this.dir = dir;
	}
	
	public MyIndexReader( String path_dir ) throws IOException {
		this( new File(path_dir) );
	}
	
	/**
	 * Get the (non-negative) integer docid for the requested docno.
	 * If -1 returned, it indicates the requested docno does not exist in the index.
	 * 
	 * @param docno
	 * @return
	 */
	public int getDocid( String docno ) {
		// you should implement this method.
		
		// corner case check
		if ( docno == null || docno.equals("") ) return -1;
		
		BufferedReader reader = null;
		int docId = -1;
		
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/DocNo.txt"), "UTF-8" ) );
			String line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split(":"); // "docd, docNo"
				if ( docno.equals(map[1]) ) {
					docId = Integer.parseInt(map[0]); // find docNO, return its docId
					break;
				}
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docId;
	}
	
	/**
	 * Retrive the docno for the integer docid.
	 * 
	 * @param docid
	 * @return
	 */
	public String getDocno( int docid ) {
		// you should implement this method.
		
		// corner case check
		if ( docid < 0 ) return null;
		
		BufferedReader reader = null;
		String docNo = null;
		
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/DocNo.txt"), "UTF-8" ) );
			String line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split(":"); // "docd, docNo"
				if ( docid == Integer.parseInt(map[0]) ) {
					docNo = map[1]; // find docNO, return its docId
					break;
				}
				line = reader.readLine();
			}
			
			reader.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docNo;
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] getPostingList( String token ) throws IOException {
		// you should implement this method.
		
		// corner case check
		if ( token == null || token.equals("")) return null;
		
		BufferedReader reader = null;
		int[][] postingList = null;
		int pointer = -1;
		
		try {
			// find pointer from dictionary file
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/Dictionary.txt"), "UTF-8" ) );
			String line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split(":"); // "pointer, term, df, cf"
				if ( token.equals(map[1]) ) {
					pointer = Integer.parseInt(map[0]); // find term, return its pointer
					postingList = new int[ Integer.parseInt(map[2]) ][2]; // initialize int[][]
					break;
				}
				line = reader.readLine();
			}
			reader.close();
			
			if ( pointer == -1 ) return null; // given token dose not in dictionary
			
			// find posting list from posting file
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/Posting.txt"), "UTF-8" ) );
			line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split("-"); // "pointer- docid, tf: docid, tf: ......"
				if ( pointer == Integer.parseInt(map[0]) ) {
					String[] tuples =  map[1].split(":"); // find pointer, return its posting list
					
					// construct posting list to int[][]
					for ( int i = 0; i < tuples.length; i++ ) {
						String[] tuple = tuples[i].split("="); // "docid, tf"
						postingList[i][0] = Integer.parseInt(tuple[0]); // docid
						postingList[i][1] = Integer.parseInt(tuple[1]); // tf
					}
					break;
				}
				line = reader.readLine();
			}
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return postingList;
	}
	
	/**
	 * Return the number of documents that contains the token.
	 * 
	 * @param token
	 * @return
	 */
	public int DocFreq( String token ) throws IOException {
		// you should implement this method.
		
		// corner case check
		if ( token == null || token.equals("")) return 0;
		
		BufferedReader reader = null;
		int df = 0;
		
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/Dictionary.txt"), "UTF-8" ) );
			String line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split(":"); // "pointer, term, df, cf"
				if ( token.equals(map[1]) ) {
					df = Integer.parseInt(map[2]); // find term, return its df
					break;
				}
				line = reader.readLine();
			}
			
			reader.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return df;
	}
	
	/**
	 * Return the total number of times the token appears in the collection.
	 * 
	 * @param token
	 * @return
	 */
	public long CollectionFreq( String token ) throws IOException {
		// you should implement this method.
		
		// corner case check
		if ( token == null || token.equals("")) return 0;
		
		BufferedReader reader = null;
		long cf = 0;
		
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream(dir.getAbsolutePath()+"/Dictionary.txt"), "UTF-8" ) );
			String line = reader.readLine();
			while ( line != null ) {
				String[] map = line.split(":"); // "pointer, term, df, cf"
				if ( token.equals(map[1]) ) {
					cf = Integer.parseInt(map[3]); // find term, return its cf
					break;
				}
				line = reader.readLine();
			}
			
			reader.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cf;
	}
	
	public void close() throws IOException {
		// you should implement this method when necessary
	}
}
