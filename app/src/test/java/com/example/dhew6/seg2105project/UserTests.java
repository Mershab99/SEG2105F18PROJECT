package com.example.dhew6.seg2105project;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTests {
    User user = new User("TestUser", "TestUsername","testpassword1234", "testemail@email.com");

    @Test
    public void test_user_getName(){
        assertEquals("TestUser", user.getName());
    }
    @Test
    public void test_user_getEmail(){
        assertEquals("testemail@email.com", user.getEmail());
    }
    @Test
    public void test_user_getUsername(){
        assertEquals("TestUsername", user.getUsername());
    }
    @Test
    public void test_user_getPassword(){
        assertEquals("testpassword1234", user.getPassword());
    }
    @Test
    public void test_user_setters(){
        user.setPassword("newpassword1234");
        user.setEmail("newemail@email.com");
        user.setName("NewUser");
        user.setUsername("NewUsername");
        assertEquals("NewUser", user.getName());
        assertEquals("NewUsername", user.getUsername());
        assertEquals("newemail@email.com", user.getEmail());
        assertEquals("newpassword1234", user.getPassword());
    }
    @After
    public void after(){
        System.out.println("finished running User Tests");
    }
}