package com.drpashu.sdk.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("feed_en")
        @Expose
        private String feedEn;
        @SerializedName("benefit_en")
        @Expose
        private String benefitEn;
        @SerializedName("Source")
        @Expose
        private String source;
        @SerializedName("feed")
        @Expose
        private String feed;
        @SerializedName("benefit")
        @Expose
        private String benefit;
        @SerializedName("recommended")
        @Expose
        private Boolean isChecked;
        @SerializedName("deep_link")
        @Expose
        private String deep_link;

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public String getImage() {
            return image;
        }

        public String getFeedEn() {
            return feedEn;
        }

        public String getBenefitEn() {
            return benefitEn;
        }

        public String getSource() {
            return source;
        }

        public String getFeed() {
            return feed;
        }

        public String getBenefit() {
            return benefit;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }

        public String getDeep_link() {
            return deep_link;
        }

        public void setDeep_link(String deep_link) {
            this.deep_link = deep_link;
        }
    }
}
