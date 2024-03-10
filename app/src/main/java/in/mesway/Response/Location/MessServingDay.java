
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessServingDay {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("monday_value")
    @Expose
    private MondayValue mondayValue;
    @SerializedName("tuesday_value")
    @Expose
    private TuesdayValue tuesdayValue;
    @SerializedName("wednesday_value")
    @Expose
    private WednesdayValue wednesdayValue;
    @SerializedName("thrusday_value")
    @Expose
    private ThrusdayValue thrusdayValue;
    @SerializedName("friday_value")
    @Expose
    private FridayValue fridayValue;
    @SerializedName("saturday_value")
    @Expose
    private SaturdayValue saturdayValue;
    @SerializedName("sunday_value")
    @Expose
    private SundayValue sundayValue;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public MondayValue getMondayValue() {
        return mondayValue;
    }

    public void setMondayValue(MondayValue mondayValue) {
        this.mondayValue = mondayValue;
    }

    public TuesdayValue getTuesdayValue() {
        return tuesdayValue;
    }

    public void setTuesdayValue(TuesdayValue tuesdayValue) {
        this.tuesdayValue = tuesdayValue;
    }

    public WednesdayValue getWednesdayValue() {
        return wednesdayValue;
    }

    public void setWednesdayValue(WednesdayValue wednesdayValue) {
        this.wednesdayValue = wednesdayValue;
    }

    public ThrusdayValue getThrusdayValue() {
        return thrusdayValue;
    }

    public void setThrusdayValue(ThrusdayValue thrusdayValue) {
        this.thrusdayValue = thrusdayValue;
    }

    public FridayValue getFridayValue() {
        return fridayValue;
    }

    public void setFridayValue(FridayValue fridayValue) {
        this.fridayValue = fridayValue;
    }

    public SaturdayValue getSaturdayValue() {
        return saturdayValue;
    }

    public void setSaturdayValue(SaturdayValue saturdayValue) {
        this.saturdayValue = saturdayValue;
    }

    public SundayValue getSundayValue() {
        return sundayValue;
    }

    public void setSundayValue(SundayValue sundayValue) {
        this.sundayValue = sundayValue;
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
        sb.append(MessServingDay.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("mondayValue");
        sb.append('=');
        sb.append(((this.mondayValue == null)?"<null>":this.mondayValue));
        sb.append(',');
        sb.append("tuesdayValue");
        sb.append('=');
        sb.append(((this.tuesdayValue == null)?"<null>":this.tuesdayValue));
        sb.append(',');
        sb.append("wednesdayValue");
        sb.append('=');
        sb.append(((this.wednesdayValue == null)?"<null>":this.wednesdayValue));
        sb.append(',');
        sb.append("thrusdayValue");
        sb.append('=');
        sb.append(((this.thrusdayValue == null)?"<null>":this.thrusdayValue));
        sb.append(',');
        sb.append("fridayValue");
        sb.append('=');
        sb.append(((this.fridayValue == null)?"<null>":this.fridayValue));
        sb.append(',');
        sb.append("saturdayValue");
        sb.append('=');
        sb.append(((this.saturdayValue == null)?"<null>":this.saturdayValue));
        sb.append(',');
        sb.append("sundayValue");
        sb.append('=');
        sb.append(((this.sundayValue == null)?"<null>":this.sundayValue));
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
