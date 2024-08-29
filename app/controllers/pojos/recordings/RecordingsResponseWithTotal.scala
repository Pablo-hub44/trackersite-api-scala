package controllers.pojos.recordings

case class RecordingsResponseWithTotal(
                                        total: Int,
                                        recordings: Seq[RecordingsResponse]
                                      )

