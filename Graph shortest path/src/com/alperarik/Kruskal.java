package com.alperarik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Alper ARIK
 */
public final class Kruskal {

    public static UndirectedGraph mst(UndirectedGraph graph) {
        // Build up the graph that stores result
        UndirectedGraph result = new UndirectedGraph();
        //Check graph size
        if (graph.size() <= 1)
            return result;

        List<Edge> edges = getEdges(graph);

        //Sort the edges in ascending order
        Collections.sort(edges);

        //Prepare union finding
        UnionFind unionFind = new UnionFind();
        for (Map.Entry<City, Map<City, Double>> entry : graph.mGraph.entrySet())
            unionFind.add(entry.getKey());

        //Add each node to the resulting graph
        for (Map.Entry<City, Map<City, Double>> entry : graph.mGraph.entrySet())
            result.addNode(entry.getKey());

        //number of edges
        int numEdges = 0;

        for (Edge edge: edges) {
            //If nodes have already connected then continue
            if (unionFind.find(edge.start) == unionFind.find(edge.end))
                continue;

            //If nodes are not connected then add edge
            result.addEdge(edge.start, edge.end, edge.cost);

            //Links nodes
            unionFind.union(edge.start, edge.end);

            //If edge adding process is enough
            if (++numEdges == graph.size()) break;
        }

        return result;
    }

    private static List<Edge> getEdges(UndirectedGraph graph) {
        
        Set<City> used = new HashSet<City>();
        List<Edge> result = new ArrayList<Edge>();
        City node = null;
        
        //Iterate all cities(nodes)
        for (Map.Entry<City, Map<City, Double>> e : graph.mGraph.entrySet()){
            node = e.getKey();

            for (Map.Entry<City, Double> entry : graph.edgesFrom(node).entrySet()) {

                if (used.contains(entry.getKey())) continue;

                //Add edge
                result.add(new Edge(node, entry.getKey(), entry.getValue()));
            }
            
            //Visited city
            used.add(node);
        }
    
        return result;
    }

    private static final class Edge implements Comparable<Edge> {
        public final City start, end;  //Endpoints of edge
        public final double cost;      //cost of edge
        public final int tiebreaker;   //Tiebreaker for comparing
        public static int nextTiebreaker = 0;

        public Edge(City start, City end, double cost) {
            this.start = start;
            this.end = end;
            this.cost = cost;
           
            tiebreaker = nextTiebreaker++;
        }

        @Override
        public int compareTo(Edge other) {
            
            if (cost < other.cost) return -1;
            if (cost > other.cost) return +1;
            //If these costs are equal
            return tiebreaker - other.tiebreaker;
        }
    }
}