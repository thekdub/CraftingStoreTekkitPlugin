package com.thekdub.craftingstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionList {

  private boolean success = false;
  private String message;
  private ArrayList<Transaction> data;
  private Map<String, Integer> meta;

  public TransactionList() {}

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setData(ArrayList<Transaction> data) {
    this.data = data;
  }

  public ArrayList<Transaction> getTransactions() {
    return data;
  }

  public Map<String, Integer> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, Integer> meta) {
    this.meta = meta;
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (Exception e) {
      CraftingStore.getLog().log("[ERROR] Could not map to JSON! " + e.getMessage());
    }
    return "TransactionList{" +
          "success=" + success +
          ", message='" + message + '\'' +
          ", data=" + data +
          ", meta=" + meta +
          '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TransactionList that = (TransactionList) o;
    return success == that.success &&
          Objects.equals(message, that.message) &&
          Objects.equals(data, that.data) &&
          Objects.equals(meta, that.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, data, meta);
  }
}
