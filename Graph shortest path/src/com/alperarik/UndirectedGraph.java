package com.alperarik;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Alper ARIK
 */
public final class UndirectedGraph implements Iterable{

    //Graph which stores nodes and edges
    public final Map<City, Map<City, Double>> mGraph = new HashMap<City, Map<City, Double>>();

    public boolean addNode(City node) {
        //If city has already exist than return
        if (mGraph.containsKey(node))
            return false;

        //Add City to graph with empty edge
        mGraph.put(node, new HashMap<City, Double>());
        return true;
    }

    public void addEdge(City one, City two, double distance) {
        //Check both cities exist
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two)){
            System.out.println("Both nodes must be in the graph.");
            return;
        }
           
        //Add the edge in both directions
        mGraph.get(one).put(two, distance);
        mGraph.get(two).put(one, distance);
    }

    public void removeEdge(City one, City two) {
        //Check both cities exist
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two)){
            System.out.println("Both nodes must be in the graph.");
            return;
        }

        // Remove edges from both adjacency lists
        mGraph.get(one).remove(two);
        mGraph.get(two).remove(one);
    }

    public double edgeCost(City one, City two) {
        //Check both cities exist
        if (!mGraph.containsKey(one) || !mGraph.containsKey(two)){
            System.out.println("Both nodes must be in the graph.");
            return -1.0;
        }
        
        //Look up the edge
        Double result = mGraph.get(one).get(two);

        if (result == null){
            System.out.println("Edge does not exist in the graph.");
            return -1.0;
        }

        return result;
    }

    public Map<City, Double> edgesFrom(City node) {
        // Check node exists
        Map<City, Double> link = mGraph.get(node);
        if (link == null){
            System.out.println("Source node does not exist.");
            return null;
        }
            
        return Collections.unmodifiableMap(link);
    }

    public boolean containsNode(City node) {
        return mGraph.containsKey(node);
    }

    public Iterator iterator() {
        return mGraph.keySet().iterator();
    }

    public int size() {
        return mGraph.size();
    }

    public boolean isEmpty() {
        return mGraph.isEmpty();
    }
}