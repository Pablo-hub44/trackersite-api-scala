package com.qrsof.trackersite.scala.admin.utils.files

import com.qrsof.libs.filesystem.FileSystemService
import com.qrsof.trackersite.scala.admin.utils.files.pojos.{FileInLocalNotFoundException, FileResource, ViewFileExceptions}
import jakarta.inject.Inject
import org.slf4j.{Logger, LoggerFactory}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardCopyOption, StandardOpenOption}

class FilesLocalActionsImpl @Inject() (
                                        fileSystemService: FileSystemService
                                      ) extends FilesActions {

  val logger: Logger = LoggerFactory.getLogger(classOf[FilesLocalActionsImpl])
  private val pathUrl = "/Users/qrsof/TestRecordings"

  override def getFile(fileKey: String, recordingKey: String, contentType: String): Either[ViewFileExceptions, FileResource] = {
    logger.info("Get Storage Resource File")
    try {
      val resp = fileSystemService.getLocalFile(fileKey, pathUrl)
      val getContentType = if (resp.contentType() == null) {
        contentType
      } else {
        resp.contentType()
      }
      val buildFR = FileResource(
        fileKey,
        resp.content(),
        resp.originalName(),
        getContentType,
        resp.path()
      )
      Right(buildFR)
    } catch {
      case _: Throwable =>
        Left(FileInLocalNotFoundException(fileKey))
    }
  }

  override def saveFile(events: Seq[String]): String = {
    val eventsString = events.mkString("\n")
    val fileData = eventsString.getBytes(StandardCharsets.UTF_8)
    try {
      val resourceKey = fileSystemService.saveOrUpdLocalFile(fileData, pathUrl)
      val originalFilePath = Paths.get(pathUrl, resourceKey.key()).toString
      val newFileName = s"${resourceKey.key()}.json"
      val newFilePath = Paths.get(pathUrl, newFileName)
      Files.move(Paths.get(originalFilePath), newFilePath, StandardCopyOption.REPLACE_EXISTING)
      resourceKey.key()
    } catch {
      case e: Throwable =>
        logger.error("Failed to save file", e)
        throw e
    }
  }

  override def updateFile(fileKey: String, newEvents: Seq[String]): Either[ViewFileExceptions, String] = {
    logger.info(s"Updating file with key: $fileKey")
    try {
      val fileExist = fileSystemService.getLocalFile(fileKey, pathUrl)
      val currentContent = new String(fileExist.content().readAllBytes(), StandardCharsets.UTF_8)
      val currentJson = ujson.read(currentContent).arr
      newEvents.foreach { event =>
        val parsedEvent = ujson.read(event)
        currentJson.addAll(parsedEvent.arr)
      }
      val updatedContent = currentJson.render().getBytes(StandardCharsets.UTF_8)
      Files.write(Paths.get(fileExist.path()), updatedContent, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)
      Right(fileKey)
    } catch {
      case _: Throwable =>
        Left(FileInLocalNotFoundException(fileKey))
    }
  }
}
