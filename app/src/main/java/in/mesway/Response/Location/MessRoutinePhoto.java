
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessRoutinePhoto {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("breakfast_routine_photo")
    @Expose
    private String breakfastRoutinePhoto;
    @SerializedName("lunch_routine_photo")
    @Expose
    private String lunchRoutinePhoto;
    @SerializedName("dinner_routine_photo")
    @Expose
    private String dinnerRoutinePhoto;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;
    @SerializedName("updated_timestamp")
    @Expose
    private String updatedTimestamp;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getBreakfastRoutinePhoto() {
        return breakfastRoutinePhoto;
    }

    public void setBreakfastRoutinePhoto(String breakfastRoutinePhoto) {
        this.breakfastRoutinePhoto = breakfastRoutinePhoto;
    }

    public String getLunchRoutinePhoto() {
        return lunchRoutinePhoto;
    }

    public void setLunchRoutinePhoto(String lunchRoutinePhoto) {
        this.lunchRoutinePhoto = lunchRoutinePhoto;
    }

    public String getDinnerRoutinePhoto() {
        return dinnerRoutinePhoto;
    }

    public void setDinnerRoutinePhoto(String dinnerRoutinePhoto) {
        this.dinnerRoutinePhoto = dinnerRoutinePhoto;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessRoutinePhoto.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("breakfastRoutinePhoto");
        sb.append('=');
        sb.append(((this.breakfastRoutinePhoto == null)?"<null>":this.breakfastRoutinePhoto));
        sb.append(',');
        sb.append("lunchRoutinePhoto");
        sb.append('=');
        sb.append(((this.lunchRoutinePhoto == null)?"<null>":this.lunchRoutinePhoto));
        sb.append(',');
        sb.append("dinnerRoutinePhoto");
        sb.append('=');
        sb.append(((this.dinnerRoutinePhoto == null)?"<null>":this.dinnerRoutinePhoto));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        sb.append("updatedTimestamp");
        sb.append('=');
        sb.append(((this.updatedTimestamp == null)?"<null>":this.updatedTimestamp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
