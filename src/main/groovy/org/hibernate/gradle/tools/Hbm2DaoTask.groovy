package org.hibernate.gradle.tools

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

/*
 * Copyright LABGeM 20/01/15
 *
 * author: Jonathan MERCIER
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

class Hbm2DaoTask  extends DefaultTask{
    def Config  config
    def boolean enabled     = true
    def String  description = "Generate DAO classes"
    def String  group       = "hibernatetools"

    @TaskAction
    def run(){
        Task compileJava = project.getTasksByName('compileJava',false).iterator().next()
        compileJava.dependsOn('hbm2dao')
        hbm2dao(project)
    }

    def hbm2dao(final Project project){
        //reversestrategy must not be provided if it is null
        def preparedJdbcConfiguration = [
            configurationfile:  "${config.hibernateConfigXml.path}",
            revengfile:         "${config.hibernateRevEngXml.path}",
            packagename:        "${project.database.basePackage}"
        ]
        if (project.database.reverseStrategyClass != null) {
            preparedJdbcConfiguration.reversestrategy = "${project.database.reverseStrategyClass}"
        }
        
        project.ant {
            taskdef(name: "hibernatetool",
                    classname: "org.hibernate.tool.ant.HibernateToolTask",
                    classpath: config.classPath
            )
            hibernatetool( destdir : config.javaSrcGeneratedDir ) {
                jdbcconfiguration(preparedJdbcConfiguration)
                hbm2dao(
                        jdk5: true,
                        ejb3: true
                )
                classpath {
                    pathelement( path: "config" )
                }
            }
        }

    }
}
