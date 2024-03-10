
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessDeliveryBoy {

    @SerializedName("delivery_boy_id")
    @Expose
    private String deliveryBoyId;
    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;
    @SerializedName("updated_timestamp")
    @Expose
    private String updatedTimestamp;

    private int delivery_charge;

    public int getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(int delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        sb.append(MessDeliveryBoy.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("deliveryBoyId");
        sb.append('=');
        sb.append(((this.deliveryBoyId == null)?"<null>":this.deliveryBoyId));
        sb.append(',');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("number");
        sb.append('=');
        sb.append(((this.number == null)?"<null>":this.number));
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
