
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FridayValue {

    @SerializedName("serving_meals_id")
    @Expose
    private String servingMealsId;
    @SerializedName("breakfast")
    @Expose
    private Boolean breakfast;
    @SerializedName("lunch")
    @Expose
    private Boolean lunch;
    @SerializedName("dinner")
    @Expose
    private Boolean dinner;
    @SerializedName("meals_number")
    @Expose
    private Integer mealsNumber;

    public String getServingMealsId() {
        return servingMealsId;
    }

    public void setServingMealsId(String servingMealsId) {
        this.servingMealsId = servingMealsId;
    }

    public Boolean getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Boolean breakfast) {
        this.breakfast = breakfast;
    }

    public Boolean getLunch() {
        return lunch;
    }

    public void setLunch(Boolean lunch) {
        this.lunch = lunch;
    }

    public Boolean getDinner() {
        return dinner;
    }

    public void setDinner(Boolean dinner) {
        this.dinner = dinner;
    }

    public Integer getMealsNumber() {
        return mealsNumber;
    }

    public void setMealsNumber(Integer mealsNumber) {
        this.mealsNumber = mealsNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FridayValue.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("servingMealsId");
        sb.append('=');
        sb.append(((this.servingMealsId == null)?"<null>":this.servingMealsId));
        sb.append(',');
        sb.append("breakfast");
        sb.append('=');
        sb.append(((this.breakfast == null)?"<null>":this.breakfast));
        sb.append(',');
        sb.append("lunch");
        sb.append('=');
        sb.append(((this.lunch == null)?"<null>":this.lunch));
        sb.append(',');
        sb.append("dinner");
        sb.append('=');
        sb.append(((this.dinner == null)?"<null>":this.dinner));
        sb.append(',');
        sb.append("mealsNumber");
        sb.append('=');
        sb.append(((this.mealsNumber == null)?"<null>":this.mealsNumber));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
