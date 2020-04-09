package com.xdja.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Set;

/**
 * http工具
 *
 * @author zk
 * @since 2018/7/18
 */
public class HttpUtil {
    /**
     * static logger
     */
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * clientBuilder
     */
    private static HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    /**
     * 创建get请求
     *
     * @param url url
     * @return get工具
     */
    public static GetUtil createGet(String url) {
        return new GetUtil(url);
    }

    /**
     * 创建post请求
     *
     * @param url url
     * @return post请求工具
     */
    public static PostUtil createPost(String url) {
        return new PostUtil(url);
    }

    /**
     * 创建post请求
     *
     * @param url url
     * @return post请求工具
     */
    public static PostUtil createPost(String url, ContentType contentType) {
        return new PostUtil(url, contentType);
    }

    /**
     * 创建form表单请求
     *
     * @param url url
     * @return form表单工具
     */
    public static FormUtil createForm(String url) {
        return new FormUtil(url);
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
     * 获取 HttpClient
     *
     * @param requestBase request
     * @return httpClient
     */
    private static CloseableHttpClient build(HttpRequestBase requestBase) {
        //https 处理
        if ("https".equalsIgnoreCase(requestBase.getURI().getScheme())) {
            clientBuilder.setSSLSocketFactory(getSSLSocketFactory());
        }
        return clientBuilder.build();
    }

    /**
     * 执行http请求
     *
     * @param requestBase request请求
     * @return 封装好的请求结果
     */
    private static ResponseWrap exec(HttpRequestBase requestBase) {
        ResponseWrap responseWrap = null;
        try {
            CloseableHttpClient httpClient = build(requestBase);
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse execute = httpClient.execute(requestBase, context);
            responseWrap = new ResponseWrap(httpClient, requestBase, execute, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseWrap;
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
            requestConfigBuilder = RequestConfig.custom();
        }

        /**
         * 添加header
         *
         * @param name  name
         * @param value value
         * @return this
         */
        public GetUtil addHeader(String name, String value) {
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
        public GetUtil addParameter(String param, String value) {
            uriBuilder.setParameter(param, value);
            return this;
        }

        /**
         * 设置超时时间(该模块待抽象出去)
         *
         * @param timeout timeOut mills
         * @return this
         */
        public GetUtil setConnectTimeOut(int timeout){
            requestConfigBuilder.setConnectTimeout(timeout);
            return this;
        }

        /**
         * 设置socketTimeOut(该模块待抽象出去)
         *
         * @param timeout timeout mills
         * @return this
         */
        public GetUtil setSocketTimeout(int timeout){
            requestConfigBuilder.setSocketTimeout(timeout);
            return this;
        }

        /**
         * 执行并返回结果
         */
        public ResponseWrap execute() {
            try {
                URI uri = new URI(uriBuilder.build().toString().replace("%3F","?"));// replace %3F -> ?
                httpGet.setURI(uri);
                httpGet.setConfig(requestConfigBuilder.build());
            } catch (URISyntaxException e) {
                logger.error(e.getMessage(),e);
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

        public PostUtil(String url) {
            httpPost = new HttpPost(url);
            entityBuilder = EntityBuilder.create().setParameters(new ArrayList<NameValuePair>());
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
         * 添加header
         *
         * @param name  name
         * @param value value
         * @return this
         */
        public PostUtil addHeader(String name, String value) {
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
        public PostUtil addParameter(String param, String value) {
            entityBuilder.getParameters().add(new BasicNameValuePair(param, value));
            return this;
        }

        /**
         * 添加请求参数列表
         *
         * @param parameters 参数列表
         * @return this
         */
        public PostUtil addParameters(Map<String, String> parameters) {
            Set<String> keys = parameters.keySet();
            for (String key : keys) {
                addParameter(key, parameters.get(key));
            }
            return this;
        }

        /**
         * 添加jsonBody
         *
         * @param object object
         * @return this
         */
        public PostUtil addJsonBody(Object object) {
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding("utf-8");
            entityBuilder.setBinary(JSON.toJSONString(object).getBytes());
            return this;
        }

        /**
         * 添加二进制body
         *
         * @param bytes bytes
         * @return postUtil
         */
        public PostUtil addBody(byte[] bytes){
            entityBuilder.setBinary(bytes);
            return this;
        }
        /**
         * 执行 请求
         *
         * @return responseWrap
         */
        public ResponseWrap execute() {
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);

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
        public FormUtil addParameter(String name, String value) {
            builder.addTextBody(name, value, ContentType.TEXT_PLAIN.withCharset(Charset.forName("UTF-8")));
            return this;
        }

        /**
         * 添加普通参数list
         *
         * @param parameters normal parameter list
         * @return this
         */
        public FormUtil addParameters(Map<String, String> parameters) {
            Set<String> keySet = parameters.keySet();
            for (String key : keySet) {
                addParameter(key, parameters.get(key));
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
        public FormUtil addParameter(String name, byte[] bytes) {
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
        public FormUtil addParameter(String name, byte[] bytes, ContentType contentType, String filename) {
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
        public FormUtil addParameter(String name, File file, ContentType contentType, String fileName) {
            builder.addBinaryBody(name, file, contentType, fileName);
            return this;
        }

        /**
         * 添加 boundary
         *
         * @param boundary boundary
         * @return this
         */
        public FormUtil setBoundary(String boundary) {
            builder.setBoundary(boundary);
            return this;
        }

        /**
         * 执行请求
         *
         * @return 封装返回参数
         */
        public ResponseWrap execute() {
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            return exec(httpPost);
        }
    }

    /**
     * 响应包装
     */
    public static class ResponseWrap {
        private CloseableHttpClient httpClient;
        private HttpRequestBase request;
        private CloseableHttpResponse response;
        private HttpClientContext context;
        private HttpEntity entity;

        ResponseWrap(CloseableHttpClient httpClient, HttpRequestBase request, CloseableHttpResponse response, HttpClientContext context) {
            this.httpClient = httpClient;
            this.request = request;
            this.response = response;
            this.context = context;

            try {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    this.entity = new BufferedHttpEntity(entity);
                } else {
                    this.entity = new BasicHttpEntity();
                }

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
         * 获取Http响应码
         *
         * @return
         */
        public int statusCode() {
            return response.getStatusLine().getStatusCode();
        }

        public String getResponseHeader(String name) {
            for (Header header:response.getAllHeaders()) {
                if (name.equals(header.getName())) {
                    return header.getValue();
                }
            }
            return null;
        }
    }
}
