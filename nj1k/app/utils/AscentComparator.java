package utils;

import java.util.Comparator;

import models.AscentEntity;

public class AscentComparator implements Comparator<AscentEntity> {
	
	@Override
	public int compare(AscentEntity arg0, AscentEntity arg1) {
		return arg0.mountain.id.compareTo(arg1.mountain.id);
	}

}
