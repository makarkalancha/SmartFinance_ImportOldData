package com.makco.smartfinance.h2db.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makco.smartfinance.h2db.DBConnectionResource;
import com.makco.smartfinance.h2db.functions.TestDateUnitFunctions;
import com.makco.smartfinance.h2db.utils.H2DbUtilsTest;
import com.makco.smartfinance.h2db.utils.JsonUtils;
import com.makco.smartfinance.h2db.utils.TestUtilDateUnit;
import com.makco.smartfinance.h2db.utils.schema_constants.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 11 Jul 2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableItem_NoTrTest {
    private static final Logger LOG = LogManager.getLogger(TableItem_NoTrTest.class);
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TableOrganizationTest tableOrganizationTest = new TableOrganizationTest();
    private TableInvoice_NoTrTest tableInvoiceTest = new TableInvoice_NoTrTest();
    private TableCategoryGroupTest tableCategoryGroupTest = new TableCategoryGroupTest();
    private TableCategoryTest tableCategoryTest = new TableCategoryTest();
    private TableTaxTest tableTaxTest = new TableTaxTest();
    private TableFamilyMemberTest tableFamilyMemberTest = new TableFamilyMemberTest();

    @ClassRule
    public static DBConnectionResource dbConnectionResource = new DBConnectionResource();

    @BeforeClass
    public static void setUpClass() throws Exception {
        String mess1 = "TriggerItemTest: Test->BeforeClass";
        System.out.println(mess1);
        LOG.debug(mess1);
        //        H2DbUtils.setSchema(dbConnectionResource.getConnection(), "TEST");
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM_notr);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ITEM);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE_notr);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.INVOICE);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.ORGANIZATION);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.FAMILY_MEMBER);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TAX_CHILD);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.TAX);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY);
        H2DbUtilsTest.emptyTable(dbConnectionResource.getConnection(), Table.Names.CATEGORY_GROUP);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //first remove schema test
        String mess1 = "TriggerItemTest: Test->AfterClass";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @Before
    public void setUp() throws Exception {
        String mess1 = "TriggerItemTest: Test->Before";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    @After
    public void tearDown() throws Exception {
        String mess1 = "TriggerItemTest: Test->After";
        System.out.println(mess1);
        LOG.debug(mess1);
    }

    public Long insert(Integer orderNumber, Long invoiceId, Long categoryId, Long taxId,
                       Long familyMemberId,
                       String description1, String description2,
                       String comment, BigDecimal grossAmount, BigDecimal netAmount) throws Exception {
        LOG.debug("insert");
        String queryInsert = "INSERT INTO " + Table.Names.ITEM_notr +
                " (" + Table.ITEM.ORDER_NUMBER + ", " + Table.ITEM.INVOICE_ID + ", " + Table.ITEM.CATEGORY_ID + ", " +
                Table.ITEM.TAX_ID + ", " + Table.ITEM.FAMILY_MEMBER_ID + ", " + Table.ITEM.DESCRIPTION1 + ", " +
                Table.ITEM.DESCRIPTION2 + ", " + Table.ITEM.COMMENT + ", " + Table.ITEM.SUB_TOTAL + ", " +
                Table.ITEM.TOTAL + ") " +
                "VALUES(" + orderNumber + ", " + invoiceId + ", " + categoryId + ", " + taxId + ", " +
                familyMemberId + ", '" +
                description1 + "', '" + description2 + "', '" + comment + "', " +
                grossAmount + ", " + netAmount + ")";

        LOG.debug(queryInsert);
        ResultSet rs = null;
        Long result = -1L;
        try (
                PreparedStatement insertPS = dbConnectionResource.getConnection().prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
        ){
            insertPS.executeUpdate();
            rs = insertPS.getGeneratedKeys();
            rs.next();
            result = rs.getLong(1);
            return result;
        } finally {
            if(rs != null){
                rs.close();
            }
        }
    }

    @Test
    public void testItem_11_insert() throws Exception {
        LOG.debug("testItem_11_insert");
        String queryItemDates = "SELECT " + Table.ITEM.ID + ", " + Table.ITEM.DATEUNIT_UNITDAY + " FROM " + Table.Names.ITEM_notr +
                " WHERE " + Table.ITEM.ID + " = ?" +
                " AND " + Table.ITEM.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_CREATEDON + " = " + Table.ITEM.T_UPDATEDON;

        String queryInvoice = "SELECT " + Table.INVOICE.ID + ", " + Table.INVOICE.DATEUNIT_UNITDAY + " FROM " + Table.Names.INVOICE_notr +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.SUB_TOTAL + "  = (SELECT SUM(" + Table.ITEM.SUB_TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )" +
                " AND " + Table.INVOICE.TOTAL + "  = (SELECT SUM(" + Table.ITEM.TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )";

        LOG.debug(queryItemDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectItemDatesPS = dbConnectionResource.getConnection().prepareStatement(queryItemDates);
                PreparedStatement selectInvoicePS = dbConnectionResource.getConnection().prepareStatement(queryInvoice);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            long organizationId = tableOrganizationTest.insert("testItem_11_insert", "Organization Description From TableItemTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert("Item_11_insert", organizationId,
                    dateunitUnitday, "invoice comment");

            long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group11", "debit category group desc");
            long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category", "debit category desc");

            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long taxId = tableTaxTest.insert("tax name11", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

            long familyMemberId = tableFamilyMemberTest.insert("family member #11", "family member desc");

            long idJustInserted = insert(
                    1, invoiceId, categoryId, taxId, familyMemberId,
                    "product desc1", "product desc2", "comment",
                    new BigDecimal("5.0"), new BigDecimal("15.0"));
            LOG.debug("idJustInserted > 0: idJustInserted=" + idJustInserted);
            assert (idJustInserted > 0);
            selectItemDatesPS.setLong(1, idJustInserted);
            rs = selectItemDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            long dateUnitItemWithDates = rs.getLong(2);
            LOG.debug("idWithDates > 0: idWithDates=" + idWithDates);
            LOG.debug("idJustInserted == idWithDates: " + (idJustInserted == idWithDates) +
                    "; idJustInserted=" + idJustInserted +
                    "; idWithDates=" + idWithDates);
            assert (idWithDates > 0);
            assert (idJustInserted == idWithDates);
            LOG.debug(String.format("dateunit: invoice (to be inserted)=%s; item=%s", dateunitUnitday, dateUnitItemWithDates));
            assert (dateunitUnitday != dateUnitItemWithDates);

            selectInvoicePS.setLong(1, invoiceId);
            selectInvoicePS.setLong(2, invoiceId);
            selectInvoicePS.setLong(3, invoiceId);
            rs = selectInvoicePS.executeQuery();
            boolean dataIsAvail= rs.next();
            assert (!dataIsAvail);

        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test(expected=JdbcSQLException.class)
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_TM_NVCDRDRNMBR ON TEST.ITEM(INVOICE_ID, ORDER_NUMBER) VALUES (31, 1, 13)"; SQL statement:
    public void testItem_12_insert_duplicate() throws Exception {
        LOG.debug("testItem_11_insert");

        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        long organizationId = tableOrganizationTest.insert("testItem_12_insert", "Organization Description From TableItemTest #testItem_11_insert");
        long invoiceId = tableInvoiceTest.insert("Item_12_insert", organizationId,
                dateunitUnitday, "invoice comment");

        long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group12", "debit category group desc");
        long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category12", "debit category desc");

        Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        long taxId = tableTaxTest.insert("tax name12", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

        long familyMemberId = tableFamilyMemberTest.insert("family member #12", "family member desc");

        insert(
                1, invoiceId, categoryId, taxId, familyMemberId,
                "product desc1", "product desc2", "comment",
                new BigDecimal("5.0"), new BigDecimal("15.0"));
        insert(
                1, invoiceId, categoryId, taxId, familyMemberId,
                "product desc1", "product desc2", "comment",
                new BigDecimal("5.0"), new BigDecimal("15.0"));
    }

    public void updateComment(Long id, String comment) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.ITEM_notr + " SET " + Table.ITEM.COMMENT + " = ? " +
                " WHERE " + Table.ITEM.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setString(1, comment);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    public void updateOrderNumbert(Long id, Integer orderNumber) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.ITEM_notr + " SET " + Table.ITEM.ORDER_NUMBER + " = ? " +
                " WHERE " + Table.ITEM.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setInt(1, orderNumber);
            updatePS.setLong(2, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    public void updateSubtotalAndTotal(Long id, BigDecimal subtotal, BigDecimal total) throws Exception {
        LOG.debug("update");
        String queryUpdate = "UPDATE " + Table.Names.ITEM_notr + " SET " + Table.ITEM.SUB_TOTAL + " = ? , " +
                Table.ITEM.TOTAL + " = ? " +
                " WHERE " + Table.ITEM.ID + " = ?";
        LOG.debug(queryUpdate);
        ResultSet rs = null;
        try (
                PreparedStatement updatePS = dbConnectionResource.getConnection().prepareStatement(queryUpdate);
        ){
            updatePS.setBigDecimal(1, subtotal);
            updatePS.setBigDecimal(2, total);
            updatePS.setLong(3, id);
            updatePS.executeUpdate();
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testItem_21_update_check_T_UPDATEDON() throws Exception {
        LOG.debug("testItem_21_update");
        String querySelect = "SELECT MAX(" + Table.ITEM.ID + ") FROM " + Table.Names.ITEM_notr;
        String queryDates = "SELECT " + Table.ITEM.ID + ", " + Table.ITEM.INVOICE_ID + " FROM " + Table.Names.ITEM_notr +
                " WHERE " + Table.ITEM.ID + " = ?" +
                " AND " + Table.ITEM.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.ITEM.T_CREATEDON + " != " + Table.ITEM.T_UPDATEDON;
        String queryInvoice = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE_notr +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.SUB_TOTAL + "  = (SELECT SUM(" + Table.ITEM.SUB_TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )" +
                " AND " + Table.INVOICE.TOTAL + "  = (SELECT SUM(" + Table.ITEM.TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )";

        LOG.debug(querySelect);
        LOG.debug(queryDates);
        ResultSet rs = null;
        long idMax = 0L;
        try (
                PreparedStatement selectPS = dbConnectionResource.getConnection().prepareStatement(querySelect);
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
                PreparedStatement selectInvoicePS = dbConnectionResource.getConnection().prepareStatement(queryInvoice);
        ){
            rs = selectPS.executeQuery();
            rs.next();
            idMax = rs.getLong(1);

            updateComment(idMax, "_new comment");

            selectDatesPS.setLong(1, idMax);
            rs = selectDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            long invoiceId = rs.getLong(2);
            assert (idWithDates > 0);
            assert (idMax == idWithDates);

            selectInvoicePS.setLong(1, invoiceId);
            selectInvoicePS.setLong(2, invoiceId);
            selectInvoicePS.setLong(3, invoiceId);
            rs = selectInvoicePS.executeQuery();
            boolean dataIsAvail = rs.next();
            assert (!dataIsAvail);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testItem_22_update_check_invoiceSubtotalTotal() throws Exception {
        LOG.debug("testItem_22_update");
        String queryInvoice = "SELECT " + Table.INVOICE.ID + "," + Table.INVOICE.SUB_TOTAL + "," + Table.INVOICE.TOTAL + " FROM " + Table.Names.INVOICE_notr +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.SUB_TOTAL + "  = (SELECT SUM(" + Table.ITEM.SUB_TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )" +
                " AND " + Table.INVOICE.TOTAL + "  = (SELECT SUM(" + Table.ITEM.TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )";

        LOG.debug(queryInvoice);
        ResultSet rs = null;
        try (
                PreparedStatement selectInvoicePS = dbConnectionResource.getConnection().prepareStatement(queryInvoice);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            long organizationId = tableOrganizationTest.insert("testItem_22_update", "Organization Description From TableItemTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert("Item_22_update", organizationId,
                    dateunitUnitday, "invoice comment");

            long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group22", "debit category group desc");
            long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category", "debit category desc");

            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long taxId = tableTaxTest.insert("tax name22", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

            long familyMemberId = tableFamilyMemberTest.insert("family member #22", "family member desc");

            long idJustInserted1 = insert(
                    1, invoiceId, categoryId, taxId, familyMemberId,
                    "product1 desc1", "product1 desc2", "comment",
                    new BigDecimal("5.0"), new BigDecimal("15.0"));
            insert(
                    2, invoiceId, categoryId, taxId, familyMemberId,
                    "product2 desc1", "product2 desc2", "comment",
                    new BigDecimal("10.0"), new BigDecimal("30.0"));

            //check sum after insert
            selectInvoicePS.setLong(1, invoiceId);
            selectInvoicePS.setLong(2, invoiceId);
            selectInvoicePS.setLong(3, invoiceId);
            rs = selectInvoicePS.executeQuery();
            boolean dataIsAvail = rs.next();
            assert (!dataIsAvail);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testItem_23_update_check_invoiceUpdateDate() throws Exception {
        //checks TriggerInvoice.update
        LOG.debug("testItem_23_update");
        String queryDates = "SELECT " + Table.INVOICE.ID + " FROM " + Table.Names.INVOICE_notr +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.T_CREATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_UPDATEDON + " IS NOT NULL" +
                " AND " + Table.INVOICE.T_CREATEDON + " != " + Table.INVOICE.T_UPDATEDON;
        String items = "SELECT " + Table.ITEM.ID + ", " + Table.ITEM.DATEUNIT_UNITDAY +
                " FROM " + Table.Names.ITEM_notr +
                " WHERE " + Table.ITEM.INVOICE_ID + " = ?";
        LOG.debug(queryDates);
        ResultSet rs = null;
        try (
                PreparedStatement selectDatesPS = dbConnectionResource.getConnection().prepareStatement(queryDates);
                PreparedStatement selectItemsPS = dbConnectionResource.getConnection().prepareStatement(items);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            long organizationId = tableOrganizationTest.insert("testItem_23_update",
                    "Organization Description From TableItemTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert("Item_23_update", organizationId,
                    dateunitUnitday, "invoice comment");

            long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group23", "debit category group desc");
            long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category23", "debit category desc");

            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long taxId = tableTaxTest.insert("tax name23", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

            long familyMemberId = tableFamilyMemberTest.insert("family member #23", "family member desc");

            insert(1, invoiceId, categoryId, taxId, familyMemberId,
                    "product desc11", "product desc21", "comment",
                    new BigDecimal("5.0"), new BigDecimal("15.0"));

            insert(2, invoiceId, categoryId, taxId, familyMemberId,
                    "product desc12", "product desc22", "comment",
                    new BigDecimal("6.0"), new BigDecimal("26.0"));

            insert(3, invoiceId, categoryId, taxId, familyMemberId,
                    "product desc13", "product desc23", "comment",
                    new BigDecimal("7.0"), new BigDecimal("37.0"));

            LOG.debug(">>>>old date=" + dateunitUnitday);

            selectItemsPS.setLong(1, invoiceId);
            rs = selectItemsPS.executeQuery();
            while(rs.next()) {
                long itemId = rs.getLong(1);
                long itemDate = rs.getLong(2);
                LOG.debug(String.format(">>>>before item: id=%s, date=%s; invoice.date=%s", itemId, itemDate, dateunitUnitday));
                assert (dateunitUnitday != itemDate);
            }

            long newDateunitInvoiceWithIdMax = dateunitUnitday + 10;
            LocalDate newLocalDate = TestUtilDateUnit.EPOCH.plus(newDateunitInvoiceWithIdMax, ChronoUnit.DAYS);
            TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            tableInvoiceTest.updateDate(invoiceId, newDateunitInvoiceWithIdMax);

            selectDatesPS.setLong(1, invoiceId);
            rs = selectDatesPS.executeQuery();
            rs.next();
            long idWithDates = rs.getLong(1);
            assert (idWithDates > 0);
            assert (invoiceId == idWithDates);

            selectItemsPS.setLong(1, invoiceId);
            rs = selectItemsPS.executeQuery();
            while(rs.next()) {
                long itemId = rs.getLong(1);
                long itemDate = rs.getLong(2);
                LOG.debug(String.format(">>>>after item: id=%s, date=%s; invoice.date=%s", itemId, itemDate, newDateunitInvoiceWithIdMax));
                assert (newDateunitInvoiceWithIdMax != itemDate);
            }

        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test(expected=JdbcSQLException.class)
    //org.h2.jdbc.JdbcSQLException: Unique index or primary key violation: "IDX_UNQ_TM_NVCDRDRNMBR ON TEST.ITEM(INVOICE_ID, ORDER_NUMBER) VALUES (32, 2, 16)"; SQL statement:
    public void testItem_24_update_duplicate() throws Exception {
        long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        long organizationId = tableOrganizationTest.insert("testItem_24_update",
                "Organization Description From TableItemTest #testItem_24_update");
        long invoiceId = tableInvoiceTest.insert("Item_24_update", organizationId,
                dateunitUnitday, "invoice comment");

        long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group24", "debit category group desc");
        long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category24", "debit category desc");

        Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        long taxId = tableTaxTest.insert("tax name24", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

        long familyMemberId = tableFamilyMemberTest.insert("family member #24", "family member desc");

        long itemId = insert(1, invoiceId, categoryId, taxId, familyMemberId,
                "product desc11", "product desc21", "comment",
                new BigDecimal("5.0"), new BigDecimal("15.0"));

        insert(2, invoiceId, categoryId, taxId, familyMemberId,
                "product desc12", "product desc22", "comment",
                new BigDecimal("6.0"), new BigDecimal("26.0"));

        insert(3, invoiceId, categoryId, taxId, familyMemberId,
                "product desc13", "product desc23", "comment",
                new BigDecimal("7.0"), new BigDecimal("37.0"));

        updateOrderNumbert(itemId, 2);
    }

    @Test
    public void testItem_31_delete() throws Exception {
        LOG.debug("testItem_31_delete");
        String querySelect = "SELECT MIN(" + Table.ITEM.ID + ") FROM " + Table.Names.ITEM_notr;
        String queryDelete = "DELETE FROM " + Table.Names.ITEM_notr + " WHERE " +
                Table.ITEM.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.ITEM_notr + "')";

        LOG.debug(querySelect);
        LOG.debug(queryDelete);
        LOG.debug(querySelectDeletedRow);
        ResultSet rs = null;
        long idMin = 0L;
        long idNewMin = 0L;
        try (
                PreparedStatement selectPS = dbConnectionResource.getConnection().prepareStatement(querySelect);
                PreparedStatement deletePS = dbConnectionResource.getConnection().prepareStatement(queryDelete);
                PreparedStatement selectDeletedRowsPS = dbConnectionResource.getConnection().prepareStatement(querySelectDeletedRow);
        ){
            rs = selectPS.executeQuery();
            rs.next();
            idMin = rs.getLong(1);

            deletePS.setLong(1, idMin);
            deletePS.executeUpdate();

            rs = selectPS.executeQuery();
            rs.next();
            idNewMin = rs.getLong(1);

            assertEquals(true, idMin != idNewMin);

            rs = selectDeletedRowsPS.executeQuery();
            rs.next();
            String jsonRow = rs.getString(1);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(jsonRow).getAsJsonObject();
            JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

            long idFromDeletedRows = rowJsonObject.get(Table.ITEM.ID.toString()).getAsLong();
            assertEquals(true, idMin == idFromDeletedRows);
        } finally {
            if (rs != null) rs.close();
        }
    }

    @Test
    public void testItem_32_delete() throws Exception {
        LOG.debug("testItem_31_delete");
        String queryInvoice = "SELECT " + Table.INVOICE.ID + "," + Table.INVOICE.SUB_TOTAL + "," + Table.INVOICE.TOTAL + " FROM " + Table.Names.INVOICE_notr +
                " WHERE " + Table.INVOICE.ID + " = ?" +
                " AND " + Table.INVOICE.SUB_TOTAL + "  = (SELECT SUM(" + Table.ITEM.SUB_TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )" +
                " AND " + Table.INVOICE.TOTAL + "  = (SELECT SUM(" + Table.ITEM.TOTAL + ") FROM " + Table.Names.ITEM_notr + " WHERE " + Table.ITEM.INVOICE_ID + " = ? )";
        String queryDelete = "DELETE FROM " + Table.Names.ITEM_notr + " WHERE " +
                Table.ITEM.ID + " = ?";
        String querySelectDeletedRow = "SELECT JSON_ROW FROM _DELETED_ROWS WHERE ID = (SELECT MAX(ID) FROM _DELETED_ROWS WHERE SCHEMA_NAME = 'TEST' AND TABLE_NAME = '" + Table.Names.ITEM_notr + "')";

        LOG.debug(queryDelete);
        LOG.debug(querySelectDeletedRow);
        ResultSet rs = null;
        try (
                PreparedStatement selectInvoicePS = dbConnectionResource.getConnection().prepareStatement(queryInvoice);
                PreparedStatement deletePS = dbConnectionResource.getConnection().prepareStatement(queryDelete);
                PreparedStatement selectDeletedRowsPS = dbConnectionResource.getConnection().prepareStatement(querySelectDeletedRow);
        ){
            long dateunitUnitday = TestDateUnitFunctions.insertSelectDate(dbConnectionResource.getConnection(),
                    Date.from(LocalDate.of(2016, Month.FEBRUARY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            long organizationId = tableOrganizationTest.insert("testItem_32_delete", "Organization Description From TableItemTest #testItem_11_insert");
            long invoiceId = tableInvoiceTest.insert("Item_32_delete", organizationId,
                    dateunitUnitday, "invoice comment");

            long categoryGroupId = tableCategoryGroupTest.insert("D", "debit category group32", "debit category group desc");
            long categoryId = tableCategoryTest.insert(categoryGroupId, "D", "debit category", "debit category desc");

            Date startDate = Date.from(LocalDate.of(2008, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(LocalDate.of(2010, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            long taxId = tableTaxTest.insert("tax name32", "tax desc", new BigDecimal("10"), "1+2", startDate, endDate);

            long familyMemberId = tableFamilyMemberTest.insert("family member #32", "family member desc");

            long idJustInserted1 = insert(
                    1, invoiceId, categoryId, taxId, familyMemberId,
                    "product1 desc1", "product1 desc2", "comment",
                    new BigDecimal("5.0"), new BigDecimal("15.0"));
            insert(
                    2, invoiceId, categoryId, taxId, familyMemberId,
                    "product2 desc1", "product2 desc2", "comment",
                    new BigDecimal("10.0"), new BigDecimal("30.0"));

            selectInvoicePS.setLong(1, invoiceId);
            selectInvoicePS.setLong(2, invoiceId);
            selectInvoicePS.setLong(3, invoiceId);
            rs = selectInvoicePS.executeQuery();
            rs.next();
            long invoiceIdBefore = rs.getLong(1);
            BigDecimal subtotalBefore = rs.getBigDecimal(2);
            BigDecimal totalBefore = rs.getBigDecimal(3);
            LOG.debug("before delete: invoiceIdBefore > 0: invoiceIdBefore=" + invoiceIdBefore);
            LOG.debug("before delete: idJustInserted == invoiceIdBefore: " + (invoiceId == invoiceIdBefore) +
                    "; invoiceId=" + invoiceId +
                    "; invoiceIdBefore=" + invoiceIdBefore);
            assert (invoiceIdBefore > 0);
            assert (invoiceId == invoiceIdBefore);
            LOG.debug("before delete: subtotalBefore=" + subtotalBefore);
            LOG.debug("before delete: totalBefore=" + totalBefore);
            assert(new BigDecimal("15").compareTo(subtotalBefore) == 0);
            assert(new BigDecimal("45").compareTo(totalBefore) == 0);

            deletePS.setLong(1, idJustInserted1);
            deletePS.executeUpdate();

            rs = selectDeletedRowsPS.executeQuery();
            rs.next();
            String jsonRow = rs.getString(1);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(jsonRow).getAsJsonObject();
            JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

            long idFromDeletedRows = rowJsonObject.get(Table.ITEM.ID.toString()).getAsLong();
            assertEquals(true, idJustInserted1 == idFromDeletedRows);

            selectInvoicePS.setLong(1, invoiceId);
            selectInvoicePS.setLong(2, invoiceId);
            selectInvoicePS.setLong(3, invoiceId);
            rs = selectInvoicePS.executeQuery();
            rs.next();
            long invoiceIdAfter = rs.getLong(1);
            BigDecimal subtotalAfter = rs.getBigDecimal(2);
            BigDecimal totalAfter = rs.getBigDecimal(3);
            LOG.debug("after delete: invoiceIdAfter > 0: invoiceIdAfter=" + invoiceIdAfter);
            LOG.debug("after delete: idJustInserted == invoiceIdAfter: " + (invoiceId == invoiceIdAfter) +
                    "; invoiceId=" + invoiceId +
                    "; invoiceIdAfter=" + invoiceIdAfter);
            assert (invoiceIdAfter > 0);
            assert (invoiceId == invoiceIdAfter);
            LOG.debug("after delete: subtotalBefore=" + subtotalAfter);
            LOG.debug("after delete: totalBefore=" + totalAfter);
            assert(new BigDecimal("10").compareTo(subtotalAfter) == 0);
            assert(new BigDecimal("30").compareTo(totalAfter) == 0);
        } finally {
            if (rs != null) rs.close();
        }
    }

    private JsonObject createJsonObject() throws Exception {
        String schemaName = "TEST";

        Object[] row = new Object[Table.ITEM.values().length];
        row[0] = 1L;
        row[1] = 2;
        row[2] = 3L;
        row[3] = 4L;
        row[4] = 5L;
        row[5] = 6L;
        row[6] = "desc1";
        row[7] = "desc2";
        row[8] = "comm";
        row[9] = new BigDecimal("2.0");
        row[10] = new BigDecimal("4.0");
        row[11] = SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06");
        row[12] = SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01");
        JsonObject rowJson = new JsonObject();
        rowJson.addProperty(Table.ITEM.ID.toString(), (Long) row[0]);
        rowJson.addProperty(Table.ITEM.ORDER_NUMBER.toString(), (Integer) row[1]);
        rowJson.addProperty(Table.ITEM.INVOICE_ID.toString(), (Long) row[2]);
        rowJson.addProperty(Table.ITEM.CATEGORY_ID.toString(), (Long) row[3]);
        rowJson.addProperty(Table.ITEM.TAX_ID.toString(), (Long) row[4]);
        rowJson.addProperty(Table.ITEM.FAMILY_MEMBER_ID.toString(), (Long) row[5]);
        rowJson.addProperty(Table.ITEM.DESCRIPTION1.toString(), (String) row[6]);
        rowJson.addProperty(Table.ITEM.DESCRIPTION2.toString(), (String) row[7]);
        rowJson.addProperty(Table.ITEM.COMMENT.toString(), (String) row[8]);
        rowJson.addProperty(Table.ITEM.SUB_TOTAL.toString(), (BigDecimal) row[9]);
        rowJson.addProperty(Table.ITEM.TOTAL.toString(), (BigDecimal) row[10]);
        rowJson.addProperty(Table.ITEM.T_CREATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[11]));
        rowJson.addProperty(Table.ITEM.T_UPDATEDON.toString(), SIMPLE_DATE_TIME_FORMAT.format((Date) row[12]));

        JsonObject tableJson = new JsonObject();
        tableJson.addProperty(Table.Elements.tableName.toString(), schemaName + "." + Table.Names.ITEM_notr);
        tableJson.add(Table.Elements.row.toString(), rowJson);

        return tableJson;
    }

    @Test
    public void testDeleteToJsonObject() throws Exception{
        JsonObject tableJson = createJsonObject();
        String expectedJsonString = "{\"tableName\":\"TEST.ITEM\",\"row\":" +
                "{\"ID\":1,\"ORDER_NUMBER\":2,\"INVOICE_ID\":3,\"CATEGORY_ID\":4,\"TAX_ID\":5," +
                "\"FAMILY_MEMBER_ID\":6,\"DESCRIPTION1\":\"desc1\",\"DESCRIPTION2\":\"desc2\"," +
                "\"COMMENT\":\"comm\",\"SUB_TOTAL\":2.0,\"TOTAL\":4.0," +
                "\"T_CREATEDON\":\"2001-02-03 14:05:06\",\"T_UPDATEDON\":\"2006-05-04 03:02:01\"}}";

        LOG.debug("testDeleteToJsonObject.expectedJsonString:" + expectedJsonString);
        LOG.debug("testDeleteToJsonObject.actualJsonString:" + tableJson.toString());
        assertEquals(expectedJsonString, tableJson.toString());
    }

    @Test
    public void testDeleteReadJsonObject() throws Exception{
        String tableJsonString = createJsonObject().toString();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(tableJsonString).getAsJsonObject();

        String schemaTableName = jsonObject.get(Table.Elements.tableName.toString()).getAsString();
        assertEquals("TEST.ITEM", schemaTableName);

        JsonObject rowJsonObject = jsonObject.get(Table.Elements.row.toString()).getAsJsonObject();

        LOG.debug("testDeleteReadJsonObject.rowJsonObject:" + rowJsonObject.toString());

        long id = rowJsonObject.get(Table.ITEM.ID.toString()).getAsLong();
        assertEquals(1L, id);
        int orderNumber = rowJsonObject.get(Table.ITEM.ORDER_NUMBER.toString()).getAsInt();
        assertEquals(2, orderNumber);
        long invoiceId = rowJsonObject.get(Table.ITEM.INVOICE_ID.toString()).getAsLong();
        assertEquals(3L, invoiceId);
        long categoryId = rowJsonObject.get(Table.ITEM.CATEGORY_ID.toString()).getAsLong();
        assertEquals(4L, categoryId);
        long taxId = rowJsonObject.get(Table.ITEM.TAX_ID.toString()).getAsLong();
        assertEquals(5L, taxId);
        long familyMemberId = rowJsonObject.get(Table.ITEM.FAMILY_MEMBER_ID.toString()).getAsLong();
        assertEquals(6L, familyMemberId);

        JsonElement jsonElementDesc1 = rowJsonObject.get(Table.ITEM.DESCRIPTION1.toString());
        String description1 = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc1);
        assertEquals("desc1", description1);
        JsonElement jsonElementDesc2 = rowJsonObject.get(Table.ITEM.DESCRIPTION2.toString());
        String description2 = JsonUtils.getNullableFromJsonElementAsString(jsonElementDesc2);
        assertEquals("desc2", description2);
        JsonElement jsonElementComm = rowJsonObject.get(Table.ITEM.COMMENT.toString());
        String comment = JsonUtils.getNullableFromJsonElementAsString(jsonElementComm);
        assertEquals("comm", comment);

        JsonElement jsonElementGrAmt = rowJsonObject.get(Table.ITEM.SUB_TOTAL.toString());
        BigDecimal grAmt = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementGrAmt);
        assertEquals(new BigDecimal("2.0"), grAmt);
        JsonElement jsonElementNtAmt = rowJsonObject.get(Table.ITEM.TOTAL.toString());
        BigDecimal ntAmt = JsonUtils.getNullableFromJsonElementAsBigDecimal(jsonElementNtAmt);
        assertEquals(new BigDecimal("4.0"), ntAmt);

        Date createdOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.ITEM.T_CREATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2001-02-03 14:05:06"), createdOn);
        Date updatedOn = SIMPLE_DATE_TIME_FORMAT.parse(rowJsonObject.get(Table.ITEM.T_UPDATEDON.toString()).getAsString());
        assertEquals(SIMPLE_DATE_TIME_FORMAT.parse("2006-05-04 03:02:01"), updatedOn);
    }
}