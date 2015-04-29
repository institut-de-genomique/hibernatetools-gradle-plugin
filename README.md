# Hibernatetools-gradle-plugins

## Purpose

This plugin intend to ease hibernatetools through gradle. It offer three tasks:
- hibernate-config: to generate hibernate config files
- hbm2java: to generate DAO classes
- hbm2dao: to generate java classes

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
        classpath   "org.hibernate.gradle.tools:hibernatetools-gradle-plugin:0.1.0"
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

```
database{
    name        = "myDB"
    basePackage = "org.foo.bar"
    schema      = "myDB"
}
```

This plugin allow to specify multiple tables using comma as separator:

```
database{
    name        = "myDB"
    basePackage = "org.foo.bar"
    tables      = "foo,bar,other*"
}

```

The symbol * means any characters. In given example class will be mapped from foo and bar  tables and also any table where name start with other



by default database section are defined as:

```
database {
    name        = ""
    tables      = ".*"
    schema      = ".*"
    user        = ""
    password    = ""
    url         = "jdbc:mysql://127.0.0.1"
    port        = 3306
    basePackage = "com.project.database"
    driver      = "com.mysql.jdbc.Driver"
    dialect     = "org.hibernate.dialect.MySQLDialect"
}
```

dialect and driver to use are located on [hibernate](http://www.tutorialspoint.com/hibernate/hibernate_configuration.htm])

### Reverse engineering

Once the plugin is declared and database connection defined to get java classes you need only to run this command:

```
$ gradle hbm2java
```

This plugin provides three gradle target:
- hbm2java
- hbm2dao
- hibernate-config

enjoy
