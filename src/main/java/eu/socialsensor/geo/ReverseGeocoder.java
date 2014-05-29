package eu.socialsensor.geo;

import java.util.Map;

import eu.socialsensor.geonames.GeoObject;
import eu.socialsensor.util.EasyBufferedReader;
import net.sf.javaml.core.kdtree.KDTree;
import net.sf.javaml.core.kdtree.KeyDuplicateException;
import net.sf.javaml.core.kdtree.KeySizeException;


/**
 * Given an input geo-point (lat/lon), it produces a human-readable label of the place where this point belongs to.
 * @author papadop
 *
 */
public class ReverseGeocoder extends AbstractGeoService {

	private final KDTree tree;
	

	/**
	 * Given a pair of lat/lon coordinates return the country where it belongs.
	 * @param lat
	 * @param lon
	 * @return Name of country where it belongs.
	 */
	public String getCountryByLatLon(double lat, double lon){
		double[] q = new double[2];
		q[0] = lat;
		q[1] = lon;
		LightweightGeoObject city = null;
		try {
			city = (LightweightGeoObject)tree.nearest(q);
		} catch (KeySizeException e) {
			e.printStackTrace();
		}
		if (city == null) return null;
		return countryCodes.get(city.getCountryCode());
	}
	
	
	/**
	 * Constructor
	 * @param gnObjectFile	Geonames object file (e.g. (e.g. cities1000.txt, allCountries.txt), e.g. in zipped form: http://download.geonames.org/export/dump/cities1000.zip
	 * @param gnCountryInfoFile Geonames countryInfo.txt file, http://download.geonames.org/export/dump/countryInfo.txt
	 */
	public ReverseGeocoder(String gnObjectFile, String gnCountryInfoFile){
		super(gnCountryInfoFile);
		tree = new KDTree(2);
		
		Map<String, String> countryCodeMap = readCountryCodeMap(gnCountryInfoFile);
		loadCitiesFileToTree(gnObjectFile, countryCodeMap);
		
	}
	
	/**
	 * A kd-tree is loaded with the set of geographical objects. The key for each entry
	 * corresponds to its lat/lon coordinates, while the value is a LightweightGeoObject
	 * @param citiesFilename
	 * @param countryCodes
	 */
	protected void loadCitiesFileToTree(String citiesFilename, Map<String, String> countryCodes){
		EasyBufferedReader reader = new EasyBufferedReader(citiesFilename);
		String line = null;
		int count = 0;
		long t0 = System.currentTimeMillis();
		while ((line = reader.readLine()) != null){
			if (line.trim().length() < 1) continue;

			GeoObject city = new GeoObject(line);
			if (count++ % 100000 == 0){
				System.out.print("*");
			}
			double[] coords = new double[2];
			coords[0] = city.getLat();
			coords[1] = city.getLon();
			try {
				tree.insert(coords, new LightweightGeoObject(city));
			} catch (KeySizeException e) {
				e.printStackTrace();
			} catch (KeyDuplicateException e) {
				e.printStackTrace();
			}
		}
		System.out.println("... in " + (System.currentTimeMillis()-t0)/1000.0 + "secs");
		System.out.println();
		reader.close();
	}
}
