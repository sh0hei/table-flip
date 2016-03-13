package com.github.sh0hei.mascalade.tableflip

import org.specs2.mutable.Specification

class TableFlipSpec extends Specification {

  "TableFlip" should {

    "empty" in {
      val headers: Array[String] = Array("Test", "Header")
      val data: Array[Array[String]] = Array.empty[Array[String]]
      val expected: String =
        """ |╔══════╤════════╗
            |║ Test │ Header ║
            |╠══════╧════════╣
            |║ (empty)       ║
            |╚═══════════════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "emptyWide" in {
      val headers: Array[String] = Array("Test", "Headers", "Are", "The", "Best")
      val data: Array[Array[String]] = Array.empty[Array[String]]
      val expected: String =
        """ |╔══════╤═════════╤═════╤═════╤══════╗
            |║ Test │ Headers │ Are │ The │ Best ║
            |╠══════╧═════════╧═════╧═════╧══════╣
            |║ (empty)                           ║
            |╚═══════════════════════════════════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "emptyThinOneColumn" in {
      val headers: Array[String] = Array("A")
      val data: Array[Array[String]] = Array.empty[Array[String]]
      val expected: String =
        """ |╔═════════╗
            |║ A       ║
            |╠═════════╣
            |║ (empty) ║
            |╚═════════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "emptyThinTwoColumns" in {
      val headers: Array[String] = Array("A", "B")
      val data: Array[Array[String]] = Array.empty[Array[String]]
      val expected: String =
        """ |╔═══╤═════╗
            |║ A │ B   ║
            |╠═══╧═════╣
            |║ (empty) ║
            |╚═════════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "simple" in {
      val headers: Array[String] = Array("Hi", "Header")
      val data: Array[Array[String]] = Array(Array("Foo", "Bar"), Array("Kit", "Kat"), Array("Ping", "Pong"))
      val expected: String =
        """ |╔══════╤════════╗
            |║ Hi   │ Header ║
            |╠══════╪════════╣
            |║ Foo  │ Bar    ║
            |╟──────┼────────╢
            |║ Kit  │ Kat    ║
            |╟──────┼────────╢
            |║ Ping │ Pong   ║
            |╚══════╧════════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "dataNewlineFirstLineLong" in {
      val headers: Array[String] = Array("One", "Two")
      val data: Array[Array[String]] = Array(Array("Foo Bar\nBaz", "Two"))
      val expected: String =
        """ |╔═════════╤═════╗
            |║ One     │ Two ║
            |╠═════════╪═════╣
            |║ Foo Bar │ Two ║
            |║ Baz     │     ║
            |╚═════════╧═════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "dataNewlineFirstLineShort" in {
      val headers: Array[String] = Array("One", "Two")
      val data: Array[Array[String]] = Array(Array("Foo\nBar Baz", "Two"))
      val expected: String =
        """ |╔═════════╤═════╗
            |║ One     │ Two ║
            |╠═════════╪═════╣
            |║ Foo     │ Two ║
            |║ Bar Baz │     ║
            |╚═════════╧═════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "headerNewlineFirstLineLong" in {
      val headers: Array[String] = Array("One Two\nThree", "Four")
      val data: Array[Array[String]] = Array(Array("One", "Four"))
      val expected: String =
        """ |╔═════════╤══════╗
            |║ One Two │ Four ║
            |║ Three   │      ║
            |╠═════════╪══════╣
            |║ One     │ Four ║
            |╚═════════╧══════╝
            |""".stripMargin
      TableFlip.of(headers, data) must_== expected
    }

    "headerNewlineFirstLineShort" in {
      val headers: Array[String] = Array("One\nTwo Three", "Four")
      val data: Array[Array[String]] = Array(Array("One", "Four"))
      val expected: String =
        """ |╔═══════════╤══════╗
            |║ One       │ Four ║
            |║ Two Three │      ║
            |╠═══════════╪══════╣
            |║ One       │ Four ║
            |╚═══════════╧══════╝
            |""".stripMargin

      TableFlip.of(headers, data) must_== expected
    }

    "nested" in {
      val innerHeaders: Array[String] = Array("One", "Two")
      val innerData: Array[Array[String]] = Array(Array("1", "2"))
      val nested: String = TableFlip.of(innerHeaders, innerData)
      val outerHeaders: Array[String] = Array("Left", "Right")
      val outerData: Array[Array[String]] = Array(Array(nested, nested))
      val expected: String =
        """ |╔═══════════════╤═══════════════╗
            |║ Left          │ Right         ║
            |╠═══════════════╪═══════════════╣
            |║ ╔═════╤═════╗ │ ╔═════╤═════╗ ║
            |║ ║ One │ Two ║ │ ║ One │ Two ║ ║
            |║ ╠═════╪═════╣ │ ╠═════╪═════╣ ║
            |║ ║ 1   │ 2   ║ │ ║ 1   │ 2   ║ ║
            |║ ╚═════╧═════╝ │ ╚═════╧═════╝ ║
            |╚═══════════════╧═══════════════╝
            |""".stripMargin

      TableFlip.of(outerHeaders, outerData) must_== expected
    }

    "rowColumnMismatchThrowsWhenLess" in {
      val headers: Array[String] = Array("The", "Headers")
      val less: Array[Array[String]] = Array(Array("Less"))

      TableFlip.of(headers, less) must throwA[IllegalArgumentException]
    }

    "rowColumnMismatchThrowsWhenMore" in {
      val headers: Array[String] = Array("The", "Headers")
      val more: Array[Array[String]] = Array(Array("More", "Is", "Not", "Less"))
      TableFlip.of(headers, more) must throwA[IllegalArgumentException]
    }

    "nullHeadersThrows" in {
      TableFlip.of(null, new Array[Array[String]](0)) must throwA[IllegalArgumentException]
    }

    "emptyHeadersThrows" in {
      TableFlip.of(new Array[String](0), new Array[Array[String]](0)) must throwA[IllegalArgumentException]
    }

    "nullDataThrows" in {
      TableFlip.of(new Array[String](1), null) must throwA[IllegalArgumentException]
    }
  }
}
