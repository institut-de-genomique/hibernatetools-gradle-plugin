/*
 * Copyright LABGeM 13/01/15
 *
 * author: Jonathan MERCIER
 *
 *
 * This software is a computer program whose purpose is to annotate a complete genome.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package org.hibernate.gradle.tools

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class HibernateConfigTask extends DefaultTask {
    def boolean enabled     = true
    def String  description = "Generate hibernate config files"
    def String  group       = "hibernatetools"
    def Config config



    @TaskAction
    def run(){
            config.resourcesSrcGeneratedDir.exists()    || config.resourcesSrcGeneratedDir.mkdirs()
            config.srcGeneratedDir.exists() || config.srcGeneratedDir.mkdirs()
            if( ! config.hibernateRevEngXml.exists() ){
                checkDataBase( project )
                writeRevengConfigFile(project)
            }
            config.hibernateConfigXml.exists() || writeHibernateConfigFile(project)
    }

    def checkDataBase(Project project){
        def console = System.console()
        console.writer().println()
        console.writer().println("== User definition ==")
        if( project.database.user == "" )
            project.database.user = console.readLine('> Please enter your username: ')
        if( project.database.password == "" )
            project.database.password = new String(console.readPassword('> Please enter your password: '))
        console.writer().println("========")
    }

    def writeHibernateConfigFile(Project project){
        config.hibernateConfigXml.append(
"""<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration 
    SYSTEM "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">${project.database.dialect}</property>
        <property name="hibernate.connection.driver_class">${project.database.driver}</property>
        <property name="hibernate.connection.url">${project.database.url}:${project.database.port}</property>
        <property name="hibernate.connection.username">${project.database.user}</property>
        <property name="hibernate.connection.password">${project.database.password}</property>
        <property name="hibernate.current_session_context_class">thread</property>
        <property name="hibernate.connection.zeroDateTimeBehavior">convertToNull</property>
    </session-factory>
</hibernate-configuration>"""
        )

    }

    def writeRevengConfigFile(final Project project){

        config.hibernateRevEngXml.append(
"""<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-reverse-engineering
    SYSTEM "http://hibernate.sourceforge.net/hibernate-reverse-engineering-3.0.dtd">

<hibernate-reverse-engineering>
"""
        )
        project.database.catalog.each{ catalogName, schema ->
            schema.tables.each{ tableName ->
                config.hibernateRevEngXml.append(
"""
    <schema-selection match-catalog ="${catalogName}" match-schema="${schema.name}" match-table="${tableName}"/>
"""
                )
            }
            config.hibernateRevEngXml.append(
"""
    <table-filter match-catalog ="${catalogName}" package="${project.basePackage}.${catalogName}" />
"""
            )
        }
        config.hibernateRevEngXml.append(
"""
</hibernate-reverse-engineering>
"""
            )

    }

}
