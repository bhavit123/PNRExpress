/**
 * Copyright 2013 Bhavit S. Sengar
 */

/**
 *A simple class which can be used to consume restful web-services.
 * 
 * @date December 15, 2013
 * @author Bhavit S. Sengar (bhavit.s.sengar@gmail.com)
 */

package com.bhavit.pnrexpress.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {

	String data = new String();
	JSONObject obj = new JSONObject();
	String url;
	String headerName = null;
	String headerValue = null;
	int CONNECTION_TIMEOUT = 5000;

	public RestClient(String s) {

		url = s;
	}

	public void addStringBody(String obj) {

		data = obj;

	}

	public void addJsonObject(JSONObject obj) {

		data = obj.toString();

	}

	public void addHeader(String name, String value) {

		headerName = name;
		headerValue = value;

	}

	public void addParam(String key, String value) {

		try {
			obj.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String executePost() {

		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(url);
			urlConnection = (HttpURLConnection) urlToRequest.openConnection();
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);

			String urlParameters = "";
			if(obj.length() == 0)
				urlParameters = data;
			else
				urlParameters = obj.toString();
			DataOutputStream wr = new DataOutputStream(
					urlConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				// handle unauthorized (if service requires user login)
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				// handle any other errors, like 404, 500,..
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return getResponseText(in);

		} catch (MalformedURLException e) {
			// URL is invalid
		} catch (SocketTimeoutException e) {
			// data retrieval or connection timed out
		} catch (IOException e) {
			// could not read response body
			// (could not create input stream)
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return null;
	}

	public String executeGet() {

		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(url);
			urlConnection = (HttpURLConnection) urlToRequest.openConnection();
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			// urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				// handle unauthorized (if service requires user login)
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				// handle any other errors, like 404, 500,..
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return getResponseText(in);

		} catch (MalformedURLException e) {
			// URL is invalid
		} catch (SocketTimeoutException e) {
			// data retrieval or connection timed out
		} catch (IOException e) {
			// could not read response body
			// (could not create input stream)
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return null;
	}

	private static String getResponseText(InputStream inStream) {
		// very nice trick from
		// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

}