package io.github.itsflicker.itstools.module.resourcepack

import com.aliyun.oss.OSSClientBuilder
import io.github.itsflicker.itstools.conf
import java.io.File
import java.io.FileInputStream

object OSSUploader : ResourcePackUploader {

    override fun upload(file: File): Boolean {
        val oss = conf.automatically_upload.oss
        if (oss.run { endpoint.isEmpty() || access_key_id.isEmpty() || access_key_secret.isEmpty() || bucket.isEmpty() || key.isEmpty() }) {
            return false
        }
        return try {
            val ossClient = OSSClientBuilder().build(oss.endpoint, oss.access_key_id, oss.access_key_secret)
            FileInputStream(file).use { inputStream ->
                ossClient.putObject(oss.bucket, oss.key, inputStream)
                ossClient.shutdown()
            }
            true
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

}