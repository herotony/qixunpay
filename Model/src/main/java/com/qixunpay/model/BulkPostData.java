package com.qixunpay.model;

import java.util.List;

/**
 * Created by saosinwork on 2017/7/26.
 */
public class BulkPostData {

    private String bulkId;
    private List<String> listJsonData;

    public String getBulkId() {
        return bulkId;
    }

    public void setBulkId(String bulkId) {
        this.bulkId = bulkId;
    }

    public List<String> getListJsonData() {
        return listJsonData;
    }

    public void setListJsonData(List<String> listJsonData) {
        this.listJsonData = listJsonData;
    }
}
