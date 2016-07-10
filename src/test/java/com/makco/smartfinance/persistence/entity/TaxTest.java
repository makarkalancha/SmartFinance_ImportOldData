package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.session.Tax_v1;
import com.makco.smartfinance.utils.BigDecimalUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 29 Jun 2016.
 */
public class TaxTest {

    private Tax_v1 one;
    private Tax_v1 two;
    private Tax_v1 three;
    private Tax_v1 four;
    private Tax_v1 five;
    private Tax_v1 six;
    private Tax_v1 seven;
    private Tax_v1 nine;

    @Before
    public void setTaxFields() throws Exception{
        one = new Tax_v1(
                "TAX1",
                "tax number 1",
                new BigDecimal("1"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        one.setId(1L);

        two = new Tax_v1(
                "TAX2",
                "tax number 2",
                new BigDecimal("2"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        two.setId(2L);

        three = new Tax_v1(
                "TAX3",
                "tax number 3",
                new BigDecimal("3"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        three.setId(3L);

        four = new Tax_v1(
                "TAX4",
                "tax number 4",
                new BigDecimal("4"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        four.setId(4L);

        five = new Tax_v1(
                "TAX5",
                "tax number 5",
                new BigDecimal("5"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        five.setId(5L);

        six = new Tax_v1(
                "TAX6",
                "tax number 6",
                new BigDecimal("6"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        six.setId(6L);

        Set<Tax_v1> taxSet7 = new HashSet<>();
        taxSet7.add(five);
        taxSet7.add(two);
        seven = new Tax_v1(
                "TAX7",
                "tax number 7",
                new BigDecimal("7"),
                //num+tax5-rate
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("+")
                        .append(DataBaseConstants.getTaxChildIdPlaceholder(five.getId()))
                        .append("-")
                        .append(DataBaseConstants.getTaxChildIdPlaceholder(two.getId()))
                        .append("/")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .toString(),
                null,
                null,
                null,
                taxSet7
        );
        seven.setId(7L);
        five.setParentTaxes(new ArrayList<Tax_v1>(){{
            addAll(five.getParentTaxes());
            add(seven);

        }});

        Set<Tax_v1> taxSet9 = new HashSet<>();
        taxSet9.add(five);
        taxSet9.add(six);
        nine = new Tax_v1(
                "TAX9",
                "tax number 9",
                new BigDecimal("9"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.getTaxChildIdPlaceholder(five.getId()))
                        .append("/100)")
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                taxSet9
        );
        nine.setId(9L);
        five.setParentTaxes(new ArrayList<Tax_v1>(){{
            addAll(five.getParentTaxes());
            add(nine);

        }});
        six.setParentTaxes(new ArrayList<Tax_v1>(){{
            addAll(nine.getParentTaxes());
            add(nine);
        }});
    }
    //TaxFormulaEditorController.fillChildTaxes
    private Set<Tax_v1> fillChildTaxes(String taxFormula, List<Tax_v1> taxList) throws Exception {
        Set<Tax_v1> childTaxes = new HashSet<>();
        Pattern patternFormula = Pattern.compile(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN);
        Matcher matcherFormula = patternFormula.matcher(taxFormula);
        while (matcherFormula.find()) {
            System.out.println(">>>>matcher.group: " + matcherFormula.group());
            long taxId = DataBaseConstants.getTaxChildId(matcherFormula.group());
            System.out.println(">>>>taxId: " + taxId);
            Tax_v1 tax_v1 = taxList.stream()
                    .filter(tax -> tax.getId().equals(taxId))
                    .findFirst()
                    .orElse(null);
            if(tax_v1 != null){
                childTaxes.add(tax_v1);
            }
        }
        return childTaxes;
    }

    @Test
    public void testCalculateFormula_1() throws Exception {
        Tax tax = new Tax(
                "TAX1",
                "tax number 1",
                new BigDecimal("0.05"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append(")")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        System.out.println(">>>Formula:" + tax.getFormula());
        BigDecimal result = BigDecimalUtils.calculateFormulaRPN(tax.getDenormalizedFormula(), new BigDecimal("100"));
        assertEquals(new BigDecimal("105.00"), result);
    }

    @Test
    public void testDenormalize_1() throws Exception {
        assertEquals("{NUM}*(1+5/100)*(1+9/100)", nine.getDenormalizedFormula());
        assertEquals(1, nine.getChildTaxes().size());
    }

    @Test
    public void testRefreshFormula() {
        assertEquals("{NUM}*(1+5/100)*(1+9/100)", nine.getDenormalizedFormula());
        assertEquals(1, nine.getChildTaxes().size());
    }

    @Test
    public void testFillChildTaxes_1() throws Exception {
        List<Tax_v1> taxList = new ArrayList<Tax_v1>() {{
            add(one);
            add(two);
            add(five);//<-in nine
            add(six);//<-in nine
        }};
        String taxFormula = "{NUM}*(1+{TAX5}/100)*(1+{TAX6}/100)";

        Pattern patternFormula = Pattern.compile(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN);
        Matcher matcherFormula = patternFormula.matcher(taxFormula);

        Pattern patternTaxId = Pattern.compile("\\d+");
        while (matcherFormula.find()) {
            System.out.println(">>>>matcher.group: " + matcherFormula.group());
            Matcher matcherTaxId = patternTaxId.matcher(matcherFormula.group());
            if (matcherTaxId.find()) {
                System.out.println(">>>>matcherTaxId: " + matcherTaxId.group());
            }
        }

        matcherFormula.reset();
        System.out.println("======================================");
        Set<Tax_v1> childTaxes = new HashSet<>();
        while (matcherFormula.find()) {
            System.out.println(">>>>matcher.group: " + matcherFormula.group());
            long taxId = DataBaseConstants.getTaxChildId(matcherFormula.group());
            System.out.println(">>>>taxId: " + taxId);
            childTaxes.add(
                    taxList.stream()
                            .filter(tax -> tax.getId().equals(taxId))
                            .findFirst()
                            .get()
            );
        }

        assertEquals(2, childTaxes.size());
        assert (childTaxes.contains(five));
        assert (childTaxes.contains(six));
    }

    @Test
    //{TAXa} is not a pattern like {TAX\\d+}, so result should contain only 1 record
    public void testFillChildTaxes_2_invalidFormula() throws Exception {
        List<Tax_v1> taxList = new ArrayList<Tax_v1>() {{
            add(one);
            add(two);
            add(five);//<-in nine
            add(six);//<-in nine
        }};
        String taxFormula = "{NUM}*(1+{TAX5}/100)*(1+{TAXa}/100)";

        Set<Tax_v1> childTaxes = fillChildTaxes(taxFormula, taxList);

        assertEquals(1, childTaxes.size());
        assert (childTaxes.contains(five));
    }

    @Test
    //{TAX100} is not in tax list, so result should contain only 1 record
    public void testFillChildTaxes_3_invalidFormula() throws Exception {
        List<Tax_v1> taxList = new ArrayList<Tax_v1>() {{
            add(one);
            add(two);
            add(five);//<-in nine
            add(six);//<-in nine
        }};
        String taxFormula = "{NUM}*(1+{TAX5}/100)*(1+{TAX100}/100)";
        Set<Tax_v1> childTaxes = fillChildTaxes(taxFormula, taxList);

        assertEquals(1, childTaxes.size());
        assert (childTaxes.contains(five));
    }

    @Test
    public void test_changeParentRate() throws Exception {
        /*
        seven has five, so we change five's rate
        nine has five and six, so we change five's rate
         */
        assertEquals (new BigDecimal("5"), five.getRate());
        assertEquals ("{NUM}+5-2/7", seven.getDenormalizedFormula());
        assertEquals ("{NUM}*(1+5/100)*(1+9/100)", nine.getDenormalizedFormula());

        five.setRate(new BigDecimal("55"));
        assertEquals (new BigDecimal("55"), five.getRate());
        assertEquals ("{NUM}+55-2/7", seven.getDenormalizedFormula());
        assertEquals ("{NUM}*(1+55/100)*(1+9/100)", nine.getDenormalizedFormula());
        String sevenDenormForm2 = seven.getDenormalizedFormula();
        String nineDenormForm2 = nine.getDenormalizedFormula();
        System.out.println(">>>1:" + sevenDenormForm2);
        System.out.println(">>>2:" + nineDenormForm2);

    }
}