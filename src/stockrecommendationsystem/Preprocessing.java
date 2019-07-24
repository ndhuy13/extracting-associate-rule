/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockrecommendationsystem;

import java.io.BufferedReader;
import java.util.*;  
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


/**
 *
 * @author  Phan The Hien, Nguyen Duc Huy
 */

class Category {
    String symbol;
    float value_start;
    float value_end;
    int number_of_value;
   
    public Category(String sym, float vs, float ve, int num_val) {
        symbol = sym;
        value_start = vs;
        value_end = ve;
        number_of_value = num_val;
    }
    public void set_val_start(float new_start) {
        value_start = new_start;
    }
    public void set_val_end(float new_end) {
        value_end = new_end;
    }
    public void set_num_val(int num_val) {
        number_of_value = num_val;
    }
    public int get_num_val() {
        return number_of_value;
    }
    
    public boolean check_in_category(float ratio) {
        return (ratio >= value_start && ratio < value_end);
    }
}
public class Preprocessing {
    
    public List<String> convertQueryPatternToSymbol(List<Float> queryPattern, List<Category> listCategory) {
        List<String> value_symbol = new ArrayList<>();
        
        List<Float> value_ratio = new ArrayList<>();
        for (int i = 0; i < queryPattern.size()-1; i++) {
            float next_val = (queryPattern.get(i+1) - queryPattern.get(i))*100/(float)queryPattern.get(i);
            value_ratio.add(next_val);
        }
        
        value_ratio.forEach((r) -> {
            listCategory.stream().filter((cat) -> (cat.check_in_category(r))).forEachOrdered((cat) -> {
                value_symbol.add(cat.symbol);
            });
        });
        
        System.out.println("Query Symbol" + value_symbol);
        
       return value_symbol; 
    }
    
    public List<Category> constructCategory(List<Float> allStockValue) {
        List<Float> value_ratio = new ArrayList<>();
        for (int i = 0; i < allStockValue.size()-1; i++) {
            float next_val = (allStockValue.get(i+1) - allStockValue.get(i))*100/(float)allStockValue.get(i);
            value_ratio.add(next_val);
        }

        // CONSTRUCT CATEGORY, AFTER CALCULATE
        List<Category> list_category = new ArrayList<>();
        final int MAX_OF_CATEGORY = 11;
        final float INF = 1e18f;

        List<Float> sort_value_ratio = new ArrayList<>();
        value_ratio.stream().map((r) -> {
            return r;
        }).forEachOrdered((r) -> {
            sort_value_ratio.add(r);
        });
        Collections.sort(sort_value_ratio);
        int valueInOneCategory = sort_value_ratio.size() /  MAX_OF_CATEGORY;
        float begin = -INF;
        float end = -INF;
        int categoryNum = 1;
        int startIndex = 0;
        int endIndex = 0;
        while (categoryNum <= MAX_OF_CATEGORY && end != INF) {
            if (categoryNum == MAX_OF_CATEGORY) {
                end = INF;
            }
            else {
                endIndex = Math.min(endIndex + valueInOneCategory, sort_value_ratio.size() - 1);
                if (endIndex == sort_value_ratio.size() - 1) {
                    end = INF;
                }
                else {
                    end = sort_value_ratio.get(endIndex);
                }
                while (endIndex < sort_value_ratio.size() && sort_value_ratio.get(endIndex) == end)
                    endIndex++;
            }
            char categoryName = (char)(categoryNum + 64);
            list_category.add(new Category(String.valueOf(categoryName), begin, end, endIndex - startIndex));
            
            if (endIndex < sort_value_ratio.size())
                begin = sort_value_ratio.get(endIndex - 1);
            else 
                break;
            startIndex = endIndex;
            
            categoryNum++;
        }
        
        for (Category cat: list_category) {
            System.out.println(cat.symbol + ":" + cat.value_start + ".." + cat.value_end + ". size= " + cat.number_of_value);
        }
        
        return list_category;
    }
    
    public Pair<String, String> preprocessEachCompany(String companyName, List<Float> stock_value, List<Category> list_category) {
        // CALCULATE RATIO
        List<Float> value_ratio = new ArrayList<>();
        for (int i = 0; i < stock_value.size()-1; i++) {
            float next_val = (stock_value.get(i+1) - stock_value.get(i))*100/(float)stock_value.get(i);
            value_ratio.add(next_val);
        }

        List<Float> sort_value_ratio = new ArrayList<>();
        value_ratio.stream().map((r) -> {
            return r;
        }).forEachOrdered((r) -> {
            sort_value_ratio.add(r);
        });
        Collections.sort(sort_value_ratio);

        // CONVERT RATIO TO SYMBOL;
        List<String> value_symbol = new ArrayList<>();

        value_ratio.forEach((r) -> {
            list_category.stream().filter((cat) -> (cat.check_in_category(r))).forEachOrdered((cat) -> {
                value_symbol.add(cat.symbol);
            });
        });
        System.out.println(companyName + ": " + String.join("",value_symbol) + ". Size=" + value_symbol.size() );
        Pair result = new Pair(companyName, String.join("",value_symbol));

        return result; 
    }
    
    public Pair<HashMap<String,String>, List<String>> preprocessing(Query query) {
        List<Float> value_int = new ArrayList<>();
        List<Date> date_list = new ArrayList<>();
        HashMap<String, String> allCompany = new HashMap<>(); // Keep all companyname, symbol
        
        final String FORMAT_DATA_FILE = ".csv";
        final String DATA_STOCK_PATH = "src\\stockrecommendationsystem\\RawStockData";
        final String DATE_BEGIN_DATA = "05/01/2010";
        final String DATE_END_DATA = "31/12/2017";
        final String SEPARATOR = ",";
        final int DATE_ROW_INDEX = 0;
        final int VALUE_ROW_INDEX = 1;
        
        // READ FILE CSV, CONVERT DATA, STORE DATA 
        try {
            File f = new File(DATA_STOCK_PATH);// your folder path
            String [] fileList = f.list(); // It gives list of all files in the folder.
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateNeedData = sdf.parse(DATE_BEGIN_DATA);
            Date dateEndData = sdf.parse(DATE_END_DATA);

            for(String str : fileList){
                if(str.endsWith(FORMAT_DATA_FILE)){
                    // Read Excel File
                    String idComp = str.substring(0, str.indexOf(".")).toUpperCase();
                    String excelFilePath = "src\\stockrecommendationsystem\\RawStockData\\" + str;
                    
                    String line;
                    boolean isFirstLine = true;
                    try (BufferedReader br = new BufferedReader(new FileReader(excelFilePath))) {
                        while ((line = br.readLine()) != null) {
                            if (!isFirstLine) {
                                String[] data = line.split(SEPARATOR);
                                Date dataDate = sdf.parse(data[DATE_ROW_INDEX]);
                                if (dataDate.before(dateNeedData)) {
                                    break;
                                }
                                value_int.add(Float.parseFloat(data[VALUE_ROW_INDEX]));
                                date_list.add(dataDate);
                            }
                            else {
                                isFirstLine = false;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Collections.reverse(value_int);
                    Collections.reverse(date_list);
                    
                    Date beginDate = date_list.get(0);
                    long difference = Math.abs(dateNeedData.getTime() - beginDate.getTime());
                    int daysBetween1 = Math.round(difference / (1000*60*60*24));
//                    System.out.println(idComp + "..." + daysBetween1);
                    
                    Date endDate = date_list.get(date_list.size() - 1);
                    difference = Math.abs(dateEndData.getTime() - endDate.getTime());
                    int daysBetween2 = Math.round(difference / (1000*60*60*24));
//                    System.out.println(idComp + "..." + daysBetween2);
                    
                    
                    if (daysBetween1 < 4 && daysBetween2 < 4) {
                        AllStockData.addStockData(idComp, value_int, date_list);
                    }
                    
                    value_int.clear();
                    date_list.clear();
                }
            }
            
            List<Float> allStockValue = AllStockData.getAllStockValue();
            List<Category> listCategory = constructCategory(allStockValue);
            HashMap<String, StockData> allStockData = AllStockData.getAllStockData();
            
            System.out.println("All Stock Value size: " + allStockValue.size());
            System.out.println("Numbers of company used: " + allStockData.size());
            
            for (Map.Entry<String, StockData> entry : allStockData.entrySet()) {
                String idComp = entry.getKey();
                StockData stockData = entry.getValue();
                Pair res = preprocessEachCompany(idComp, stockData.getStockValue(), listCategory);
                System.out.println("..." + idComp + "...size = " + stockData.getStockValue().size());
                allCompany.put(res.getKey().toString(), res.getValue().toString().trim());
            }
            
            List<String> value_symbol = convertQueryPatternToSymbol(query.getQueryPattern(), listCategory);
            
            return new Pair<>(allCompany, value_symbol);
        }
        catch (ParseException pe){
            System.err.format("Exception occurred when parse Date from string!");
            return null;
        }
        //TODO: return a hashmap contain tuples <Company Name, Symbol String>
    }
}
