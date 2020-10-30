package com.eastday.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {

	/**
	 * get请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				// httpclient.close();
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	/**
	 * get异步请求
	 * @param url
	 * @return
	 */
	public static String doGetAsync(String url) {
		String resultString = "";
		try {
			CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
			client.start();
			HttpGet request = new HttpGet(url);
			client.execute(request, new FutureCallback<HttpResponse>() {
				//执行异步操作  请求完成后
				@Override
				public void completed(final HttpResponse response) {
					try {
						String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
						getResultString(resultString);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//请求失败处理
				@Override
				public void failed(final Exception ex) {
					System.out.println(request.getRequestLine() + "->" + ex);
					System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				}

				@Override
				public void cancelled() {
					System.out.println(request.getRequestLine() + " cancelled");
					System.out.println(" callback thread id is : " + Thread.currentThread().getId());
				}

			});
		} catch (Exception e) {
			log.error(e.toString());
		}
		return  resultString;
	}

	/**
	 * post请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	//上传
	public static String doPostOfMultipartFormData(String url, File file, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			if (file != null && file.exists() && file.isFile()) {
				multipartEntityBuilder.addBinaryBody("file", file);
			}
			if (param != null) {
				for (String key : param.keySet()) {
					multipartEntityBuilder.addTextBody(key, param.get(key));
				}
			}
			httpPost.setEntity(multipartEntityBuilder.build());
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	/**
	 * post JSON格式
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostOfJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
			log.error("报错数据"+json);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	/**
	 * put JSON
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPutOfJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Put请求
			HttpPut httpPut = new HttpPut(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPut.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPut);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	/**
	 * delete
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doDelete(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建Http Delete请求
			HttpDelete httpDelete = new HttpDelete(uri);
			// 执行http请求
			response = httpClient.execute(httpDelete);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}

	/**
	 * delete JSON
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doDeleteOfJson(String url, String json) {
		/**
		 * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
		 */
		class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
			public static final String METHOD_NAME = "DELETE";

			@SuppressWarnings("unused")
			public HttpDeleteWithBody() {
			}

			@SuppressWarnings("unused")
			public HttpDeleteWithBody(URI uri) {
				setURI(uri);
			}

			public HttpDeleteWithBody(String uri) {
				setURI(URI.create(uri));
			}

			@Override
			public String getMethod() {
				return METHOD_NAME;
			}
		}

		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
			StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpDelete.setEntity(params);
			// 执行http请求
			response = httpClient.execute(httpDelete);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return resultString;
	}


	public static String getResultString(String resultString){
		return resultString;
	}

}
