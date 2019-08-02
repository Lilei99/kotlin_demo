package com.sixiangtianxia.commonlib.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sixiangtianxia.commonlib.R

/**
 * Glide 图片加载器
 */

object GlideUtil {

    //显示网络图片
    fun loaderImage(context: Context, gifUrl: String, imageView: ImageView) {
        Glide.with(context).load(gifUrl).placeholder(R.mipmap.ic_launcher).dontAnimate().centerCrop().into(imageView)
    }

    fun loaderImageBig(context: Context, gifUrl: String, imageView: ImageView) {
        Glide.with(context).load(gifUrl).placeholder(R.mipmap.ic_launcher).dontAnimate().fitCenter().into(imageView)
    }

    /**
     * 加载头像的时候  不要缓存 -- 修改图片后，图片更新不过来--又改了，地址变了，所以不用清缓存了
     */
    fun loaderUserPhoto(context: Context, gifUrl: String, imageView: ImageView) {
//        Glide.with(context).load(gifUrl).placeholder(R.mipmap.ic_launcher).skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().into(imageView)
        Glide.with(context).load(gifUrl).placeholder(R.mipmap.ic_launcher).dontAnimate().into(imageView)
    }

    fun loaderImageTest(context: Context, gifUrl: String, imageView: ImageView) {
        Glide.with(context)
                .load(gifUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).crossFade()
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(imageView)
    }
}