package com.alperarik;

import static com.alperarik.main.mst;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {

    //Stores how many element is dequeued from Queue
    public static int pollNum = 0;
    public static int maxQueueSize = 0;
    public static PriorityQueue<City> queue;
    
    public static ArrayList<City> AstarSearch() {
        City source = main.SOURCE;
        City goal = main.GOAL;
        //For performance, priority queue is used.
        queue = new PriorityQueue<City>(
                new Comparator<City>() {
            //Priority queue uses this comparator for sorting elements 
            //which are inside in queue(ascending order)
            @Override
            public int compare(City c1, City c2) {
                if (c1.getF_scores() > c2.getF_scores()) {
                    return 1;
                } else if (c1.getF_scores() < c2.getF_scores()) {
                    return -1;
                } else {
                    return 0;
                }
            }

        }
        );
        //Stores path
        Map<City, City> path = new HashMap<City, City>();
        //Stores explored cities
        ArrayList<City> explored = new ArrayList<City>();

        //set source g_score to 0
        source.setG_scores(0.0);
        //Add source city to queue
        queue.add(source);

        boolean found = false;
        //loop until goal is found or queue is empty
        while ((!queue.isEmpty() && !found)) {

            if (queue.size() > maxQueueSize) {
                maxQueueSize = queue.size();
            }
            //the City in having the lowest f_score value
            City current = queue.poll();
            pollNum++;
            //Add city to explored list
            explored.add(current);
            if ((current.getX() == goal.getX()) && (current.getY() == goal.getY())) {
                found = true;
                break;
            }
            //adjacencies of current City
            Map<City, Double> adjacencies = mst.edgesFrom(current);
            //Calculate scores
            for (Map.Entry<City, Double> e : adjacencies.entrySet()) {
                if (!mst.containsNode(e.getKey())) {
                    continue;
                }
                City child = e.getKey();
                double cost = e.getValue();
                double temp_g_scores = current.getG_scores() + cost;
                double temp_f_scores = temp_g_scores + child.getH_scores();

                //if child City has been evaluated and the newer f_score is higher, skip
                if ((explored.contains(child)) && (temp_f_scores >= child.getF_scores())) {
                    continue;
                } //else if child City is not in queue or newer f_score is lower
                else if ((!queue.contains(child)) || (temp_f_scores < child.getF_scores())) {
                    //Set scores of child
                    child.setG_scores(temp_g_scores);
                    child.setF_scores(temp_f_scores);
                    //If chiled exist in queue than delete it    
                    if (queue.contains(child)) {
                        queue.remove(child);
                    }
                    //Add updated child to queue
                    queue.add(child);
                    path.put(child, current);
                }
                
                if (queue.size() > maxQueueSize) {
                    maxQueueSize = queue.size();
                }
            }

        }
        //return source to goal path
        final ArrayList<City> pathList = new ArrayList<City>();
        pathList.add(goal);
        while (path.containsKey(goal)) {
            goal = path.get(goal);
            pathList.add(goal);
        }
        Collections.reverse(pathList);
        return pathList;

    }

}
