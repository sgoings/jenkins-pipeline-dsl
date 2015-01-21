package com.readytalk.jenkins.jobs.components

import groovy.transform.AutoClone

import javaposse.jobdsl.dsl.Job

@AutoClone
class JavaMetrics implements JobComponent {

  boolean metrics = true

  void apply(Object job) {
    if(metrics) {
      job.publishers {
        checkstyle('**/reports/checkstyle/main.xml')
        findbugs('**/reports/findbugs/main.xml', true)
        warnings(['Java Compiler (javac)'])
        archiveJunit('**/test-results/**/*.xml')
        jacocoCodeCoverage {
          execPattern "**/**.exec"
          classPattern "**/classes/main"
        }
      }
    }
  }

}