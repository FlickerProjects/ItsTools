package io.github.itsflicker.itstools.module.resourcepack

import java.io.File

interface ResourcePackUploader {

    fun upload(file: File): Boolean

}