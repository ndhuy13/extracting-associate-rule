/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author Nguyen Duc Huy
 */
public class Apriori {
    
    private HashMap<String, Rule> rules = new HashMap();
    private  List<Integer> datumLength = new ArrayList<>();
    
    
    public HashMap<String, Rule> generateRules(HashMap<String,String> data, float minSup){
        
        HashMap<String, Rule> oldTempRule = new HashMap<>();
        HashMap<String, Rule> newTempRule;
        
        boolean hasCandidate = true;
        
        for(Map.Entry<String, String> datum: data.entrySet()){
                String companyName = datum.getKey();
                String symbolSequence = datum.getValue();
                datumLength.add(symbolSequence.length());
        }
        
        while(hasCandidate){
            newTempRule = new HashMap();
            
            Vector<Tuple3> oldRulePostion = new Vector();
            
            //////////////////////////////////////////////
            for(Map.Entry<String, String> datum: data.entrySet()){
                String companyName = datum.getKey();
                String symbolSequence = datum.getValue();
            
            
                if( oldTempRule.isEmpty() ){
                    //init - first iteration
                    for(int i = 0; i< symbolSequence.length(); i++){
                        increaseFrequencyOrCreateRule(String.valueOf(symbolSequence.charAt(i)),
                                newTempRule, companyName, i, oldRulePostion);                 
                    }
                    
                }
                else{
                    //Base on old rules to genarate new rules
                    for(Map.Entry<String, Rule> entry : oldTempRule.entrySet())
                    {
                        String oldRuleName = entry.getKey();
                        Rule oldRule = entry.getValue();


                        for(Tuple tuple: oldRule._positions){                         
                            for(int j = 0; j < tuple._positions.size(); j++){                                  
                                int oldRulePosition = tuple._positions.elementAt(j);                             
                                
                                if(oldRulePosition + oldRule._lengthOfRule < data.get(tuple._companyName).length()){ //Check out of range
                                    String newRuleName = oldRuleName +
                                        String.valueOf(data.get(tuple._companyName).charAt(oldRulePosition + oldRule._lengthOfRule));
                                    increaseFrequencyOrCreateRule(newRuleName,
                                            newTempRule,tuple._companyName ,oldRulePosition, oldRulePostion);
                                    
                                }                
                            //
                            }

                        }
                    }
                }


            }
                updatePositionOfOldRule(newTempRule, oldRulePostion);

                calculateSup(newTempRule);

                eliminateRule(newTempRule, minSup);
                   
                if(newTempRule.isEmpty()) hasCandidate = false;
                else {
                    hasCandidate = true;                 
                }
               
                for(Map.Entry<String, Rule> entry : newTempRule.entrySet()) {
                    String ruleName = entry.getKey();
                    Rule rule = entry.getValue();

                    rules.put(ruleName, rule);
                }
                
                

                oldTempRule = newTempRule;              //prepare for next iteration



                ////////////////////////////////
        }
        //Test

        for(Map.Entry<String, Rule> entry : rules.entrySet()) {
            String ruleName = entry.getKey();
            Rule rule = entry.getValue();
            
            rules.put(ruleName, rule);
            
            System.out.println(rule._ruleName + " " + rule.getNumberOfAppearance() + " " + rule._support);
        }
        
        return rules;
    }
    
    public boolean isNewRule(String ruleName, HashMap<String, Rule> tempRules){
        Rule r = tempRules.get(ruleName);
        if(r == null) return true;
        else return false;
    }
    
    public boolean isFrequentPattern(String ruleName, HashMap<String, Rule> rules){
        Rule r = rules.get(ruleName);
        if(r == null) return true;
        else return false;
    }
    
    public void increaseFrequencyOrCreateRule(String ruleName, HashMap<String, Rule> tempRules, String companyName, int position, Vector<Tuple3> oldRulePosition){
        if(isNewRule(ruleName, tempRules)){                            //new rule
            Rule newRule = new Rule(ruleName, companyName , position, ruleName.length());
            tempRules.put(ruleName, newRule);
        }
        else{                                                         //old rule
            Rule oldRule = tempRules.get(ruleName);
            //oldRule.setSupport(oldRule.getSupport() + 1.0f);           //increase frequency by 1 unit
            oldRulePosition.add(new Tuple3(ruleName, companyName, position));
            //oldRule.findTupleByCompanyName(companyName)
            //        .addNewPostion(position); //add new postion
        }
    }
    
    public void updatePositionOfOldRule(HashMap<String, Rule> tempRules ,Vector<Tuple3> oldRulePosition){
        for(int i = 0; i < oldRulePosition.size(); i++){
            Rule rule = tempRules.get(oldRulePosition.get(i)._ruleName);
//            if(!rule.hasTupleWithTheSameAsCompanyNameAndPostion(oldRulePosition.get(i)._companyName, oldRulePosition.get(i)._pos)){
//                rule.addNewTuple(new Tuple(oldRulePosition.get(i)._companyName,oldRulePosition.get(i)._pos));
//            }
            if(rule.hasTupleWithTheSameAsCompanyName(oldRulePosition.get(i)._companyName)){
                if(!rule.hasTupleWithTheSameAsCompanyNameAndPostion(oldRulePosition.get(i)._companyName, oldRulePosition.get(i)._pos)){
                rule.findTupleWithTheSameAsCompanyName(oldRulePosition.get(i)._companyName)
                        .addNewPostion(oldRulePosition.get(i)._pos);
                }
            }
            else{
                
                    rule.addNewTuple(new Tuple(oldRulePosition.get(i)._companyName,oldRulePosition.get(i)._pos));
                
                
            }
            rule._support += 1.0f;      ////increase frequency by 1 unit
        }
    }

    public void calculateSup(HashMap<String, Rule> rule){

        int length = 0;
        for(Map.Entry<String, Rule> entry : rule.entrySet()) {
            Rule r = entry.getValue();
            length = entry.getKey().length();
            break;                     
        }
        int numOfRuleSameLength = 0;
        for(int i = 0; i < datumLength.size(); i++){
            numOfRuleSameLength += datumLength.get(i) - length + 1;  
        }
        for(Map.Entry<String, Rule> entry : rule.entrySet()) {
            Rule r = entry.getValue();
            r._support =  ((float) r._support )/ ((float) numOfRuleSameLength);
                     
        }
        
    }
    
    public void eliminateRule(HashMap<String, Rule> rule, float minSup){          //Delete rule if any rule support < minSup
        Vector<String> delRule = new Vector(); 
        for(Map.Entry<String, Rule> entry : rule.entrySet()) {
            String key = entry.getKey();
            Rule r = entry.getValue();
            if(r._support < minSup){
                delRule.add(key);
            }
        }
        if(delRule.size() > 0){
            for(int i=0; i< delRule.size(); i++){
                rule.remove(delRule.get(i));
            }
        }
    }
    
    
    

}

class Tuple3 {      //support to avoid concurrency error 
    String _ruleName;
    String _companyName;
    int _pos;

    public Tuple3(String _ruleName, String _companyName, int _pos) {
        this._ruleName = _ruleName;
        this._companyName = _companyName;
        this._pos = _pos;
    }
        
        
}