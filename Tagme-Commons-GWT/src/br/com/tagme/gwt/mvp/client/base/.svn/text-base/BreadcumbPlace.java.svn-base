package br.com.tagme.gwt.mvp.client.base;

import java.util.ArrayList;


public class BreadcumbPlace extends DefaultPlace implements IBreadcumbPlace{
	
	private ArrayList<AbstractPlace> path = new ArrayList<AbstractPlace>();
	
	public BreadcumbPlace(String prefix, String token, String descriptor){
		super(prefix, token, descriptor);
	}

	public void addPathItem(AbstractPlace placeItem){
		path.add(placeItem);
	}
	
	public AbstractPlace getPathItem(int index){
		return index >= path.size() ? null : path.get(index);
	}
	
	public int getPathSize(){
		return path.size();
	}

}
