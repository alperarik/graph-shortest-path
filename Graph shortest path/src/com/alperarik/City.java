package com.alperarik;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alper ARIK
 */
public class City {
    private int x; //x coordinate
    private int y; //y coordinate
    //Scores for A* algorithm
    private double f_scores;
    private double g_scores;
    private double h_scores;
    
    private Random r = new Random();//Creating random object
   
    public City(int distance){
        this.x = r.nextInt(main.X_SIZE); //coordinates are generated randomly
        this.y = r.nextInt(main.Y_SIZE);
    }
    //Creates Cities
    public static ArrayList<City> getCities(int size){
        ArrayList<City> tmp = new ArrayList(size);
        for(int i = 0; i < size; i++){
            tmp.add(new City(i));
        }
        return tmp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public double getF_scores() {
        return f_scores;
    }

    public void setF_scores(double f_scores) {
        this.f_scores = f_scores;
    }

    public double getG_scores() {
        return g_scores;
    }

    public void setG_scores(double g_scores) {
        this.g_scores = g_scores;
    }

    public double getH_scores() {
        return h_scores;
    }

    public void setH_scores(double h_scores) {
        this.h_scores = h_scores;
    }
}
