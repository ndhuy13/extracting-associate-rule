/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;

import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author hienphan
 */
public class AllStockData {
    static HashMap<String, StockData> _allStockData = new HashMap<String, StockData>();
    private AllStockData(Vector<StockData> allStockData) {
        throw new AssertionError();
    }
    public static HashMap<String, StockData> getAllStockData() {
        return _allStockData;
    }
    public static void addStockData(String companyName, List<Float> stock_val, List<Date> date_val) {
//        System.out.println(".." + stock_val.toString());
        StockData sd = new StockData(stock_val, date_val);
        _allStockData.put(companyName, sd);
        
    }
    public static List<Float> getAllStockValue() {
        List<Float> allValue = new ArrayList<>();
        getAllStockData().entrySet().stream().map((entry) -> {
            String key = entry.getKey();
            return entry;
        }).map((entry) -> entry.getValue()).forEachOrdered((value) -> {
            allValue.addAll(value.getStockValue());
        });
        return allValue;
    }
}

class StockData {
    List<Float> _stockValue = new ArrayList<>();
    List<Date> _dateList = new ArrayList<>();
    public StockData(List<Float> stockValue, List<Date> stockDate) {
        for (int i = 0; i < stockValue.size(); i++) {
            this._stockValue.add(stockValue.get(i));
            this._dateList.add(stockDate.get(i));
        }
    }
    public void assignStockValue(List<Float> stock_val, List<Date> stock_date) {
        this._stockValue = stock_val;
        this._dateList = stock_date;
    }
    public void addStockValue(float stockVal, Date stockDate) {
        this._stockValue.add(stockVal);
        this._dateList.add(stockDate);
    }
    public List<Float> getStockValue() {
        return this._stockValue;
    }
    public List<Date> getDateValue() {
        return this._dateList;
    }
}
