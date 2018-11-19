package com.example.dhew6.seg2105project;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ServiceTests {
    Service service = new Service("Test Service", 14.00);

    @Test
    public void test_service_getName(){
        assertEquals("Test Service", service.getName());
    }
    @Test
    public void test_service_getRate(){
        assertEquals(14.00, service.getRate(), 0.001);
    }

    @Test
    public void test_service_equals(){
        Service new_service = new Service("Test Service", 14.00);
        assertEquals(true, service.equals(new_service));
        new_service.setRate(0.0);
        assertEquals(false, service.equals(new_service));
        new_service = new Service("Test Service", 14.00);
        assertEquals(true, service.equals(new_service));
        new_service.setName("Name Changed");
        assertEquals(false, service.equals(new_service));

    }
    @Test
    public void test_service_setters(){
        service.setName("New Name");
        service.setRate(0.00);
        assertEquals("New Name", service.getName());
        assertEquals(0.0, service.getRate(), 0.001);
    }
    @After
    public void after(){
        System.out.println("test run");
    }
}