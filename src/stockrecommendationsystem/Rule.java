/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Nguyen Duc Huy
 */
public class Rule implements Serializable {
    String _ruleName;                                  //Name of Rule
    float _support = 0.0f;                                    //Support
    Vector<Tuple> _positions = new Vector();          //List of positions
    int _lengthOfRule = -1;

    public Rule(String _ruleName, float _support) {
        this._ruleName = _ruleName;
        this._support = _support;
    }
    public Rule(String _ruleName, String companyName, int position, int length){
        this._ruleName = _ruleName;
        this._positions.add(new Tuple(companyName, position));
        this._lengthOfRule = length;
        this._support = 1.0f;
    }
    public Rule(Rule r){
        _ruleName = r._ruleName;
        _support = r._support;
        _positions = r._positions;
    }

    public void setSupport(float _support) {
        this._support = _support;
    }
    
    public float getSupport() {
        return this._support;
    }
    
    /**
     *
     * @param companyName
     * @param position
     * @return a Tuple which name is equivalent to companyName
     */
    public boolean hasTupleWithTheSameAsCompanyNameAndPostion(String companyName, int position){                
        for(int i = 0; i < _positions.size(); i++){
            if(_positions.elementAt(i)._companyName.equalsIgnoreCase(companyName) 
                    && findNumberInVector(position, _positions.elementAt(i)._positions) ){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasTupleWithTheSameAsCompanyName(String companyName){
        for(int i = 0 ; i < _positions.size(); i++){
            if(_positions.elementAt(i)._companyName.equalsIgnoreCase(companyName) )
                return true;
        }
        return false;
    }
    public Tuple findTupleWithTheSameAsCompanyName(String companyName){
        for(int i = 0 ; i < _positions.size(); i++){
            if(_positions.elementAt(i)._companyName.equalsIgnoreCase(companyName) )
                return _positions.elementAt(i);
        }
        return null;
    }
    
    public boolean findNumberInVector(int pos, Vector<Integer> _pos){
        for(int i = 0; i < _pos.size(); i++){
            if(pos == _pos.get(i)) return true;
        }
        return false;
    }
    
    public int getNumberOfAppearance(){
        int numberOfAppearance = 0;
        for(Tuple tuple: _positions){
            numberOfAppearance += tuple._positions.size();
        }
        return numberOfAppearance;
    }
    
    public int getNumberOfTuples(){
        return _positions.size();
    }
    
    public void addNewTuple(Tuple tuple){
        this._positions.add(tuple);
    }
    
    public void addNewPostion(Tuple tuple, int pos){
        tuple._positions.add(pos);
    }
    
    public int getTheNumberOfAppearance(){
        int number = 0;
        for(int i = 0; i <  _positions.size(); i++){
            number += _positions.get(i)._positions.size();
        }
        return number;
    }
        
}

class Tuple implements Serializable {
    String _companyName;
    Vector<Integer> _positions = new Vector();

    public Tuple(String _companyName) {
        this._companyName = _companyName;
    }
    
    public Tuple(String _companyName, int postion){
        this._companyName = _companyName;
        _positions.add(postion);
    }
    
    public void addNewPostion(int newPosition){
        this._positions.add(newPosition);
    }
    

}
