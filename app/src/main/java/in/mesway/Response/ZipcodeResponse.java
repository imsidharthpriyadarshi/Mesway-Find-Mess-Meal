package in.mesway.Response;

import java.util.List;

public class ZipcodeResponse {
    private String Status;
    private List<PostOffice> PostOffice;

    public String getStatus() {
        return Status;
    }

    public List<PostOffice> getPostOfficeList() {
        return PostOffice;
    }
}
