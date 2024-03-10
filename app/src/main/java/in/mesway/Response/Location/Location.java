
package in.mesway.Response.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("mess_id")
    @Expose
    private String messId;
    @SerializedName("building_no")
    @Expose
    private String buildingNo;
    @SerializedName("company_address")
    @Expose
    private String companyAddress;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("address_approved")
    @Expose
    private Boolean addressApproved;
    @SerializedName("not_approving_reason")
    @Expose
    private String notApprovingReason;
    @SerializedName("langitude")
    @Expose
    private String langitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("created_timestamp")
    @Expose
    private String createdTimestamp;
    @SerializedName("updated_timestamp")
    @Expose
    private String  updatedTimestamp;
    @SerializedName("mess_info")
    @Expose
    private MessInfo messInfo;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getAddressApproved() {
        return addressApproved;
    }

    public void setAddressApproved(Boolean addressApproved) {
        this.addressApproved = addressApproved;
    }

    public String getNotApprovingReason() {
        return notApprovingReason;
    }

    public void setNotApprovingReason(String notApprovingReason) {
        this.notApprovingReason = notApprovingReason;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getLatitude() {
        return latitude;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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

    public MessInfo getMessInfo() {
        return messInfo;
    }

    public void setMessInfo(MessInfo messInfo) {
        this.messInfo = messInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Location.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("locationId");
        sb.append('=');
        sb.append(((this.locationId == null)?"<null>":this.locationId));
        sb.append(',');
        sb.append("messId");
        sb.append('=');
        sb.append(((this.messId == null)?"<null>":this.messId));
        sb.append(',');
        sb.append("buildingNo");
        sb.append('=');
        sb.append(((this.buildingNo == null)?"<null>":this.buildingNo));
        sb.append(',');
        sb.append("companyAddress");
        sb.append('=');
        sb.append(((this.companyAddress == null)?"<null>":this.companyAddress));
        sb.append(',');
        sb.append("landmark");
        sb.append('=');
        sb.append(((this.landmark == null)?"<null>":this.landmark));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        sb.append("addressApproved");
        sb.append('=');
        sb.append(((this.addressApproved == null)?"<null>":this.addressApproved));
        sb.append(',');
        sb.append("notApprovingReason");
        sb.append('=');
        sb.append(((this.notApprovingReason == null)?"<null>":this.notApprovingReason));
        sb.append(',');
        sb.append("langitude");
        sb.append('=');
        sb.append(((this.langitude == null)?"<null>":this.langitude));
        sb.append(',');
        sb.append("latitude");
        sb.append('=');
        sb.append(((this.latitude == null)?"<null>":this.latitude));
        sb.append(',');
        sb.append("district");
        sb.append('=');
        sb.append(((this.district == null)?"<null>":this.district));
        sb.append(',');
        sb.append("pinCode");
        sb.append('=');
        sb.append(((this.pinCode == null)?"<null>":this.pinCode));
        sb.append(',');
        sb.append("createdTimestamp");
        sb.append('=');
        sb.append(((this.createdTimestamp == null)?"<null>":this.createdTimestamp));
        sb.append(',');
        sb.append("updatedTimestamp");
        sb.append('=');
        sb.append(((this.updatedTimestamp == null)?"<null>":this.updatedTimestamp));
        sb.append(',');
        sb.append("messInfo");
        sb.append('=');
        sb.append(((this.messInfo == null)?"<null>":this.messInfo));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
