package com.sixiangtianxia.commonlib.network.parse

import com.google.gson.*
import java.lang.reflect.Type

class IntegerDefaultOAdapter : JsonSerializer<Integer>, JsonDeserializer<Integer> {
    override fun serialize(src: Integer?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Integer {
        return if (json!!.asString == "") {
            Integer(0)
        } else {
            Integer(json.asInt)
        }
    }
}