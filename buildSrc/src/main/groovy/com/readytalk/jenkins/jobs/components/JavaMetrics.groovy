package com.readytalk.jenkins.jobs.components

trait JavaMetrics {

  boolean metrics = true

  void apply() {
    if(metrics) {
      publishers {
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