package com.readytalk.jenkins.dsl

import javaposse.jobdsl.dsl.DslFactory

import com.readytalk.jenkins.jobs.components.GradleArtifactory

@Category(DslFactory)
class PipelineDsl {

  static Map<Class, Object> defaults = [:]

  void defaults(Class type, Closure closure) {
    object = type.newInstance()
    object.with(closure)
    defaults[type] = object
  }

  void create(Class type, Closure closure) {
    //TODO: Handle this dependency (jm) via DI (so we can just do injector.get(Some.class)
    // and have components auto cloned + added
    def jobGroup = type.newInstance(jm, defaults[GradleArtifactory])

    jobGroup.with(closure)

    referencedJobs.addAll(jobGroup.generate())
  }
}