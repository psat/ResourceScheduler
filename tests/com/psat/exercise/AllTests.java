package com.psat.exercise;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * TestSuite to execute all tests from the project
 * 
 * @author Sérgio Teixeira
 * @date 14/06/2015 21:37:50
 */
@RunWith(Suite.class)
@SuiteClasses({ ResourceSchedulerTest.class, ConcreteMessageTest.class, GroupPriorityTest.class })
public class AllTests {

}
