//******************************************************************
//
//  PackRepo.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski.repository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.io.CharStreams;

public class PackagistRepository implements IRepository {
	private String addr;

	@Override
	public List<String[]> getNextResults(IProgressMonitor monitor) {
		LinkedList<String[]> result = new LinkedList<String[]>();
		try {
			InputStream input = new URL(addr).openStream();
			JSONObject object = new JSONObject(
					CharStreams.toString(new InputStreamReader(input)));
			input.close();
			JSONArray resultArray = object.getJSONArray("results");
			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject obj = resultArray.getJSONObject(i);
				result.add(new String[] { obj.getString("name"),
						obj.getString("description") });
			}
			addr = null;
			if (object.has("next")) {
				addr = object.getString("next");
			}
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public void setQuery(String query) {
		try {
			addr = "https://packagist.org/search.json?q="
					+ URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
	}

	@Override
	public boolean hasMoreResults() {
		return addr != null;
	}

	@Override
	public String toString() {
		return "Packagist Repository";
	}

}
