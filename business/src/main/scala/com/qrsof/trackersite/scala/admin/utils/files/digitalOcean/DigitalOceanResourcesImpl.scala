package com.qrsof.trackersite.scala.admin.utils.files.digitalOcean

import com.qrsof.libs.storage.{NewResource, ResourceKey, StorageService}
import com.qrsof.trackersite.scala.admin.utils.files.pojos.{FileKey, FileResource}
import io.scalaland.chimney.dsl._
import jakarta.inject.Inject
import org.apache.pekko.http.scaladsl.common.StrictForm.FileData

import java.io.{ByteArrayInputStream, InputStream}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.OptionConverters.RichOptional

class DigitalOceanResourcesImpl @Inject()(storageService: StorageService)(implicit ex: ExecutionContext) extends DigitalOceanResources {


  override def save(folder: String, fileData: FileData): Future[Option[FileKey]] = Future {
    val byteArrayInputStreamFileData = new ByteArrayInputStream(fileData.entity.data.toArray)
    val newResource = new NewResource(
      byteArrayInputStreamFileData,
      s"trackersite/files/$folder",
      fileData.filename.get
    )
    Some(
      storageService
        .save(newResource)
        .into[FileKey]
        .withFieldComputed(
          _.key,
          _.key()
        )
        .transform
    )
  }


  override def get(resourceKey: String, storeKey: String): Future[Option[FileResource]] = Future {
    try {
      val maybeResource = storageService
        .retrieve(
          new ResourceKey(resourceKey)
        )
        .toScala
      maybeResource match {
        case Some(resource) =>
          val originalName: String = resource.originalName()
          val contentType = originalName.split('.').tail.head
          builtRespFileRes(resource.key, resource.content, resource.originalName, contentType, resource.path())
        case None => None
      }
    } catch {
      case _: Throwable => None
    }  }


  private def builtRespFileRes(key: String, content: InputStream, originalName: String, contentType: String, path: String): Option[FileResource] = {
    Some(
      FileResource(
        key = key,
        content = content,
        name = originalName,
        contentType = contentType,
        path = path
      )
    )
  }
}
