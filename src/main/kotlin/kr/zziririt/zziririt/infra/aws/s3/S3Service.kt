package kr.zziririt.zziririt.infra.aws.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.util.UUID

@Service
class S3Service(
    private val amazonS3Client: AmazonS3Client,
) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    fun uploadFiles(dir: String, files: List<MultipartFile>): List<String> {
        val imageUrls = ArrayList<String>()
        for (file in files) {
            val randomFileName = "$dir/${UUID.randomUUID()}${LocalDateTime.now()}${file.originalFilename}"

            file.originalFilename?.isImageFileOrThrow()

            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            try {
                val inputStream: InputStream = file.inputStream
                amazonS3Client.putObject(bucket, randomFileName, inputStream, objectMetadata)
                val uploadFileUrl = amazonS3Client.getUrl(bucket, randomFileName).toString()
                imageUrls.add(uploadFileUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return imageUrls
    }

}
