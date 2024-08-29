package com.qrsof.trackersite.scala.admin.utils.files.pojos

import java.io.InputStream


case class FileResource(
                       key: String,
                       content: InputStream,
                       name: String,
                       contentType: String,
                       path: String
                       )
