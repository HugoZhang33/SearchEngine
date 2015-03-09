package edu.pitt.sis.infsci2140.search;

import java.io.IOException;
import java.util.List;

import edu.pitt.sis.infsci2140.index.MyIndexReader;

public class MyRetrievalModel {
	
	protected MyIndexReader ixreader;
	
	public MyRetrievalModel() {
		// you should implement this method
	}
	
	public MyRetrievalModel setIndex( MyIndexReader ixreader ) {
		this.ixreader = ixreader;
		return this;
	}
	
	/**
	 * Search for the topic information. 
	 * The returned results should be ranked by the score (from the most relevant to the least).
	 * max_return specifies the maximum number of results to be returned.
	 * 
	 * @param topic The topic information to be searched for.
	 * @param max_return The maximum number of returned document
	 * @return
	 */
	public List<SearchResult> search( Topic topic, int max_return ) throws IOException {
		// you should implement this method
		return null;
	}
	
}
