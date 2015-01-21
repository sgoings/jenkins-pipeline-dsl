package com.readytalk.jenkins.dsl

import com.google.inject.AbstractModule

import com.readytalk.jenkins.jobs.components.*
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.JobManagement

import com.google.inject.Provides;

public class ReadyTalkModule extends AbstractModule {

  JobManagement jobManagement

  @Override
  protected void configure() {

  }

  @Provides
  JobManagement provideJobManagement() {
    return jobManagement;
  }

  @Provides
  Publisher provideArtifactory() {
  	return artifactory
  }

}