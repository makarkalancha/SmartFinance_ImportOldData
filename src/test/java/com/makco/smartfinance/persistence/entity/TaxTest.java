package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.session.Tax_v1;
import com.makco.smartfinance.utils.BigDecimalUtils;
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
        Tax_v1 five = new Tax_v1(
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

        Set<Tax_v1> taxSet = new HashSet<>();
        taxSet.add(five);
        Tax_v1 nine = new Tax_v1(
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
                taxSet
        );
        nine.setId(9L);
        String denormalizedFormula = nine.getDenormalizedFormula();
        assertEquals("{NUM}*(1+5/100)*(1+9/100)", denormalizedFormula);
    }

    @Test
    public void testRefreshFormula(){
        Tax_v1 five = new Tax_v1(
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

        Tax_v1 six = new Tax_v1(
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

        Set<Tax_v1> taxSet = new HashSet<>();
        taxSet.add(five);
        taxSet.add(six);
        Tax_v1 nine = new Tax_v1(
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
                taxSet
        );
        nine.setId(9L);
        String denormalizedFormula = nine.getDenormalizedFormula();
        assertEquals("{NUM}*(1+5/100)*(1+9/100)", denormalizedFormula);
        assertEquals(1,nine.getChildTaxes().size());
    }

    @Test
    public void testFillChildTaxes_1() throws Exception{
        Tax_v1 one = new Tax_v1(
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

        Tax_v1 two = new Tax_v1(
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

        Tax_v1 five = new Tax_v1(
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

        Tax_v1 six = new Tax_v1(
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

        Set<Tax_v1> taxSet = new HashSet<>();
        taxSet.add(five);
        taxSet.add(six);
        Tax_v1 nine = new Tax_v1(
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
                taxSet
        );
        nine.setId(9L);
        List<Tax_v1> taxList = new ArrayList<Tax_v1>(){{
            add(one);
            add(two);
            add(five);//<-in nine
            add(six);//<-in nine
        }};
        String taxFormula = "{NUM}*(1+{TAX5}/100)*(1+{TAX6}/100)";

        Pattern patternFormula = Pattern.compile(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN);
        Matcher matcherFormula = patternFormula.matcher(taxFormula);

        Pattern patternTaxId = Pattern.compile("\\d+");
        while (matcherFormula.find()){
            System.out.println(">>>>matcher.group: "+matcherFormula.group());
            Matcher matcherTaxId = patternTaxId.matcher(matcherFormula.group());
            if (matcherTaxId.find()) {
                System.out.println(">>>>matcherTaxId: " + matcherTaxId.group());
            }
        }

        matcherFormula.reset();
        System.out.println("======================================");
        Set<Tax_v1> childTaxes = new HashSet<>();
        while (matcherFormula.find()){
            System.out.println(">>>>matcher.group: "+matcherFormula.group());
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
        assert(childTaxes.contains(five));
        assert(childTaxes.contains(six));
    }

    @Test
    public void testFillChildTaxes_2_invalidFormula() throws Exception{
        Tax_v1 one = new Tax_v1(
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

        Tax_v1 two = new Tax_v1(
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

        Tax_v1 five = new Tax_v1(
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

        Tax_v1 six = new Tax_v1(
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

        Set<Tax_v1> taxSet = new HashSet<>();
        taxSet.add(five);
        taxSet.add(six);
        Tax_v1 nine = new Tax_v1(
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
                taxSet
        );
        nine.setId(9L);
        List<Tax_v1> taxList = new ArrayList<Tax_v1>(){{
            add(one);
            add(two);
            add(five);//<-in nine
            add(six);//<-in nine
        }};
        String taxFormula = "{NUM}*(1+{TAX5}/100)*(1+{TAXa}/100)";

        Pattern patternFormula = Pattern.compile(DataBaseConstants.TAX_CHILD_ID_PLACEHOLDER_PATTERN);
        Matcher matcherFormula = patternFormula.matcher(taxFormula);

        Set<Tax_v1> childTaxes = new HashSet<>();
        while (matcherFormula.find()){
            System.out.println(">>>>matcher.group: "+matcherFormula.group());
            long taxId = DataBaseConstants.getTaxChildId(matcherFormula.group());
            System.out.println(">>>>taxId: " + taxId);
            childTaxes.add(
                    taxList.stream()
                            .filter(tax -> tax.getId().equals(taxId))
                            .findFirst()
                            .get()
            );
        }

        assertEquals(1, childTaxes.size());
        assert(childTaxes.contains(five));
    }

}