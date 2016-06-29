#CoolCal

| production                                                                                                                                                     | master                                                                                                                                                    |                                                     
|----------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![Build Status](https://www.bitrise.io/app/f8461c8e446b8de1.svg?token=Ydp21H8UswECEztLf4Zn9A&branch=production)](https://www.bitrise.io/app/f8461c8e446b8de1) | [![Build Status](https://www.bitrise.io/app/f8461c8e446b8de1.svg?token=Ydp21H8UswECEztLf4Zn9A&branch=master)](https://www.bitrise.io/app/f8461c8e446b8de1) |

Welcome to the **CoolCal Android** project, simple weather/calendar application.

## Building

### Product flavors
The following flavors are used for building the project:
- **staging**: used for the development cycle with workflows: _master_.
- **production**: used for deployment, built with _production_ workflow.

### Build types 
- **debug** 
- **release** 

Build types currently do not differ.

### Build properties
Each build requires defining a set of properties that are injected into the application code through BuildConfig generation. The properties are:

- HockeyAppAppIdStaging
- HockeyAppAppIdProduction
- OpenWeatherMap Api Key

Instructions for providing properties:

#### Local builds
Define properties in `gradle.properties`. Paste the following code and insert values, **without quotation marks**: 
```
HockeyAppAppIdStaging=
HockeyAppAppIdProduction=
OpenWeatherMapApiKey=
```

#### Bitrise builds
Define **secret environment variables** for your app on Bitrise. Variables should be named:
```
HOCKEYAPP_APP_ID_STAGING
HOCKEYAPP_APP_ID_PRODUCTION
OPENWEATHERMAP_API_KEY
KEYSTORE_PASSWORD
KEYSTORE_ALIAS
KEYSTORE_PRIVATE_PASSWORD
```
### ProGuard
Configured.

## Deployment
We use Bitrise for CI and HockeyApp for APK deployment.

### Bitrise workflows:
- **Feature** - used with feature branches.
- **Production** - triggered by pushing to `production` branch, deploys to HockeyApp CoolCal production, used for production.
- **Master** - triggered by pushing to `master` branch, deploys to HockeyApp CoolCal master, used for development cycle.

## Development

### Integrations
#### HockeyApp
HockeyApp integration serves as deployment platform. The api key for different
build flavors MUST be different. HockeyApp's crash manager SHOULD only be initialized for non-debug
builds.

### Supported devices
- minSdkVersion: 19
- targetSdkVersion: 23

### Coding guidelines
- The code should be readable and self-explanatory according to Netguru guidelines - full variable names, meaningful methods, etc.
- Document public interfaces
- Please leave no commented-out code.

### Architecture
The project is based on Dagger 2. Activities represent basic business processes in the app and Fragments correspond with views. API calls to open weather backend are based on Retrofit 2.

### Code review
Code review is performed for each Pull Request that corresponds to a ticket in Jira. When you are ready to show your code set "Ready for review" label. After review reviewer sets "Review done" label and comments if there is any feedback. Developer responsible for PR is obliged to merge it into the master branch.

### Workflow
- Start ticket in Jira
- Create new branch from master f.e. feature/JIR-123
- Commit often to new feature branch
- When you finish creating Pull Request and ask for review
- When review is done merge PR into master and delete branch
- QA is testing build from master branch
- At the end of sprint after QA review create PR and merge from master to production branch.
