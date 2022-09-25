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
        val cos = conf.automatically_upload.cos
        if (cos.run { secret_id.isEmpty() || secret_key.isEmpty() || region.isEmpty() || bucket.isEmpty() || key.isEmpty() }) {
            return null
        }
        if (!this::client.isInitialized) {
            val cred = BasicCOSCredentials(cos.secret_id, cos.secret_key)
            val config = ClientConfig(Region(cos.region))
            client = COSClient(cred, config)
        }
        val request = PutObjectRequest(cos.bucket, cos.key, file)
        return try {
            client.putObject(request)
            null
        } catch (e: Exception) {
            e
        }
    }

}