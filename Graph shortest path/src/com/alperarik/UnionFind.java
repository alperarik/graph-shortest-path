package com.alperarik;

import java.util.HashMap;
import java.util.Map;

/**
 * Union operation for Kruskal Algorithm
 * @author Alper ARIK
 */
public final class UnionFind {

    public UnionFind() {

    }

    private static final class Link {
        public City parent;     //parent node
        public int rank = 0;    //rank of node

        Link(City parent) {
            this.parent = parent;
        }
    }

    //Stores elements
    private final Map<City, Link> elems = new HashMap<City, Link>();

    public boolean add(City city) {

        if (city == null) {
            System.out.println("Null City has been added");
            return false;
        }

        //If city has already added than return false
        if (elems.containsKey(city)) {
            return false;
        }

        //Add city and link 
        elems.put(city, new Link(city));
        return true;
    }

    public City find(City city) {
        //Check whether the city exists
        if (!elems.containsKey(city)) {
            System.out.println(city + " is not an element.");
            return null;
        }

        //Recursively search the structure and return the result
        return recFind(city);
    }

    private City recFind(City city) {
        //Get link info on this object
        Link info = elems.get(city);

        // If the element is its own parent
        if (info.parent.equals(city)) {
            return city;
        }
        //Look up for parent
        info.parent = recFind(info.parent);
        return info.parent;
    }

    public void union(City one, City two) {
        // Get the link info for the parents
        Link oneLink = elems.get(find(one));
        Link twoLink = elems.get(find(two));

        // If these are equal then union process has done
        if (oneLink == twoLink) {
            return;
        }

        //Linking by rank
        if (oneLink.rank > twoLink.rank) {
            twoLink.parent = oneLink.parent;
        } else if (oneLink.rank < twoLink.rank) {
            oneLink.parent = twoLink.parent;
        } else {
            // Arbitrarily wire one to be the parent of two.
            twoLink.parent = oneLink.parent;
            oneLink.rank++;
        }
    }
}
