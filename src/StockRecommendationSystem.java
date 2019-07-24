/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;


/**
 *
 * @author Nguyen Duc Huy
 */
public class StockRecommendationSystem {

    String _filePath = "src\\test-10comp-01012010.txt";
    HashMap<String, String> data = new HashMap();
    HashMap<String, Rule> rules = new HashMap();
    float minSup = 0.1f;
    //List<Float> QP = new ArrayList<Float>(Arrays.asList(10f,10.4f, 10.15f, 10.1f));
    //Query query = new Query("FPT", QP, 0, 3, -5.0f, 5.0f, 50);        //You must create your query
    
    //List<StockPricePattern> stockPricePatterns = new ArrayList<>();
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        StockRecommendationSystem stockRecommendationSystem = new StockRecommendationSystem();
        
        stockRecommendationSystem.data = new Preprocessing()
                .preprocessing(stockRecommendationSystem._filePath);
        
//        stockRecommendationSystem.data.put("VNG", "ACABAAB");
//        stockRecommendationSystem.data.put("HAG", "BEC");

        long start = System.currentTimeMillis();
        stockRecommendationSystem.rules = new Apriori()
                .generateRules(stockRecommendationSystem.data,
                                stockRecommendationSystem.minSup);
        long end = System.currentTimeMillis();
        long t = end - start;
        System.out.println("Tổng thời gian: " + t + " millisecond");
//        stockRecommendationSystem.stockPricePatterns = new RuleMatching().
//                match(stockRecommendationSystem.rules, stockRecommendationSystem.query); //Miss real_stock data param
    }
    
}
