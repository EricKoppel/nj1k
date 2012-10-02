package utils;

import java.util.Comparator;

import models.MountainEntity;

public class MountainComparator implements Comparator<MountainEntity> {

	@Override
	public int compare(MountainEntity m0, MountainEntity m1) {
		return m0.name.compareToIgnoreCase(m1.name);
	}

}
