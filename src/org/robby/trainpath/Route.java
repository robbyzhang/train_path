/*
 * Author : Robby Zhang
 * Created Date : 2012/8/30
 * 
 * */

package org.robby.trainpath;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * The Route class 
 * */
public class Route{
    public static final int MAXINT = 9999;
	LinkedList<String> stops;
	int distance;
	
	public void init(){
		stops = new LinkedList<String>();
		distance = 0;
	}
	
	public Route(){
		init();
	}
	
	public Route(Route r){
		init();
		this.distance = r.getDistance();
		this.stops.addAll(r.getStops());
	}
	
	public Route(int dis, String...dest){
		init();
		distance = dis;
		
		for(String tmp:dest)
			stops.addLast(tmp);
	}
	
	public void clear(){
		distance = 0;
		stops.clear();
	}
	
	public LinkedList<String> getStops() {
		return stops;
	}

	public void setStops(LinkedList stops) {
		this.stops = stops;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void addStop(String name, int dis){
		distance += dis;
		stops.addLast(name);
	}
	
	public void addStopFromFirst(String name){
		stops.addFirst(name);
	}
	
	public String getRouteInfo(){
		String out = "";
		for(String tmp:stops)
			out += tmp;
		return out;
	}
}
