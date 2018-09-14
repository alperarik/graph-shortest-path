package com.alperarik;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Alper ARIK
 */
public class main {

    public static final int X_SIZE = 700; //Size of panel
    public static final int Y_SIZE = 600;
    public static final int CITY_SIZE = 100; //Number of City
    public static int numS; //Source city id
    public static int numG; //Goal city id
    public static City SOURCE; //Source city
    public static City GOAL; //Goal city
    public static UndirectedGraph mst; //Minimum spannig tree

    //Swing components
    private static JFrame testFrame;
    private static final LinesComponent comp = new LinesComponent();
    private static TextArea output;
    //Stores cities
    private static ArrayList<City> cities;
    //Random number generating object
    private static final Random r = new Random();

    public static void main(String[] args) {
        //CREATING FRAME
        testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        comp.setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        output = new TextArea("OUTPUT\n");
        testFrame.add(output, BorderLayout.EAST);
        JButton button1 = new JButton("INPUT NEW VALUES");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
                getValuesFromUser();
                applyAStar();
            }
        });
        testFrame.add(button1, BorderLayout.NORTH);
        JButton button2 = new JButton("CLEAR CONSOLE");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setText("");
            }
        });
        testFrame.add(button2, BorderLayout.SOUTH);

        init();
        getValuesFromUser();
        applyAStar();

        testFrame.pack();
        testFrame.setVisible(true);
    }

    /**
     * Inits frame Creates MST and Random paths
     */
    private static void init() {
        //inits values
        comp.clear();
        numS = -1;
        numG = -1;
        AStar.maxQueueSize = 0;
        AStar.pollNum = 0;

        //Creates cities
        cities = City.getCities(CITY_SIZE);
        //Draws cities
        for (City c : cities) {
            comp.addOval(c.getX(), c.getY(), Color.RED);
        }

        //Adds cities to graph as node
        UndirectedGraph u = new UndirectedGraph();
        for (City c : cities) {
            u.addNode(c);
        }

        //CREATE FULLY CONNECTED GRAPH
        for (int i = 0; i < CITY_SIZE; i++) {
            for (int j = i + 1; j < CITY_SIZE; j++) {
                City tmp1 = cities.get(i);
                City tmp2 = cities.get(j);
                u.addEdge(tmp1, tmp2, getDistance(tmp1, tmp2));
            }
        }

        //Apply Kruskal
        output.append("\n\nMST\n--------------------");
        mst = Kruskal.mst(u);

        //CREATE RANDOM PATHS
        //keeps random numbers to avoid from dublicate edges
        HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
        for (int i = 0; i < CITY_SIZE; i++) {
            int num1 = r.nextInt(CITY_SIZE);
            int num2 = r.nextInt(CITY_SIZE);

            if (num1 != num2) {
                if ((hm.containsKey(num1)) && (num2 == hm.get(num1))) {
                    i--;
                } else if ((hm.containsKey(num2)) && (num1 == hm.get(num2))) {
                    i--;
                } else {
                    hm.put(num1, num2);
                    hm.put(num2, num1);

                    City c1 = cities.get(num1);
                    City c2 = cities.get(num2);
                    mst.addEdge(c1, c2, getDistance(c1, c2));
                }
            } else {
                i--;
            }
        }

        //Iterating mst
        Iterator<City> iterator = mst.iterator();
        while (iterator.hasNext()) {
            City c1 = iterator.next();
            Map<City, Double> adjacencies = mst.edgesFrom(c1);
            for (Map.Entry<City, Double> entry : adjacencies.entrySet()) {
                City c2 = entry.getKey();
                comp.addLine(c1.getX(), c1.getY(), c2.getX(), c2.getY(), Color.LIGHT_GRAY);
                int c1Name = cities.indexOf(c1);
                int c2Name = cities.indexOf(c2);

                String tmp = "\n" + c2Name + " to " + c1Name + " distance : " + entry.getValue();
                if (!output.getText().contains(tmp)) {
                    output.append("\n" + c1Name + " to " + c2Name + " distance : " + entry.getValue());
                }
            }
        }

        testFrame.pack();
        testFrame.setVisible(true);
    }

    /**
     * Gets S (Source,Start)and G (Goal) value from user
     */
    private static void getValuesFromUser() {
        String strS = JOptionPane.showInputDialog(null, "Please enter S value", "INPUT S", 1);
        String strG = JOptionPane.showInputDialog(null, "Please enter G value", "INPUT G", 1);
        numS = Integer.parseInt(strS);
        numG = Integer.parseInt(strG);
        //inits S and G city
        SOURCE = cities.get(numS);
        GOAL = cities.get(numG);
    }

    /**
     * Applies A* algorithm on Graph(MST + random paths)
     */
    private static void applyAStar() {
        //Calculate H scores
        for (City c : cities) {
            c.setH_scores(getDistance(c, GOAL));
        }

        //Calculates A* working time
        output.append("\n\nA STAR (A*)\n--------------------");
        long startTime = System.nanoTime();
        ArrayList<City> path = AStar.AstarSearch();
        long endTime = System.nanoTime();
        double elapsedMicroSeconds = (endTime - startTime) / 1000;
        output.append("\nA* process time : " + elapsedMicroSeconds + " micro seconds\n");
        output.append("\nStart : " + numS + " , Goal : " + numG + "\n");

        if (path != null) {
            //Write Path
            output.append("\nPATH : ");
            for (Integer tmp : findCityIndexes(cities, path)) {
                output.append(tmp + "| ");
            }
            output.append("\n");

            //Draws Source to Goal path
            comp.addLine(SOURCE.getX(), SOURCE.getY(), path.get(0).getX(), path.get(0).getY(), Color.RED);
            for (int i = 0; i < path.size() - 1; i++) {
                if (path.get(i).getX() == GOAL.getX() && path.get(i).getY() == GOAL.getY()) {
                    break;
                }
                comp.addLine(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY(), Color.RED);
                output.append("\nFrom : " + findCityIndex(cities, path.get(i)) + " to : " + findCityIndex(cities, path.get(i + 1)));
            }
        } else {
            output.append("\nQUEUE : NULL");
        }

        //Write Path
        output.append("\n\nQUEUE : ");
        ArrayList<City> lastQueueInfo = new ArrayList<>(AStar.queue);
        int tmpCounter = 0;
        for (Integer tmp : findCityIndexes(cities, lastQueueInfo)) {
            if(tmpCounter++ % 10 == 0)
                output.append("\t\n");
            output.append(tmp + "| ");            
        }
        
        //Writes pollNum and MaxQueueSize
        output.append("\n\nPollNum : " + AStar.pollNum);
        output.append("\n\nMax Queue Size : " + AStar.maxQueueSize);

        testFrame.pack();
        testFrame.setVisible(true);

    }

    /**
     * Calculates distance between two city
     */
    private static double getDistance(City c1, City c2) {
        double distance = Math.sqrt((c1.getX() - c2.getX()) * (c1.getX() - c2.getX())
                + (c1.getY() - c2.getY()) * (c1.getY() - c2.getY()));
        int coefficent = r.nextInt(41) + 10;
        distance = distance + distance * coefficent / 100;
        return distance;
    }

    /**
     * Finds indexes of cities in path
     */
    private static ArrayList<Integer> findCityIndexes(ArrayList<City> cities, ArrayList<City> path) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (City tmp1 : path) {
            for (City tmp2 : cities) {
                if ((tmp1.getX() == tmp2.getX()) && (tmp1.getY() == tmp2.getY())) {
                    tmp.add(cities.indexOf(tmp2));
                }
            }
        }
        return tmp;
    }

    /**
     * Finds index of given city
     */
    private static int findCityIndex(ArrayList<City> cities, City c) {
        for (int i = 0; i < cities.size(); i++) {
            City tmp = cities.get(i);
            if ((tmp.getX() == c.getX()) && (tmp.getY() == c.getY())) {
                return i;
            }
        }
        return -1;
    }

}
