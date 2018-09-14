package com.alperarik;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JComponent;
/**
 * @author Alper ARIK
 * Responsible for draw components
 */
public class LinesComponent extends JComponent {
    //Defining Line class
    private static class Line {
        //Coordinates and color for Line
        final int x1;
        final int y1;
        final int x2;
        final int y2;
        final Color color;

        public Line(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }
    //defining Oval object
    private static class Oval {
        //Coordinates and color for Oval
        final int x1;
        final int y1;
        final Color color;

        public Oval(int x1, int y1, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.color = color;
        }
    }
    //Stores draw components
    private final LinkedList<Line> lines = new LinkedList<Line>();
    private final LinkedList<Oval> ovals = new LinkedList<Oval>();
    
    //If color of line is not determined so it will be black
    public void addLine(int x1, int x2, int x3, int x4) {
        addLine(x1, x2, x3, x4, Color.black);
    }

    public void addLine(int x1, int x2, int x3, int x4, Color color) {
        lines.add(new Line(x1, x2, x3, x4, color));
        repaint();
    }
    //If color of oval is not determined so it will be black
    public void addOval(int x1, int x2) {
        addOval(x1, x2, Color.black);
    }

    public void addOval(int x1, int x2, Color color) {
        ovals.add(new Oval(x1, x2, color));
        repaint();
    }
    
    public void clear(){
        lines.clear();
        ovals.clear();
    }

    //Draws all prepeared components to JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Line line : lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        //Source and Goal cities are drawing differently from other cities
        int i = 0;
        for (Oval oval : ovals) {
            if (i == main.numS) {
                i++;
                g.setColor(Color.RED);
                g.drawOval(oval.x1, oval.y1, 3, 3);
                g.setColor(Color.GREEN);
                g.drawString("START", oval.x1, oval.y1);
            } else if (i == main.numG) {
                i++;
                g.setColor(Color.RED);
                g.drawOval(oval.x1, oval.y1, 3, 3);
                g.setColor(Color.MAGENTA);
                g.drawString("GOAL", oval.x1, oval.y1);
            } else {
                g.setColor(oval.color);
                g.drawOval(oval.x1, oval.y1, 3, 3);
                String num = String.valueOf(i++);
                g.setColor(Color.BLUE);
                g.drawString(num, oval.x1, oval.y1);
            }
        }

    }
}
