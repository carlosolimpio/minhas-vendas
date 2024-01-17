# Minhas Vendas App

Minhas Vendas app show a list with the orders that you registered in your device. It allows you to create new orders and have a summary of how much did you earn from them.

## Architecture

The architecture of the app follows the best practices and recommended architecture for modern app development from the [android developer guide](https://developer.android.com/topic/architecture).

The overall architecture looks as shown on the image below:

<img src="https://developer.android.com/topic/libraries/architecture/images/mad-arch-overview.png" width="360" height="240">

The app has three main well-defined layers, implementing the [MVVM architecture pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel), is separated into packages where each package contains a child feature package with each feature's components. In this case, the architecture has only one main package `order` and the `presentation`package contains `home` and `orderdetails` packages for each screen.

- **Presentation**:
    - The Presentation layer, also known as UI layer, is responsible for displaying the application data on the screen and also serve as the primary entry point for user interaction.
    - In this layer the app has the `Activity`, `Fragments` and its `ViewModels` and `Adapter` for the list used.
    - It also contains the `di` and `extensions` packages used in above components.
- **Data**:
    - The Data layer implements a local database used to store the orders.
    - Inside the `data` layer the app has the implementation of the domain's repository contract to store the data. The app uses a `Entity` types of classes to convert between local and domain models in order for the domain models to not have any dependency to the remote implementation selected.
- **Domain**:
    - The domain Layer contains some core classes that are used on the App.
    - There is the `Resource` used to pass the data response from data layer to presentation layer whereas Success is used after the execution returned successfully and Error is used when an error occurred during execution.
    - Each feature has a package following the app architecture with its model and its repository's contract.
    - The `core`package contains some core classes used across the application.

Overall this architecture is very robust, follows the official android developer guideline and makes it easy to maintain and scale the application.

## Unit Tests

For unit testing there is a unit test for each screen's view model (`HomeViewModelTest` and `OrderDetailsViewModelTest`) which is where the business logic of the application is.

## Instruction to build the project

This is a standard Android application. To run the project you will need to follow the steps:
- Import the project into Android Studio
- Build and Run the project 

## Demo

[Watch the demo video on Google Drive](https://drive.google.com/file/d/16WlTbCjD23KzQl47y6b4Yxom_dZ8qPP9/view?usp=sharing)

<img src="https://github.com/carlosolimpio/minhas-vendas/assets/11680359/2727bd25-4ea7-4ce3-969d-9303fbb74ea5" alt="drawing" width="250"/>
<img src="https://github.com/carlosolimpio/minhas-vendas/assets/11680359/485e27ab-e054-4bea-b59c-131c2fa4e7cc" alt="drawing" width="250"/>
<img src="https://github.com/carlosolimpio/minhas-vendas/assets/11680359/5048faa2-3391-4603-b563-55e6783a97fa" alt="drawing" width="250"/>



## Libraries used

The project was developed using some of the Android Jetpack Libraries and some third-party libraries as well.

### Android Jetpack Libraries
- View Binding: feature that allows to write code that interacts with views, replacing the `findViewById`. For more details see the [official docs](https://developer.android.com/topic/libraries/view-binding).
- Navigation: navigation is a framework for navigating between 'destinations' within an Android application that provides a consistent API. Check out [its guide](https://developer.android.com/guide/navigation). 
- Safe-Args Plugin: the recommended way to navigate between destinations is to use the Safe Args Gradle plugin. This plugin generates object and builder classes that enable type-safe navigation between destinations. [Docs](https://developer.android.com/guide/navigation/use-graph/safe-args)
- Room: the persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. For more details see the [official docs](https://developer.android.com/jetpack/androidx/releases/room)

### Kotlin library:
- Kotlin coroutines: used to manage long-running tasks that might otherwise block the main thread. For more information see [The Kotlin coroutines official doc for Android](https://developer.android.com/kotlin/coroutines)
- Kotlin serialization: used to serialize data to be stored into the database. For more information check [the official docs](https://kotlinlang.org/docs/serialization.html).

### Dependency Injection
- Koin: Koin is a smart Kotlin dependency injection library that provides a simple API. [Official website](https://insert-koin.io/).

### Testing libraries:
- MockK: a mocking library for Kotlin. [Official website](https://mockk.io/)
