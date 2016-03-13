package com.github.sh0hei.mascalade.tableflip.util

sealed abstract class PersonType

object PersonType {

  case object Human extends PersonType

  case object Costume extends PersonType

  case object Puppet extends PersonType

}