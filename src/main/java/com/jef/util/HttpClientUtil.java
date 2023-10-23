package com.jef.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger log = LoggerFactory
            .getLogger(HttpClientUtil.class);


    public static final String CHARSET_UTF = "UTF-8";


    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_XML = "application/xml";

    private static String sendRequest(HttpEntityEnclosingRequestBase httpRequest, HttpEntity stringEntity, String mediaType) throws Exception {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            httpRequest.setHeader(HttpHeaders.ACCEPT, mediaType);
            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, mediaType);
            if (stringEntity != null) httpRequest.setEntity(stringEntity);

            ResponseHandler<String> handler = new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response)
                        throws IOException {
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity httpEntity = response.getEntity();
                    String resultStr = null;
                    if (httpEntity != null) {
                        resultStr = EntityUtils.toString(httpEntity, CHARSET_UTF);
                    }
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        log.info("sendRequest success response = " + resultStr);
                    } else {
                        log.error("sendRequest fail response : " + resultStr);
                    }
                    return resultStr;
                }
            };
            return httpClient.execute(httpRequest, handler);
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpRequest != null) {
                httpRequest.releaseConnection();
            }
        }
    }

    public static String requestPost(String urlPath, String strReq, String mydiaType) throws Exception {
        log.info(" apiUrl: {}, reqStr: {},  mydiaType: {}", urlPath, strReq, mydiaType);
        HttpPost httpPost = new HttpPost(urlPath);
        StringEntity stringEntity = new StringEntity(strReq, CHARSET_UTF);
        return sendRequest(httpPost, stringEntity, mydiaType.toString());
    }

    public HttpClientUtil() {
    }

    public static String sendGetRequest(String reqURL) {
        return sendGetRequest(reqURL, null);
    }

    /**
     * 发送HTTP_GET请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址(含参数)
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws MalformedURLException
     * @throws URISyntaxException
     */

    public static String sendGetRequest(String reqURL, String decodeCharset) {

        long responseLength = 0; // 响应长度

        String responseContent = null; // 响应内容

        HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
        // 防止 java.net.URISyntaxException
        // URL url = new URL(reqURL);
        // URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(),
        // url.getQuery(), null);

        HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet

        try {

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求

            HttpEntity entity = response.getEntity(); // 获取响应实体

            if (null != entity) {

                responseLength = entity.getContentLength();

                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);

                EntityUtils.consume(entity); // Consume response content

            }

            // System.out.println("请求地址: " + httpGet.getURI());

            // System.out.println("响应状态: " + response.getStatusLine());

            // System.out.println("响应长度: " + responseLength);

            // System.out.println("响应内容: " + responseContent);

        } catch (ClientProtocolException e) {

            log.error(
                    "该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下",
                    e);

        } catch (ParseException e) {

            log.error(e.getMessage(), e);

        } catch (IOException e) {

            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源

        }

        return responseContent;

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
     *
     * @param isEncoder 用于指明请求数据是否需要UTF-8编码,true为需要
     * @throws IOException
     */

    public static String sendPostRequest(String reqURL, String sendData,
                                         boolean isEncoder) throws IOException {

        return sendPostRequest(reqURL, sendData, isEncoder, null, null);

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param sendData      请求参数,若有多个参数则应拼接成param11=value11¶m22=value22¶m33=value33的形式后,
     *                      传入该参数中
     * @param isEncoder     请求数据是否需要encodeCharset编码,true为需要
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws IOException
     */

    public static String sendPostRequest(String reqURL, String sendData,
                                         boolean isEncoder, String encodeCharset, String decodeCharset)
            throws IOException {
        String responseContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        // httpPost.setHeader(HTTP.CONTENT_TYPE,
        // "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for (String str : sendData.split("&")) {
                    formParams.add(new BasicNameValuePair(str.substring(0,
                            str.indexOf("=")),
                            str.substring(str.indexOf("=") + 1)));
                }
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(
                        formParams, encodeCharset == null ? "UTF-8"
                                : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(sendData));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
            throw e;
        } finally {
            httpClient.close();
        }
        return responseContent;
    }

    /**
     * @param reqURL
     * @param data
     * @return
     * @throws IOException
     */
    public static String sendPostRequest4Json(String reqURL, String data)
            throws IOException {
        return sendPostRequest4Json(reqURL, data, null);
    }

    /**
     * @param reqURL
     * @param data
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String sendPostRequest4Json(String reqURL, String data,
                                              ContentType contentType) throws IOException {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        try {
            httpPost.setEntity(new StringEntity(data, contentType));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequest(String reqURL,
                                         Map<String, String> params, String encodeCharset,
                                         String decodeCharset) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();


            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    responseContent = EntityUtils.toString(entity,
                            decodeCharset == null ? "UTF-8" : decodeCharset);
                    EntityUtils.consume(entity);
                }
            } else {
                throw new Exception("Unknown error：" + statusCode);
            }
        } catch (Exception e) {
            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }


    /**
     * 发送HTTPS_POST请求
     */

    public static String sendPostSSLRequest(String reqURL,
                                            Map<String, String> params) {

        return sendPostSSLRequest(reqURL, params, null, null);

    }

    /**
     * 发送HTTPS_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendPostSSLRequest(String reqURL,
                                            Map<String, String> params, String encodeCharset,
                                            String decodeCharset) {

        String responseContent = "";

        HttpClient httpClient = new DefaultHttpClient();

        X509TrustManager xtm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        try {

            SSLContext ctx = SSLContext.getInstance("TLS");

            ctx.init(null, new TrustManager[]{xtm}, null);

            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            httpClient.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", 443, socketFactory));

            HttpPost httpPost = new HttpPost(reqURL);

            List<NameValuePair> formParams = new ArrayList<NameValuePair>();

            for (Map.Entry<String, String> entry : params.entrySet()) {

                formParams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));

            }

            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (null != entity) {

                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);

                EntityUtils.consume(entity);

            }

        } catch (Exception e) {

            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息为", e);

        } finally {

            httpClient.getConnectionManager().shutdown();

        }

        return responseContent;

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据,其数据类型为<code>java.util.Map<String, String></code>
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     */

    private static String sendPostRequestByJava(String reqURL,
                                                Map<String, String> params) {

        StringBuilder sendData = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {

            sendData.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");

        }

        if (sendData.length() > 0) {

            sendData.setLength(sendData.length() - 1); // 删除最后一个&符号

        }

        return sendPostRequestByJava(reqURL, sendData.toString());

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     */

    public static String sendPostRequestByJava(String reqURL, String sendData) {

        HttpURLConnection httpURLConnection = null;

        OutputStream out = null; // 写

        InputStream in = null; // 读

        int httpStatusCode = 0; // 远程主机响应的HTTP状态码

        try {

            URL sendUrl = new URL(reqURL);

            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true); // 指示应用程序要将数据写入URL连接,其值默认为false

            httpURLConnection.setUseCaches(false);

            httpURLConnection.setConnectTimeout(30000); // 30秒连接超时

            httpURLConnection.setReadTimeout(30000); // 30秒读取超时

            out = httpURLConnection.getOutputStream();

            out.write(sendData.toString().getBytes());

            // 清空缓冲区,发送数据

            out.flush();

            // 获取HTTP状态码

            httpStatusCode = httpURLConnection.getResponseCode();

            // 该方法只能获取到[HTTP/1.0 200 OK]中的[OK]

            // 若对方响应的正文放在了返回报文的最后一行,则该方法获取不到正文,而只能获取到[OK],稍显遗憾

            // respData = httpURLConnection.getResponseMessage();

            // 处理返回结果

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));

            String row = null;

            String respData = "";

            if ((row = br.readLine()) != null) { // readLine()方法在读到换行[\n]或回车[\r]时,即认为该行已终止

                respData = row; // HTTP协议POST方式的最后一行数据为正文数据

            }

            br.close();

            in = httpURLConnection.getInputStream();

            byte[] byteDatas = new byte[in.available()];

            in.read(byteDatas);

            return new String(byteDatas) + "`" + httpStatusCode;

        } catch (Exception e) {

            log.error(e.getMessage());

            return "Failed`" + httpStatusCode;

        } finally {

            if (out != null) {

                try {

                    out.close();

                } catch (Exception e) {

                    log.error("关闭输出流时发生异常,堆栈信息如下", e);

                }

            }

            if (in != null) {

                try {

                    in.close();

                } catch (Exception e) {

                    log.error("关闭输入流时发生异常,堆栈信息如下", e);

                }

            }

            if (httpURLConnection != null) {

                httpURLConnection.disconnect();

                httpURLConnection = null;

            }

        }

    }


    /**
     * @param reqURL
     * @param paramJson     JSON格式的请求体
     * @param head          请求头
     * @param encodeCharset
     * @param decodeCharset
     * @return
     * @throws Exception
     */
    public static String sendPostRequest(
            String reqURL, String paramJson, Map<String, String> head,
            String encodeCharset, String decodeCharset) throws Exception {

        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        if (!CollectionUtils.isEmpty(head)) {
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {

            httpPost.setEntity(new StringEntity(paramJson, encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            log.info("与[{}]通信过程,请求参数如下{}: ", reqURL, paramJson);

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    responseContent = EntityUtils.toString(entity,
                            decodeCharset == null ? "UTF-8" : decodeCharset);
                    EntityUtils.consume(entity);
                }
            } else {
                log.error("与[{}]通信过程中发生异常,请求参数如下: {}", reqURL, paramJson);
                throw new Exception("Unknown error：" + statusCode);
            }
        } catch (Exception e) {
            log.error("与[{}]通信过程中发生异常,堆栈信息如下: {}", reqURL, e);
            throw e;
        } finally {
            httpclient.close();
        }

        return responseContent;
    }
    

    /**
     * 发送HTTP_GET请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址(含参数)
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws MalformedURLException
     * @throws URISyntaxException
     */

    public static String sendGetRequest(String reqURL, Map<String, String> head, String decodeCharset) {

        long responseLength = 0; // 响应长度

        String responseContent = null; // 响应内容

        HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
        // 防止 java.net.URISyntaxException
        // URL url = new URL(reqURL);
        // URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(),
        // url.getQuery(), null);

        HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
        if (!CollectionUtils.isEmpty(head)) {
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求

            HttpEntity entity = response.getEntity(); // 获取响应实体

            if (null != entity) {

                responseLength = entity.getContentLength();

                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);

                EntityUtils.consume(entity); // Consume response content

            }

            // System.out.println("请求地址: " + httpGet.getURI());

            // System.out.println("响应状态: " + response.getStatusLine());

            // System.out.println("响应长度: " + responseLength);

            // System.out.println("响应内容: " + responseContent);

        } catch (ClientProtocolException e) {

            log.error(
                    "该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下",
                    e);

        } catch (ParseException e) {

            log.error(e.getMessage(), e);

        } catch (IOException e) {

            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源

        }

        return responseContent;

    }

    /**
     * 发送SSL POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param paramJson     请求参数
     * @param head          请求头
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendPostSSLRequest(String reqURL, String paramJson, Map<String, String> head,
                                            String encodeCharset, String decodeCharset) {

        String responseContent = "";

        HttpClient httpClient = new DefaultHttpClient();

        X509TrustManager xtm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        try {

            SSLContext ctx = SSLContext.getInstance("TLS");

            ctx.init(null, new TrustManager[]{xtm}, null);

            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            httpClient.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", 443, socketFactory));

            HttpPost httpPost = new HttpPost(reqURL);

            if (!CollectionUtils.isEmpty(head)) {
                for (Map.Entry<String, String> entry : head.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            httpPost.setEntity(new StringEntity(paramJson, encodeCharset == null ? "UTF-8" : encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (null != entity) {

                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);

                EntityUtils.consume(entity);

            }

        } catch (Exception e) {

            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息为", e);

        } finally {

            httpClient.getConnectionManager().shutdown();

        }

        return responseContent;

    }

    public static String sendGetSSLRequest(String reqURL, Map<String, String> head) {
        return sendGetSSLRequest(reqURL, head, null);
    }

    /**
     * 发送SSL HTTP_GET请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址(含参数)
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendGetSSLRequest(String reqURL, Map<String, String> head, String decodeCharset) {
        String responseContent = null; // 响应内容

        HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
        X509TrustManager xtm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        httpClient.getConnectionManager().getSchemeRegistry()
                .register(new Scheme("https", 443, socketFactory));

        HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
        if (!CollectionUtils.isEmpty(head)) {
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求

            HttpEntity entity = response.getEntity(); // 获取响应实体

            if (null != entity) {

                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);

                EntityUtils.consume(entity); // Consume response content

            }

        } catch (ClientProtocolException e) {

            log.error(
                    "该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下",
                    e);

        } catch (ParseException e) {

            log.error(e.getMessage(), e);

        } catch (IOException e) {

            log.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源

        }

        return responseContent;

    }

    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
    private static HttpClientUtil instance = null;


    public static HttpClientUtil getInstance() {
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost, null);
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, null);
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     * @param charset 请求参数字符集
     */
    public String sendHttpPost(String httpUrl, String params, String charset) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            StringEntity stringEntity = new StringEntity(params, charset);
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, charset);
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, null);
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl   地址
     * @param headParam 请求头设置的参数
     * @param reqParams 请求参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> headParam, Map<String, String> reqParams) {
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            for (String key : headParam.keySet()) {
                httpPost.setHeader(key, headParam.get(key));
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key1 : reqParams.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key1, reqParams.get(key1)));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, null);
    }

    /**
     * Description:发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     * @param charset post 携带参数的字符集
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, String charset) {
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, charset);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl 请求地址
     */
    public String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet, null);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl 请求地址
     * @param charset 返回数据字符集
     */
    public String sendHttpGet(String httpUrl, String charset) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet, charset);
    }

    /**
     * 发送 Https get请求
     *
     * @param httpUrl 请求连接
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpsGet(httpGet, null);
    }

    /**
     * 发送 Https get请求
     *
     * @param httpUrl 请求连接
     * @param charset 返回数据字符集
     */
    public String sendHttpsGet(String httpUrl, String charset) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpsGet(httpGet, charset);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost post 对象
     * @param charset  返回数据字符集
     */
    private String sendHttpPost(HttpPost httpPost, String charset) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String retMessage = EntityUtils.toString(response.getEntity(), StringUtils.isEmpty(charset) ? "UTF-8" : charset);
            EntityUtils.consume(response.getEntity());
            return retMessage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(response, httpClient);
        }
        return null;
    }

    /**
     * 发送Get请求
     *
     * @param httpGet httpget 对象
     * @param charset 字符集
     */
    private String sendHttpGet(HttpGet httpGet, String charset) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String retMessage = EntityUtils.toString(entity, StringUtils.isEmpty(charset) ? "UTF-8" : charset);
            EntityUtils.consume(response.getEntity());
            return retMessage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(response, httpClient);
        }
        return null;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet httpget 对象
     * @param charset 字符集
     */
    private String sendHttpsGet(HttpGet httpGet, String charset) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            responseContent = EntityUtils.toString(response.getEntity(), StringUtils.isEmpty(charset) ? "UTF-8" : charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(response, httpClient);
        }
        return responseContent;
    }

    /**
     * Description:关闭资源
     */
    private void closeResource(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            // 关闭连接,释放资源
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送Basic Auth请求
     *
     * @param url
     * @param requestParams
     * @param userName
     * @param passWord
     * @return java.lang.String
     * @author Administrator
     * @date 2021/10/25
     */
    public static String httpClientWithBasicAuth(String url, Map<String, Object> requestParams, String userName, String passWord) {
        String result = "";
        try {
            // 创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            HttpPost httpPost = new HttpPost(url);
            //添加http头信息
            httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((userName + ":" + passWord).getBytes()));
            httpPost.addHeader("Content-type", "application/json; charset=UTF-8");
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(requestParams));
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse;
            HttpEntity entity;
            try {
                httpResponse = closeableHttpClient.execute(httpPost);
                entity = httpResponse.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 关闭连接
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
