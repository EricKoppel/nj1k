package dto;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

import models.MountainEntity;

import flexjson.JSON;

public class MountainDistanceBean {

	private static final double EARTH_RADIUS_KM = 6371;
	private static final double EARTH_RADIUS_MI = 3959;
	
	private MountainEntity m1;
	private MountainEntity m2;
	private double distance;

	public MountainDistanceBean(MountainEntity m1, MountainEntity m2, double distance) {
		this.m1 = m1;
		this.m2 = m2;
		this.distance = distance;
	}
	
	public MountainEntity getM1() {
		return m1;
	}

	public void setM1(MountainEntity m1) {
		this.m1 = m1;
	}

	public MountainEntity getM2() {
		return m2;
	}

	public void setM2(MountainEntity m2) {
		this.m2 = m2;
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
	
	public void setDistance(double distance) {
		this.distance = distance;
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
	
	@Override
	public String toString() {
		return String.format("Distance between %s and %s is %s miles\n", m1.name, m2.name, getDistanceAsMiles());
	}
}
