package com.xdja.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * EffectiveHttp
 *
 * @author zhangming
 * @date 2019/3/1 1:15
 */
public class EffectiveHttp {

	/**
	 * static logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(EffectiveHttp.class);

	/**
	 * 保活时间
	 */
	private static int keepAlive = 30;

	/**
	 * 连接池最大连接数
	 */
	private static final int maxTotal = 1000;

	/**
	 * 路由的默认最大连接
	 */
	private static final int defaultMaxPerRoute = 400;

	/**
	 * 连接超时时间
	 */
	private static final int maxIdleTime = 30000;

	/**
	 * 请求超时时间
	 */
	private static final int connectTimeout = 30000;

	/**
	 * 获取数据超时时间
	 */
	private static final int socketTimeout = 30000;

	/**
	 * 保活连接策略
	 */
	private static ConnectionKeepAliveStrategy keepAliveStrategy;

	/**
	 * Http连接池
	 */
	private static PoolingHttpClientConnectionManager connectionManager;

	/**
	 * clientBuilder
	 */
	private static HttpClientBuilder clientBuilder;

	/**
	 * httpClient
	 */
	private volatile static CloseableHttpClient httpClient;

	/**
	 * monitor Thread
	 */
	private static ScheduledExecutorService monitorExecutor;

	/**
	 * 创建get请求
	 *
	 * @param url url
	 * @return get工具
	 */
	public static EffectiveHttp.GetUtil createGet(String url) {
		return new EffectiveHttp.GetUtil(url);
	}

	/**
	 * 创建post请求
	 *
	 * @param url url
	 * @return post请求工具
	 */
	public static EffectiveHttp.PostUtil createPost(String url) {
		return new EffectiveHttp.PostUtil(url);
	}

	/**
	 * 创建post请求
	 *
	 * @param url url
	 * @return post请求工具
	 */
	public static EffectiveHttp.PostUtil createPost(String url, ContentType contentType) {
		return new EffectiveHttp.PostUtil(url, contentType);
	}

	public String httpPostJson(String url, String data) throws Exception {

		String retResult = "";
		HttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(500000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Connection", "close");
		httpPost.setHeader("Content-Type", "application/json");
		HttpResponse httpResponse = null;
		try {
			httpPost.setEntity(new StringEntity(data, "utf-8"));
			httpResponse = client.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				return "";
			}
			retResult = EntityUtils.toString(httpResponse.getEntity());
		} catch (IOException e) {
			throw new Exception("调用接口失败");
		} finally {
			httpPost.releaseConnection();
			EntityUtils.consumeQuietly(httpResponse.getEntity());
		}
		return retResult;
	}

	/**
	 * 创建form表单请求
	 *
	 * @param url url
	 * @return form表单工具
	 */
	public static EffectiveHttp.FormUtil createForm(String url) {
		return new EffectiveHttp.FormUtil(url);
	}

	/**
	 * 初始化参数
	 */
	static {
		init();
	}

	/**
	 * 初始化Http配置
	 */
	private static void init() {
		logger.debug("@EffectiveHttp-------初始化Http连接池配置......");

		connectionManager = new PoolingHttpClientConnectionManager(getRegistry());
		connectionManager.setMaxTotal(maxTotal);
		connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

		keepAliveStrategy = getKeepAliveStrategy();

		clientBuilder = createClientBuilder();
	}

	/**
	 * 创建 HttpClient实例(单例模式)
	 */
	public static CloseableHttpClient getInstance() {

		if (httpClient == null) {
			//多线程下多个线程同时调用getHttpClient容易导致重复创建httpClient对象的问题,所以加上了同步锁
			synchronized (EffectiveHttp.class) {
				if (httpClient == null) {
					httpClient = clientBuilder.build();
					//开启监控线程,对异常和空闲线程进行关闭
					monitorExecutor = Executors.newScheduledThreadPool(1);
					monitorExecutor.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							//关闭异常连接
							connectionManager.closeExpiredConnections();
							//关闭15s空闲的连接
							connectionManager.closeIdleConnections(maxIdleTime, TimeUnit.SECONDS);
							logger.info("close expired and idle for over 15s connection");
						}
					}, 30, 30, TimeUnit.SECONDS);
				}
			}
		}
		return httpClient;
	}

	/**
	 * 执行http请求
	 *
	 * @param requestBase request请求
	 * @return 封装好的请求结果
	 */
	private static EffectiveHttp.ResponseWrap exec(HttpRequestBase requestBase) {
		EffectiveHttp.ResponseWrap responseWrap = null;
		try {
			httpClient = getInstance();
			HttpClientContext context = HttpClientContext.create();
			CloseableHttpResponse execute = httpClient.execute(requestBase, context);
			responseWrap = new EffectiveHttp.ResponseWrap(execute);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseWrap;
	}

	/**
	 * HttpClientBuilder
	 */
	public static HttpClientBuilder createClientBuilder() {
		HttpClientBuilder clientBuilder = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setKeepAliveStrategy(keepAliveStrategy);
		return clientBuilder;
	}

	/**
	 * 连接策略
	 *
	 * @return
	 */
	private static ConnectionKeepAliveStrategy getKeepAliveStrategy() {
		ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				HeaderElementIterator it = new BasicHeaderElementIterator
						(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && "timeout".equalsIgnoreCase
							(param)) {
						return Long.parseLong(value) * 1000;
					}
				}
				return keepAlive * 1000;
			}
		};
		return keepAliveStrategy;
	}

	/**
	 * @return
	 */
	private static Registry getRegistry() {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", getSSLSocketFactory())
				.register("http", new PlainConnectionSocketFactory())
				.build();
		return socketFactoryRegistry;
	}

	/**
	 * 构建sslSocketFactory
	 *
	 * @return sslFactory
	 */
	private static LayeredConnectionSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				//信任所有
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			}).build();
			return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * get请求工具类
	 */
	public static class GetUtil {
		/**
		 * 请求参数
		 */
		private HttpGet httpGet;
		/**
		 * get url builder
		 */
		private URIBuilder uriBuilder;
		/**
		 * request config builder
		 */
		private RequestConfig.Builder requestConfigBuilder;

		private GetUtil(String url) {
			httpGet = new HttpGet(url);
			uriBuilder = new URIBuilder().setPath(httpGet.getURI().toString());
			uriBuilder.setCharset(Charset.forName("UTF-8"));
			requestConfigBuilder = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout);
		}

		/**
		 * 添加header
		 *
		 * @param name  name
		 * @param value value
		 * @return this
		 */
		public EffectiveHttp.GetUtil addHeader(String name, String value) {
			httpGet.addHeader(name, value);
			return this;
		}

		/**
		 * 添加请求参数
		 *
		 * @param param param
		 * @param value value
		 * @return this
		 */
		public EffectiveHttp.GetUtil addParameter(String param, String value) {
			uriBuilder.setParameter(param, value);
			return this;
		}

		/**
		 * 设置超时时间(该模块待抽象出去)
		 *
		 * @param timeout timeOut mills
		 * @return this
		 */
		public EffectiveHttp.GetUtil setConnectTimeOut(int timeout) {
			requestConfigBuilder.setConnectTimeout(timeout);
			return this;
		}

		/**
		 * 设置socketTimeOut(该模块待抽象出去)
		 *
		 * @param timeout timeout mills
		 * @return this
		 */
		public EffectiveHttp.GetUtil setSocketTimeout(int timeout) {
			requestConfigBuilder.setSocketTimeout(timeout);
			return this;
		}

		/**
		 * 执行并返回结果
		 */
		public EffectiveHttp.ResponseWrap execute() {
			try {
				URI uri = new URI(uriBuilder.build().toString().replace("%3F", "?"));// replace %3F -> ?
				httpGet.setURI(uri);
				httpGet.setConfig(requestConfigBuilder.build());
			} catch (URISyntaxException e) {
				logger.error(e.getMessage(), e);
			}

			return exec(httpGet);
		}
	}

	/**
	 * post请求
	 */
	public static class PostUtil {
		/**
		 * 请求参数
		 */
		private HttpPost httpPost;
		/**
		 * entityBuilder
		 */
		private EntityBuilder entityBuilder;
		/**
		 * request config builder
		 */
		private RequestConfig.Builder requestConfigBuilder;

		public PostUtil(String url) {
			httpPost = new HttpPost(url);
			entityBuilder = EntityBuilder.create().setParameters(new ArrayList<NameValuePair>());
			requestConfigBuilder = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout);
		}

		/**
		 * 带contentType的post
		 *
		 * @param url         url
		 * @param contentType 类型
		 */
		public PostUtil(String url, ContentType contentType) {
			this(url);
			entityBuilder.setContentType(contentType);
		}



		/**
		 * 设置超时时间(该模块待抽象出去)
		 *
		 * @param timeout timeOut mills
		 * @return this
		 */
		public EffectiveHttp.PostUtil setConnectTimeOut(int timeout) {
			requestConfigBuilder.setConnectTimeout(timeout);
			return this;
		}

		/**
		 * 设置socketTimeOut(该模块待抽象出去)
		 *
		 * @param timeout timeout mills
		 * @return this
		 */
		public EffectiveHttp.PostUtil setSocketTimeout(int timeout) {
			requestConfigBuilder.setSocketTimeout(timeout);
			return this;
		}

		/**
		 * 添加header
		 *
		 * @param name  name
		 * @param value value
		 * @return this
		 */
		public EffectiveHttp.PostUtil addHeader(String name, String value) {
			httpPost.addHeader(name, value);
			return this;
		}

		/**
		 * 添加请求参数
		 *
		 * @param param param
		 * @param value value
		 * @return this
		 */
		public EffectiveHttp.PostUtil addParameter(String param, String value) {
			entityBuilder.getParameters().add(new BasicNameValuePair(param, value));
			return this;
		}

		/**
		 * 添加请求参数列表
		 *
		 * @param parameters 参数列表
		 * @return this
		 */
		public EffectiveHttp.PostUtil addParameters(Map<String, String> parameters) {
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				addParameter(e.getKey(), e.getValue());
			}
			return this;
		}

		/**
		 * 添加jsonBody
		 *
		 * @param object object
		 * @return this
		 */
		public EffectiveHttp.PostUtil addJsonBody(Object object) {
			entityBuilder.setContentType(ContentType.APPLICATION_JSON);
			entityBuilder.setContentEncoding("utf-8");
			try {
				entityBuilder.setBinary(JSON.toJSONString(object).getBytes("UTF-8"));
			} catch (Exception e) {
				throw new RuntimeException("@EffectiveHttp-------charset error!");
			}
			return this;
		}

		/**
		 * 添加二进制body
		 *
		 * @param bytes bytes
		 * @return postUtil
		 */
		public EffectiveHttp.PostUtil addBody(byte[] bytes) {
			entityBuilder.setBinary(bytes);
			return this;
		}

		/**
		 * 执行 请求
		 *
		 * @return responseWrap
		 */
		public EffectiveHttp.ResponseWrap execute() {
			HttpEntity httpEntity = entityBuilder.build();
			httpPost.setEntity(httpEntity);
			httpPost.setConfig(requestConfigBuilder.build());
			return exec(httpPost);
		}
	}

	/**
	 * form表单提交
	 */
	public static class FormUtil {
		/**
		 * httpPost
		 */
		private HttpPost httpPost;
		/**
		 * form builder
		 */
		private MultipartEntityBuilder builder;

		public FormUtil(String url) {
			httpPost = new HttpPost(url);
			builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		}

		/**
		 * 添加普通参数
		 *
		 * @param name  name
		 * @param value value
		 * @return this
		 */
		public EffectiveHttp.FormUtil addParameter(String name, String value) {
			builder.addTextBody(name, value, ContentType.TEXT_PLAIN.withCharset(Charset.forName("UTF-8")));
			return this;
		}

		/**
		 * 添加普通参数list
		 *
		 * @param parameters normal parameter list
		 * @return this
		 */
		public EffectiveHttp.FormUtil addParameters(Map<String, String> parameters) {
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				addParameter(e.getKey(), e.getValue());
			}
			return this;
		}

		/**
		 * 添加字节流
		 *
		 * @param name  name
		 * @param bytes 字节流
		 * @return this
		 */
		public EffectiveHttp.FormUtil addParameter(String name, byte[] bytes) {
			builder.addBinaryBody(name, bytes);
			return this;
		}

		/**
		 * 添加二进制流
		 *
		 * @param name        参数名称
		 * @param bytes       参数值
		 * @param contentType 参数类型
		 * @param filename    文件名
		 * @return this
		 */
		public EffectiveHttp.FormUtil addParameter(String name, byte[] bytes, ContentType contentType, String filename) {
			builder.addBinaryBody(name, bytes, contentType, filename);
			return this;
		}

		/**
		 * 添加文件
		 *
		 * @param name        参数名称
		 * @param file        文件
		 * @param contentType contentType
		 * @param fileName    文件名称
		 * @return this
		 */
		public EffectiveHttp.FormUtil addParameter(String name, File file, ContentType contentType, String fileName) {
			builder.addBinaryBody(name, file, contentType, fileName);
			return this;
		}

		/**
		 * 添加 boundary
		 *
		 * @param boundary boundary
		 * @return this
		 */
		public EffectiveHttp.FormUtil setBoundary(String boundary) {
			builder.setBoundary(boundary);
			return this;
		}

		/**
		 * 执行请求
		 *
		 * @return 封装返回参数
		 */
		public EffectiveHttp.ResponseWrap execute() {
			HttpEntity httpEntity = builder.build();
			httpPost.setEntity(httpEntity);
			return exec(httpPost);
		}
	}

	/**
	 * 响应包装
	 */
	public static class ResponseWrap {
		private CloseableHttpResponse response;
		private HttpEntity entity;

		ResponseWrap(CloseableHttpResponse response) {
			this.response = response;

			try {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					this.entity = new BufferedHttpEntity(entity);
				} else {
					this.entity = new BasicHttpEntity();
				}
				//释放连接
				EntityUtils.consume(entity);
				this.response.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		/**
		 * 获取响应文本信息
		 *
		 * @return
		 */
		public String getString() {
			try {
				return EntityUtils.toString(entity, Consts.UTF_8);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		public byte[] getBytes() {
			try {
				return EntityUtils.toByteArray(entity);
			} catch (ParseException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		/**
		 * 获取Http响应码
		 *
		 * @return
		 */
		public int statusCode() {
			return response.getStatusLine().getStatusCode();
		}

		/**
		 * 获取InputStream
		 *
		 * @return
		 */
		public InputStream getInputStream() {
			try {
				return entity.getContent();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		/**
		 * 获取Response
		 */
		public CloseableHttpResponse getResponse() {
			return this.response;
		}



	}
}
