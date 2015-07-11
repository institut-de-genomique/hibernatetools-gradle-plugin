# Hibernatetools-gradle-plugins

## Purpose

This plugin intend to ease hibernatetools through gradle. It offer three tasks:
- hibernate-config: to generate hibernate config files
- hbm2java: to generate java classes
- hbm2dao: to generate DAO classes

## Installation

Put the plugins inside you local .m2 directory:

```
$ git clone https://github.com/institut-de-genomique/hibernatetools-gradle-plugin.git
$ cd hibernatetools-gradle-plugin
$ gradle publishToMavenLocal
```

## Usage

Inside your build.gradle file add this:

```
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath   "org.hibernate.gradle.tools:hibernatetools-gradle-plugin:1.0.0"
    }
}
apply plugin: "hibernatetools-gradle-plugin"
```

Now you have only to configure the database, by using database section inside build.gradle file.
Example:

```
database{
    name        = "myDB"
    url         = "jdbc:mysql://myDB.domain.fr"
}
```

This plugin allow to specify multiple catalog using:


```
import org.hibernate.gradle.tools.*

database{
    catalog = [ "catalog1": new Schema("schemaY", ".*")), "catalog2": new Schema("schemaX", "*") ]
    basePackage = "org.foo.bar"
}
```

This plugin allow to specify multiple schema using:

```
database{
    catalog = [ "catalog1": new Schema("schemaX", ["tableY"]), "catalog2": new Schema("schemaY", ["".*"]) ]
    basePackage = "org.foo.bar"
}

```

This plugin allow to specify multiple tables using:

```
database{
    catalog = [ "catalog1": new Schema("schemaX", ["tableY","tableZ"]), "catalog2": new Schema("schemaY", [".*"]) ]
    basePackage = "org.foo.bar"
}

```

The symbol * means any characters. In given example class will be mapped from foo and bar  tables and also any table where name start with other



by default database section are defined as:

```
class Database {
    def catalog         = [".*":new Schema(".*", ".*")] // Schema parameter are: schema pattern name, table pattern name
    String  user        = ""
    String  password    = ""
    String  url         = "jdbc:mysql://127.0.0.1"
    Integer port        = 3306
    String  driver      = "com.mysql.jdbc.Driver"
    String  dialect     = "org.hibernate.dialect.MySQLDialect"
    String  basePackage = "com.project.database.model"
}
```


dialect and driver to use are located on [hibernate](http://www.tutorialspoint.com/hibernate/hibernate_configuration.htm])

### Reverse engineering

Once the plugin is declared and database connection defined to get java classes you need only to run this command:

```
$ gradle hbm2dao
```

This plugin provides three gradle target:
- hbm2java
- hbm2dao
- hibernate-config

enjoy
