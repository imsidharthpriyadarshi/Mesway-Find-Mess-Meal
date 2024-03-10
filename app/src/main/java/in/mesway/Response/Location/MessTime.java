
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessTime {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("breakfast_time")
    @Expose
    private String breakfastTime;
    @SerializedName("lunch_time")
    @Expose
    private String lunchTime;
    @SerializedName("dinner_time")
    @Expose
    private String dinnerTime;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(String breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
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
        sb.append(MessTime.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("breakfastTime");
        sb.append('=');
        sb.append(((this.breakfastTime == null)?"<null>":this.breakfastTime));
        sb.append(',');
        sb.append("lunchTime");
        sb.append('=');
        sb.append(((this.lunchTime == null)?"<null>":this.lunchTime));
        sb.append(',');
        sb.append("dinnerTime");
        sb.append('=');
        sb.append(((this.dinnerTime == null)?"<null>":this.dinnerTime));
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
