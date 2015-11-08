package org.nla.geocoding;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.GeocodingApi.ComponentFilter;
import com.google.maps.PendingResult.Callback;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class GeocodingTest {

	private GeoApiContext context;

	@Test
	public void testGeocodeAddressInFrance() {
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context,
					"13 rue Charles d'Orleans 16100 Cognac France").await();

		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(1));
		assertThat(results[0].formattedAddress, equalTo("13 Rue Charles d'Orléans, 16100 Cognac, France"));
	}

	@Test
	@Ignore(value="I don't lake any difference with or without the language restriction")
	public void testGeocodeAddressInFranceWithResultsInFrench() {
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi
					.geocode(context,
							"13 rue Charles d'Orleans 16100 Cognac France")
					.language("fr").await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(1));
	}
	
	@Test
	public void testGeocodeParisWorldwide() {
		GeocodingResult[] results = null;
		try {

			results = GeocodingApi.geocode(context, "Paris").await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(5));
	}

	@Test
	public void testGeocodeParisInFrance() {
		GeocodingResult[] results = null;
		try {

			results = GeocodingApi.geocode(context, "Paris").components(ComponentFilter.country("fr")).await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(1));
	}
	

	
	@Test
	public void testGeocodeAddressInFranceRestrictedToFrance() {
		GeocodingResult[] results = null;
		try {

			results = GeocodingApi
					.geocode(context,
							"13 rue Charles d'Orleans 16100 Cognac France")
					.components(ComponentFilter.country("fr")).await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(1));
	}
	
	@Test
	public void testGeocodeAddressThatDoesntExistInGermany() {
		GeocodingResult[] results = null;
		try {

			results = GeocodingApi
					.geocode(context,
							"13 rue Charles d'Orleans 16100 Cognac")
					.components(ComponentFilter.country("de")).await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(1));
		assertThat(results[0].partialMatch, is(true));
		assertThat(results[0].formattedAddress, equalTo("Germany"));
		
	}

	@Before
	public void setUp() {
		context = new GeoApiContext()
				.setApiKey("AIzaSyCrU1j1-tmeSNQrD2DgxSI5v5Tp-Imzd6c");
	}

	@Test
	public void testReverseGeocodeCognac() {		
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.reverseGeocode(context, new LatLng(45.7, -0.3333)).await();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertThat(results.length, equalTo(6));
		assertThat(results[0].formattedAddress, equalTo("8 Rue de Boutiers, 16100 Cognac, France"));
	}
	
	@Test
	public void testGeocodeAddressAsync() throws Exception {
		final List<GeocodingResult[]> responses = new ArrayList<GeocodingResult[]>();
		
		GeocodingApiRequest request = GeocodingApi.newRequest(context)
				.address("13 rue Charles d'Orleans 16100 Cognac France")
				.components(ComponentFilter.country("fr")).language("fr");

		Callback<GeocodingResult[]> callback = new Callback<GeocodingResult[]>() {

			public void onResult(GeocodingResult[] result) {
				responses.add(result);
			}

			public void onFailure(Throwable t) {
				fail("Unable to decode address: " + t.getMessage());
			}
		};
		request.setCallback(callback);
		Thread.sleep(2500);

		assertThat(responses.size(), equalTo(1));
		assertThat(responses.get(0).length, equalTo(1));
	}
}
