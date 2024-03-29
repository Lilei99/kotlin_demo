package com.sixiangtianxia.commonlib.manager

import android.content.Context
import android.os.Environment
import android.text.TextUtils

import java.io.File
import java.math.BigDecimal

/**
 * 本应用数据清除管理器
 */
object DataCleanManager {
    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    fun cleanDatabases(context: Context) {
        deleteFilesByDirectory(File("/data/data/" + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    fun cleanSharedPreference(context: Context) {
        deleteFilesByDirectory(File("/data/data/" + context.packageName + "/shared_prefs"))
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    fun cleanDatabaseByName(context: Context, dbName: String) {
        context.deleteDatabase(dbName)
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    fun cleanCustomCache(filePath: String) {
        //        deleteFilesByDirectory(new File(filePath));
        deleteDir(File(filePath))
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(context: Context, vararg filepath: String) {
        clearAllCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
        if (filepath == null) {
            return
        }
        for (filePath in filepath) {
            cleanCustomCache(filePath)
        }
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private fun deleteFilesByDirectory(directory: File?) {
        if (directory != null && directory.exists() && directory.isDirectory) {
            for (item in directory.listFiles()) {
                item.delete()
            }
        }
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    @Throws(Exception::class)
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                if (fileList[i].isDirectory) {
                    size = size + getFolderSize(fileList[i])
                } else {
                    size = size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {// 如果下面还有文件
                    val files = file.listFiles()
                    for (i in files.indices) {
                        deleteFolderFile(files[i].absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {// 如果是文件，删除
                        file.delete()
                    } else {// 目录
                        if (file.listFiles().size == 0) {// 目录下没有文件或者目录，删除
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    @Throws(Exception::class)
    fun getCacheSize(file: File): String {
        return getFormatSize(getFolderSize(file).toDouble())
    }

}
