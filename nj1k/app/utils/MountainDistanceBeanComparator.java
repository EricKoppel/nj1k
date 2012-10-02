package utils;

import java.util.Comparator;

import dto.MountainDistanceBean;


public class MountainDistanceBeanComparator implements Comparator<MountainDistanceBean>{

	@Override
	public int compare(MountainDistanceBean a, MountainDistanceBean b) {
		return Double.compare(a.getDistance(), b.getDistance());
	}
}
