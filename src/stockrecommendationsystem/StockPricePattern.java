/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;

import java.util.Date;
import java.util.List;

/**
 *
 * @author  Nguyen Duc Huy
 */

public class StockPricePattern {
    List<Float> _beginningPattern;
    List<Float> _nextPattern;
    List<Date> _dateOfBeginningPattern;
    List<Date> _dateOfNextPattern;
    public StockPricePattern(List<Float> beginningPattern, List<Float> nextPattern, 
            List<Date> dateBeginPattern, List<Date> dateNextPattern) {
        this._beginningPattern = beginningPattern;
        this._nextPattern = nextPattern;
        this._dateOfBeginningPattern = dateBeginPattern;
        this._dateOfNextPattern = dateNextPattern;
    }    
}

