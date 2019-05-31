package wangzx.scala_commons.sql_test

import wangzx.scala_commons.sql._
import wangzx.scala_commons.sql.ORM._

object InsertSaveTest {

  val dataSource = SampleDB.dataSource

  case class users(name: String, email: String, age: Int)


  def main(args: Array[String]): Unit = {

    val res = dataSource.save(users("maple", "295482300@qq.com", 27))

    println(res)

    val res1 = dataSource.rows[users]("select * from users")

    res1 foreach println
  }


  def testRow(): Unit = {

    val row = dataSource.row[Row]("select * from users where name = 'user1'")
    assert(row == None)
    //    assert(row.get.getString("name") == "user1")

  }

}

object X extends App {

  //
  //  final class $anon extends Insert[wangzx.scala_commons.sql_test.InsertSaveTest.users] {
  //    def build(t: wangzx.scala_commons.sql_test.InsertSaveTest.users) = scala.Tuple2("users", scala.collection.immutable.Map(scala.Tuple2("name", t.name), scala.Tuple2("email", t.email), scala.Tuple2("age", t.age)))
  //  }
  //
  //  val res = new $anon()
  //
  //  val res1 = res.build(users("maple", "295482300@qq.com", 27))
  //
  //  println(res1)
  //
  //  res1._1
  //
  //  val unzip: (immutable.Iterable[String], immutable.Iterable[Any]) = res1._2.unzip
  //
  //  val s1 = s"( ${unzip._1.mkString(",")} ) "
  //  val s2 = s"${unzip._2.mkString(",")}"
  //
  //  val qq = (0 until unzip._2.size).map(_ ⇒ "?").mkString(",")
  //
  //  println(qq)
  //
  //  val base = s"insert into ${res1._1} $s1 VALUES"
  //
  //  val sql = base + sql" ( $s2 )"
  //
  //  println(sql)

  //  {
  //    final class $anon extends Insert[wangzx.scala_commons.sql_test.InsertSaveTest.users] {
  //      def build(t: wangzx.scala_commons.sql_test.InsertSaveTest.users) = scala.Tuple3("users", scala.collection.immutable.List("name", "email", "age"), scala.collection.immutable.List({
  ////        val jdbcAccessor = implicitly[JdbcValueAccessor[rtype]];
  //        JdbcValue.wrap(t.name)
  //      }, {
  //        val jdbcAccessor = implicitly[JdbcValueAccessor[rtype]];
  //        JdbcValue.wrap(t.email.asInstanceOf[
  //        => String
  //        ] )
  //      }, {
  //        val jdbcAccessor = implicitly[JdbcValueAccessor[rtype]];
  //        JdbcValue.wrap(t.age.asInstanceOf[
  //        => Int
  //        ] )
  //      }))
  //    };
  //    new $anon()
  //  }

  {
    final class $anon extends Insert[wangzx.scala_commons.sql_test.InsertSaveTest.users] {
      def build(t: wangzx.scala_commons.sql_test.InsertSaveTest.users) = scala.Tuple3("users", scala.collection.immutable.List("name", "email", "age"), scala.collection.immutable.List(JdbcValue.wrap(t.name), JdbcValue.wrap(t.email), JdbcValue.wrap(t.age)))
    }
    new $anon()

  }

}

