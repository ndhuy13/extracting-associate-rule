/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;  
import javafx.util.Pair;

/**
 *
 * @author  Phan The Hien, Nguyen Duc Huy
 */

class Category {
    String symbol;
    float value_start;
    float value_end;
    public Category(String sym, float vs, float ve) {
        symbol = sym;
        value_start = vs;
        value_end = ve;
    }
    public void set_val_start(float new_start) {
        value_start = new_start;
    }
    public void set_val_end(float new_end) {
        value_end = new_end;
    }
    public boolean check_in_category(float ratio) {
        return (ratio >= value_start && ratio < value_end);
    }
}
public class Preprocessing {
    
    public String changeToSymbol(String value) {
       return ""; 
    }
    
    public Pair<String, String> preprocessEachCompany(String companyName, List<Float> stock_value) {
        if (companyName != "") {
            AllStockData.addStockData(companyName, stock_value);
        }

        // CALCULATE RATIO
        List<Float> value_ratio = new ArrayList<Float>();
        for (int i = 0; i < stock_value.size()-1; i++) {
            float next_val = (stock_value.get(i+1) - stock_value.get(i))*100/(float)stock_value.get(i);
            value_ratio.add(next_val);
        }

        try {
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            File f = new File("src\\symbol-sequence-data.txt");
            FileWriter fw = new FileWriter(f);

            //Bước 2: Ghi dữ liệu

                for(int g = 0; g < value_ratio.size(); g++){
                    fw.append(value_ratio.get(g).toString() + "\n");
                }


            //Bước 3: Đóng luồng
            fw.close();
        } catch (IOException ex) {
            System.out.println("Loi ghi file: " + ex);
        }
        
        // CONSTRUCT CATEGORY, AFTER CALCULATE
        List<Category> list_category = new ArrayList<Category>();
        final int MAX_OF_CATEGORY = 9;
        final float INF = 1e18f;
        
        List<Float> sort_value_ratio = new ArrayList<Float>();
        for (float r: value_ratio) {
            sort_value_ratio.add(r);
        }
        Collections.sort(sort_value_ratio);
        int valueInOneCategory = sort_value_ratio.size() /  MAX_OF_CATEGORY;
        float begin = -INF;
        float end = -INF;
        int categoryNum = 1;
        while (categoryNum <= MAX_OF_CATEGORY) {
            if (categoryNum == MAX_OF_CATEGORY) {
                end = INF;
            }
            else {
                end = value_ratio.get(valueInOneCategory * categoryNum);
            }
            //System.out.println("begin: " + begin + ", end: " + end);
            char categoryName = (char)(categoryNum + 64);
            list_category.add(new Category(String.valueOf(categoryName), begin, end));
            begin = end;
            categoryNum++;
        }
        
        // CONVERT RATIO TO SYMBOL;
        List<String> value_symbol = new ArrayList<>();
        
        for (float r:value_ratio) {
            for (Category cat:list_category) {
                if (cat.check_in_category(r)) {
                    value_symbol.add(cat.symbol);
                }
            }
        }
        System.out.println(companyName + ": " + String.join(" ",value_symbol) + ". Size=" + value_symbol.size() );
        Pair result = new Pair(companyName, String.join("",value_symbol));
        return result; 
    }
    
    public HashMap<String,String> preprocessing(String filePath){
        //TODO
        List<Float> value_int = new ArrayList<Float>();
        String companyName = "";
        HashMap<String, String> allCompany = new HashMap<String, String>();
        
        // READ FILE, CONVERT DATA, STORE DATA
        try
        {
          BufferedReader reader = new BufferedReader(new FileReader(filePath));
          String line;
          int lineNum = 0;
          while ((line = reader.readLine()) != null)
          {
            if (lineNum % 2 == 0) {
                companyName = line;
            }
            else {
                String[] values = line.split(" ");

                for(String str:values){
                    value_int.add(Float.parseFloat(str.trim()));
                }
                Pair res = preprocessEachCompany(companyName, value_int);
                value_int.clear();
                // PUT COMPANY PREPROCESS RESULT TO HASHMAP
                allCompany.put(res.getKey().toString(), res.getValue().toString());
            }
            lineNum++;
          }
          reader.close();
        }
        catch (Exception e)
        {
          System.err.format("Exception occurred trying to read '%s'.", filePath);
          e.printStackTrace();
          return null;
        }
        //Write Symbol Sequence to file
        /*try {
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            File f = new File("src\\symbol-sequence-data.txt");
            FileWriter fw = new FileWriter(f);

            //Bước 2: Ghi dữ liệu
            for(Map.Entry<String, String> entry : allCompany.entrySet()) {
                String _companyName = entry.getKey();
                String _symbolSequence = entry.getValue();
                fw.append(_companyName + "\n");
                fw.append(_symbolSequence);
            }

            //Bước 3: Đóng luồng
            fw.close();
        } catch (IOException ex) {
            System.out.println("Loi ghi file: " + ex);
        }*/
        
        return allCompany;           //TODO: return a hashmap contain tuples <Company Name, Symbol String>

    }
}
