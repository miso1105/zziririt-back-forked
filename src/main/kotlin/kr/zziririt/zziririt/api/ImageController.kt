package kr.zziririt.zziririt.api

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kr.zziririt.zziririt.infra.aws.s3.S3Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val amazonS3Client: AmazonS3Client,
    private val s3Service: S3Service,
) {


    // 코루틴 병렬처리
//    @PostMapping("/imageUploadWithCoroutine")
//    suspend fun imageUploadWithCoroutine(
//        @RequestParam multipartFile: List<MultipartFile>,
//    ) = withContext(Dispatchers.IO) {
//        val uploadImage = multipartFile.map {
//            val objectMetadata = ObjectMetadata().apply {
//                this.contentType = it.contentType
//                this.contentLength = it.size
//            }
//            async {
//                val putObjectRequest = PutObjectRequest(
//                    bucket,
//                    "objectKey",
//                    it.inputStream,
//                    objectMetadata,
//                )
//                amazonS3Client.putObject(putObjectRequest)
//            }
//        }
//    uploadImage.awaitAll()
//    return@withContext "업로드 완료"
//    }

    @PostMapping("/imageUploadWithCoroutine")
    suspend fun imageUploadWithCotoutine(@RequestParam multipartFile: List<MultipartFile>): String {
        return s3Service.imageUploadWithCoroutine(dir = "test", multipartFile)
    }

    @PostMapping("/imageUpload")
    fun uploadImage(@RequestParam multipartFile: List<MultipartFile>): String {
        return s3Service.uploadFiles(dir = "test", multipartFile)
    }
}