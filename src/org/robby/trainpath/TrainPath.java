/*
 * Author : Robby Zhang
 * Created Date : 2012/8/30
 * 
 * */

package org.robby.trainpath;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * The TrainPath class provides two main functions: searchRoute(...) and searchShortestRoute(...),
 * The first one implements Depth-First-Search and second one implements dijkstra.
 * */
public class TrainPath {
	int[][] graph;
	int maxNum;
	Map<String, Integer> mMapStat;

	public TrainPath(int n) {
		if(n<=0)
			throw new IllegalArgumentException();
		maxNum = n;
		graph = new int[n][n];
		mMapStat = new HashMap<String, Integer>();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				graph[i][j] = Route.MAXINT;
	}

	public void addRoute(String from, String to, int distance){
		/* For dijkstra, negtive distance is NOT allowed. **/
		if(distance < 0)
			throw new IllegalArgumentException();
		
		int m, n;
		if(mMapStat.containsKey(from))
			m = mMapStat.get(from);
		else{
			m = mMapStat.size();
			if(m >= maxNum)
				throw new IllegalArgumentException();
			mMapStat.put(from, m);
		}
		
		if(mMapStat.containsKey(to))
			n = mMapStat.get(to);
		else{
			n = mMapStat.size();
			if(n >= maxNum)
				throw new IllegalArgumentException();
			mMapStat.put(to, n);
		}
		
		addRoute(m, n, distance);
	}
	
	
	/* 
	 * Calculate the distance from a giving route. 
	 * */
	public int caclRoute(Route path){
		int d = 0;
		Iterator<String> itr = path.getStops().iterator();
		
		int from = -1;
		if(itr.hasNext())
			from = getIdByName(itr.next());
		
		while (itr.hasNext()) {
			int to = getIdByName(itr.next());
		    if(graph[from][to] != Route.MAXINT){
		    	d += graph[from][to];
		    	from = to;
		    }
		    else
		    	return 0;
		}
		
		return d;
	}
	
	private void addRoute(int from, int to, int distance) {
		graph[from][to] = distance;
	}
	
	/*
	 * It returns all possible routes from "from" to "to" in "res" 
	 * */
	public void searchRoute(Map<String, Route> res, String from, String to, int maxstop, int maxdis){
		searchAllRoute(res, getIdByName(from), getIdByName(to), new Route(), 0, maxstop, maxdis);
	}
	
	private void searchAllRoute(Map<String, Route> res, int orig, int dest, Route path, int dis, int maxstop, int maxdis){
		if(path.getStops().size()>maxstop || (path.distance + dis) >= maxdis)
			return;
		
		path.addStop(getNameById(orig), dis);
		if(orig == dest && path.getStops().size()>1){
			res.put(path.getRouteInfo(), path);
		}
		
		for (int i = 0; i < maxNum; i++) {
			if(graph[orig][i] != Route.MAXINT){
				Route r = new Route(path);
				searchAllRoute(res, i, dest, r, graph[orig][i], maxstop, maxdis);
			}
		}
	}
	
	/*
	 * It returns the shortest route in "res" 
	 * */
	public void searchShortestRoute(Route res, String from, String to){
		int orig = getIdByName(from);
		int dest = getIdByName(to);
		
		boolean[] visited = new boolean[maxNum];
		for(int i=0; i<maxNum; i++)
			visited[i] = false;
		
		int[] dist = new int[maxNum];
		int[] prev = new int[maxNum];
		
		/* Initialization **/
		for(int i=0; i<maxNum; i++){
			visited[i] = false;
			dist[i] = maxNum;
			prev[i] = -1;
		}
		
		for(int i=0; i<maxNum; i++){
			if(graph[orig][i] > 0){
				prev[i] = orig;
				dist[i] = graph[orig][i];
			}
		}

		/* start dijkstra **/
		visited[orig] = true;
		
		for(int i=1; i<maxNum; i++){
			/* 1, Find the nearest station. **/
			int tmp = Route.MAXINT;
			int u = orig;
			
			for(int j=0; j<maxNum; j++){
				if(!visited[j] && (dist[j]<tmp)){
					u = j;
					tmp = dist[j];
				}
			}
			visited[u] = true;
			
			/* 2, Update the distance. **/
			for(int j=0; j<maxNum; j++){
				if(!visited[j] && (graph[u][j]!=Route.MAXINT)){
					int newDist = dist[u] + graph[u][j];
					if(newDist < dist[j]){
						dist[j] = newDist;
						prev[j] = u;
					}
				}
			}
		}
		
		if(orig != dest){
			int n=dest;
			res.setDistance(dist[n]);
			while(n!=-1){
				res.addStopFromFirst(getNameById(n));
				n = prev[n];
				if(n == orig){
					res.addStopFromFirst(getNameById(n));
					break;
				}
			}
		}
	}
	
	private int getIdByName(String name){
		if(!mMapStat.containsKey(name))
			throw new IllegalArgumentException();
		return mMapStat.get(name);
	}
	
	private String getNameById(int id){
		for(Map.Entry<String, Integer> m:mMapStat.entrySet()){
			if(m.getValue() == id)
				return m.getKey();
		}
		throw new IllegalArgumentException();
	}
}
