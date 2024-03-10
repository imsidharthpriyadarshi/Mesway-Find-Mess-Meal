package in.mesway.Response.UpcomingMeal;

public class UserNotification {
    private String user_notification_id;

    private String user_id;

    private String title;

    private String content;

    private String where;

    private String delivered_timestamp;

    private String seen;


    public String getUser_notification_id() {
        return user_notification_id;
    }

    public void setUser_notification_id(String user_notification_id) {
        this.user_notification_id = user_notification_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDelivered_timestamp() {
        return delivered_timestamp;
    }

    public void setDelivered_timestamp(String delivered_timestamp) {
        this.delivered_timestamp = delivered_timestamp;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "UserNotification{" +
                "user_notification_id='" + user_notification_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", where='" + where + '\'' +
                ", delivered_timestamp='" + delivered_timestamp + '\'' +
                ", seen='" + seen + '\'' +
                '}';
    }
}
