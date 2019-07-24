/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this stockRecommendationSystem.stockPricePatternslate file, choose Tools | Templates
 * and open the stockRecommendationSystem.stockPricePatternslate in the editor.
 */
package stockrecommendationsystem;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;


/**
 *
 * @author Nguyen Duc Huy
 */


public class StockRecommendationSystem {

    //String _fileQueryPath = "src\\stockrecommendationsystem\\query_test.txt";
    //Linux
    String _fileQueryPath = "src/stockrecommendationsystem/query_test.txt";
    HashMap<String, String> data = new HashMap();
    HashMap<String, Rule> rules = new HashMap();
    float minSup = 0.02f;
    Query query = readQueryFromFile(_fileQueryPath);
    List<String> querySymbol;
    List<StockPricePattern> stockPricePatterns = new ArrayList<>();
    HashMap<String, List<StockPricePattern>> companyStockPricePatterns = new HashMap();
    public enum Confident {
        NORMAL, OPTIMISTIC, PESSIMISTIC 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        StockRecommendationSystem stockRecommendationSystem = new StockRecommendationSystem();
        long startTime1 = System.nanoTime();
        
        
        Pair<HashMap<String,String>, List<String>> HashMapAndQuerySymbol = new Preprocessing()
                .preprocessing(stockRecommendationSystem.query);
        
        long endTime1   = System.nanoTime();
        long totalTime1 = endTime1 - startTime1;
        System.out.println((float)totalTime1/(float)1000000000 + " ");
        stockRecommendationSystem.data = HashMapAndQuerySymbol.getKey();
        stockRecommendationSystem.querySymbol = HashMapAndQuerySymbol.getValue();

//        //Write
//          stockRecommendationSystem.rules = new Apriori()
//                    .generateRules(stockRecommendationSystem.data,
//                                    stockRecommendationSystem.minSup);
//        try {
//            FileOutputStream f = new FileOutputStream(new File("Apriori.txt")); 
//            ObjectOutputStream o = new ObjectOutputStream(f);
//            
//            o.writeObject(stockRecommendationSystem.rules);
//            
//            o.close();
//            f.close();
//        } catch (IOException ex) {
//            Logger.getLogger(StockRecommendationSystem.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        //Read

        try {
            FileInputStream   fi = new FileInputStream(new File("Apriori.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            
            stockRecommendationSystem.rules = (HashMap<String,Rule>) oi.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StockRecommendationSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StockRecommendationSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        

         

       long startTime = System.nanoTime(); 
        stockRecommendationSystem.stockPricePatterns = new RuleMatching().
                match(stockRecommendationSystem.rules, stockRecommendationSystem.query, stockRecommendationSystem.querySymbol); //Miss real_stock data para
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println((float)totalTime/(float)1000000000 + " ");
        List<StockPricePattern> temp = stockRecommendationSystem.stockPricePatterns;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        for(int i=0; i < temp.size(); i++ ){
            System.out.print("< ");
            for(int j = 0; j < temp.get(i)._beginningPattern.size(); j++) {
                if (j == 0) {
                   System.out.print("[" + formatDate.format(temp.get(i)._dateOfBeginningPattern.get(j)) + "]: "); 
                }
                System.out.print(temp.get(i)._beginningPattern.get(j) + " ");
            }
            System.out.print(">");
            System.out.print("  <");
            for(int j = 0; j < temp.get(i)._nextPattern.size(); j++) {
                if (j == 0) {
                   System.out.print("[" + formatDate.format(temp.get(i)._dateOfNextPattern.get(j)) + "]: "); 
                }
                System.out.print(temp.get(i)._nextPattern.get(j) + " ");
            }
            System.out.print(">");
            System.out.println();
        }
        
        
        new MakeDecision().makeDecision(stockRecommendationSystem.stockPricePatterns, stockRecommendationSystem.query);
        
    }
    private Query readQueryFromFile(String fileQueryPath) {
        Query query = null;
        int maxNumParameter = 8;
        boolean queryValid = true;
        try
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileQueryPath))) {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    queryValid = true;
                    System.out.println("Query: " + line);
                    line = line.replace("(", "").replace(")", "");
                    String [] parameter = line.split(",");
                    System.out.println("number of parameters = " + parameter.length); 
                    if (parameter.length == maxNumParameter || parameter.length == maxNumParameter - 1) {
                        List<Float> paraQP = stringToArrayFloat(parameter[1].trim());
                        int paraTimeInterval = Integer.parseInt(parameter[2].trim());
                        int paraBodyLen = Integer.parseInt(parameter[3].trim());
                        float paraMinHold = Float.parseFloat(parameter[4].trim());
                        float paraMaxHold = Float.parseFloat(parameter[5].trim());
                        float paraMinConfidence = Float.parseFloat(parameter[6].trim());
                        Confident paraInvestorConfident = null;
                        if (parameter.length == maxNumParameter - 1) {
                            paraInvestorConfident = Confident.NORMAL;
                        } else {
                            String confidentString = parameter[7].trim();
                            try {
                                int op1 = Integer.parseInt(confidentString);
                                if (op1 >= 0 && op1 <= 2) {
                                    paraInvestorConfident = Confident.values()[op1];
                                }
                                else {
                                    queryValid = false;
                                    throw new Exception("Confident parameter is not valid (0/1/2) or text");
                                }
                            } catch (NumberFormatException e) {
                                try {
                                    paraInvestorConfident = Confident.valueOf(confidentString.toUpperCase());
                                }
                                catch (IllegalArgumentException iae) {
                                    queryValid = false;
                                    System.out.println("Confident Parameter is not valid (0/1/2) or text (normal/optimistic/pessimistic)");
                                }
                            } 
                            catch (Exception e) {
                                queryValid = false;
                                System.out.println("Confident Parameter is not valid (0/1/2) or text (normal/optimistic/pessimistic)");
                            }
                        }
                        
                        if (parameter[0].contains("[")) {
                            List<String> paraCompanyName = stringToArrayString(parameter[0]);
                            query =  new Query(paraCompanyName, paraQP, paraTimeInterval, paraBodyLen,
                                    paraMinHold, paraMaxHold, paraMinConfidence);
                            query.investorType = paraInvestorConfident;
                        }
                        else {
                            String oneCompanyName = parameter[0].trim();
                            query =  new Query(oneCompanyName, paraQP, paraTimeInterval, paraBodyLen,
                                    paraMinHold, paraMaxHold, paraMinConfidence);
                            query.investorType = paraInvestorConfident;
                        }
                        
                    }
                    else {
                        throw new Exception("Query's Content is not valid!");
                    }
                    
                } 
            }
            if (!queryValid) {
                System.out.println("Query is not valid!!!");
                query = null;
            }
            else {
                System.out.println("Query is valid!");
            }
        }
        catch (Exception e)
        {
          System.err.format("Exception occurred trying to read '%s'.", fileQueryPath);
          return null;
        }
        return query;
    }
    private List<Float> stringToArrayFloat(String s) {
       List<Float> arr = new ArrayList<>();
       s = s.trim();
       String[] split = s.replace("[", "").replace("]", "").split(" ");
       for (String str: split) {
//           System.out.println(str);
           arr.add(Float.parseFloat(str));
       }
       return arr;
    }
    private List<String> stringToArrayString(String s) {
       List<String> arr = new ArrayList<>();
       s = s.trim();
       String[] split = s.replace("[", "").replace("]", "").split(" ");
       for (String str: split) {
//           System.out.println(str);
           arr.add(str);
       }
       return arr;
    }
}
