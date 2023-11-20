package com.example.third_lab;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class xmlParser {
    private ArrayList<Currency> values;
    private ArrayList<String> currencies;

    public xmlParser(){
        currencies = new ArrayList<>();
        values = new ArrayList<>();
    }

    public ArrayList<String> getCurrency(){
        return currencies;
    }

    public ArrayList<Currency> getValue(){
        return values;
    }

    public Boolean parse(String xmlData){
        boolean status = true;
        Currency currentCurrency = null;
        boolean inEntry = false;
        String textValue = "";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("Valute".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentCurrency = new Currency();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("Valute".equalsIgnoreCase(tagName)){
                                currencies.add(currentCurrency.getName());
                                values.add(currentCurrency);
                                inEntry = false;
                            } else
                            if("CharCode".equalsIgnoreCase(tagName)){
                                currentCurrency.setName(textValue);
                            } else if("Value".equalsIgnoreCase(tagName)){
                                currentCurrency.setValue(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
