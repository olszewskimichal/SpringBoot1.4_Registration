package com.register.example.optimalizationSQL;

import com.register.example.IntegrationTestBase;
import com.register.example.configuration.HibernateStatisticsInterceptor;
import com.register.example.entity.test.Dupa;
import com.register.example.entity.test.Test;
import com.register.example.repository.test.DupaRepository;
import com.register.example.repository.test.TestRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class OptimalizationTest extends IntegrationTestBase {
    @Autowired
    private HibernateStatisticsInterceptor hibernateStatisticsInterceptor;

    @Autowired
    private DupaRepository dupaRepository;

    @Autowired
    private TestRepository testRepository;

    private Long id = 1L;

    @Before
    public void setUp() throws Exception {
        testRepository.deleteAll();
        dupaRepository.deleteAll();
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
        hibernateStatisticsInterceptor.startCounter();
    }

    @After
    public void tearDown() {
        hibernateStatisticsInterceptor.clearCounter();
    }

    @org.junit.Test
    public void should_findTestWithChilds_In_1Query() {
        testRepository.findALL();
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(1L);
    }

    @org.junit.Test
    public void should_DeleteChildFromTest_In_1Query() {
        dupaRepository.deleteDupaByID(1L);
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(1L);
    }

    @org.junit.Test
    public void should_AddChildToTest_In_2Query() {
        Test test = testRepository.findByID(id);
        test.getDupas().add(new Dupa("dupa4", test));
        testRepository.save(test);
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        //Powinno byc w 2L
        assertThat(queryCount).isEqualTo(3L);
    }

    @org.junit.Test
    public void should_DeleteTestWithChilds_In_2Query() {
        dupaRepository.deleteDupaByTestId(id);
        testRepository.deleteTestByID(id);
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(2L);
    }

    @org.junit.Test
    public void should_ReadChildren_In_1Query() {
        Test test2 = testRepository.findOne(id);
        test2.getDupas().forEach(Dupa::getZupa);
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(1L);
    }
}
