package com.drpashu.sdk.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("1")
        @Expose
        private List<Rating> rating1;
        @SerializedName("2")
        @Expose
        private List<Rating> rating2;
        @SerializedName("3")
        @Expose
        private List<Rating> rating3;
        @SerializedName("4")
        @Expose
        private List<Rating> rating4;
        @SerializedName("5")
        @Expose
        private List<Rating> rating5;

        public List<Rating> getRating1() {
            return rating1;
        }

        public void setRating1(List<Rating> rating1) {
            this.rating1 = rating1;
        }

        public List<Rating> getRating2() {
            return rating2;
        }

        public void setRating2(List<Rating> rating2) {
            this.rating2 = rating2;
        }

        public List<Rating> getRating3() {
            return rating3;
        }

        public void setRating3(List<Rating> rating3) {
            this.rating3 = rating3;
        }

        public List<Rating> getRating4() {
            return rating4;
        }

        public void setRating4(List<Rating> rating4) {
            this.rating4 = rating4;
        }

        public List<Rating> getRating5() {
            return rating5;
        }

        public void setRating5(List<Rating> rating5) {
            this.rating5 = rating5;
        }

        public class Rating {
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("review")
            @Expose
            private String review;
            @SerializedName("feedbackSelection")
            @Expose
            private Boolean feedbackSelection;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getReview() {
                return review;
            }

            public void setReview(String review) {
                this.review = review;
            }

            public Boolean getFeedbackSelection() {
                return feedbackSelection;
            }

            public void setFeedbackSelection(Boolean feedbackSelection) {
                this.feedbackSelection = feedbackSelection;
            }
        }
    }
}