/*
 * Author : Robby Zhang
 * Created Date : 2012/8/30
 * 
 * */

package org.robby.trainpath;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/*
 * This is the JUnit test case for class TrainPath
 * */
public class TestTrainPath extends TestCase{
	TrainPath tp;
	public final int MAXINT = 9999;
	
	public void setUp() throws Exception {
		tp = new TrainPath(5);
		tp.addRoute("A", "B", 5);
		tp.addRoute("B", "C", 4);
		tp.addRoute("C", "D", 8);
		tp.addRoute("D", "C", 8);
		tp.addRoute("D", "E", 6);
		tp.addRoute("A", "D", 5);
		tp.addRoute("C", "E", 2);
		tp.addRoute("E", "B", 3);
		tp.addRoute("A", "E", 7);
    }
	
	//1. The distance of the route A-B-C.
	public void test1(){
		Route r = new Route(0, "A", "B", "C");
		assertEquals(9, tp.caclRoute(r));
	}
	
	//2. The distance of the route A-D.
	public void test2(){
		Route r = new Route(0, "A", "D");
		assertEquals(5, tp.caclRoute(r));
	}
	
	//3. The distance of the route A-D-C.
	public void test3(){
		Route r = new Route(0, "A", "D", "C");
		assertEquals(13, tp.caclRoute(r));
	}
	
	//4. The distance of the route A-E-B-C-D.
	public void test4(){
		Route r = new Route(0, "A", "E", "B", "C", "D");
		assertEquals(22, tp.caclRoute(r));
	}
	
	//5. The distance of the route A-E-D.
	public void test5(){
		Route r = new Route(0, "A", "E", "D");
		assertEquals(0, tp.caclRoute(r));
	}
	
	//6. The number of trips starting at C and ending at C with a maximum of 3 stops.  In the sample data below, there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
	public void test6(){
		Map<String, Route> m = new HashMap<String, Route>();
		tp.searchRoute(m, "C", "C", 3, MAXINT);
		assertEquals(2, m.size());
		assertTrue(m.containsKey("CDC"));
		assertTrue(m.containsKey("CEBC"));
	}
	
	//7. The number of trips starting at A and ending at C with exactly 4 stops.  In the sample data below, there are three such trips: A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
	public void test7(){
		Map<String, Route> m = new HashMap<String, Route>();
		tp.searchRoute(m, "A", "C", 4, MAXINT);
		assertTrue(m.containsKey("ABCDC"));
		assertTrue(m.containsKey("ADCDC"));
		assertTrue(m.containsKey("ADEBC"));
	}
	
	//8. The length of the shortest route (in terms of distance to travel) from A to C.
	public void test8(){
		Map<String, Route> m = new HashMap<String, Route>();
		tp.searchRoute(m, "A", "C", 5, MAXINT);
		int d = MAXINT;
		for(Map.Entry<String, Route> tmp : m.entrySet()){
			if(d > tmp.getValue().getDistance())
				d = tmp.getValue().getDistance();
		}
		assertEquals(9, d);
		
		//dijkstra
		Route r = new Route();
		tp.searchShortestRoute(r, "A", "C");
		assertEquals(9, r.getDistance());
		assertEquals("ABC", r.getRouteInfo());
	}
	
	//9. The length of the shortest route (in terms of distance to travel) from B to B.
	public void test9(){
		Map<String, Route> m = new HashMap<String, Route>();
		tp.searchRoute(m, "B", "B", 5, MAXINT);
		int d = MAXINT;
		for(Map.Entry<String, Route> tmp : m.entrySet()){
			if(d > tmp.getValue().getDistance())
				d = tmp.getValue().getDistance();
		}
		assertEquals(9, d);
		
	}
	
	//10. The number of different routes from C to C with a distance of less than 30.  In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.
	public void test10(){
		Map<String, Route> m = new HashMap<String, Route>();
		tp.searchRoute(m, "C", "C", 20, 30);
		
		assertEquals(7, m.size());
		assertTrue(m.containsKey("CDC"));
		assertTrue(m.containsKey("CEBC"));
		assertTrue(m.containsKey("CEBCDC"));
		assertTrue(m.containsKey("CDCEBC"));
		assertTrue(m.containsKey("CDEBC"));
		assertTrue(m.containsKey("CEBCEBC"));
		assertTrue(m.containsKey("CEBCEBCEBC"));
	}
}
