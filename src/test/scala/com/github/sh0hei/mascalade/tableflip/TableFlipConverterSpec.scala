package com.github.sh0hei.mascalade.tableflip

import java.sql.ResultSet
import com.github.sh0hei.mascalade.tableflip.util.{FakeResultSet, Person, PersonType}
import org.specs2.mutable.Specification

class TableFlipConverterSpec extends Specification {

  "TableFlipConverterSpec" should {

    /* TODO
    "simpleIterator" in {
      val people: Array[Person] = Array(
        Person("Big", "Bird", 16, "Big Yellow"),
        Person("Joe", "Smith", 42, "Proposition Joe"),
        Person("Oscar", "Grouchant", 8, "Oscar The Grouch"))

      val expected: String =
        """
          |╔═════╤═══════════╤═══════════╤══════════════════╗
          |║ Age │ FirstName │ LastName  │ NickName         ║
          |╠═════╪═══════════╪═══════════╪══════════════════╣
          |║ 16  │ Big       │ Bird      │ Big Yellow       ║
          |╟─────┼───────────┼───────────┼──────────────────╢
          |║ 42  │ Joe       │ Smith     │ Proposition Joe  ║
          |╟─────┼───────────┼───────────┼──────────────────╢
          |║ 8   │ Oscar     │ Grouchant │ Oscar The Grouch ║
          |╚═════╧═══════════╧═══════════╧══════════════════╝
        """.stripMargin

      val table: String = TableFlipConverter.fromIterable(people, classOf[Person])

      table must_== expected
    }

    "emptyIterator" in {
      val people: Array[Person] = Array.empty[Person]
      val expected: String =
        """
          |╔═════╤═══════════╤══════════╤══════════╗
          |║ Age │ FirstName │ LastName │ NickName ║
          |╠═════╧═══════════╧══════════╧══════════╣
          |║ (empty)                               ║
          |╚═══════════════════════════════════════╝
        """.stripMargin

      val table: String = TableFlipConverter.fromIterable(people, classOf[Person])
      table must_== expected
    }
    */

    // throws Exception
    "simpleResultSet" in {
      val headers: Array[String] = Array("English", "Digit", "Spanish")
      val data: Array[Array[String]] = Array(Array("One", "1", "Uno"), Array("Two", "2", "Dos"), Array("Three", "3", "Tres"))
      val resultSet: ResultSet = new FakeResultSet(headers, data)
      val expected: String =
        """ |╔═════════╤═══════╤═════════╗
            |║ English │ Digit │ Spanish ║
            |╠═════════╪═══════╪═════════╣
            |║ One     │ 1     │ Uno     ║
            |╟─────────┼───────┼─────────╢
            |║ Two     │ 2     │ Dos     ║
            |╟─────────┼───────┼─────────╢
            |║ Three   │ 3     │ Tres    ║
            |╚═════════╧═══════╧═════════╝
            |""".stripMargin
      val table: String = TableFlipConverter.fromResultSet(resultSet)
      table must_== expected
    }

    // throws SQLException
    "emptyResultSet" in {
      val headers: Array[String] = Array("English", "Digit", "Spanish")
      val data: Array[Array[String]] = Array.empty[Array[String]]
      val resultSet: ResultSet = new FakeResultSet(headers, data)
      val expected =
        """ |╔═════════╤═══════╤═════════╗
            |║ English │ Digit │ Spanish ║
            |╠═════════╧═══════╧═════════╣
            |║ (empty)                   ║
            |╚═══════════════════════════╝
            |""".stripMargin
      val table: String = TableFlipConverter.fromResultSet(resultSet)
      table must_== expected
    }

    "simpleObjects" in {
      val headers: Array[String] = Array("First Name", "Last Name", "Age", "Type")
      val data: Array[Array[Any]] = Array(
        Array("Big", "Bird", 16, PersonType.Costume),
        Array("Joe", "Smith", 42, PersonType.Human),
        Array("Oscar", "Grouchant", 8, PersonType.Puppet)
      )

      val expected: String =
        """ |╔════════════╤═══════════╤═════╤═════════╗
            |║ First Name │ Last Name │ Age │ Type    ║
            |╠════════════╪═══════════╪═════╪═════════╣
            |║ Big        │ Bird      │ 16  │ Costume ║
            |╟────────────┼───────────┼─────┼─────────╢
            |║ Joe        │ Smith     │ 42  │ Human   ║
            |╟────────────┼───────────┼─────┼─────────╢
            |║ Oscar      │ Grouchant │ 8   │ Puppet  ║
            |╚════════════╧═══════════╧═════╧═════════╝
            |""".stripMargin
      val table: String = TableFlipConverter.fromObjects(headers, data)
      table must_== expected
    }

    "emptyObjects" in {
      val headers: Array[String] = Array("First Name", "Last Name", "Age", "Type")
      val data: Array[Array[Any]] = Array.empty[Array[Any]]
      val expected: String =
        """ |╔════════════╤═══════════╤═════╤══════╗
            |║ First Name │ Last Name │ Age │ Type ║
            |╠════════════╧═══════════╧═════╧══════╣
            |║ (empty)                             ║
            |╚═════════════════════════════════════╝
            |""".stripMargin
      val table: String = TableFlipConverter.fromObjects(headers, data)
      table must_== expected
    }
  }

}
