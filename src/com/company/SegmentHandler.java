package com.company;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SegmentHandler extends DefaultHandler {

    Map<String, Integer> map = new HashMap<>();

    boolean isCATEGORYTEXT;
    String[] parseSegments;
    int count = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(qName.equals("CATEGORYTEXT")) {
            isCATEGORYTEXT = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("CATEGORYTEXT")) {
            isCATEGORYTEXT = false;
        }
        if(qName.equals("SHOP")) {
            System.out.println(count);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isCATEGORYTEXT) {
            String strings = new String(ch, start,length);
            if(length>1) {
                parseSegments = strings.split("\\|");
                if(parseSegments.length>=2) {
                    if(parseSegments[0].contains("Heureka.sk")) {
                        if (map.containsKey(parseSegments[1])) {
                            map.put(parseSegments[1], map.get(parseSegments[1]) + 1);
                        } else {
                            map.put(parseSegments[1], 1);
                        }
                        count++;
                    }
                    else{
                        if (map.containsKey(parseSegments[0])) {
                            map.put(parseSegments[0], map.get(parseSegments[0]) + 1);
                        } else {
                            map.put(parseSegments[0], 1);
                        }
                        count++;
                    }
                }
                else {
                    System.out.println("CATEGORYTEXT without | separator");
                }
            }
            else{
                System.out.println("No waaay!");
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {

        try {
            FileWriter myWriter = new FileWriter("elements.txt");
            for (String i : map.keySet()) {
                myWriter.write(i.strip()  + ";" + map.get(i) + '\n');
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
