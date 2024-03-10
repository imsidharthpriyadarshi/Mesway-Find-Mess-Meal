
package in.mesway.Response.Location;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessInfo {

    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("mess_name")
    @Expose
    private String messName;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("mess_phone_number")
    @Expose
    private String messPhoneNumber;
    @SerializedName("mess_email")
    @Expose
    private String messEmail;
    @SerializedName("mess_rating")
    @Expose
    private Double messRating;
    @SerializedName("discription")
    @Expose
    private String discription;
    @SerializedName("email_verified")
    @Expose
    private String emailVerified;
    @SerializedName("number_verified")
    @Expose
    private String numberVerified;
    @SerializedName("mesway_verified")
    @Expose
    private String meswayVerified;
    @SerializedName("mess_reg_steps")
    @Expose
    private Integer messRegSteps;
    @SerializedName("serving_meal")
    @Expose
    private String servingMeal;
    @SerializedName("pure_veg")
    @Expose
    private Boolean pureVeg;
    @SerializedName("mess_verified")
    @Expose
    private Boolean messVerified;
    @SerializedName("logout_form_all")
    @Expose
    private String logoutFormAll;
    @SerializedName("payment_detail_verified")
    @Expose
    private Boolean paymentDetailVerified;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;

    private String start_from_price;
    @SerializedName("updated_timestamp")
    @Expose
    private String updatedTimestamp;
    @SerializedName("mess_serving_meal")
    @Expose
    private MessServingMeal messServingMeal;
    @SerializedName("mess_serving_day")
    @Expose
    private List<MessServingDay> messServingDay;
    @SerializedName("mess_subscription_type")
    @Expose
    private List<MessSubscriptionType> messSubscriptionType;
    @SerializedName("mess_routine_photos")
    @Expose
    private List<MessRoutinePhoto> messRoutinePhotos;
    @SerializedName("mess_time")
    @Expose
    private List<MessTime> messTime;
    @SerializedName("mess_location")
    @Expose
    private List<MessLocation> messLocation;
    @SerializedName("mess_delivery_boy")
    @Expose
    private List<MessDeliveryBoy> messDeliveryBoy;
    @SerializedName("mess_images")
    @Expose
    private List<MessImage> messImages;
    @SerializedName("mess_ratings")
    @Expose
    private List<MessRating> messRatings;
    @SerializedName("how_many_rated")
    @Expose
    private Integer how_many_rated;

    public Integer getHow_many_rated() {
        return how_many_rated;
    }

    public void setHow_many_rated(Integer how_many_rated) {
        this.how_many_rated = how_many_rated;
    }

    public String getStart_from_price() {
        return start_from_price;
    }

    public void setStart_from_price(String start_from_price) {
        this.start_from_price = start_from_price;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getMessPhoneNumber() {
        return messPhoneNumber;
    }

    public void setMessPhoneNumber(String messPhoneNumber) {
        this.messPhoneNumber = messPhoneNumber;
    }

    public String getMessEmail() {
        return messEmail;
    }

    public void setMessEmail(String messEmail) {
        this.messEmail = messEmail;
    }

    public Double getMessRating() {
        return messRating;
    }

    public void setMessRating(Double messRating) {
        this.messRating = messRating;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getNumberVerified() {
        return numberVerified;
    }

    public void setNumberVerified(String numberVerified) {
        this.numberVerified = numberVerified;
    }

    public String getMeswayVerified() {
        return meswayVerified;
    }

    public void setMeswayVerified(String meswayVerified) {
        this.meswayVerified = meswayVerified;
    }

    public Integer getMessRegSteps() {
        return messRegSteps;
    }

    public void setMessRegSteps(Integer messRegSteps) {
        this.messRegSteps = messRegSteps;
    }

    public String getServingMeal() {
        return servingMeal;
    }

    public void setServingMeal(String servingMeal) {
        this.servingMeal = servingMeal;
    }

    public Boolean getPureVeg() {
        return pureVeg;
    }

    public void setPureVeg(Boolean pureVeg) {
        this.pureVeg = pureVeg;
    }

    public Boolean getMessVerified() {
        return messVerified;
    }

    public void setMessVerified(Boolean messVerified) {
        this.messVerified = messVerified;
    }

    public String getLogoutFormAll() {
        return logoutFormAll;
    }

    public void setLogoutFormAll(String logoutFormAll) {
        this.logoutFormAll = logoutFormAll;
    }

    public Boolean getPaymentDetailVerified() {
        return paymentDetailVerified;
    }

    public void setPaymentDetailVerified(Boolean paymentDetailVerified) {
        this.paymentDetailVerified = paymentDetailVerified;
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

    public MessServingMeal getMessServingMeal() {
        return messServingMeal;
    }

    public void setMessServingMeal(MessServingMeal messServingMeal) {
        this.messServingMeal = messServingMeal;
    }

    public List<MessServingDay> getMessServingDay() {
        return messServingDay;
    }

    public void setMessServingDay(List<MessServingDay> messServingDay) {
        this.messServingDay = messServingDay;
    }

    public List<MessSubscriptionType> getMessSubscriptionType() {
        return messSubscriptionType;
    }

    public void setMessSubscriptionType(List<MessSubscriptionType> messSubscriptionType) {
        this.messSubscriptionType = messSubscriptionType;
    }

    public List<MessRoutinePhoto> getMessRoutinePhotos() {
        return messRoutinePhotos;
    }

    public void setMessRoutinePhotos(List<MessRoutinePhoto> messRoutinePhotos) {
        this.messRoutinePhotos = messRoutinePhotos;
    }

    public List<MessTime> getMessTime() {
        return messTime;
    }

    public void setMessTime(List<MessTime> messTime) {
        this.messTime = messTime;
    }

    public List<MessLocation> getMessLocation() {
        return messLocation;
    }

    public void setMessLocation(List<MessLocation> messLocation) {
        this.messLocation = messLocation;
    }

    public List<MessDeliveryBoy> getMessDeliveryBoy() {
        return messDeliveryBoy;
    }

    public void setMessDeliveryBoy(List<MessDeliveryBoy> messDeliveryBoy) {
        this.messDeliveryBoy = messDeliveryBoy;
    }

    public List<MessImage> getMessImages() {
        return messImages;
    }

    public void setMessImages(List<MessImage> messImages) {
        this.messImages = messImages;
    }

    public List<MessRating> getMessRatings() {
        return messRatings;
    }

    public void setMessRatings(List<MessRating> messRatings) {
        this.messRatings = messRatings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("messName");
        sb.append('=');
        sb.append(((this.messName == null)?"<null>":this.messName));
        sb.append(',');
        sb.append("referenceId");
        sb.append('=');
        sb.append(((this.referenceId == null)?"<null>":this.referenceId));
        sb.append(',');
        sb.append("messPhoneNumber");
        sb.append('=');
        sb.append(((this.messPhoneNumber == null)?"<null>":this.messPhoneNumber));
        sb.append(',');
        sb.append("messEmail");
        sb.append('=');
        sb.append(((this.messEmail == null)?"<null>":this.messEmail));
        sb.append(',');
        sb.append("messRating");
        sb.append('=');
        sb.append(((this.messRating == null)?"<null>":this.messRating));
        sb.append(',');
        sb.append("discription");
        sb.append('=');
        sb.append(((this.discription == null)?"<null>":this.discription));
        sb.append(',');
        sb.append("emailVerified");
        sb.append('=');
        sb.append(((this.emailVerified == null)?"<null>":this.emailVerified));
        sb.append(',');
        sb.append("numberVerified");
        sb.append('=');
        sb.append(((this.numberVerified == null)?"<null>":this.numberVerified));
        sb.append(',');
        sb.append("meswayVerified");
        sb.append('=');
        sb.append(((this.meswayVerified == null)?"<null>":this.meswayVerified));
        sb.append(',');
        sb.append("messRegSteps");
        sb.append('=');
        sb.append(((this.messRegSteps == null)?"<null>":this.messRegSteps));
        sb.append(',');
        sb.append("servingMeal");
        sb.append('=');
        sb.append(((this.servingMeal == null)?"<null>":this.servingMeal));
        sb.append(',');
        sb.append("pureVeg");
        sb.append('=');
        sb.append(((this.pureVeg == null)?"<null>":this.pureVeg));
        sb.append(',');
        sb.append("messVerified");
        sb.append('=');
        sb.append(((this.messVerified == null)?"<null>":this.messVerified));
        sb.append(',');
        sb.append("logoutFormAll");
        sb.append('=');
        sb.append(((this.logoutFormAll == null)?"<null>":this.logoutFormAll));
        sb.append(',');
        sb.append("paymentDetailVerified");
        sb.append('=');
        sb.append(((this.paymentDetailVerified == null)?"<null>":this.paymentDetailVerified));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        sb.append("updatedTimestamp");
        sb.append('=');
        sb.append(((this.updatedTimestamp == null)?"<null>":this.updatedTimestamp));
        sb.append(',');
        sb.append("messServingMeal");
        sb.append('=');
        sb.append(((this.messServingMeal == null)?"<null>":this.messServingMeal));
        sb.append(',');
        sb.append("messServingDay");
        sb.append('=');
        sb.append(((this.messServingDay == null)?"<null>":this.messServingDay));
        sb.append(',');
        sb.append("messSubscriptionType");
        sb.append('=');
        sb.append(((this.messSubscriptionType == null)?"<null>":this.messSubscriptionType));
        sb.append(',');
        sb.append("messRoutinePhotos");
        sb.append('=');
        sb.append(((this.messRoutinePhotos == null)?"<null>":this.messRoutinePhotos));
        sb.append(',');
        sb.append("messTime");
        sb.append('=');
        sb.append(((this.messTime == null)?"<null>":this.messTime));
        sb.append(',');
        sb.append("messLocation");
        sb.append('=');
        sb.append(((this.messLocation == null)?"<null>":this.messLocation));
        sb.append(',');
        sb.append("messDeliveryBoy");
        sb.append('=');
        sb.append(((this.messDeliveryBoy == null)?"<null>":this.messDeliveryBoy));
        sb.append(',');
        sb.append("messImages");
        sb.append('=');
        sb.append(((this.messImages == null)?"<null>":this.messImages));
        sb.append(',');
        sb.append("messRatings");
        sb.append('=');
        sb.append(((this.messRatings == null)?"<null>":this.messRatings));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
