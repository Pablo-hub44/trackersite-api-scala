package controllers.pojos.recordings


case class AddRecordingForm(
                           events: Array[String],
                           recordingKey: Option[String]
                           )

