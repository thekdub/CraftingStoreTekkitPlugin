package com.thekdub.craftingstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class APIHandler {

  private static final OkHttpClient client = new OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build();
  private static final HttpClient httpClient;

  static {
    HttpClient client;
    try {
      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(null, null, null);
      client = HttpClients.custom().setSSLContext(context).build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
      client = HttpClients.createDefault();
    }
    httpClient = client;
  }

  public static TransactionList getTransactions() {
    Request request = new Request.Builder()
          .addHeader("token", CraftingStore.getInstance().getConfig().getString("token", ""))
          .url("https://api.craftingstore.net/v7/payments")
          .build();
    try (Response response = client.newCall(request).execute(); ResponseBody body = response.body()) {
      if (response.isSuccessful() && body != null) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body.string(), TransactionList.class);
      }
    } catch (IOException e) {
      CraftingStore.getLog().log("[ERROR] Unable to retrieve transactions! " + e.getMessage());
    }
    return new TransactionList();
  }

  public static CommandQueue getCommands() {
    Request request = new Request.Builder()
          .addHeader("token", CraftingStore.getInstance().getConfig().getString("token", ""))
          .url("https://api.craftingstore.net/v4/queue")
          .build();
    try (Response response = client.newCall(request).execute(); ResponseBody body = response.body()) {
      if (response.isSuccessful() && body != null) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body.string(), CommandQueue.class);
      }
    } catch (IOException e) {
      CraftingStore.getLog().log("[ERROR] Unable to retrieve commands! " + e.getMessage());
    }
    return new CommandQueue();
  }

  public static boolean complete(int[] ids) {
    ObjectMapper mapper = new ObjectMapper();
    HttpPost req = new HttpPost("https://api.craftingstore.net/v4/queue/markComplete");
    req.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectTimeout(10000).build());
    req.addHeader("token", CraftingStore.getInstance().getConfig().getString("token", ""));
    ArrayList<BasicNameValuePair> params = new ArrayList<>();
    try {
      params.add(new BasicNameValuePair("removeIds", mapper.writeValueAsString(ids)));
      req.setEntity(new UrlEncodedFormEntity(params));
      HttpResponse resp = null;
      resp = httpClient.execute(req);
      EntityUtils.consumeQuietly(resp.getEntity());
    } catch (IOException e) {
      CraftingStore.getLog().log("[ERROR] Unable to complete commands " + Arrays.toString(ids) + "! " + e.getMessage());
      return false;
    }
    return true;
  }

  private static class RemoveIDs {
    private final String removeIds;

    public RemoveIDs(int[] ids) {
      removeIds = Arrays.toString(ids).replace(" ", "");
    }

    public String getRemoveIds() {
      return removeIds;
    }
  }

}
