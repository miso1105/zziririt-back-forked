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
import java.io.Serializable

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val amazonS3Client: AmazonS3Client,
    private val s3Service: S3Service,
) {

    @PostMapping("/imageUploadWithCoroutine")
    suspend fun imageUploadWithCotoutine(@RequestParam multipartFile: List<MultipartFile>): String {
        return s3Service.imageUploadWithCoroutine(dir = "test", multipartFile)
    }

    @PostMapping("/imageUpload")
    fun uploadImage(@RequestParam multipartFile: List<MultipartFile>): String {
        return s3Service.uploadFiles(dir = "test", multipartFile)
    }

    @PostMapping("/imageUploadWithPreSignedUrl")
    fun uploadImageWithPreSignedUrl(@RequestParam multipartFile: List<MultipartFile>): Map<String, Serializable>? {
        return s3Service.getPreSignedUrl(multipartFile)
    }
}