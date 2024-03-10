
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessSubsTypes {

    @SerializedName("subs_id")
    @Expose
    private String subsId;
    @SerializedName("subs_name")
    @Expose
    private String subsName;
    @SerializedName("subs_day")
    @Expose
    private Integer subsDay;

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
    }

    public String getSubsName() {
        return subsName;
    }

    public void setSubsName(String subsName) {
        this.subsName = subsName;
    }

    public Integer getSubsDay() {
        return subsDay;
    }

    public void setSubsDay(Integer subsDay) {
        this.subsDay = subsDay;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessSubsTypes.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("subsId");
        sb.append('=');
        sb.append(((this.subsId == null)?"<null>":this.subsId));
        sb.append(',');
        sb.append("subsName");
        sb.append('=');
        sb.append(((this.subsName == null)?"<null>":this.subsName));
        sb.append(',');
        sb.append("subsDay");
        sb.append('=');
        sb.append(((this.subsDay == null)?"<null>":this.subsDay));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
