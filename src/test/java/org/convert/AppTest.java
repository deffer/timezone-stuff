package org.convert;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
    public void testConvertLine(){
		FileProcessor fp = new FileProcessor(",");

		String testLine = "2013-07-10 02:52:49,-44.490947,171.220966";
		String result = fp.convertLine(testLine, 2).trim();
        assertEquals("2013-07-10 02:52:49,-44.490947,171.220966,Pacific/Auckland,2013-07-10 14:52:49", result);

		testLine = "2014-01-10 02:52:49,36.51,33.46";
		result = fp.convertLine(testLine, 1).trim();
		assertEquals("2014-01-10 02:52:49,36.51,33.46,Europe/Istanbul,2014-01-10 04:52:49", result);

	}
}
