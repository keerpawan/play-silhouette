package models

import play.api.libs.json.Json

case class Role(
  id: Int,
  name: String,
  permission: Option[List[String]]
) extends be.objectify.deadbolt.scala.models.Role

object Role {
  implicit val jsonFormat = Json.format[Role]
}

case class Permission(
  value: String
) extends be.objectify.deadbolt.scala.models.Permission

object Permission {
  implicit val jsonFormat = Json.format[Permission]
}

case class Subject(
  identifier: String,
  roles: List[Role],
  permissions: List[Permission]
) extends be.objectify.deadbolt.scala.models.Subject

object Subject {
  implicit val jsonFormat = Json.format[Subject]
}