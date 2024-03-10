package in.mesway.Response.UpcomingMeal;

public class UserSuggestion {
    private String suggestion_id, user_id, suggestion, created_timestamp;

    public String getSuggestion_id() {
        return suggestion_id;
    }

    public void setSuggestion_id(String suggestion_id) {
        this.suggestion_id = suggestion_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(String created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
}
