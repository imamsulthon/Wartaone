# Wartaone
Simple News Headlines apps from The News API data source (https://www.thenewsapi.com/documentation)

Developer: Imam Sulthon (imamsulthon@gmail.com)

This Android apps was developed in order for technical test submission for Android Developer job at PT Visi Prima Nusantara
<img src="/screenshot_1.jpg" width="150">
<img src="/screenshot_2.jpg" width="150">
<img src="/screenshot_3.jpg" width="150">
<img src="/screenshot_4.jpg" width="150">

Libraries:
- [Hilt](https://dagger.dev/hilt/) - Hilt provides a standard way to incorporate Dagger dependency injection into an Android application.
- [Retrofit](https://square.github.io/retrofit/) - Type-safe http client
  and supports coroutines out of the box.
- [GSON](https://github.com/square/gson) - JSON Parser,used to parse
  requests on the data layer for Entities and understands Kotlin non-nullable
  and default parameters.
- [OkHttp Logging Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md)
  Logs HTTP request and response data.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines.
- [Flows](https://developer.android.com/kotlin/flow)
  Flows are built on top of coroutines and can provide multiple values. A flow is conceptually a stream of data that can be computed asynchronously.
- [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) The Paging library helps you load and display pages of data from a larger dataset from local storage or over network.
- This approach allows your app to use both network bandwidth and system resources more efficiently
- [Glide](https://github.com/bumptech/glide) is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.
- [CircleIndicator](https://github.com/ongakuer/CircleIndicator) A lightweight indicator like in nexus 5 launcher
- [Mockk](https://mockk.io) Provides DSL to mock behavior. Built from zero to fit Kotlin language. Supports named parameters, object mocks, coroutines and extension function mocking.

- This App contains various pages that follow the design requirement with one addition Main Page
- Main Page Activity: This page contains button to main feature Home Page also as configuration that apps should use remote or local stub data
- Main Page Activity with 2 Optional buttons:
  * Go To Home Page (XML) [enabled]-> Home Page, The whole features requirement developed under XML layout 
  * Go To Home Page (Compose [disabled] -> Home Page and its navigation that designed under Jetpack Compose (not yet developed and not included in requirement),
- Home Page. Main page of the whole feature requirements developed with XML layout (mandatory). Contains three section; Headline News, Top News, All News (limited items)
- Detail Page. The detail news page showing details of the news such as date created, snippet, desc, real webview url, etc
- News List Page. Contains all news (latest/top news updated from API) with infinite scrollable/pagination
- Home Page (Compose). Under developed, this is not what requirement ask but I prefer to developed this in next future. 

Also, this project has 4 modules: app, core, domain, data

Noted to Reviewers:
This apps may not in fancy looks UI, my focus while developing this for test purpose is to design domain, data layer as clean as possible following modular and SOLID Principle.

You can check out my another mini project in recent 3 months 
- https://github.com/imamsulthon/Boardminton (Badminton Score Board, using Jetpack compose)
- https://github.com/imamsulthon/Animalia (Simple pagination apps)
- https://github.com/imamsulthon/OpenCurrency (Simple currency converter apps)
- https://github.com/imamsulthon/SimpleForm (simple registration form)