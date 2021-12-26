package com;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class Painter extends JFrame {

    public void drawGraph(TreeMap<String, String> values){
        var series = new XYSeries("");
        for(Map.Entry<String, String> entry: values.entrySet()){
            series.add(getCorrectStageInt(entry.getKey()), Integer.parseInt(entry.getValue()));
        }
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        var chart = ChartFactory.createXYLineChart(
                "BuildingsStage",
                "Stage",
                "Count",
                dataset
        );

        var chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1000, 600));
        setContentPane(chartPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private int getCorrectStageInt(String str){
        try {
            var newInt = Integer.parseInt(str.substring(0,2));
            return newInt;
        } catch(NumberFormatException e){
            return Integer.parseInt(str.substring(0,1));
        }
    }
}
