package com.sixiangtianxia.commonlib.network

import com.sixiangtianxia.commonlib.model.AddressItemModel
import com.sixiangtianxia.commonlib.model.BaseModelReq
import com.sixiangtianxia.commonlib.model.PublicBean
import io.reactivex.Observable
import retrofit2.http.*
import java.util.HashMap

interface NetWorkService {

    @FormUrlEncoded
    @POST("resource/app/dictitem/selectDictitemByCode")
    fun login(@Field("code") code: String): Observable<PublicBean<AddressItemModel>>
}
