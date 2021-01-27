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

    private String spliter = ";";
    private String errorSpliter = ",";

    /** Нужно добавить обработку событий, когда заголовок был не сразу в данных, т.к. структура задана не жестко **/
    public String getCsvHeader(){
        //Time
        StringBuilder stringBuilder = new StringBuilder("Timestamp").append(spliter);
        //Params
        for(Map.Entry<String, Double> entry: params.entrySet()){
            stringBuilder.append(entry.getKey()).append(spliter);
        }
        //Flags
        for(Map.Entry<String, Boolean> entry: flags.entrySet()){
            stringBuilder.append(entry.getKey()).append(spliter);
        }
        //Errors (spn+fmi)
        stringBuilder.append("Errors (spn.fmi)").append(spliter);
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    public String getCsvData(){
        //Time
        StringBuilder stringBuilder = new StringBuilder(time + spliter);
        //Params
        for(Map.Entry<String, Double> entry: params.entrySet()){
//            stringBuilder.append(String.format("%f", entry.getValue())).append(spliter);
            stringBuilder.append(entry.getValue().toString().replace(".", ",")).append(spliter);
        }
        //Flags
        for(Map.Entry<String, Boolean> entry: flags.entrySet()){
            stringBuilder.append(entry.getValue()).append(spliter);
        }
        //Errors (spn+fmi)
        if (!hstErrors.isEmpty()) {
            for (Double d : hstErrors) {
                stringBuilder.append(String.format("%.2f", d).replaceAll(",", ":")).append(errorSpliter);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(spliter);
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