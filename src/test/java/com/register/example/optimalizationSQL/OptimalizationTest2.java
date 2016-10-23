package com.register.example.optimalizationSQL;

import com.register.example.IntegrationTestBase;
import com.register.example.configuration.HibernateStatisticsInterceptor;
import com.register.example.configuration.HibernateStatisticsStopWatch;
import com.register.example.entity.test.Dupa;
import com.register.example.entity.test.Dupka;
import com.register.example.entity.test.Test;
import com.register.example.entity.test.Upa;
import com.register.example.repository.test.DupaRepository;
import com.register.example.repository.test.DupkaRepository;
import com.register.example.repository.test.TestRepository;
import com.register.example.repository.test.UpaRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class OptimalizationTest2 extends IntegrationTestBase {

    @Autowired
    private HibernateStatisticsStopWatch stats;
    @Autowired
    private HibernateStatisticsInterceptor hibernateStatisticsInterceptor;

    @Autowired
    private DupaRepository dupaRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UpaRepository upaRepository;

    @Autowired
    private DupkaRepository dupkaRepository;

    private Long id = 1L;

    private Long id2= 2L;

    @Before
    public void setUp() throws Exception {
        testRepository.deleteAll();
        upaRepository.deleteAll();
        dupaRepository.deleteAll();
        dupkaRepository.deleteAll();


        Test test = new Test();
        Set dupas = new HashSet<Dupa>() {
            {
                add(new Dupa("test", test));
                add(new Dupa("test2", test));
                add(new Dupa("test3", test));
            }
        };
        test.setDupas(dupas);
        Test test1 = testRepository.save(test);
        id = test1.getId();

        Upa upa = new Upa();
        upa.getDupas().add(new Dupka("test",upa));
        upa.getDupas().add(new Dupka("test2",upa));
        upa.getDupas().add(new Dupka("test3",upa));
        Upa upa1=upaRepository.save(upa);
        id2=upa1.getId();

        hibernateStatisticsInterceptor.startCounter();
    }

    @After
    public void tearDown() {
        hibernateStatisticsInterceptor.clearCounter();
    }

    @org.junit.Test
    @Transactional
    public void testSet() throws Exception {
        System.out.println(String.valueOf(stats.getObject().toString()));
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        Long queryBefore=stats.getObject().getPrepareStatementCount();

        System.out.println(testRepository.findAll());
        Test test = testRepository.findOne(id);
        Iterator<Dupa> iterator = test.getDupas().iterator();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();
        test.getDupas().add(new Dupa("test4",test));
        testRepository.save(test);
        assertThat(test.getDupas().size()).isEqualTo(2);
        Test test2=testRepository.findByID(test.getId());
        assertThat(test2.getDupas().size()).isEqualTo(2);

        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(3L);
        assertThat(stats.getObject().getPrepareStatementCount()-queryBefore).isEqualTo(3L);
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        System.out.println(stats.getObject().toString());

        System.out.println(testRepository.findAll());
        System.out.println(dupaRepository.findAll());

        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        System.out.println(String.valueOf(stats.getObject().toString()));
    }

    @org.junit.Test
    @Transactional
    public void testList() throws Exception {
        System.out.println(String.valueOf(stats.getObject().toString()));
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        Long queryBefore=stats.getObject().getPrepareStatementCount();
        System.out.println(upaRepository.findAll());

        Upa test=upaRepository.findOne(id2);

        Iterator<Dupka> iterator = test.getDupas().iterator();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();

        test.getDupas().add(new Dupka("test4",test));
        upaRepository.save(test);
        assertThat(test.getDupas().size()).isEqualTo(2);
        Upa test2=upaRepository.findOne(test.getId());
        assertThat(test2.getDupas().size()).isEqualTo(2);

        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(2L);
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        assertThat(stats.getObject().getPrepareStatementCount()-queryBefore).isEqualTo(2L);


        System.out.println(upaRepository.findAll());
        System.out.println(dupkaRepository.findAll());
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        System.out.println(String.valueOf(stats.getObject().toString()));
    }

    @org.junit.Test
    @Transactional
    public void testSet2() throws Exception {
        System.out.println(String.valueOf(stats.getObject().toString()));
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        Long queryBefore=stats.getObject().getPrepareStatementCount();
        System.out.println(testRepository.findAll());
        Test test=testRepository.findOne(id);
        Iterator<Dupa> iterator = test.getDupas().iterator();
        Dupa next = iterator.next();
        iterator.remove();
        dupaRepository.delete(next.getId());
        next = iterator.next();
        iterator.remove();
        dupaRepository.delete(next.getId());
        test.getDupas().add(new Dupa("test4",test));

        Test test2=testRepository.findByID(test.getId());
        assertThat(test2.getDupas().size()).isEqualTo(2);

        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(5L);
        assertThat(stats.getObject().getPrepareStatementCount()-queryBefore).isEqualTo(5L);

        System.out.println(testRepository.findAll());
        System.out.println(dupaRepository.findAll());

        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        System.out.println(String.valueOf(stats.getObject().toString()));
    }

    @org.junit.Test
    @Transactional
    public void testList2() throws Exception {
        System.out.println(String.valueOf(stats.getObject().toString()));
        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        Long queryBefore=stats.getObject().getPrepareStatementCount();
        System.out.println(upaRepository.findAll());
        Upa test=upaRepository.findOne(id2);

        Iterator<Dupka> iterator = test.getDupas().iterator();
        Dupka next = iterator.next();
        iterator.remove();
        dupkaRepository.delete(next.getId());
        next = iterator.next();
        iterator.remove();
        dupkaRepository.delete(next.getId());
        Thread.sleep(1000);
        test.getDupas().add(new Dupka("test4",test));

        Upa test2=upaRepository.findOne(test.getId());
        assertThat(test2.getDupas().size()).isEqualTo(2);

        System.out.println(upaRepository.findAll());
        System.out.println(dupkaRepository.findAll());

        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(6L);
        assertThat(stats.getObject().getPrepareStatementCount()-queryBefore).isEqualTo(6L);

        System.out.println(String.valueOf(stats.getObject().getPrepareStatementCount()));
        System.out.println(String.valueOf(stats.getObject().toString()));

    }
}
