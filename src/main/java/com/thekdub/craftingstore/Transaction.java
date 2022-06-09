package com.thekdub.craftingstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

  private int id;
  private String transactionId;
  private String externalTransactionId;
  private double price;
  private String packageName;
  private String inGameName;
  private String uuid;
  private String email;
  private String notes;
  private String gateway;
  private String status;
  private long timestamp;

  public Transaction() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getExternalTransactionId() {
    return externalTransactionId;
  }

  public void setExternalTransactionId(String externalTransactionId) {
    this.externalTransactionId = externalTransactionId;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getInGameName() {
    return inGameName;
  }

  public void setInGameName(String inGameName) {
    this.inGameName = inGameName;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getGateway() {
    return gateway;
  }

  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (Exception e) {
      CraftingStore.getLog().log("[ERROR] Could not map to JSON! " + e.getMessage());
    }
    return "Transaction{" +
          "id=" + id +
          ", transactionId='" + transactionId + '\'' +
          ", externalTransactionId='" + externalTransactionId + '\'' +
          ", price=" + price +
          ", packageName='" + packageName + '\'' +
          ", inGameName='" + inGameName + '\'' +
          ", uuid='" + uuid + '\'' +
          ", email='" + email + '\'' +
          ", notes='" + notes + '\'' +
          ", gateway='" + gateway + '\'' +
          ", status='" + status + '\'' +
          ", timestamp=" + timestamp +
          '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaction that = (Transaction) o;
    return id == that.id &&
          Double.compare(that.price, price) == 0 &&
          timestamp == that.timestamp &&
          Objects.equals(transactionId, that.transactionId) &&
          Objects.equals(externalTransactionId, that.externalTransactionId) &&
          Objects.equals(packageName, that.packageName) &&
          Objects.equals(inGameName, that.inGameName) &&
          Objects.equals(uuid, that.uuid) &&
          Objects.equals(email, that.email) &&
          Objects.equals(notes, that.notes) &&
          Objects.equals(gateway, that.gateway) &&
          Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, transactionId, externalTransactionId, price, packageName, inGameName, uuid, email,
          notes, gateway, status, timestamp);
  }
}
