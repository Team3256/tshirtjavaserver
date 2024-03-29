package com.panos.utils;

public class Location {
    private double longitude;
    private double latitude;

    // Create and initialize a point with given name and
    // (latitude, longitude) specified in degrees
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Return distance between this location and that location
    // measured in feet
    public double distanceTo(Location that) {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(that.latitude);
        double lon2 = Math.toRadians(that.longitude);

        // Great circle distance in radians, using law of cosines formula
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // Each degree on a great circle of Earth is 60 nautical miles
        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return (statuteMiles * STATUTE_MILES_PER_NAUTICAL_MILE) * 5280;
    }

    public void setLongitude(double lng) {
        longitude = lng;
    }

    public void setLatitude(double lat) {
        latitude = lat;
    }

    public float getAngle(Location that) {
        float angle = (float) Math.toDegrees(Math.atan2(that.latitude - this.latitude, that.longitude - this.longitude));

        return angle;
    }

    // Return string representation of this point
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}