package com.readytalk.jenkins.dsl

import javaposse.jobdsl.dsl.DslFactory

import com.readytalk.jenkins.jobs.components.GradleArtifactory

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.AbstractModule

import com.readytalk.jenkins.jobs.components.JobComponent

@Category(DslFactory)
class PipelineDsl {

  static AbstractModule module = new ReadyTalkModule()
  static Injector injector = Guice.createInjector(module)

  void defaults(Closure closure) {
  	module.with(closure)
  }

  void create(Class type, Closure closure) {
  	module.jobManagement = jm

    def jobGroup = injector.getInstance(type)

    jobGroup.with(closure)

    referencedJobs.addAll(jobGroup.generate())
  }
}