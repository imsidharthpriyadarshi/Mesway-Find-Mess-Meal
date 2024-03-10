package in.mesway.Response.UpcomingMeal;

public class UserComplaints {

    private String complaint_id;

    private String user_id;

    private String mess_id;

    private String complaints;

    private String created_timestamp;


    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    @Override
    public String toString() {
        return "UserComplaints{" +
                "complaint_id='" + complaint_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", mess_id='" + mess_id + '\'' +
                ", complaints='" + complaints + '\'' +
                ", created_timestamp='" + created_timestamp + '\'' +
                '}';
    }
}
