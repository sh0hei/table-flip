package com.github.sh0hei.mascalade.tableflip

private final class TableFlip private(headers: Array[String], data: Array[Array[String]]) {

  private val columns: Int = headers.length
  private val columnWidths: Array[Int] = new Array[Int](columns)
  private var emptyWidth: Int = 0

  for (row: Int <- -1 to data.length - 1) {
    val rowData: Array[String] = if (row == -1) headers else data(row)
    if (rowData.length != columns) {
      throw new IllegalArgumentException(
        "Row %s's %s columns != %s columns".format(row + 1, rowData.length, columns)
      )
    }
    for (column: Int <- 0 to columns - 1) {
      val foo = rowData(column).split("\\n")
      for (rowDataLine: String <- foo) {
        columnWidths(column) = Math.max(columnWidths(column), rowDataLine.length)
      }
    }
  }

  emptyWidth = 3 * (columns - 1) // Account for column dividers and their spacing.
  for (columnWidth: Int <- columnWidths) {
    emptyWidth = emptyWidth + columnWidth
  }

  if (emptyWidth < TableFlip.EMPTY.length) {
    // Make sure we're wide enough for the empty text.
    columnWidths(columns - 1) += TableFlip.EMPTY.length() - emptyWidth
  }

  override def toString: String = {
    val builder: StringBuilder = new StringBuilder()
    printDivider(builder, "╔═╤═╗")
    printData(builder, headers)
    if (data.length == 0) {
      printDivider(builder, "╠═╧═╣")
      builder.append('║').append(pad(emptyWidth, TableFlip.EMPTY)).append("║\n")
      printDivider(builder, "╚═══╝")
    } else {
      for (row <- 0 to data.length - 1) {
        val foo = if (row == 0) "╠═╪═╣" else "╟─┼─╢"
        printDivider(builder, foo)
        printData(builder, data(row))
      }
      printDivider(builder, "╚═╧═╝")
    }
    builder.toString()
  }

  private def printDivider(out: StringBuilder, format: String): Unit = {
    for (column <- 0 to columns - 1) {
      val foo = if (column == 0) format.charAt(0) else format.charAt(2)
      out.append(foo)
      out.append(pad(columnWidths(column), "").replace(' ', format.charAt(1)))
    }
    out.append(format.charAt(4)).append('\n')
  }

  private def printData(out: StringBuilder, data: Seq[String]): Unit = {
    val maxCellLine: Int = data.map(_.split("\\n").length).max
    for (line <- 0 to maxCellLine - 1) {
      for (column <- 0 to columns - 1) {
        out.append(if (column == 0) '║' else '│')
        val cellLines: Array[String] = data(column).split("\\n")
        val cellLine: String = if (line < cellLines.length) cellLines(line) else ""
        out.append(pad(columnWidths(column), cellLine))
      }
      out.append("║\n")
    }
  }

  private def pad(width: Int, data: String): String = {
    String.format(" %1$-" + width + "s ", data)
  }

}

object TableFlip {

  private val EMPTY: String = "(empty)"

  def of(headers: Array[String], data: Array[Array[String]]): String = {
    require(headers != null)
    require(headers.length > 0)
    require(data != null)

    new TableFlip(headers, data).toString()
  }
}