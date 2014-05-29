package eu.socialsensor.geo;

import java.util.HashMap;
import java.util.Map;

import eu.socialsensor.util.EasyBufferedReader;

/**
 * Abstract GeoService class that includes common methods to be inherited by different implementations.
 * @author papadop
 *
 */
public abstract class AbstractGeoService {

	protected final Map<String, String> countryCodes;
	
	
	public AbstractGeoService(String gnCountryInfoFile){
		countryCodes = readCountryCodeMap(gnCountryInfoFile);
	}
	
	
	protected Map<String, String> readCountryCodeMap(String countryInfoFile){
		Map<String, String> countryCodes = new HashMap<String, String>();
		
		EasyBufferedReader reader = new EasyBufferedReader(countryInfoFile);
		String line = null;
		while ((line = reader.readLine())!= null){
			if (line.startsWith("#")) continue;
			String[] parts = line.split("\t");
			countryCodes.put(parts[0], parts[4]);
		}
		reader.close();
		
		return countryCodes;
	}
	
}
