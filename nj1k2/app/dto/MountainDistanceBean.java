package dto;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

import models.MountainEntity;
import flexjson.JSON;

public class MountainDistanceBean {

	private static final double EARTH_RADIUS_KM = 6371;
	private static final double EARTH_RADIUS_MI = 3959;
	
	private final MountainEntity m1;
	private final MountainEntity m2;
	private final double distance;

	public MountainDistanceBean() {
		this.m1 = null;
		this.m2 = null;
		this.distance = 0;
	}
	
	public MountainDistanceBean(MountainEntity m1, MountainEntity m2, double distance) {
		this.m1 = m1;
		this.m2 = m2;
		this.distance = distance;
	}
	
	public MountainEntity getM1() {
		return m1;
	}

	public MountainEntity getM2() {
		return m2;
	}

	public double getDistance() {
		return distance;
	}

	public double getDistanceAsKm() {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);

		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setMaximumFractionDigits(2);
		
		return Double.parseDouble(df.format(distance * EARTH_RADIUS_KM));
	}
	
	@JSON
	public double getDistanceAsMiles() {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);

		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setMaximumFractionDigits(2);
		
		return Double.parseDouble(df.format(distance * EARTH_RADIUS_MI));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MountainDistanceBean) {
			MountainDistanceBean mdb = (MountainDistanceBean) o;
			
			if (this.m1.equals(mdb.m1) || this.m1.equals(mdb.m2)) {
				if (this.m2.equals(mdb.m2) || this.m2.equals(mdb.m1)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
