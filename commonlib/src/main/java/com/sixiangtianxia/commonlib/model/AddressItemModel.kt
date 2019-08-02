package com.sixiangtianxia.commonlib.model

class AddressItemModel {

    var data: List<DataBean>? = null

    class DataBean {
        /**
         * code : 2
         * description : 中专
         * id : 165
         * value : 中专
         * sorts : 2
         * did : 11
         */

        var code: String? = null
        var description: String? = null
        var id: Int = 0
        var value: String? = null
        var sorts: Int = 0
        var did: Int = 0

        override fun toString(): String {
            return "DataBean(code=$code, description=$description, id=$id, value=$value, sorts=$sorts, did=$did)"
        }

    }

}
