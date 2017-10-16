# Vert.x & Kubernetes - From zero to (micro-) hero.

This repository is a lab about vert.x explaining how to build distributed _microservice_ reactive applications using
Eclipse Vert.x and deploy them on Kubernetes

Instructions are available on http://escoffier.me/vertx-kubernetes

Complete code is available in the `solution` directory.

## Teasing

When coming to microservices, event-driven, asynchronous and reactive are quickly mentioned to implement them right. It avoids building _distributed_ monolith. In addition, in order to keep everything on track, you need a way to package and manage them. OpenShift is a container platform, based on Kubernetes, able to build, deploy, manage and update your microservices.  

Eclipse Vert.x is a toolkit to create reactive distributed applications running on the top of the Java Virtual Machine. Vert.x exhibits very good performances, and a very simple and small API based on the asynchronous, non-blocking development model.  With vert.x, you can developed microservices in Java, but also in JavaScript, Groovy, Ruby and Ceylon. Vert.x also lets you interact with Node.JS, .NET or C applications. Vert.x is a container-native runtime taking care of the efficient usage of your CPU and memory granted to your container. 
  
This lab is an introduction to microservice development using Vert.x and OpenShift. The application is a fake _trading_ application, and maybe you are going to become (virtually) rich! The applications is a federation of interacting microservices running on OpenShift.

## Want to improve this lab ?

Forks and PRs are definitely welcome !

## Building

To build the code:

    mvn clean install

To build the documentation:

    cd docs
    docker run -it -v `pwd`:/documents/ asciidoctor/docker-asciidoctor "./build.sh" "html"
    # or for fish
    docker run -it -v (pwd):/documents/ asciidoctor/docker-asciidoctor "./build.sh" "html"
