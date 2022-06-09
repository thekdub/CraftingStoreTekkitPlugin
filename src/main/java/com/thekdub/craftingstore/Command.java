package com.thekdub.craftingstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Command {
  int id;
  String paymentId;
  String command;
  String mcName;
  String uuid;
  String packageName;
  double packagePrice;
  long packagePriceCents;
  double couponDiscount;
  String couponName;
  boolean requireOnline;

  public Command() {}

  public Command(int id, String paymentId, String command, String mcName, String uuid, String packageName,
        double packagePrice, long packagePriceCents, double couponDiscount, String couponName, boolean requireOnline) {
    this.id = id;
    this.paymentId = paymentId;
    this.command = command;
    this.mcName = mcName;
    this.uuid = uuid;
    this.packageName = packageName;
    this.packagePrice = packagePrice;
    this.packagePriceCents = packagePriceCents;
    this.couponDiscount = couponDiscount;
    this.couponName = couponName;
    this.requireOnline = requireOnline;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getMcName() {
    return mcName;
  }

  public void setMcName(String mcName) {
    this.mcName = mcName;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public double getPackagePrice() {
    return packagePrice;
  }

  public void setPackagePrice(double packagePrice) {
    this.packagePrice = packagePrice;
  }

  public long getPackagePriceCents() {
    return packagePriceCents;
  }

  public void setPackagePriceCents(long packagePriceCents) {
    this.packagePriceCents = packagePriceCents;
  }

  public double getCouponDiscount() {
    return couponDiscount;
  }

  public void setCouponDiscount(double couponDiscount) {
    this.couponDiscount = couponDiscount;
  }

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public boolean isRequireOnline() {
    return requireOnline;
  }

  public void setRequireOnline(boolean requireOnline) {
    this.requireOnline = requireOnline;
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (Exception e) {
      CraftingStore.getLog().log("[ERROR] Could not map to JSON! " + e.getMessage());
    }
    return "Command{" +
          "id=" + id +
          ", paymentId='" + paymentId + '\'' +
          ", Command='" + command + '\'' +
          ", mcName='" + mcName + '\'' +
          ", uuid='" + uuid + '\'' +
          ", packageName='" + packageName + '\'' +
          ", packagePrice=" + packagePrice +
          ", packagePriceCents=" + packagePriceCents +
          ", couponDiscount=" + couponDiscount +
          ", couponName='" + couponName + '\'' +
          ", requireOnline=" + requireOnline +
          '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Command command1 = (Command) o;
    return id == command1.id &&
          Double.compare(command1.packagePrice, packagePrice) == 0 &&
          packagePriceCents == command1.packagePriceCents &&
          Double.compare(command1.couponDiscount, couponDiscount) == 0 &&
          requireOnline == command1.requireOnline &&
          Objects.equals(paymentId, command1.paymentId) &&
          Objects.equals(command, command1.command) &&
          Objects.equals(mcName, command1.mcName) &&
          Objects.equals(uuid, command1.uuid) &&
          Objects.equals(packageName, command1.packageName) &&
          Objects.equals(couponName, command1.couponName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, paymentId, command, mcName, uuid, packageName, packagePrice, packagePriceCents,
          couponDiscount, couponName, requireOnline);
  }
}
