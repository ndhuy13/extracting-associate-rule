/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author Phan The Hien, Nguyen Duc Huy
 */
public class RuleMatching {
    private List<StockPricePattern> matchACompany(HashMap<String,Rule> rules, Query query, String companyName, String QueryRuleName) {
        List<StockPricePattern> listStockPricePattern = new ArrayList<>();
        int bodyLen = query._bodyLen;
        int len = query._timeInterval;
        
        HashMap<String, StockData> stockdata = AllStockData.getAllStockData();
        Rule rule = rules.get(QueryRuleName);
        
        if (stockdata.get(companyName) != null) {
            System.out.println("Found company with ID: " + companyName + ": " + stockdata.get(companyName).getStockValue());
        }
        else {
            System.out.println("Not found company " + companyName);
        }
        //
        if (rule != null) {
            for (int i = 0; i < rule._positions.size(); i++) {
                Tuple position = rule._positions.get(i);
                String company = position._companyName;
                if (company != null && company.equals(companyName)) {
                    for (int j = 0; j < position._positions.size(); j++) {
                        int sizeStockValue = stockdata.get(company)._stockValue.size();
                        int beginIndexOfHead = position._positions.get(j); //indexHead -> indexHead + lenRule + 1
                        int endIndexOfHead = Math.min(beginIndexOfHead + rule._lengthOfRule + 1, sizeStockValue);
                        int beginIndexOfBody = Math.min(beginIndexOfHead + rule._lengthOfRule + len + 1, sizeStockValue - 1); //indexBody -> indexBody + bodyLen
                        int endIndexOfBody = Math.min(beginIndexOfBody + bodyLen, sizeStockValue);
                        System.out.println(beginIndexOfHead + " " + endIndexOfHead + ". " + beginIndexOfBody + " " + endIndexOfBody);
                        
                        List<Float> begin_pattern = stockdata.get(company)._stockValue.subList(beginIndexOfHead, endIndexOfHead);
                        List<Float> next_pattern = stockdata.get(company)._stockValue.subList(beginIndexOfBody, endIndexOfBody);
                        List<Date> date_begin_pattern = stockdata.get(company)._dateList.subList(beginIndexOfHead, endIndexOfHead);
                        List<Date> date_next_pattern = stockdata.get(company)._dateList.subList(beginIndexOfBody, endIndexOfBody);

                        listStockPricePattern.add(new StockPricePattern(begin_pattern, next_pattern, date_begin_pattern, date_next_pattern));
                    }
                }
            }
        }
        else {
            System.out.println("Don't have any rule " + "'" + QueryRuleName + "'" + " match Input Query!");
        }
        
        return listStockPricePattern;
    }
    
    public List<StockPricePattern> match(HashMap<String,Rule> rules, Query query, List<String> querySymbol){     // You can add more params if you need
        if (query == null) {
            System.out.println("Query is not valid to create!");
            return null;
        }
        List<StockPricePattern> listStockPricePattern = new ArrayList<>();
        String queryRuleName = String.join("", querySymbol).trim();
        //

        System.out.println("queryrulename: " + queryRuleName);
        
        if (query._listCompanyName != null) {
            for (String company: query._listCompanyName) {
                List<StockPricePattern> next = matchACompany(rules, query, company, queryRuleName);
                listStockPricePattern.addAll(next);
            }
        }
        else {
            // only one item company in query
            String companyName = query._companyName;
            if ("ALL".equals(companyName.toUpperCase())) {
                // get all company from stockData
                HashMap<String, StockData> stockdata = AllStockData.getAllStockData();
                for (String company : stockdata.keySet()) {
                    List<StockPricePattern> next = matchACompany(rules, query, company, queryRuleName);
                    listStockPricePattern.addAll(next);
                }
            }
            else {
                List<StockPricePattern> next = matchACompany(rules, query, companyName, queryRuleName);
                listStockPricePattern.addAll(next);
            }
        }
        return listStockPricePattern;
    }
}