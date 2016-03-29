package com.makco.smartfinance.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * User: Makar Kalancha
 * Date: 13/03/2016
 * Time: 03:29
 */
public class ReplaceInputStreamTest {
    private byte[] needle;
    private byte[] replace;
    @Before
    public void setUp() throws Exception {
        needle = "needle".getBytes("UTF-8");
        replace = "pineapple".getBytes("UTF-8");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReplaceInputStream_String() throws Exception{
        byte[] bytes = "apple ~needle~ pear plum".getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        InputStream ris = new ReplaceInputStream(bais, needle, replace);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while((b = ris.read()) != -1){
            baos.write(b);
        }
        String result = new String(baos.toByteArray());
        System.out.println(result);
        assertEquals(result, "apple ~pineapple~ pear plum");
    }

    @Test
    public void testReplaceInputStream_file() throws Exception{
        byte[] bytes = "apple ~needle~ pear plum".getBytes("UTF-8");
        String fileName = "file.txt";
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

        InputStream ris = new ReplaceInputStream(is, needle, replace);
        FileOutputStream fos = new FileOutputStream("file_output.txt");
        int b;
        while((b = ris.read()) != -1){
            fos.write(b);
        }
//        String result = new String(baos.toByteArray());
//        System.out.println(result);
//        assertEquals(result, "apple ~pineapple~ pear plum");
    }
}