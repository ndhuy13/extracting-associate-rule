/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;

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
    public static void addStockData(String companyName, List<Float> stock_val) {
        System.out.println(".." + stock_val.toString());
        StockData sd = new StockData(stock_val);
        _allStockData.put(companyName, sd);
    }
}

class StockData {
    List<Float> _stockValue = new ArrayList<Float>();
    public StockData(List<Float> stockValue) {
        for (int i = 0; i < stockValue.size(); i++) {
            this._stockValue.add(stockValue.get(i));
        }
    }
    public void assignStockValue(List<Float> stock_val) {
        this._stockValue = stock_val;
    }
    public void addStockValue(float stockVal) {
        this._stockValue.add(stockVal);
    }
    public List<Float> getStockValue() {
        return this._stockValue;
    }
}
