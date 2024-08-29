package controllers.pojos.applications


case class ApplicationsResponseWithTotal(
                                          total: Int,
                                          applications: Seq[ApplicationsResponse]
                                        )

