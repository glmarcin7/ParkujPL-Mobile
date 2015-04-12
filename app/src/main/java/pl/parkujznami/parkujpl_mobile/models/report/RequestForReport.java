package pl.parkujznami.parkujpl_mobile.models.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestForReport {

    @SerializedName("report")
    @Expose
    private ReportInRequest reportInRequest;

    /**
     * @return The reportInRequest
     */
    public ReportInRequest getReportInRequest() {
        return reportInRequest;
    }

    /**
     * @param reportInRequest The reportInRequest
     */
    public void setReportInRequest(ReportInRequest reportInRequest) {
        this.reportInRequest = reportInRequest;
    }

}