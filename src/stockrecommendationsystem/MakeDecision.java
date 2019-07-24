/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;

import java.util.List;

/**
 *
 * @author Nguyen Duc Huy
 */
public class MakeDecision {
    public void makeDecision(List<StockPricePattern> lstStockPricePattern, Query query){
        int numOfPatternIncrease = 0;
        int numOfPatternDecrease = 0;
        int numOfPatternUnchanged = 0;
        
        float ratioOfIncrease;
        float ratioOfDecrease;
        float ratioOfUnchanged;
        switch(query.investorType){
            case NORMAL:
                for(int i = 0; i < lstStockPricePattern.size(); i++){
                    float AverageIncreaseRatio = ((average(lstStockPricePattern.get(i)._nextPattern) 
                            - lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1))
                            / lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1)) * 100;
                    if( AverageIncreaseRatio > query._maxHold) numOfPatternIncrease++;
                    else if ( AverageIncreaseRatio < query._minHold) numOfPatternDecrease++;
                    else numOfPatternUnchanged++;
                }
                
                ratioOfIncrease = ((float)numOfPatternIncrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfDecrease = ((float)numOfPatternDecrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfUnchanged = ((float)numOfPatternUnchanged / (float)lstStockPricePattern.size()) * 100;
                
                if( ratioOfIncrease <= query._minConfidence 
                        && ratioOfDecrease <= query._minConfidence 
                        && ratioOfUnchanged <= query._minConfidence)
                    System.out.println("No Recommendation");
                else if( ratioOfIncrease > query._minConfidence) System.out.println("Investment Type: BUY");
                else if( ratioOfDecrease > query._minConfidence) System.out.println("Investment Type: SELL");
                else if( ratioOfUnchanged > query._minConfidence) System.out.println("Investment Type: HOLD");
                
                System.out.println("Ratio of Increase: " + ratioOfIncrease + " %");
                System.out.println("Ratio of Decrease: " + ratioOfDecrease + " %");
                System.out.println("Ratio of Unchange: " +  ratioOfUnchanged + " %");
                
                break;
                
            case OPTIMISTIC:
                for(int i = 0; i < lstStockPricePattern.size(); i++){
                    float MaximunIncreaseRatio = ((findMax(lstStockPricePattern.get(i)._nextPattern) 
                            - lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1))
                            / lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1)) * 100;
                    if( MaximunIncreaseRatio > query._maxHold) numOfPatternIncrease++;
                    else if ( MaximunIncreaseRatio < query._minHold) numOfPatternDecrease++;
                    else numOfPatternUnchanged++;
                }
                
                ratioOfIncrease = ((float)numOfPatternIncrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfDecrease = ((float)numOfPatternDecrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfUnchanged = ((float)numOfPatternUnchanged / (float)lstStockPricePattern.size()) * 100;
                
                if( ratioOfIncrease <= query._minConfidence 
                        && ratioOfDecrease <= query._minConfidence 
                        && ratioOfUnchanged <= query._minConfidence)
                    System.out.println("No Recommendation");
                else if( ratioOfIncrease > query._minConfidence) System.out.println("Investment Type: BUY");
                else if( ratioOfDecrease > query._minConfidence) System.out.println("Investment Type: SELL");
                else if( ratioOfUnchanged > query._minConfidence) System.out.println("Investment Type: HOLD");
                
                System.out.println("Ratio of Increase: " + ratioOfIncrease + " %");
                System.out.println("Ratio of Decrease: " + ratioOfDecrease + " %");
                System.out.println("Ratio of Unchange: " +  ratioOfUnchanged + " %");
                
                break;
                
            case PESSIMISTIC: 
                for(int i = 0; i < lstStockPricePattern.size(); i++){
                    float MaximunIncreaseRatio = ((findMin(lstStockPricePattern.get(i)._nextPattern) 
                            - lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1))
                            / lstStockPricePattern.get(i)._beginningPattern.get(lstStockPricePattern.get(i)._beginningPattern.size()-1)) * 100;
                    if( MaximunIncreaseRatio > query._maxHold) numOfPatternIncrease++;
                    else if ( MaximunIncreaseRatio < query._minHold) numOfPatternDecrease++;
                    else numOfPatternUnchanged++;
                }
                
                ratioOfIncrease = ((float)numOfPatternIncrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfDecrease = ((float)numOfPatternDecrease / (float)lstStockPricePattern.size()) * 100;
                ratioOfUnchanged = ((float)numOfPatternUnchanged / (float)lstStockPricePattern.size()) * 100;
                
                if( ratioOfIncrease <= query._minConfidence 
                        && ratioOfDecrease <= query._minConfidence 
                        && ratioOfUnchanged <= query._minConfidence)
                    System.out.println("No Recommendation");
                else if( ratioOfIncrease > query._minConfidence) System.out.println("Investment Type: BUY");
                else if( ratioOfDecrease > query._minConfidence) System.out.println("Investment Type: SELL");
                else if( ratioOfUnchanged > query._minConfidence) System.out.println("Investment Type: HOLD");
                
                System.out.println("Ratio of Increase: " + ratioOfIncrease +" %");
                System.out.println("Ratio of Decrease: " + ratioOfDecrease +" %");
                System.out.println("Ratio of Unchange: " +  ratioOfUnchanged +" %");
                
                break;
        }
        
        
    }
    
    
    public float average(List<Float> lst){
        float sum = 0;
        for(int i =0; i< lst.size(); i++){
            sum += lst.get(i);
        }
        return sum / lst.size();
    }
    
    public float findMax(List<Float> lst){
        float max = 0.0f;
        for(int i=0; i<lst.size(); i++)
            if(lst.get(i) > max) max = lst.get(i);
        return max;
    }
    
    public float findMin(List<Float> lst){
        float min = 0.0f;
        for(int i=0; i<lst.size(); i++)
            if(lst.get(i) < min) min = lst.get(i);
        return min;
    }
}
