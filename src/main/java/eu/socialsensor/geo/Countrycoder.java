package eu.socialsensor.geo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.socialsensor.geonames.GeoObject;
import eu.socialsensor.util.EasyBufferedReader;


/**
 * Given a city/area name it resolves the country where it belongs. 
 * @author papadop
 *
 */
public class Countrycoder extends AbstractGeoService {

	
	private final Map<String, String> countryVocabulary;
	
	public Countrycoder(String gnObjectsFile, String gnCountryInfoFile){
		super(gnCountryInfoFile, Logger.getLogger("eu.socialsensor.geo.Countrycoder"));
		countryVocabulary = readCountryVocabulary(gnObjectsFile);
	}
	
	/**
	 * Given a location name (e.g. city, area), it returns the country to which it belongs
	 * @param locationName 
	 * @return Country where the locationName belongs
	 */
	public String getCountryByLocationName(String locationName){
		String country = countryVocabulary.get(locationName.toLowerCase());
		if (country == null) {
			return "unknown";
		} else {
			return countryCodes.get(country);
		}
	}
	
	
	/**
	 * 
	 * @param gnObjectsFilename
	 * @return
	 */
	protected Map<String, String> readCountryVocabulary(String gnObjectsFilename) {
		Map<String, String> countryVoc = new HashMap<String, String>();
		
		EasyBufferedReader reader = new EasyBufferedReader(gnObjectsFilename);
		String line = null;
		int count = 0;
		long t0 = System.currentTimeMillis();
		logger.info("loading of objects started");
		while ((line = reader.readLine()) != null){
			if (line.trim().length() < 1) continue;
			count++;
			GeoObject city = new GeoObject(line);
			countryVoc.put(city.getName().toLowerCase(), city.getCountryCode());
			countryVoc.put(city.getAsciiName().toLowerCase(), city.getCountryCode());
			for (String alternateName : city.getAlternateNames()) {
				String existingName = countryVoc.get(alternateName.toLowerCase());
				if (existingName == null) {
					countryVoc.put(alternateName.toLowerCase(), city.getCountryCode());
				}
			}
			
		}
		logger.info(count + "objects loaded in " + (System.currentTimeMillis()-t0)/1000.0 + "secs");
		
		reader.close();
		logger.info("file " + gnObjectsFilename + "closed ");
		return countryVoc;
	}
}
