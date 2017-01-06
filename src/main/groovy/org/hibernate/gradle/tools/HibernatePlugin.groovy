package org.hibernate.gradle.tools
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
/*
 * Copyright LABGeM 15/01/15
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

class HibernatePlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.plugins.apply JavaPlugin
        project.extensions.create( 'database', Database)
        project.repositories.mavenLocal()
        project.repositories.mavenCentral()
        project.afterEvaluate {
            def configuration = project.configurations.maybeCreate('reveng')
            configuration.dependencies.add(project.dependencies.create('org.hibernate:hibernate-tools:5.2.0.Final'))
            configuration.dependencies.add(project.dependencies.create('org.slf4j:slf4j-simple:1.7.5'))
            configuration.extendsFrom(project.configurations.findByName('compile'))
            configuration.extendsFrom(project.configurations.findByName('runtime'))
            def conf = new Config(
                                    new File( "${project.buildDir}${File.separator}generated${File.separator}src${File.separator}" ),
                                    configuration.asPath
                                 )
            project.task("hibernate-config", type: HibernateConfigTask ) {
                    config = conf
                    inputs.files conf.hibernateRevEngXml
                    outputs.dir  conf.srcGeneratedDir
            }
            project.task("hbm2java", type: Hbm2JavaTask,dependsOn: "hibernate-config" ) {
                config = conf
                inputs.files conf.hibernateRevEngXml
                outputs.dir  conf.srcGeneratedDir
            }
            project.task("hbm2dao", type: Hbm2DaoTask,dependsOn:"hbm2java"  ) {
                config = conf
                inputs.files conf.hibernateRevEngXml
                outputs.dir  conf.srcGeneratedDir
            }
            addGeneratedToSource(project)
        }
    }

    void addGeneratedToSource(Project project) {

        project.sourceSets.matching { it.name == "main" } .all {
            it.java.srcDir "${project.buildDir}${File.separator}generated${File.separator}src${File.separator}java"
        }
    }
}


class Config{
    def File   hibernateRevEngXml
    def File   hibernateConfigXml
    def File   srcGeneratedDir
    def File   javaSrcGeneratedDir
    def File   resourcesSrcGeneratedDir
    def String classPath

    public Config(final File srcGeneratedDir, final String classPath ){
        this.srcGeneratedDir            = srcGeneratedDir
        this.javaSrcGeneratedDir        = new File(srcGeneratedDir,"java")
        this.resourcesSrcGeneratedDir   = new File(srcGeneratedDir,"resources")
        this.hibernateRevEngXml         = new File(resourcesSrcGeneratedDir,"hibernate.reveng.xml")
        this.hibernateConfigXml         = new File(resourcesSrcGeneratedDir,"hibernate.cfg.xml")
        this.classPath                  = classPath
    }
}
