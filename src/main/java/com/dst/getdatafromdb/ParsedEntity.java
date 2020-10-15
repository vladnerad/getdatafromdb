package com.dst.getdatafromdb;

import java.util.Map;
import java.util.TreeMap;

public class ParsedEntity {

    private String time;
    private TreeMap<String, Double> params;

    public void setParams(Map<String, Double> params) {
        this.params = (TreeMap<String, Double>) params;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    /** Нужно добавить обработку событий, когда заголовок был не сразу в данных, т.к. структура задана не жестко **/
    public String getCsvHeader(){
        StringBuilder stringBuilder = new StringBuilder("Timestamp,");
        for(Map.Entry<String, Double> entry: params.entrySet()){
            stringBuilder.append(entry.getKey()).append(",");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    public String getCsvData(){
        StringBuilder stringBuilder = new StringBuilder(time + ",");
        for(Map.Entry<String, Double> entry: params.entrySet()){
            stringBuilder.append(entry.getValue()).append(",");
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