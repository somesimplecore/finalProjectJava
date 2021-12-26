package com;

import java.io.IOException;
import java.util.ArrayList;

public class Project {
    public static void main(String[] args) throws IOException {
        var parser = new Parser("База Санкт-Петербурга.csv");
        var buildings = parser.parseProductCsv();

        var db = new DataBase("base_spb.db");

        db.createNewDatabase();
        db.createNewTable();
        db.addBuildings(buildings);

        var firstTaskValues = db.getStagesData();
        var painter = new Painter();
        painter.drawGraph(firstTaskValues);

        var secondTaskValues = db.getAllDataFromRow("SELECT * from building " +
                "WHERE address LIKE \"%Шлиссельбургское шоссе%\" and prefix_code = \"9881\" and appellation = \"Зарегистрированный участок\"");
        printResult(getFullStringList(secondTaskValues));

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        var thirdTaskValues = db.getAllDataFromRow("SELECT * from building " +
                "where year_construction != \"\" and appellation = \"Университет\"");
        thirdTaskValues = GetBuildingsWithStageMoreThan(thirdTaskValues, 5);
        printResult(getFullStringList(thirdTaskValues));
        var middlePrefixCode = getMiddlePrefixCode(thirdTaskValues);
        System.out.println("Средний prefix_code университетов с известным годом постройки и выше 5 этажа: " + middlePrefixCode);
    }


    private static void printResult(ArrayList<String> list){
        for(var value: list)
            System.out.println(value);
    }

    private static ArrayList<String> getFullStringList(ArrayList<ArrayList<String>> lists){
        var newList = new ArrayList<String>();
        for(var list: lists) {
            var str = "";
            for (var i = 0; i < list.size(); i++)
                str += list.get(i) + ", ";
            newList.add(str);
        }
        return newList;
    }

    private static ArrayList<ArrayList<String>> GetBuildingsWithStageMoreThan(ArrayList<ArrayList<String>> lists, int stage){
        var newList = new ArrayList<ArrayList<String>>();
        for(var list: lists){
            try {
                var newInt = Integer.parseInt(list.get(4).substring(0,2));
                if(newInt > stage)
                    newList.add(list);
            } catch(NumberFormatException e){
                var newInt = Integer.parseInt(list.get(4).substring(0,1));
                if(newInt > stage)
                    newList.add(list);
            }
        }
        return newList;
    }

    private static double getMiddlePrefixCode(ArrayList<ArrayList<String>> lists) {
        var sum = 0f;
        for(var list: lists){
            sum += Integer.parseInt(list.get(5));
            }
        return sum /  lists.size();
    }
}
