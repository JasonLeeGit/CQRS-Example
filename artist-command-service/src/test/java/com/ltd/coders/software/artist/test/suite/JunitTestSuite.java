package com.ltd.coders.software.artist.test.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.ltd.coders.software.artist.controller.ArtistCommandControlleRestTest;
import com.ltd.coders.software.artist.controller.ArtistCommandControllerServiceRepositoryTest;

@Suite
@SelectClasses({ 
	ArtistCommandControlleRestTest.class,
	ArtistCommandControllerServiceRepositoryTest.class
})
public class JunitTestSuite
{
}
