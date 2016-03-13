# Table Flip

pretty-printing text tables in Scala.

(╯°□°）╯︵ ┻━┻


## Usage

A `TableFlip` requires headers and data in string form:

```scala
val headers = Array("Test", "Header")
val data = Array(
  Array("Foo", "Bar"),
  Array("Kit", "Kat")
)
println(TableFlip.of(headers, data))
```
```
╔══════╤════════╗
║ Test │ Header ║
╠══════╪════════╣
║ Foo  │ Bar    ║
╟──────┼────────╢
║ Kit  │ Kat    ║
╚══════╧════════╝
```

They can be empty:
```scala
val headers = Array("Test", "Header")
val data = Array.empty[Array[String]]
println(TableFlip.of(headers, data))
```
```
╔══════╤════════╗
║ Test │ Header ║
╠══════╧════════╣
║ (empty)       ║
╚═══════════════╝
```

Newlines are supported:
```scala
val headers = Array("One\nTwo Three", "Four")
val data = Array(Array("Five", "Six\nSeven Eight"))
println(TableFlip.of(headers, data))
```
```
╔═════════╤═════════════╗
║ One Two │ Four        ║
║ Three   │             ║
╠═════════╪═════════════╣
║ Five    │ Six         ║
║         │ Seven Eight ║
╚═════════╧═════════════╝
```

Which means tables can be nested:
```scala
val innerHeaders = Array("One", "Two")
val innerData = Array(Array("1", "2"))
val nested = TableFlip.of(innerHeaders, innerData)
val outerHeaders = Array("Left", "Right")
val outerData = Array(Array(nested, nested))
println(TableFlip.of(headers, data))
```
```
╔═══════════════╤═══════════════╗
║ Left          │ Right         ║
╠═══════════════╪═══════════════╣
║ ╔═════╤═════╗ │ ╔═════╤═════╗ ║
║ ║ One │ Two ║ │ ║ One │ Two ║ ║
║ ╠═════╪═════╣ │ ╠═════╪═════╣ ║
║ ║ 1   │ 2   ║ │ ║ 1   │ 2   ║ ║
║ ╚═════╧═════╝ │ ╚═════╧═════╝ ║
╚═══════════════╧═══════════════╝
```

Helper methods convert from types like lists:
```scala
val people = Array(
  Person("Foo", "Bar"),
  Person("Kit", "Kat")
)
println(TableFlipConverter.fromIterable(people, classOf[Person]))
```
```
╔═══════════╤══════════╗
║ FirstName │ LastName ║
╠═══════════╪══════════╣
║ Foo       │ Bar      ║
╟───────────┼──────────╢
║ Kit       │ Kat      ║
╚═══════════╧══════════╝
```

Or a database result:
```scala
val resultSet = statement.executeQuery("SELECT first_name, last_name FROM users")
println(TableFlipConverter.fromResultSet(resultSet))
```
```
╔════════════╤═══════════╗
║ first_name │ last_name ║
╠════════════╪═══════════╣
║ Kenji      │ Hujikido  ║
╟────────────┼───────────╢
║ Ichiro     │ Morita    ║
╚════════════╧═══════════╝
```

Arbitrary objects are also supported:
```scala
val headers = Array("First Name", "Last Name", "Age", "Type")
val data = Array(
  Array("Big", "Bird", 16, PersonType.Costume),
  Array("Joe", "Smith", 42, PersonType.Human),
  Array("Oscar", "Grouchant", 8, PersonType.Puppet)
)
println(TableFlipConverter.fromObjects(headers, data));
```
```
╔════════════╤═══════════╤═════╤═════════╗
║ First Name │ Last Name │ Age │ Type    ║
╠════════════╪═══════════╪═════╪═════════╣
║ Big        │ Bird      │ 16  │ Costume ║
╟────────────┼───────────┼─────┼─────────╢
║ Joe        │ Smith     │ 42  │ Human   ║
╟────────────┼───────────┼─────┼─────────╢
║ Oscar      │ Grouchant │ 8   │ Puppet  ║
╚════════════╧═══════════╧═════╧═════════╝
```


## License

```
Copyright (c) 2016 sh0hei
Released under the MIT license
http://opensource.org/licenses/mit-license.php
```