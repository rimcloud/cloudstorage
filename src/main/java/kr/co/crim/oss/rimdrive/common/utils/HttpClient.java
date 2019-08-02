package kr.co.crim.oss.rimdrive.common.utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final int httpConnectTimeOut = 30;

    private static RequestConfig getRequestConfig() {

	return RequestConfig.custom()
	.setSocketTimeout(httpConnectTimeOut * 1000)
	.setConnectTimeout(httpConnectTimeOut * 1000)
	.setConnectionRequestTimeout(httpConnectTimeOut * 1000)
	.build();
    }

    public static String doGetToString(String url) throws Exception {

	CloseableHttpClient httpClient = HttpClients.createDefault();
	CloseableHttpResponse httpResponse = null;
	String responseStr = null;

	try {
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.addHeader("User-Agent", USER_AGENT);

	    httpGet.setConfig(getRequestConfig());

	    httpResponse = httpClient.execute(httpGet);

	    int httpResponseStatus = httpResponse.getStatusLine().getStatusCode();

	    if (httpResponseStatus >= 200 && httpResponseStatus < 300) {
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
		    responseStr = EntityUtils.toString(entity);
		}

	    } 

	} catch(ConnectTimeoutException te) {

	    logger.error("GET ERROR - ConnectTimeoutException : Url [{}], Message [{}]", url, te.getMessage());

	} catch(SocketTimeoutException se) {

	    logger.error("GET ERROR - SocketTimeoutException : Url [{}], Message [{}]", url, se.getMessage());

	} catch (Exception e) {
	    logger.error("GET ERROR Excetpion {}", e);

	} finally {
		if (httpClient != null) {
			httpClient.close();
		}
		if (httpResponse != null) {
			httpResponse.close();
		}
	}

	return responseStr;
    }

    public static String doPostToString(String url, Map<String, Object> paramMap) throws Exception {

	CloseableHttpClient httpClient = HttpClients.createDefault();
	CloseableHttpResponse httpResponse = null;
	String responseStr = null;

	try {
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.addHeader("User-Agent", USER_AGENT);
	    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

	    httpPost.setConfig(getRequestConfig());

	    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	    for (String key : paramMap.keySet()) {
		if( paramMap.get(key) == null ) {
		    urlParameters.add(new BasicNameValuePair(key, ""));
		}
		else {
		    urlParameters.add(new BasicNameValuePair(key, paramMap.get(key).toString()));
		}
	    }

	    HttpEntity postParams = new UrlEncodedFormEntity(urlParameters, "UTF-8");
	    httpPost.setEntity(postParams);

	    httpResponse = httpClient.execute(httpPost);

	    int httpResponseStatus = httpResponse.getStatusLine().getStatusCode();

	    if (httpResponseStatus >= 200 && httpResponseStatus < 300) {
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
		    responseStr = EntityUtils.toString(entity);
		}

		logger.debug("POST Response Status [{}], String [{}]", httpResponseStatus, responseStr);
	    } else {

		logger.error("POST ERROR - Response Status [{}], Url [{}]", httpResponseStatus, url);
	    }

	} catch(ConnectTimeoutException te) {

	    logger.error("POST ERROR - ConnectTimeoutException : Url [{}], Message [{}]", url, te.getMessage());

	} catch(SocketTimeoutException se) {

	    logger.error("POST ERROR - SocketTimeoutException : Url [{}], Message [{}]", url, se.getMessage());

	} catch (Exception e) {
	    logger.error("POST ERROR Excetpion {}", e);

	} finally {
	    httpClient.close();
	    httpResponse.close();
	}

	return responseStr;
    }

}
