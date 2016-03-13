package com.github.sh0hei.mascalade.tableflip

/**
  * Created by sh0hei on 2016/03/12.
  */
object Main {

  def main(args: Array[String]): Unit = {
    println(TableFlip.of(Array("foo", "bar"), Array(Array("fe\nfiz", "buzz"))))

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

    println(TableFlipConverter.fromIterable(people, classOf[Person]))
  }
}
