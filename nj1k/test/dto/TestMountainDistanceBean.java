package dto;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import models.MountainEntity;

import org.junit.Test;

public class TestMountainDistanceBean {

	@Test
	public void testEquality() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				MountainEntity m1 = new MountainEntity();
				MountainEntity m2 = new MountainEntity();
				
				MountainEntity m3 = new MountainEntity();
				MountainEntity m4 = new MountainEntity();
				
				m1.id = m3.id = 1l;
				m2.id = m4.id = 2l;
				
				m1.latitude = m3.latitude = 40.123f;
				m1.longitude = m3.longitude = -74.655f;
				
				m2.latitude = m4.latitude = 41.555f;
				m2.longitude = m4.longitude = 40.912f;
				
				assertFalse(m1.equals(m2));
				assertFalse(m1.equals(m2));
				assertTrue(m1.equals(m3));
				assertTrue(m2.equals(m4));
				
				MountainDistanceBean mdb1 = new MountainDistanceBean(m1, m2, MountainEntity.calculateDistanceBetweenMountains(m1, m2));
				MountainDistanceBean mdb2 = new MountainDistanceBean(m3, m4, MountainEntity.calculateDistanceBetweenMountains(m3, m4));
				
				assertEquals(mdb1.getDistance(), mdb2.getDistance(), 1e-8);
				
				assertEquals(mdb1, mdb2);
			}
		});
	}
}
