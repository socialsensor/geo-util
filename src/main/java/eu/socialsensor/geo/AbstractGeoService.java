package eu.socialsensor.geo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.socialsensor.util.EasyBufferedReader;

/**
 * Abstract GeoService class that includes common methods to be inherited by different implementations.
 * @author papadop
 *
 */
public abstract class AbstractGeoService {

	protected final Map<String, String> countryCodes;
	protected Logger logger;
	
	public AbstractGeoService(String gnCountryInfoFile, Logger logger){
		this.logger = logger;
		logger.debug("constructor");
		countryCodes = readCountryCodeMap(gnCountryInfoFile);
	}
	
	
	protected Map<String, String> readCountryCodeMap(String countryInfoFile){
		Map<String, String> countryCodes = new HashMap<String, String>();
		
		logger.info("opening file: " + countryInfoFile);
		EasyBufferedReader reader = new EasyBufferedReader(countryInfoFile);
		String line = null;
		while ((line = reader.readLine())!= null){
			if (line.startsWith("#")) continue;
			String[] parts = line.split("\t");
			countryCodes.put(parts[0], parts[4]);
		}
		reader.close();
		logger.info("read: " + countryCodes.size() + " country codes");
		logger.info("closing file: " + countryInfoFile);
		
		return countryCodes;
	}
	
}
