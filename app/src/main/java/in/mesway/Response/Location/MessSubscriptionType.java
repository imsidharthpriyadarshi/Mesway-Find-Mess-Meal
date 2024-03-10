
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessSubscriptionType {

    @SerializedName("mess_subs_id")
    @Expose
    private String messSubsId;
    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("subs_id")
    @Expose
    private String subsId;
    @SerializedName("one_meal_price_per_day")
    @Expose
    private String oneMealPricePerDay;
    @SerializedName("two_meal_price_per_day")
    @Expose
    private String twoMealPricePerDay;
    @SerializedName("three_meal_price_per_day")
    @Expose
    private String threeMealPricePerDay;

    private boolean is_security_deposite;

    private String security_for;

    private int security_money;

    public boolean isIs_security_deposite() {
        return is_security_deposite;
    }

    public void setIs_security_deposite(boolean is_security_deposite) {
        this.is_security_deposite = is_security_deposite;
    }

    public String getSecurity_for() {
        return security_for;
    }

    public void setSecurity_for(String security_for) {
        this.security_for = security_for;
    }

    public int getSecurity_money() {
        return security_money;
    }

    public void setSecurity_money(int security_money) {
        this.security_money = security_money;
    }

    @SerializedName("mess_subs_types")
    @Expose
    private MessSubsTypes messSubsTypes;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    public String getMessSubsId() {
        return messSubsId;
    }

    public void setMessSubsId(String messSubsId) {
        this.messSubsId = messSubsId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
    }

    public String getOneMealPricePerDay() {
        return oneMealPricePerDay;
    }

    public void setOneMealPricePerDay(String oneMealPricePerDay) {
        this.oneMealPricePerDay = oneMealPricePerDay;
    }

    public String getTwoMealPricePerDay() {
        return twoMealPricePerDay;
    }

    public void setTwoMealPricePerDay(String twoMealPricePerDay) {
        this.twoMealPricePerDay = twoMealPricePerDay;
    }

    public String getThreeMealPricePerDay() {
        return threeMealPricePerDay;
    }

    public void setThreeMealPricePerDay(String threeMealPricePerDay) {
        this.threeMealPricePerDay = threeMealPricePerDay;
    }

    public MessSubsTypes getMessSubsTypes() {
        return messSubsTypes;
    }

    public void setMessSubsTypes(MessSubsTypes messSubsTypes) {
        this.messSubsTypes = messSubsTypes;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessSubscriptionType.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messSubsId");
        sb.append('=');
        sb.append(((this.messSubsId == null)?"<null>":this.messSubsId));
        sb.append(',');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("subsId");
        sb.append('=');
        sb.append(((this.subsId == null)?"<null>":this.subsId));
        sb.append(',');
        sb.append("oneMealPricePerDay");
        sb.append('=');
        sb.append(((this.oneMealPricePerDay == null)?"<null>":this.oneMealPricePerDay));
        sb.append(',');
        sb.append("twoMealPricePerDay");
        sb.append('=');
        sb.append(((this.twoMealPricePerDay == null)?"<null>":this.twoMealPricePerDay));
        sb.append(',');
        sb.append("threeMealPricePerDay");
        sb.append('=');
        sb.append(((this.threeMealPricePerDay == null)?"<null>":this.threeMealPricePerDay));
        sb.append(',');
        sb.append("messSubsTypes");
        sb.append('=');
        sb.append(((this.messSubsTypes == null)?"<null>":this.messSubsTypes));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
