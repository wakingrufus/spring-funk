---
layout: default
title: Introduction
nav_order: 2
has_children: false
---
# Introduction

This project is an attempt to build on the concepts pioneered by Sébastien Deleuze in [spring-fu](https://github.com/spring-projects-experimental/spring-fu), but with a slightly different focus.

### What is Spring-Fu?

Spring Fu is a declarative way of configuring spring boot applications. Instead of using XML, or AOP (annotations), it uses [Kotlin Type-safe builder](https://kotlinlang.org/docs/type-safe-builders.html) patterns to deliver a developer experience akin to [Ktor](https://ktor.io/). There are 3 main advantages to this approach:

1. Performance: Because it does not use reflection, it has a much faster start time than an application using AutoConfiguration.
2. GraalVM compatibility:  Because it does not use reflection,it can run out-of-the-box on GraalVM (which gives even more startup performance gains).
3. Developer ergonomics: The functional DSL style lends itself much better to traditional IDE autocomplete, making applications easier to reason about than the traditional AOP/annotation approach, which is losing popularity sue to functional programming becoming more popular.

The major drawback to Spring-Fu, is that it is an "all-in" proposition. An application must be 100% using the framework, and if it does, it can't use any existing AutoConfiguration-based libraries.

The Spring team decided to focus on GraalVM compatibility for Spring Boot 3, and forcing a rewrite of every Spring Boot application was not a good path to get there, so the team pivoted and accomplished that goal in another way. This left Spring Fu on hold.

Some of the concepts from Spring-Fu have made their way into core Spring, such as beansDsl and routerDsl, and they can be optionally used today. But there is not a seamless way to integrate these into an existing application, and most of the rest of Spring-Fu is still not available in Spring Core.

### Goals of Spring FunK

This project aims to deliver an API for accessing the existing beansDsl and routerDsl for any Spring Boot application.

It will also provide some additional DSLs that don't exist in core Spring. These will mostly be replacements for existing core Spring Boot AutoConfiguration classes.

Finally, it will also be designed for extension. This should make it ideal for platform engineers who are building internal runtime frameworks on top of Spring Boot, and want to deliver the DX/ergonomics that come with it to their teams as well.

Spring FunK will work with AutoConfigured Spring boot applications, but if your application is not using AOP at all (AutoConfiguration, Component Scanning, Spring annotations), it will also work with these features disabled in Spring, which will allow you to unlock very fast start-up times, similar to Spring-Fu.

See [Getting Started](getting_started.md) to start.