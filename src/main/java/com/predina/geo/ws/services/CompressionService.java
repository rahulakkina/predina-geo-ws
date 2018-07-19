package com.predina.geo.ws.services;

import java.io.File;
import java.util.StringTokenizer;

public interface CompressionService {

    public String gzipIt(String sourceFile);

    public String lzmaIt(String sourceFile);

    default Double fileSizeInKb(final String sourceFile){
        return ((double)new File(sourceFile).length()) / 1024.0;
    }

    default String getSimpleFileName(String zFileName){
        StringTokenizer tokenizer = new StringTokenizer(zFileName, "/");
        String value = null;
        while(tokenizer.hasMoreElements()){
            value = tokenizer.nextToken();
        }
        return value;
    }

}
