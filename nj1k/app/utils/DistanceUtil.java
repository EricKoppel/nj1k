package utils;

public class DistanceUtil {

	public static double calculateDistance(float latitude1, float longitude1, float latitude2, float longitude2) {
		double lat1 = latitude1 * Math.PI / 180;
		double long1 = longitude1 * Math.PI / 180;
	
		double lat2 = latitude2 * Math.PI / 180;
		double long2 = longitude2 * Math.PI / 180;
	
		double x = (long2 - long1) * Math.cos((lat1 + lat2) / 2);
		double y = lat2 - lat1;
		double d = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	
		return d;
	}
}
