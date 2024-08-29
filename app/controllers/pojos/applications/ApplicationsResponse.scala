package controllers.pojos.applications

import java.util.Date

case class ApplicationsResponse(key: String,
                                name: String,
                                active: Boolean,
                                createdAt: Date,
                                updatedAt: Date)

