# Atubu

This is a water tracking app  developed as part of our mobile platform development class. It helps users stay hydrated and keep track of their water consumption. The app encourages . To help you stay motivated, every time you drink water, it not only hydrates you but also nourishes a little plant. At the end of each day, the plant's appearance reflects how well you've hydrated. You can check your progress and see the plants you've nurtured through your calendar .

## Features

Although the app is currently in the proto-alpha stage, the following features have already been implemented:

- **Plant Avatar**: The plant avatar has three states: **Dry**, **Flourishing**, and **Drowned**.
- **Water Indicator**: A visual water indicator that fills up with each glass of water consumed.
- **Cancel Water**: The ability to undo a water intake if needed.
- **Reset Water Indicator**: Empty the water indicator to start fresh.
- **Customizable Water Containers**: Add and delete personalized water containers for more accurate tracking.
- **Navigation Menu**: Easily navigate between different sections of the app.
- **Daily Hydration Goals**: Set a daily water consumption goal in the app’s settings.
- **Toggle Numerical Values**: Option to hide or show numerical values.
- **Metric/Imperial Units**: Switch between metric and imperial units for measurement.
- **2D Calendar**: View a historical record of your daily water intake in a calendar format.
- **Notifications**: Receive reminders to stay hydrated throughout the day.
- **Database Storage**: The app tracks and stores daily water intake data in a local database.

## Technologies

The app is built using Kotlin and utilizes the following APIs:

- **Room**: For database handling and storing user data.
- **WorkManager & NotificationCompat**: For scheduling background tasks and sending notifications to users.
