package com.makco.smartfinance.javafx.control;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by mcalancea on 2016-05-16.
 */
public class AutoCompleteComboBoxTest {
    List<String> initialList = new ArrayList<>();

    @Before
    public void setUpTest() throws Exception{
        initialList.add("jacob.smith@example.com"); //1
        initialList.add("isabella.johnson@example.com"); //2
        initialList.add("ethan.williams@example.com");//3
        initialList.add("emma.jones@example.com");//4
        initialList.add("michael.brown@example.com");//5
        initialList.add("Daniel");//6
        initialList.add("Dustin");//7
        initialList.add("David");//8
        initialList.add("Damascus");//9
        initialList.add("Russ");//10
        initialList.add("UpdateAttributeAbstractHelper");//11
        initialList.add("UpdateAttributeAbstractHelperBean");//12
        initialList.add("UpdateAttributeAbstractHelperLocal");//13
        initialList.add("хлеб");//14
        initialList.add("молоко");//15
        initialList.add("масло раст");//16
        initialList.add("масло слив");//17
        initialList.add("помидоры зел");//18
        initialList.add("помидоры желт");//19
        initialList.add("emma(johnson)");//20
    }

    @Test
    public void test_regex_emma () throws Exception{
        String needle = "emma";
        List<String> testResult = filterString(needle);
        System.out.println(testResult);
        assertArrayEquals(new String[] {
                "emma.jones@example.com",
                "emma(johnson)"
        }, testResult.toArray());
    }

    @Test
    public void test_regex_em () throws Exception{
        String needle = "em";
        List<String> testResult = filterString(needle);
        System.out.println(testResult);
        assertArrayEquals(new String[] {"jacob.smith@example.com",
                "isabella.johnson@example.com",
                "ethan.williams@example.com",
                "emma.jones@example.com",
                "michael.brown@example.com",
                "emma(johnson)"
        }, testResult.toArray());
    }

    @Test
    public void test_regex_with_parenthesis () throws Exception{
        String needle = "em(";
        List<String> testResult = filterString(needle);
        System.out.println(testResult);
        assertArrayEquals(new String[] {
                "emma(johnson)"
        }, testResult.toArray());
    }

    private List<String> filterString(String filter){
        List<String> result = new ArrayList<>();
        StringBuilder regex = new StringBuilder();
        /**
         * accidently pasted "Platform.runLater(new Runnable() {" and regex crashes because of "("
         * unit test for regex
         * Exception in thread "JavaFX Application Thread" java.util.regex.PatternSyntaxException: Unclosed group near index xx
         * j.*a.*(.*
         * in order to escape special characters:
         * \Q -> Nothing, but quotes all characters until \E
         * \E -> Nothing, but ends quoting started by \Q
         */
        for (int i = 0; i < filter.length(); i++) {
            regex.append("\\Q");
            regex.append(filter.charAt(i));
            regex.append("\\E.*");
        }
        Pattern pattern = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE );
        /**
         * Pattern.LITERAL doesn't help, becuase characters ".*" aslo are not considered
         */
//        Pattern pattern = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
        for (String string : initialList) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                result.add(string);
            }
        }
        return result;
    }

}