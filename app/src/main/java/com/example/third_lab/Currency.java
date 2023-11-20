package com.example.third_lab;

public class Currency {
    private String name;
    private Double value;

    public String getName(){
        return name;
    }
    public Double getValue(){
        return value;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setValue(String value){
        String ans = "";
        for (int i = 0; i <value.length(); i++)
            if(value.charAt(i) == ',')
                ans += ".";
            else
                ans += value.charAt(i);
        this.value = Double.parseDouble(ans);
    }
}
