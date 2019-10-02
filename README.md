# 피보탈 뱅크 데모 앱 

본 데모는 스프링 트레이더(Trader)앱의 마이크로서비스 버전입니다. 마이크로서비스들의 클라우드 기반 동작을 지원하기 위해 Config Server, Service Discovery, Circuit breaker dashboard의 스프링 클라우드 서비스들이 사용되었습니다. 

본 데모 앱은 https://run.pivotal.io 에(Pivotal Web Services) 계정을 생성하신 분이라면 누구든 scripts/ 디렉토리의 스크립트를 이용하여 쉽게 배포할 수 있습니다. 계정 생성은 무료이며, 약 10GB의 메모리를 사용하여 애플리케이션을 컨테이너로 배포할 수 있으며, MySQL, ElasticSearch, RabbitMQ 등 다양한 서비스를 함께 배포하여 애플리케이션에 바인딩할 수 있도록 제공되고 있습니다. 

또한 이 데모는 피보탈 한국의 시리즈 밋업에 사용되고 있습니다. 밋업의 시리즈는 현재 아래와 같은 주제로 진행되고 있습니다. 

1. 왜 마이크로서비스인가 
2. 도메인 모델에 따른 데이터의 분리 저장과 API 연결 
3. Spring Boot를 사용한 마이크로서비스 개발과 페어프로그래밍 (및 XP) 
4. Spring Cloud를 사용한 마이크로서비스 연결 
5. 개발된 코드를 변경없이 로컬에서 클라우드까지 - Pivotal Application Service를 사용한 스프링 애플리케이션과 데이터베이스 배포 
6. 멀티 클라우드 및 멀티 리전에서의 가용성 확보를 위한 데이터 복제 전략 
7. 카오스 엔지니어링 적용을 통한 서비스 가용성 테스트 
8. API 게이트웨이의 사용 
9. 주가 예측 서비스 제공을 위한 데이터 과학의 적용 


## 애플리케이션의 PWS 배포

애플리케이션의 배포뿐만 아니라 다양한 내용의 학습을 위해 본 README 하단의 [Workshops](#workshops) 부분을 참고 하실 수 있겠습니다. PWS 계정이 필요하며, CLI에서 cf 도구가 필요합니다. 이 도구는 PWS에서 계정을 생성한 후 Tools 메뉴를 통해 다운로드 받을 수 있습니다. 

scripts 디렉토리에는 환경의 준비와 생성을 위해 다음의 스크립트들이 준비되어 있습니다. 

1_createServices.sh 는 애플리케이션에 필요한 스프링 클라우드 서비스(SCS)들과 MySQL 을 사용하여 Traderdb를 생성합니다. 
2_build.sh 는 각 마이크로서비스들을 빌드합니다. 기본적으로 JDK 1.8 환경이 준비 되어 있어야 합니다. 
3_deploy.sh 는 빌드된 마이크로서비스들을 배포합니다. 

배포가 완료되면 web 이라는 애플리케이션의 url 탭을 참고하여 배포된 애플리케이션에 접근할 수 있습니다.
애플리케이션이나 배포가 동작하지 않는 경우 아래의 내용을 참고해 주시면 되겠습니다. 

* SCS 서비스의 무료 플랜은 사용기간에 제한이 있습니다. 기간이 만료된 경우 생성된 서비스를 제거하고 신규로 생성하면 다시 사용할 수 있습니다. 
* createService.sh 에서는 서비스 생성에 p-mysql 의 100mb 를 사용하고 있습니다.  cf marketplace 커맨드를 통해 p-mysql 이 보이지 않는 경우라면 이를 cleardb 로, 100mb 부분은 spark 로 변경해 주시면 되겠습니다. 이는 PCFServices.list 파일에서도 마찬가지 입니다. 

모든 테스트가 종료되어 환경을 삭제하고 싶은 경우에는 아래의 순서로 스크립트를 실행하면 됩니다. 

``deleteListedApps.sh`` 
``deleteListedServices.sh``  

터미널에서 아래의 명령을 통해 동작하는 앱과 서비스가 있는지 확인할 수 있습니다. 

``$ cf apps`` 
``$ cf services``  

이 문서의 아래에는 애플리케이션의 아키텍처 다이어그램과 다양한 부연 설명이 포함되어 있으므로 본 서비스에 대해 더 자세한 이해가 필요한 경우에는 참고를 권합니다. 



# Pivotal Bank Demo App

This demo app is a microservice version of the Spring Trader application.  It demonstrates
use of Spring Cloud Services in a reasonably complex set of microservices.  

>This repository was logically forked from the original [Pivotal-Bank](https://github.com/pivotal-bank) and collapsed
into a mono-repo for ease of rapid development by single demo-er.  Additional changes have been 
made to the repo that further strayed from the original to address demo needs.  In time, these changes
will be considered for inclusion in the origin pivotal-bank source.

![Spring Trader](/docs/springtrader2.png)

# Introduction

This repository holds a collection of micro services that work together to present a trading application surfaced though a web UI, but more interfaces can be created that re-utilise the microservices.

It was created to support workshops and demonstrations of building and using `microservices` architectures and running these in **Cloud Foundry** (although it is possible to run these on other runtimes).

The workshops follow a series of exercises, or labs, and you can find links to the guides for these exercises [below](#workshops).

## Table of Contents

1. [Architecture](#architecture)
2. [Deploying the application](#deployment)
3. [Workshops](#workshops)
4. [Demos](#demos)
5. [Roadmap](#roadmap)
6. [Contributing to the project](#contributing)


# Architecture
The system is composed of 5 microservices. The relationship between the microservices is illustrated below.

![architecture](/docs/base-architecture-diagram.png)

## 1. Quote Microservice
This service is a spring boot application responsible for providing up to date company and ticker/quote information. It does this by providing a REST api with 2 calls:
* ``/v1/quotes?q={symbol1,symbol2,etc}``
Returns as up to date quote for the given symbol(s).
* ``/v1/company/{search}``
Returns a list of companies that have the search parameter in their names or symbols.

This application has no dependencies apart from an external service - [markitondemand](http://dev.markitondemand.com/) - to retrieve the real time data.

## 2. Account Microservice
This service is a spring boot application responsible for creating and managing user accounts.

It stores the accounts in a RDBMS store and uses a spring JPA respository to accomplish this. It provides several REST api calls for other services to consume its services.

## 3. Portfolio Microservice
This service is a spring boot application responsible for managing portfolios - these are collections of holdings, which in turn are collection of orders on a particular share.

This service accepts orders (both BUY and SELL) and stores these in a RDBMS store - *it does not have to be the same RDBMS as the Account service, but it can be!* It provides REST api calls for other services to consume its services.

This service is dependent on the Account service above to ensure the logged in user has enough funds to buy stock as well as keeping the account funds up to date. It is also dependent on the Quote service to retrieve up to date quote information and calculate the current value of portfolios.

## 4. Web Microservice
This service is a spring boot application providing the web interface.

The web interface is built using bootstrap and Thymeleaf and uses a Spring controller to delegate calls to the relevant services:
* Account service
* Quote service
* Portfolio service

## 5. User Microservice
This service is a spring boot application providing the user repository and authorization services.

It stores the accounts in a RDBMS store and uses a spring JPA respository to accomplish this. It provides several REST api calls for other services to consume its services. 

# Deployment

To deploy the microservices manually please follow the guides of the [workshop below](#workshops)

Or if you want to quickly deploy all services to Pivotal Cloud Foundry with [Spring Cloud Services for PCF](https://network.pivotal.io/products/p-spring-cloud-services) follow the [scripted instructions](scripts/README.md)

Alternative, you can run most of the functionality locally, by following the [local instructions](docs/lab_local.md)

# Workshops:

The following guides describe how to setup the environment and deploy the microservices to **Cloud Foundry**.

At Pivotal we love education, not just educating ourselves, but also educating others. As such, these guides follow the *"teaching you how to fish"* principle - Rather than giving you line by line/command by command instructions, we provide guidelines and links to documentation where you can read and learn more.

1. [Setting up the environment](docs/lab_setup.md)
2. [Creating a discovery service](docs/lab_registryserver.md)
3. [Creating a circuit breaker dashboard](docs/lab_circuitbreaker.md)
4. [Creating the configuration service](docs/lab_configserver.md)
5. [Pushing the Quote service](docs/lab_pushquote.md)
6. [Pushing all the services](docs/lab_pushall.md)
7. [Scaling the services](docs/lab_scale.md)
8. [Blue/Green deployments](docs/lab_bluegreen.md)

# User Acceptance Test
In order to get familiar with Pivotal Bank, follow the [user acceptance test](docs/lab_manual_test.md) to go through basic functionality.

# Bonus Workshops

You can go further with the following bonus workshops.

1. [Zipkin Tracing](docs/lab_zipkin.md)
2. [Spring Cloud Gateway](docs/lab_spring_cloud_gateway.md)
3. [Container to Container Networking](docs/lab_c2c_networking.md)
4. [Add Elasticsearch and Spring Cloud Data Flow](docs/lab_pivotal_bank_analytics.md)

# Features

- **Discovery service:**
  All microservices register with the [Discovery Service](http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html) and discover other microservices through it.
- **Correlation/Traceability:**
  Traceability of requests through all the microservices. This is done using [Spring-cloud-sleuth](http://cloud.spring.io/spring-cloud-sleuth/).
- **Config Server:**
  The microservices obtain the configuration from a [Configuration Service](http://cloud.spring.io/spring-cloud-config/) backed by a git repository. This means that configuration is now auditables and version controlled, as well as providing the ability to refresh configuration during runtime.

# Roadmap

The roadmap for this project is constantly evolving. Please feel free to reach out with ideas.
- **Better APIs:**
  Better APIs with documentation that conform to some standard and logic.
- **Security:**
  Secure microservices with OAUTH2.
- **Monitoring/Operations:**
  Show how to monitor a distributed system comprising of multiple microservices.  Zipkin or [PCF Metrics](http://docs.pivotal.io/pcf-metrics)

# Contributing
Everyone is encouraged to help improved this project.

The master branch has the latest release.

Here are some ways you can contribute:

- by reporting bugs
- by suggesting new features
- by writing or editing documentation
- by writing code (no patch is too small: fix typos, add comments, clean up inconsistent whitespace)
- by refactoring code
- by closing [issues](https://github.com/mid-atlantic-pa/pivotal-bank/issues)

## Submitting an Issue

We use the [GitHub issue tracker](https://github.com/mid-atlantic-pa/pivotal-bank/issues) to track bugs and features. Before submitting a bug report or feature request, check to make sure it hasn't already been submitted. When submitting a bug report, please include any relevant information. Ideally, a bug report should include a pull request with failing specs, and maybe even a fix!

## Submitting a Pull Request

1. Fork the project.
2. Create a topic branch.
3. Implement your feature or bug fix.
4. Commit and push your changes.
5. Submit a pull request.
