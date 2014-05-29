package eu.socialsensor.geo;

import java.util.HashMap;
import java.util.Map;

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
		super(gnCountryInfoFile);
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
	 * @param citiesFilename
	 * @return
	 */
	protected Map<String, String> readCountryVocabulary(String citiesFilename) {
		Map<String, String> countryVoc = new HashMap<String, String>();
		
		EasyBufferedReader reader = new EasyBufferedReader(citiesFilename);
		String line = null;
		int count = 0;
		long t0 = System.currentTimeMillis();
		while ((line = reader.readLine()) != null){
			if (line.trim().length() < 1) continue;
			
			GeoObject city = new GeoObject(line);
			countryVoc.put(city.getName().toLowerCase(), city.getCountryCode());
			countryVoc.put(city.getAsciiName().toLowerCase(), city.getCountryCode());
			for (String alternateName : city.getAlternateNames()) {
				String existingName = countryVoc.get(alternateName.toLowerCase());
				if (existingName == null) {
					countryVoc.put(alternateName.toLowerCase(), city.getCountryCode());
				}
			}
			
			if (count++ % 100000 == 0){
				System.out.print("*");
			}
		}
		reader.close();
		
		System.out.println("... in " + (System.currentTimeMillis()-t0)/1000.0 + "secs");
		System.out.println();
		
		return countryVoc;
	}
}
