package controllers.pojos.recordings

import java.util.Date

case class RecordingsResponse(
                             key: String,
                             eventsReference: String,
                             createdAt: Date,
                             updatedAt: Date
                             )
