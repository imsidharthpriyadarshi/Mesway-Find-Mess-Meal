package in.mesway.Models;

public class SubscriptionModel {
    private String mess_img,mess_name,mess_address,rejected_reason_value,subscription_status,plan_name,plan_type,starting_meal,plan_price,start_from,valid_upto,mess_id;

    private String payment_status, total_payable_amount, security_deposit_for, security_deposit_amount,security_deposit_status;

    public SubscriptionModel(String mess_img, String mess_name, String mess_address, String rejected_reason_value, String subscription_status, String plan_name, String plan_type, String starting_meal, String plan_price, String start_from, String valid_upto, String mess_id, String payment_status, String total_payable_amount, String security_deposit_for, String security_deposit_amount, String security_deposit_status) {
        this.mess_img = mess_img;
        this.mess_name = mess_name;
        this.mess_address = mess_address;
        this.rejected_reason_value = rejected_reason_value;
        this.subscription_status = subscription_status;
        this.plan_name = plan_name;
        this.plan_type = plan_type;
        this.starting_meal = starting_meal;
        this.plan_price = plan_price;
        this.start_from = start_from;
        this.valid_upto = valid_upto;
        this.mess_id = mess_id;
        this.payment_status = payment_status;
        this.total_payable_amount = total_payable_amount;
        this.security_deposit_for = security_deposit_for;
        this.security_deposit_amount = security_deposit_amount;
        this.security_deposit_status = security_deposit_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getTotal_payable_amount() {
        return total_payable_amount;
    }

    public void setTotal_payable_amount(String total_payable_amount) {
        this.total_payable_amount = total_payable_amount;
    }

    public String getSecurity_deposit_for() {
        return security_deposit_for;
    }

    public void setSecurity_deposit_for(String security_deposit_for) {
        this.security_deposit_for = security_deposit_for;
    }

    public String getSecurity_deposit_amount() {
        return security_deposit_amount;
    }

    public void setSecurity_deposit_amount(String security_deposit_amount) {
        this.security_deposit_amount = security_deposit_amount;
    }

    public String getSecurity_deposit_status() {
        return security_deposit_status;
    }

    public void setSecurity_deposit_status(String security_deposit_status) {
        this.security_deposit_status = security_deposit_status;
    }

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public String getMess_img() {
        return mess_img;
    }

    public void setMess_img(String mess_img) {
        this.mess_img = mess_img;
    }

    public String getMess_name() {
        return mess_name;
    }

    public void setMess_name(String mess_name) {
        this.mess_name = mess_name;
    }

    public String getMess_address() {
        return mess_address;
    }

    public void setMess_address(String mess_address) {
        this.mess_address = mess_address;
    }

    public String getRejected_reason_value() {
        return rejected_reason_value;
    }

    public void setRejected_reason_value(String rejected_reason_value) {
        this.rejected_reason_value = rejected_reason_value;
    }

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getStarting_meal() {
        return starting_meal;
    }

    public void setStarting_meal(String starting_meal) {
        this.starting_meal = starting_meal;
    }

    public String getPlan_price() {
        return "\u20B9 "+plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
    }

    public String getStart_from() {
        return start_from;
    }

    public void setStart_from(String start_from) {
        this.start_from = start_from;
    }

    public String getValid_upto() {
        return valid_upto;
    }

    public void setValid_upto(String valid_upto) {
        this.valid_upto = valid_upto;
    }
}
