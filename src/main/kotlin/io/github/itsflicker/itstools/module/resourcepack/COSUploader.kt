package io.github.itsflicker.itstools.module.resourcepack

import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.model.PutObjectRequest
import com.qcloud.cos.region.Region
import io.github.itsflicker.itstools.conf
import java.io.File

object COSUploader {

    private lateinit var client: COSClient

    fun upload(file: File): Exception? {
        if (conf.automatically_upload.cos == null) {
            return null
        }
        if (!this::client.isInitialized) {
            val cred = BasicCOSCredentials(conf.automatically_upload.cos!!.secret_id, conf.automatically_upload.cos!!.secret_key)
            val config = ClientConfig(Region(conf.automatically_upload.cos!!.region))
            client = COSClient(cred, config)
        }
        val request = PutObjectRequest(conf.automatically_upload.cos!!.bucket, conf.automatically_upload.cos!!.key, file)
        return try {
            client.putObject(request)
            null
        } catch (e: Exception) {
            e
        }
    }

}