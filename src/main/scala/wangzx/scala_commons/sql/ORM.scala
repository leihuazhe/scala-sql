package wangzx.scala_commons.sql

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object ORM {

  sealed trait Token

  object Token {
    val INSERT_BASE_SQL = sql"""INSERT INTO """
  }

  case class StringToken(string: String) extends Token {
    override def toString = string
  }


  /**
    * 把任何 CaseClass 转换为 Map
    */
  trait Insert[C] {

    def from(c: C): (String, Seq[JdbcValue[_]]) = {
      val (schema, names, args) = build(c)
      val name = names.mkString(",")
      val interrogation = names.indices.map(_ ⇒ "?").mkString(",")
      val sql = s"INSERT INTO $schema ($name) VALUES ( $interrogation )"

      (sql, args)
    }

    def build(c: C): (String, List[String], Seq[JdbcValue[_]])
  }

  object Insert {

    implicit def materialize[C]: Insert[C] = macro converterToMapMacro[C]

    def converterToMapMacro[C: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
      import c.universe._

      val tpe = weakTypeOf[C]

      val fields = tpe.decls.collectFirst {
        case m: MethodSymbol if m.isPrimaryConstructor => m
      }.get.paramLists.head

      val (names, jdbcValues) = fields.map { field =>
        val name = field.name.toTermName
        val decoded = name.decodedName.toString.toLowerCase()

        val value = q"JdbcValue.wrap(t.$name)"
        (q"$decoded", value)
      }.unzip

      val schemaName = TermName(tpe.typeSymbol.name.toString).toString

      val tree =
        q"""
         new Insert[$tpe] {
          def build(t: $tpe) = ($schemaName,$names,$jdbcValues)
         }
        """
      //      println(tree)
      tree
    }
  }


}

