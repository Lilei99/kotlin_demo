package com.sixiangtianxia.commonlib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gao MinMin.
 * Date: 2019/1/7.
 * Description: .
 */
public class BaseModel implements Parcelable {

    private Message message;

    public static class Message implements Parcelable {
        private int errcode;
        private String errinfo;

        public int getErrcode() {
            return errcode;
        }

        public void setErrcode(int errcode) {
            this.errcode = errcode;
        }

        public String getErrinfo() {
            return errinfo;
        }

        public void setErrinfo(String errinfo) {
            this.errinfo = errinfo;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errcode);
            dest.writeString(this.errinfo);
        }

        public Message() {
        }

        protected Message(Parcel in) {
            this.errcode = in.readInt();
            this.errinfo = in.readString();
        }

        public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
            @Override
            public Message createFromParcel(Parcel source) {
                return new Message(source);
            }

            @Override
            public Message[] newArray(int size) {
                return new Message[size];
            }
        };
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel in) {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.message, flags);
    }

    public BaseModel() {
    }

    protected BaseModel(Parcel in) {
        this.message = in.readParcelable(Message.class.getClassLoader());
    }

}
