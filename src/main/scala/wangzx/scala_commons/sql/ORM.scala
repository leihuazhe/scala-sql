package wangzx.scala_commons.sql

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object ORM {

    // camel case mapping support such as userId -> user_id, postURL -> post_url
    case class Token(name: String) {
        // val method: Option[java.lang.reflect.Method] = defaultName.map ( companion.getClass.getMethod(_) )
        val underscoreName: String = {
            val sb = new StringBuilder
            var i = 0
            var lastChar: Char = 0
            while (i < name.length) {
                val ch = name.charAt(i)
                if (i == 0) sb.append(ch)
                else {
                    if (Character.isLowerCase(lastChar) && Character.isUpperCase(ch)) {
                        sb.append('_')
                        sb.append(ch.toLower)
                    }
                    else sb.append(ch)
                }
                lastChar = ch
                i += 1
            }
            sb.toString
        }

    }

    /**
      * 把任何 CaseClass 转换为 Map
      */
    trait Insert[C] {

        def from(c: C, schemaName: Option[String]): (String, Seq[JdbcValue[_]]) = {
            val (schema, tokenNames, args) = build(c)
            val useSchema = schemaName match {
                case Some(value) if value != null ⇒ value
                case None ⇒ schema
            }
            val sqlFields = tokenNames.map(_.underscoreName).mkString(",")
            val interrogation = tokenNames.indices.map(_ ⇒ "?").mkString(",")
            val sql = s"INSERT INTO $useSchema ($sqlFields) VALUES ( $interrogation )"

            (sql, args)
        }

        def build(c: C): (String, List[Token], Seq[JdbcValue[_]])
    }

    object Insert {

        def converterToMapMacro[C: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
            import c.universe._

            val tpe = weakTypeOf[C]

            val fields = tpe.decls.collectFirst {
                case m: MethodSymbol if m.isPrimaryConstructor => m
            }.get.paramLists.head

            val (names, jdbcValues) = fields.map { field =>
                val name = field.name.toTermName
                val decoded = name.decodedName.toString

                val value = q"JdbcValue.wrap(t.$name)"
                (q"Token($decoded)", value)
            }.unzip

            val schemaName = TermName(tpe.typeSymbol.name.toString).toString.toLowerCase()

            val tree =
                q"""
                     new Insert[$tpe] {
                        def build(t: $tpe) = ($schemaName,$names,$jdbcValues)
                     }
            """
            println(tree)
            tree
        }
    }

}

