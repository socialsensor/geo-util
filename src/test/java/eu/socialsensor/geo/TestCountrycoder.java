package eu.socialsensor.geo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.socialsensor.util.EasyBufferedReader;

public class TestCountrycoder {

	
	public static void main(String[] args) {
		// args[0] should be the root folder where the geonames files reside
		String rootGeonamesDir = args[0];
		String gnCitiesFile = rootGeonamesDir + "cities1000.txt";
		String gnAllCountriesFile = rootGeonamesDir + "allCountries.txt";
		String gnCountryInfoFile = rootGeonamesDir + "countryInfo.txt";
		String testFile = "test-names.txt";
				
		testCountrycoderAccuracy(gnCitiesFile, gnCountryInfoFile, testFile);
		
		testCountrycoderSpeed(gnAllCountriesFile, gnCountryInfoFile, testFile, 100000);
	}
	
	public static void testCountrycoderAccuracy(String citiesFile, String countryInfoFile, String testFile){
		Countrycoder countrycodingService = new Countrycoder(citiesFile, countryInfoFile);
		
		Logger logger = Logger.getLogger("eu.socialsensor.geo.TestCountrycoder");
		
		
		EasyBufferedReader reader = new EasyBufferedReader(testFile);
		String line = null;
		while ((line = reader.readLine())!=null){
			String[] parts = line.split("\t");
			// assume that parts[0] is the name
			String country = countrycodingService.getCountryByLocationName(parts[0]);
			logger.info(parts[0] + " -> " + country);
		}
		reader.close();
	}
	

	public static void testCountrycoderSpeed(String citiesFile, String countryInfoFile, String testFile, int nrQueries){
		Countrycoder countrycoderService = new Countrycoder(citiesFile, countryInfoFile); 

		Logger logger = Logger.getLogger("eu.socialsensor.geo.TestCountrycoder");
				
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
			
			long t0 = System.currentTimeMillis();
			countrycoderService.getCountryByLocationName(parts[0]);
			execTime += System.currentTimeMillis() - t0;
		}
		
		logger.info("Total exec time: " + execTime + "msec");
		logger.info("Average query time: " + (execTime/nrQueries) + "msec");
	}
	
}
