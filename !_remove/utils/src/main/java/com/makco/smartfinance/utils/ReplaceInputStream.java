package com.makco.smartfinance.utils;
//package com.makco.smartfinance.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * User: Makar Kalancha
 * Date: 13/03/2016
 * Time: 01:57
 */
public class ReplaceInputStream extends FilterInputStream{
    private final static Logger LOG = LogManager.getLogger(ReplaceInputStream.class);
    private final LinkedList<Integer> inQueue = new LinkedList<Integer>();
    private final LinkedList<Integer> outQueue = new LinkedList<Integer>();
    private final byte[] needle;
    private final byte[] replace;

    public ReplaceInputStream(InputStream in, byte[] needle, byte[] replace) {
        super(in);
        this.needle = needle;
        this.replace = replace;
    }

    @Override
    public int read() throws IOException {
        if(outQueue.isEmpty()){
            readAhead();
            if(isMatchFound()){
                for(int i = 0 ;i<needle.length;i++) {
                    inQueue.remove();
                }
                for(byte b : replace){
                    outQueue.offer((int) b);
                }
            } else {
                outQueue.add(inQueue.remove());
            }
        }
        return outQueue.remove();
    }

    private boolean isMatchFound(){
        Iterator<Integer> inIter = inQueue.iterator();
        for (int i = 0; i < needle.length; i++) {
            if(!inIter.hasNext() || needle[i] != inIter.next()){
                return false;
            }
        }
        return true;
    }

    private void readAhead() throws IOException {
        while (inQueue.size() < needle.length) {
            int next = super.read();
            inQueue.offer(next);
            if(next == -1){
                break;
            }
        }
    }
}
