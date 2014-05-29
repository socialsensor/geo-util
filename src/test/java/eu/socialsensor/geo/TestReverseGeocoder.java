package eu.socialsensor.geo;

import java.util.ArrayList;
import java.util.List;

import eu.socialsensor.util.EasyBufferedReader;

public class TestReverseGeocoder {

	public static void main(String[] args) {
		
		// args[0] should be the root folder where the geonames files reside
		String rootGeonamesDir = args[0];
		String gnCitiesFile = rootGeonamesDir + "cities1000.txt";
		String gnAllCountriesFile = rootGeonamesDir + "allCountries.txt";
		String gnCountryInfoFile = rootGeonamesDir + "countryInfo.txt";
		String testCoordFile = "test-geo.txt";
		
		testReverseGeoAccuracy(gnCitiesFile, gnCountryInfoFile, testCoordFile);
		
		testReverseGeoSpeed(gnAllCountriesFile, gnCountryInfoFile, testCoordFile, 1000000);
	}
	
	
	
	/**
	 * Testing the accuracy of the ReverseGeocoder.
	 * @param citiesFile Geonames file to be used as a geo-index by the ReverseGeocoder (e.g. cities1000.txt, allCountries.txt)
	 * @param countryInfoFile Geonames countryInfo.txt file
	 * @param testFile 
	 */
	public static void testReverseGeoAccuracy(String citiesFile, String countryInfoFile, String testFile){
		
		ReverseGeocoder rgeoService = new ReverseGeocoder(citiesFile, countryInfoFile); 

		EasyBufferedReader reader = new EasyBufferedReader(testFile);
		String line = null;
		while ((line = reader.readLine())!=null){
			String[] parts = line.split("\t");
			double lat = Double.parseDouble(parts[0]);
			double lon = Double.parseDouble(parts[1]);
			
			System.out.println(rgeoService.getCountryByLatLon(lat, lon) + " -> " + parts[2]);		
		}	
		reader.close();
	}
	
	/**
	 * Testing the speed of the reverseGeocoder.
	 * @param citiesFile Geonames file to be used as a geo-index by the ReverseGeocoder (e.g. cities1000.txt, allCountries.txt)
	 * @param countryInfoFile Geonames countryInfo.txt file
	 * @param testFile Test file where each line is tab-separated and has the form: lat lon country
	 * @param nrQueries Number of queries to run in order to derive the estimate (e.g. 100000)
	 */
	public static void testReverseGeoSpeed(String citiesFile, String countryInfoFile, String testFile, int nrQueries){
		ReverseGeocoder rgeoService = new ReverseGeocoder(citiesFile, countryInfoFile); 

		EasyBufferedReader reader = new EasyBufferedReader(testFile);
		String line = null;
		List<String> lines = new ArrayList<String>();
		while ((line = reader.readLine())!=null){
			lines.add(line);				
		}	
		reader.close();
		
		// generate a large number of queries
		double execTime = 0.0;
		for (int i = 0; i < nrQueries; i++){
			int idx = (int)Math.floor(lines.size() * Math.random());
			idx = (idx == lines.size())? lines.size()-1 : idx;
			String[] parts = lines.get(idx).split("\t");
			
			// randomize
			double lat = Double.parseDouble(parts[0]) + Math.random();
			double lon = Double.parseDouble(parts[1]) + Math.random();
			
			long t0 = System.currentTimeMillis();
			rgeoService.getCountryByLatLon(lat, lon);
			execTime += System.currentTimeMillis() - t0;
			
			if (i%1000 == 0){
				System.out.print("-");
			}
		}
		System.out.println();
		
		System.out.println("Total exec time: " + execTime + "msec");
		System.out.println("Average query time: " + (execTime/nrQueries) + "msec");
	}
	
	
}
