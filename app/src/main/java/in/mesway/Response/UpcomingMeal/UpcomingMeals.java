package in.mesway.Response.UpcomingMeal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingMeals {

    @SerializedName("upcoming_meal")
    @Expose
    private List<UpcomingMeal> upcomingMeal;

    public List<UpcomingMeal> getUpcomingMeal() {
        return upcomingMeal;
    }

    public void setUpcomingMeal(List<UpcomingMeal> upcomingMeal) {
        this.upcomingMeal = upcomingMeal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UpcomingMeals.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("upcomingMeal");
        sb.append('=');
        sb.append(((this.upcomingMeal == null)?"<null>":this.upcomingMeal));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}