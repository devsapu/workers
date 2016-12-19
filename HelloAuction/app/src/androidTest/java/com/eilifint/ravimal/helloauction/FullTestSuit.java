package com.eilifint.ravimal.helloauction;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Ravimal on 11/26/2016.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestDb.class,
        TestUriMatcher.class,
        TestProvider.class})
public class FullTestSuit {

}