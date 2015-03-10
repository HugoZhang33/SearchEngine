package edu.pitt.sis.infsci2140.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
        
        // a min heap
        PriorityQueue<SearchResult> minHeap = new PriorityQueue<SearchResult>( max_return, new SortByScore() );
        
        ArrayList<char[]> query = topic.getTitleInfo();
        
        // get the collection length
        int cLength = 0;
        for ( int i = 0; i < ixreader.numDocs(); i++ ) {
            cLength += ixreader.docLength(i);
        }
        
        // set the parameter u
        double u = 150;
        
        // rank each document and store n heighest documents
        for ( int i = 0; i < ixreader.numDocs(); i++ ) {
            int dLength = ixreader.docLength(i);
            double score = 1;
            for ( char[] term : query ) {
                int df = 0;
                double cf = (double) ixreader.CollectionFreq( new String(term) );
                if ( cf == 0 ) continue; // term in query does not appear in index, omit it
                int[][] postingList = ixreader.getPostingList( new String(term) );
                
                for ( int[] list : postingList ) {
                    if ( list[0] == i ) { // find the corresponding doc
                        df = list[1]; // set the df
                        break;
                    }
                }
                score = score * ( ( df + u*( cf/cLength ) ) / ( dLength + u ) ); // Ditichlet Prior Smoothing
            }
            
            // put the doc into min heap if the score  is higher than the min score in the min heap
            SearchResult result = new SearchResult(i, ixreader.getDocno(i), score);
            if ( minHeap.size() < max_return ) minHeap.add(result);
            else {
                double minScore = minHeap.peek().score();
                if ( result.score() > minScore ) {
                    minHeap.remove();
                    minHeap.add(result);
                }
            }
        }
        
        // convert Heap to list
        LinkedList<SearchResult> resultList = new LinkedList<SearchResult>();
        SearchResult r = minHeap.poll();
        while ( r != null ) {
            resultList.addFirst(r);
            r = minHeap.poll();
        }
        
        // return min heap
        return resultList;
    }
    
    /**
     * comparator for minheap
     * ( docid, score ) tuple
     * sort by score 
     */
    class SortByScore implements Comparator<SearchResult> {
        
        @Override
        public int compare( SearchResult o1, SearchResult o2) {
            // TODO Auto-generated method stub
            if ( o1.score() > o2.score() ) return 1;
            if ( o1.score() < o2.score() ) return -1;
            else                 return 0;
        }
    }
    
}
