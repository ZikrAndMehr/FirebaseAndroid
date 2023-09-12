
# Welcome to FirebaseAndroid!

This app showcases various features and capabilities of Firebase services for Android devices.

## Introduction

This Android app demonstrates the implementation of Firebase services, including authentication, realtime database, cloud database, storage, messaging, crashlytics, and remote config.

## Getting Started

To get started with the FirebaseAndroid app, follow these steps:

1. Clone the repository or download the source code.

2. Open the project in Android Studio.

3. Add the necessary dependencies to the project.

4. Configure the Firebase project in the Firebase console.

5. Build and run the app on an emulator or device.

## App Details

To use every implemented Firebase service in this app, read this section:

- Sign Up or use the default account to Sign In.

- After signing in, you will be at the Home page, which consists of eight implemented Firebase services:

0. Analytics

1. Authentication

2. Realtime Database

3. Cloud Firestore

4. Storage

5. Messaging

6. Crashlytics

7. Remote Config

- ****Analytics:**** This service is implemented in the codebase and automatically logs events into Firebase Analytics' Debug View section. You don't have to do anything.

- ****Authentication:**** When you first launch the app, you can Sign up or Sign in with Google, which demonstrates Firebase Authentication. When you navigate from the Home page to the Authentication page, you can Sign out or Delete your account.

- ****Realtime Database:**** After navigating from the Home page to the Realtime Database page, you can see some notes or an empty screen. To add a new note, click the plus button, enter the Title and Description of the new note, and click Save. The note will be saved in the Realtime Database of Firebase, and you can view or delete your note from the main Realtime Database page.

- ****Cloud Firestore:**** When you are in the Cloud Firestore page, you can see a global quote with an author. You can update it with your own quote. The quote is saved in Cloud Firestore and can be updated by anyone, which is why it is called global.

- ****Storage:**** When you open the Storage page, you can see an image  or not (if your account is new). To select an image, click the "Select Image" button and choose an image from your device. After choosing the image, you can upload it to Firebase Storage. The uploaded image will be visible in your next app launch, and you can also copy the Image URL and share it.

- ****Messaging:**** Messaging is currently not implemented.

- ****Crashlytics:**** When you navigate to the Crashlytics page, you can see three buttons. Clicking one of them will crash your app (it is not recommended, but it is used to demonstrate the Firebase service). You can see Crash details in Firebase Crashlytics.

- ****Remote Config:**** Remote Config is currently not implemented.

## Important

Please note that you cannot directly access the Firebase project. In order to obtain access, you will need to request permission!

## License

This project is licensed under the [Apache License 2.0](./LICENSE).