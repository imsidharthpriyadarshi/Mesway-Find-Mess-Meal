
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessRating {

    @SerializedName("rating_id")
    @Expose
    private String ratingId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;
    @SerializedName("edited_timestamp")
    @Expose
    private String editedTimestamp;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEditedTimestamp() {
        return editedTimestamp;
    }

    public void setEditedTimestamp(String editedTimestamp) {
        this.editedTimestamp = editedTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessRating.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ratingId");
        sb.append('=');
        sb.append(((this.ratingId == null)?"<null>":this.ratingId));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("review");
        sb.append('=');
        sb.append(((this.review == null)?"<null>":this.review));
        sb.append(',');
        sb.append("rating");
        sb.append('=');
        sb.append(((this.rating == null)?"<null>":this.rating));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        sb.append("editedTimestamp");
        sb.append('=');
        sb.append(((this.editedTimestamp == null)?"<null>":this.editedTimestamp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
