geo-util
========

The project currently offers support for two common geocoding operations without the need to install complicated GIS software. 

<h2>Getting started</h2>

You first need to download and store locally at least two files from Geonames.org:

* <a href="http://download.geonames.org/export/dump/countryInfo.txt">countryInfo.txt</a>
* A geo-object file, e.g. <a href="http://download.geonames.org/export/dump/cities1000.zip">cities1000.zip</a> or <a href="http://download.geonames.org/export/dump/allCountries.zip">allCountries.zip</a>. Make sure you unzip them.

There are two basic classes that you will want to use: `ReverseGeocoder` and `Countrycoder`. Both are initialized giving the two above files as arguments to their constructors. Once initialized, you can use the following two methods:

* `ReverseGeocoder.getCountryByLatLon(lat, lon)`, which given a pair of coordinates returns the country where these belong,
* `Countrycoder.getCountryByLocationName(locationName)`, which given a location name returns the country where this belongs.


<h2>Considerations</h2>

Both of the above methods are not expected to always work correctly. This is due to the following. The first one (getCountryByLatLon) is based on a very simple approximation, i.e. returns the country of the closest geographical object in the index (therefore, you should expect better accuracy by using the allCountries file compared to the cities1000). The second method (getCountryByLocationName) simply indexes the location names (incl. alternate names) found in the geonames files and returns the country through a look-up. It does not perform any approximate text matching, nor does it handle name synonymy.



<h3>Contact for further details about the project</h3>

Symeon Papadopoulos (papadop@iti.gr)
