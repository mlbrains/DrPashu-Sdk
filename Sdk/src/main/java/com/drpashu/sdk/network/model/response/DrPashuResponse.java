package com.drpashu.sdk.network.model.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class DrPashuResponse {
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

    @Keep
    public class Data {
        @SerializedName("phone_user_id")
        @Expose
        private String phoneUserId;
        @SerializedName("color")
        @Expose
        private ColorSdk colorSdk;

        public ColorSdk getColorSdk() {
            return colorSdk;
        }

        @Keep
        public class ColorSdk {
            @SerializedName("base")
            @Expose
            private String baseColor;

            @SerializedName("dark_base")
            @Expose
            private String darkBaseColor;

            @SerializedName("light_base")
            @Expose
            private String lightBaseColor;

            @SerializedName("light_card")
            @Expose
            private String lightCardColor;

            public String getBaseColor() {
                return baseColor;
            }

            public String getDarkBaseColor() {
                return darkBaseColor;
            }

            public String getLightBaseColor() {
                return lightBaseColor;
            }

            public String getLightCardColor() {
                return lightCardColor;
            }
        }

        public String getPhoneUserId() {
            return phoneUserId;
        }

        public void setPhoneUserId(String phoneUserId) {
            this.phoneUserId = phoneUserId;
        }
    }
}