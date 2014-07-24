package eu.socialsensor.geo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.socialsensor.util.EasyBufferedReader;

public class TestCountrycoder {

	
	public static void main(String[] args) {
		// args[0] should be the root folder where the geonames files reside
		String rootGeonamesDir = args[0];
		String gnCitiesFile = rootGeonamesDir + "cities1000_mod.txt";
		String gnCountryInfoFile = rootGeonamesDir + "countryInfo.txt";
		String gnAdminNames = rootGeonamesDir + "admin1CodesASCII_mod.txt";
		String testFile = "test-names.txt";
		String twitterTestFile = "twitter_accounts_locations.txt";
				
		//testCountrycoderAccuracy(gnCitiesFile, gnCountryInfoFile, gnAdminNames, testFile);
		
		//testCountrycoderSpeed(gnCitiesFile, gnCountryInfoFile, gnAdminNames, testFile, 100000);
		
		testCountrycoderTwitterLocationField(gnCitiesFile, gnCountryInfoFile, gnAdminNames, twitterTestFile);
	}
	
	public static void testCountrycoderAccuracy(String citiesFile, String countryInfoFile, String adminNamesFile, String testFile){
		Countrycoder countrycodingService = new Countrycoder(citiesFile, countryInfoFile, adminNamesFile);
		
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
	

	public static void testCountrycoderSpeed(String citiesFile, String countryInfoFile, String adminNamesFile, String testFile, int nrQueries){
		Countrycoder countrycoderService = new Countrycoder(citiesFile, countryInfoFile, adminNamesFile); 

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
	
	
	public static void testCountrycoderTwitterLocationField(String citiesFile, String countryInfoFile, String adminNamesFile, String testFile){
		Countrycoder countrycoderService = new Countrycoder(citiesFile, countryInfoFile, adminNamesFile);
		Logger logger = Logger.getLogger("eu.socialsensor.geo.TestCountrycoder");
		
		EasyBufferedReader reader = new EasyBufferedReader(testFile);
		String line = null;
		int count = 0;
		while ((line = reader.readLine())!=null && (count++ < 10000)){
			
			String[] parts = line.split("\\s");
			if (parts.length < 2 || (parts[1].trim().length() < 1)){
				continue;
			}
			testSingleLocation(countrycoderService, line, logger);
		}
		reader.close();
	}
	
	
	protected static void testSingleLocation(Countrycoder countrycoderService, String text, Logger logger){
		Map<String,String> locMap = countrycoderService.getLocation(text);
		logger.info("Text: " + text + " -> " + Countrycoder.printLocationMap(locMap));
	}
}
