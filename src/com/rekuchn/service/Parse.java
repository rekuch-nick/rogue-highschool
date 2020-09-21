package com.rekuchn.service;

public class Parse {


    public static boolean htmlStart(String data){
        if(data.startsWith("\"><span")){
            return true;
        }
        return false;
    }

    public static String htmlColor(String data){
        for(int i=0; i<data.length(); i++){
            //System.out.println(data.charAt(i));
            if(data.startsWith("color:")){
                return data.substring(6);
            }
            data = data.substring(1);
        }
        return "";
    }

    public static String htmlGlyph(String data){
        for(int i=0; i<data.length(); i++){
            if(data.startsWith(";\">")){
                //System.out.println();
                //System.out.println(data.substring(i + 3, 1));
                //System.out.println();

                return data.substring(3);
            }
            data = data.substring(1);
        }
        return "";
    }
}
