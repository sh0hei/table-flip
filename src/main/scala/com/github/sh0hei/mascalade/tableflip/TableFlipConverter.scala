package com.github.sh0hei.mascalade.tableflip

import java.lang.reflect.Method
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.util.Arrays
import java.util.Comparator
import java.util.regex.Pattern

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

/** Helper methods for creating {@link FlipTable tables} from various different data sets. */
final object TableFlipConverter {

  // private val Method: Pattern = Pattern.compile("^(?:get|is|has)([A-Z][a-zA-Z0-9]*)+$")
  private val Method: Pattern = Pattern.compile("^([a-zA-Z]*)+$")

  private val MethodComparator: Comparator[Method] = new Comparator[Method] {
    override def compare(o1: Method, o2: Method): Int = o1.getName.compareTo(o2.getName)
  }

  /** Create a table from an array of objects using {@link String#valueOf}. */
  def fromObjects(headers: Array[String], data: Array[Array[Any]]): String = {
    if (headers == null) throw new NullPointerException("headers == null")
    if (data == null) throw new NullPointerException("data == null")

    val stringData: collection.mutable.MutableList[Array[String]] = collection.mutable.MutableList.empty[Array[String]]
    for (row: Array[Any] <- data) {
      val stringRow: Array[String] = new Array[String](row.length)
      for (column <- 0 to row.length - 1) {
        stringRow(column) = String.valueOf(row(column))
      }
      stringData += stringRow
    }

    val dataArray: Array[Array[String]] = stringData.toArray //(new Array[Array[String]](stringData.length))
    TableFlip.of(headers, dataArray)
  }

  /**
    * Create a table from a group of objects. Public accessor methods on the class type with no
    * arguments will be used as the columns.
    */
  // TODO
  def fromIterable[T <: Any: ClassTag](rows: Iterable[T], rowType: Class[T]) = {
    if (rows == null) throw new NullPointerException("rows == null")
    if (rowType == null) throw new NullPointerException("rowType == null")

    val declaredMethods: Array[Method] = rowType.getDeclaredMethods
    Arrays.sort(declaredMethods, MethodComparator)

    val methods: collection.mutable.MutableList[Method] = collection.mutable.MutableList.empty
    val headers: collection.mutable.MutableList[String] = collection.mutable.MutableList.empty

    for (declaredMethod: Method <- declaredMethods
         if declaredMethod.getParameterTypes().length <= 0
         if declaredMethod.getReturnType() != classOf[Unit]
         if Method.matcher(declaredMethod.getName()).matches()) {

      declaredMethod.setAccessible(true)
      methods += declaredMethod
      //headers += Method.matcher(declaredMethod.getName()).group(1)
      headers += declaredMethod.getName
    }

    val columnCount: Int = methods.size
    val data: collection.mutable.MutableList[Array[String]] = collection.mutable.MutableList.empty

    def getType[A: universe.TypeTag](clazz: Class[A]): universe.Type = universe.typeTag[A].tpe

    for (row <- rows) {

      val rowData: Array[String] = new Array[String](columnCount)
      for (column <- 0 to columnCount - 1) {
        try {

          //rowData(column) = String.valueOf(methods.get(column)) //.invoke(row))

          val theType = universe.typeOf[Person]
          val runtimeMirror = universe.runtimeMirror(Thread.currentThread.getContextClassLoader)
          val instanceMirror = runtimeMirror.reflect(row)
          val valField =
            theType
            //.member(universe.TermName(String.valueOf(methods.get(column))))
              .member(universe.TermName("age"))
            .asMethod
            .getter
            .asMethod
          val fieldMirror = instanceMirror.reflectField(valField)
          rowData(column) = String.valueOf(fieldMirror.get)

        } catch {
          case e: Exception => throw new RuntimeException
        }
      }
      data += rowData
    }

    val headerArray: Array[String] = headers.toArray
    val dataArray: Array[Array[String]] = data.toArray
    TableFlip.of(headerArray, dataArray)
  }

  /** Create a table from a {@link ResultSet}. */
  def fromResultSet(resultSet: ResultSet): String = {
    if (resultSet == null) throw new NullPointerException("resultSet == null")
    if (!resultSet.isBeforeFirst()) throw new IllegalStateException("Result set not at first.")

    val headers: collection.mutable.MutableList[String] = collection.mutable.MutableList.empty
    val resultSetMetaData: ResultSetMetaData = resultSet.getMetaData
    val columnCount: Int = resultSetMetaData.getColumnCount
    for (column <- 0 to columnCount - 1) {
      headers += resultSetMetaData.getColumnName(column + 1)
    }

    val data: collection.mutable.MutableList[Array[String]] = collection.mutable.MutableList.empty

    while (resultSet.next()) {
      val rowData: Array[String] = new Array[String](columnCount)
      for (column <- 0 to columnCount - 1) {
        rowData(column) = resultSet.getString(column + 1)
      }
      data += rowData
    }

    val headerArray: Array[String] = headers.toArray
    val dataArray: Array[Array[String]] = data.toArray
    TableFlip.of(headerArray, dataArray)
  }
}
