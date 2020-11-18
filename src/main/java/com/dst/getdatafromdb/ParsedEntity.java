package com.dst.getdatafromdb;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Document
public class ParsedEntity {

    private String time;
    private TreeMap<String, Double> params;
    private TreeMap<String, Boolean> flags;
    private ArrayList<Double> hstErrors = new ArrayList<>();

    /** Нужно добавить обработку событий, когда заголовок был не сразу в данных, т.к. структура задана не жестко **/
    public String getCsvHeader(){
        //Time
        StringBuilder stringBuilder = new StringBuilder("Timestamp,");
        //Params
        for(Map.Entry<String, Double> entry: params.entrySet()){
            stringBuilder.append(entry.getKey()).append(",");
        }
        //Flags
        for(Map.Entry<String, Boolean> entry: flags.entrySet()){
            stringBuilder.append(entry.getKey()).append(",");
        }
        //Errors (spn+fmi)
        stringBuilder.append("Errors (spn.fmi)").append(",");
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    public String getCsvData(){
        //Time
        StringBuilder stringBuilder = new StringBuilder(time + ",");
        //Params
        for(Map.Entry<String, Double> entry: params.entrySet()){
            stringBuilder.append(entry.getValue()).append(",");
        }
        //Flags
        for(Map.Entry<String, Boolean> entry: flags.entrySet()){
            stringBuilder.append(entry.getValue()).append(",");
        }
        //Errors (spn+fmi)
        if (!hstErrors.isEmpty()) {
            for (Double d : hstErrors) {
                stringBuilder.append(String.format("%.2f", d).replaceAll(",", ":")).append(";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(",");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    @Override
    public String toString() {
        return "ParsedEntity{" +
                "time='" + time + '\'' +
                ", params=" + params.toString() +
                '}';
    }
}