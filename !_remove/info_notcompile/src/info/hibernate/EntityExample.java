package info.hibernate;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import javax.persistence.Query;
//import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

/**
 * User: Makar Kalancha
 * Date: 22/03/2016
 * Time: 22:17
 */

//// disable (table with hundreds of columns) creation of INSERT and UPDATE statements on startup
//// SQL generation and switch to dynamic statements generated at runtime
//@org.hibernate.annotations.DynamicInsert
//@org.hibernate.annotations.DynamicUpdate
public class EntityExample {
//    private final static Logger LOG = LogManager.getLogger(EntityExample.class);
//    //old way of setting sequence, prior Hibernate v5
//    //in Hibernate v5 best practice is using strategy = "enhanced-sequence"
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAMILY_MEMBER_SEQUENCE_GENERATOR")
//    @SequenceGenerator(name = "FAMILY_MEMBER_SEQUENCE_GENERATOR", sequenceName = "SEQ_FAMILY_MEMBER", allocationSize=1)
//    @Column(name="ID")
    private Long id;
    protected LocalDateTime createdOn;
    protected LocalDateTime updatedOn;

//    //change value before insert or update
//    //not good if row is inserted or updated with native sql query
//    //then trigger is needed
//    @PrePersist
//    @PreUpdate
    private void onCreate() {
        if(this.createdOn == null){
            this.createdOn = LocalDateTime.now();
        }
        this.updatedOn = LocalDateTime.now();
    }

//    @Test
    public void testUpdate() throws Exception {
////        em.getTransaction().begin();
////        //http://stackoverflow.com/questions/3574029/what-does-jpa-entitymanager-getsingleresult-return-for-a-count-query
////        NB : there's a difference between JQPL and Native query
////        Query query = em.createQuery("SELECT COUNT(p) FROM PersonEntity p " );
////        query.getSingleResult().getClass().getCanonicalName() --> java.lang.Long
////        Query query = em.createNativeQuery("SELECT COUNT(*) FROM PERSON " );
////        query.getSingleResult().getClass().getCanonicalName() --> java.math.BigInteger
//
//
////        Query qId = em.createNativeQuery("SELECT CURRVAL('TEST.SEQ_FAMILY_MEMBER') as num");
//        Query qId = em.createQuery("SELECT max(f.id) as num from FamilyMember f");
//        Long id = ((Long) qId.getSingleResult());
//
//        LOG.debug("seq curr num = " + id);
//
//        FamilyMember husband = em.find(FamilyMember.class, id);
//////OR
////        FamilyMember husband = (FamilyMember) em.createQuery("from FamilyMember as f where f.name = :familyMemberName")
////                .setParameter("familyMemberName", familyMemberName)
////                .setMaxResults(1) //I have multiple Freddy in table, so getSingleResult returns javax.persistence.NonUniqueResultException: result returns more than one elements
////                .getSingleResult();
////        Long id = husband.getId();
//        Random random = new Random();
//        //min 0 and max 100
//        int randomInt = random.nextInt((100 - 0) + 1 + 0);
//        husband.setName("Freddy" + randomInt);
//        husband.setDescription("husband" + randomInt);
//
//        em.persist(husband);
//        em.getTransaction().commit();
//
//        LOG.debug(">>>husband.getId()=" + husband.getId());
//        LOG.debug(">>>husband.getCreatedOn()=" + husband.getCreatedOn());
//        LOG.debug(">>>husband.getUpdatedOn()=" + husband.getUpdatedOn());
//        LOG.debug(husband);
//////before @Temporal annotation: persist->commit, find entity and assert
////        FamilyMember husbandJustUpdated = em.find(FamilyMember.class, id);
////        em.refresh(husbandJustUpdated);
////        System.out.println(">>>husband.getId()=" + husbandJustUpdated.getId());
////        System.out.println(">>>husband.getCreatedOn()=" + husbandJustUpdated.getCreatedOn());
////        System.out.println(">>>husband.getUpdatedOn()=" + husbandJustUpdated.getUpdatedOn());
////        System.out.println(husbandJustUpdated);
////        assertEquals(husbandJustUpdated.getCreatedOn() != null, true);
////        assertEquals(husbandJustUpdated.getUpdatedOn() != null, true);
////        assertEquals(husbandJustUpdated.getCreatedOn() != husband.getUpdatedOn(), true);
//
//        assertEquals(husband.getCreatedOn() != null, true);
//        assertEquals(husband.getUpdatedOn() != null, true);
//        assertEquals(!husband.getCreatedOn().equals(husband.getUpdatedOn()), true);
    }

//    @Test
    public void testPersist() throws Exception{
////        em.getTransaction().begin();
//        LOG.info("start->testPersist");
//        FamilyMember husband = new FamilyMember();
//        husband.setName(familyMemberName);
//        husband.setDescription("husband");
//
//        em.persist(husband);
////        em.flush();
//        em.getTransaction().commit();
//        LOG.debug("husband.getId()=" + husband.getId());
//        LOG.debug("husband.getCreatedOn()=" + husband.getCreatedOn());
//        LOG.debug("husband.getUpdatedOn()=" + husband.getUpdatedOn());
////before @Temporal annotation: persist->commit, find entity and assert
////        Long id = husband.getId();
////        FamilyMember husbandJustInserted = em.find(FamilyMember.class, id);
////        em.refresh(husbandJustInserted);
////        System.out.println("husbandJustInserted.getId()=" + husbandJustInserted.getId());
////        System.out.println("husbandJustInserted.getCreatedOn()=" + husbandJustInserted.getCreatedOn());
////        System.out.println("husbandJustInserted.getUpdatedOn()=" + husbandJustInserted.getUpdatedOn());
////        System.out.println(husband);
////        assertEquals(husbandJustInserted.getCreatedOn() != null, true);
////        assertEquals(husbandJustInserted.getUpdatedOn() != null, true);
//
//        assertEquals(husband.getCreatedOn() != null, true);
//        assertEquals(husband.getUpdatedOn() != null, true);
//        LOG.info("end->testPersist");
    }
}
