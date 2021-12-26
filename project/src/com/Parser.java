package com;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {
    private static String filePath;

    public Parser(String fileName){
        filePath = fileName;
    }

    public static ArrayList<Building> parseProductCsv() throws IOException {


        var products = new ArrayList<Building>();
        var fileLines = Files.readAllLines(Paths.get(filePath), Charset.forName("Windows-1251"));
        for (String fileLine : fileLines.subList(1, fileLines.size())) {
            var splitedText = getCorrectSplitedText(fileLine.split(","));
            if(splitedText.size() != 9)
                System.out.println(splitedText.size() + splitedText.toString() + fileLine);
            Building product = new Building(splitedText);
            products.add(product);
        }
        return products;
    }

    private static ArrayList<String> getCorrectSplitedText(String[] splitedText){
        var newSplitedText = new ArrayList<String>();
        var textToCombine = "";
        var quoteСounter = 0;
        for(var i = 0; i < splitedText.length; i++) {
            if (splitedText[i].contains("\"")) {
                textToCombine += splitedText[i] + ",";
                quoteСounter++;
            }
            else if(quoteСounter == 1)
                textToCombine += splitedText[i] + ", ";
            else
                newSplitedText.add(splitedText[i]);
            if(quoteСounter == 2){
                newSplitedText.add(textToCombine.substring(1, textToCombine.length() - 2));
                textToCombine = "";
                quoteСounter = 0;
            }
        }
        if(newSplitedText.size() != 9)
            newSplitedText.add("");
        return  newSplitedText;
    }
}
