package kr.zziririt.zziririt.infra.aws.s3

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Service
class S3Service(
    private val amazonS3Client: AmazonS3Client,
) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    fun uploadFiles(dir: String, files: List<MultipartFile>): String {
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
//        return imageUrls
        return "업로드 완료"
    }

    suspend fun imageUploadWithCoroutine(dir: String, files: List<MultipartFile>) = withContext(Dispatchers.IO) {
        val uploadImage = files.map {
            val randomFileName = "$dir/${UUID.randomUUID()}${LocalDateTime.now()}${it.originalFilename}"

            it.originalFilename?.isImageFileOrThrow()
            val objectMetadata = ObjectMetadata().apply {
                this.contentType = it.contentType
                this.contentLength = it.size
            }
            async {
                val putObjectRequest = PutObjectRequest(
                    bucket,
                    randomFileName,
                    it.inputStream,
                    objectMetadata,
                )
                amazonS3Client.putObject(putObjectRequest)
            }
        }
        uploadImage.awaitAll()
        return@withContext "업로드 완료"

//        val imageUrls = ArrayList<String>()
//        for (file in files) {
//            val randomFileName = "$dir/${UUID.randomUUID()}${LocalDateTime.now()}${file.originalFilename}"
//
//            file.originalFilename?.isImageFileOrThrow()
//
//
//            val objectMetadata = ObjectMetadata()
//            objectMetadata.contentLength = file.size
//            objectMetadata.contentType = file.contentType
//
//            async {
//                val inputStream: InputStream = file.inputStream
//                val uploadFileUrl = amazonS3Client.getUrl(bucket, randomFileName).toString()
//                imageUrls.add(uploadFileUrl)
//                amazonS3Client.putObject(bucket, randomFileName, inputStream, objectMetadata)
//            }
//        }
//        return "업로드 완료"
    }

    fun getPreSignedUrl(fileNames: List<MultipartFile>): Map<String, Serializable>? {
        val encodedFileName = fileNames.let { "${it}_${LocalDateTime.now()}" }
//        val objectKey = encodedFileName.let { "test/${it}" }
//        val encodedFileName = "${fileName}_${LocalDateTime.now()}"
//        val objectKey = "test/${encodedFileName}"


        val expiration = Date()
        var expTimeMillis: Long = expiration.time
        expTimeMillis += (3 * 60 * 1000).toLong() // 3분
        expiration.time = expTimeMillis // url 만료 시간 설정

        val generatePresignedUrlRequest: GeneratePresignedUrlRequest =
            GeneratePresignedUrlRequest(bucket, "test/${encodedFileName}")
            //            GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration)

        return mapOf(
            "preSignedUrl" to amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest),
            "encodedFileName" to encodedFileName
        )
    }

}
