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
public class Query {
    String _companyName;
    List<String> _listCompanyName;
    List<Float> _QP;
    int _timeInterval;
    int _bodyLen;
    float _minHold;
    float _maxHold;
    float _minConfidence;
    StockRecommendationSystem.Confident investorType = StockRecommendationSystem.Confident.NORMAL;

    public Query(String _companyName, List<Float> _QP, int _timeInterval, int _bodyLen, float _minHold, float _maxHold, float _minConfidence) {
        this._companyName = _companyName;
        this._QP = _QP;
        this._timeInterval = _timeInterval;
        this._bodyLen = _bodyLen;
        this._minHold = _minHold;
        this._maxHold = _maxHold;
        this._minConfidence = _minConfidence;
    }
    
    public Query(List<String> _listCompanyName, List<Float> _QP, int _timeInterval, int _bodyLen, float _minHold, float _maxHold, float _minConfidence) {
        this._listCompanyName = _listCompanyName;
        this._QP = _QP;
        this._timeInterval = _timeInterval;
        this._bodyLen = _bodyLen;
        this._minHold = _minHold;
        this._maxHold = _maxHold;
        this._minConfidence = _minConfidence;
    }
    public List<Float> getQueryPattern() {
        return this._QP;
    }
}
