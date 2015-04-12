package pl.parkujznami.parkujpl_mobile.models.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWithReport {

    @SerializedName("report")
    @Expose
    private ReportInResponse reportInResponse;

    /**
     *
     * @return
     * The reportInResponse
     */
    public ReportInResponse getReportInResponse() {
        return reportInResponse;
    }

    /**
     *
     * @param reportInResponse
     * The report_in_response
     */
    public void setReportInResponse(ReportInResponse reportInResponse) {
        this.reportInResponse = reportInResponse;
    }

}