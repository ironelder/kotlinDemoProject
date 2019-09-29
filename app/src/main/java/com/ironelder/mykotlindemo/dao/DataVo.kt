package com.ironelder.mykotlindemo.dao

import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.common.CARD_TYPE_BLOG
import com.ironelder.mykotlindemo.common.CARD_TYPE_CAFE
import java.io.Serializable

data class DataVo (
    var meta:MetaDataVo,
    val documents: List<DocumentDataVo>
)
data class MetaDataVo(
    var total_count:Int,
    var pageable_count:Int,
    var is_end:Boolean
)
data class DocumentDataVo(
    var title:String,
    var contents:String,
    var url:String?,
    var cafename:String?,
    var blogname:String?,
    var thumbnail:String,
    var datetime:String,
    var isRead:Boolean = false
):Serializable{
    fun getType():Int{
        return if(!cafename.isNullOrEmpty() && cafename != "null"){
            CARD_TYPE_CAFE
        }else {
            CARD_TYPE_BLOG
        }
    }

    fun getName():String{
        return if(!cafename.isNullOrEmpty() && cafename != "null"){
            cafename?:"Cafe"
        }else {
            blogname?:"Blog"
        }
    }

    fun getTypeImageResourceId():Int{
        return if(!cafename.isNullOrEmpty() && cafename != "null"){
            R.mipmap.cafe
        }else {
            R.mipmap.blog
        }
    }
}