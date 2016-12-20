# Hibernatetools-gradle-plugins

## Purpose

This plugin intend to ease hibernatetools through gradle. It offer three tasks:
- hibernate-config: to generate hibernate config files
- hbm2java: to generate java classes
- hbm2dao: to generate DAO classes

## Usage

Inside your build.gradle file add this:

### Build script snippet

#### For Gradle 2.1 and after:
```groovy
plugins {
      id "org.hibernate.gradle.tools" version "1.2.4"
    }
```
#### For older Gradle release:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.org.hibernate.gradle.tools:hibernatetools-gradle-plugin:1.2.4"
  }
}

apply plugin: "org.hibernate.gradle.tools"
```


#### Configure the database

Now you have only to configure the database, by using database section inside build.gradle file.
Example:

```groovy
database{
    basePackage = "com.project.database.model"
    url         = "jdbc:mysql://myDB.domain.fr"
}
```

This plugin allow to specify multiple catalog using:


```groovy
import org.hibernate.gradle.tools.*

database{
    catalog = [ "catalog1": new Schema("schemaY", [".*"])), "catalog2": new Schema("schemaX", [".*"]) ]
    basePackage = "org.foo.bar"
}
```

This plugin allow to specify multiple schema using:

```groovy
database{
    catalog = [ "catalog1": new Schema("schemaX", ["tableY"]), "catalog2": new Schema("schemaY", [".*"]) ]
    basePackage = "org.foo.bar"
}

```

This plugin allow to specify multiple tables using:

```groovy
database{
    catalog = [ "catalog1": new Schema("schemaX", ["tableY","tableZ"]), "catalog2": new Schema("schemaY", [".*"]) ]
    basePackage = "org.foo.bar"
}

```

The symbol '.*' means any characters repeated 0 on n times. In given example class will be mapped from foo and bar  tables and also any table where name start with other


You want to provides your reveng and config xml files :


```groovy
database{
    configXml   = "/tmp/hibernate.cfg.xml"    // <<---- path to config file
    revEngXml   = "/tmp/hibernate.reveng.xml" // <<---- path to reveng file
}
```

by default database section are defined as:

```groovy
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

```bash
$ gradle hbm2dao
```

This plugin provides three gradle target:
- hbm2java
- hbm2dao
- hibernate-config

enjoy
