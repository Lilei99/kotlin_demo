package com.sixiangtianxia.commonlib.model;

import java.util.List;

public class AddressItemModel extends BaseModel {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * code : 2
         * description : 中专
         * id : 165
         * value : 中专
         * sorts : 2
         * did : 11
         */

        private String code;
        private String description;
        private int id;
        private String value;
        private int sorts;
        private int did;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getSorts() {
            return sorts;
        }

        public void setSorts(int sorts) {
            this.sorts = sorts;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }
    }
}
