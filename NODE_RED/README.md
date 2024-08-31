# Evaluation lab - Node-RED

# Node-RED Telegram Bot Project

## Overview

This project involves creating a Telegram bot using Node-RED that can respond to weather-related queries for Milano or Rome. The bot uses OpenWeather data to provide forecasts and wind speed predictions. Additionally, it keeps track of changes in the forecast and logs the number of queries it serves.

## Project Features

- **Weather Forecast**: The bot can provide weather forecasts for Milano or Rome for the next one or two days.
- **Wind Speed Forecast**: The bot can also provide wind speed predictions for the next one or two days for the same cities.
- **Change Detection**: For each query, the bot checks if the forecast has changed since the last query, regardless of the user who asked.
- **Query Logging**: Every minute, the bot logs the number of queries it served (both forecast and wind speed) to a local file.

## Message Flow Description

The Node-RED flow is structured to handle incoming messages from users and fetch data from OpenWeather as follows:

1. **Telegram Input Node**: Receives user queries via the Telegram bot.
2. **Function Node (Query Parsing)**: Parses the incoming messages to identify the type of query (weather or wind speed) and the city and day specified by the user.
3. **HTTP Request Node (OpenWeather API)**: Sends an HTTP request to the OpenWeather API to fetch the relevant forecast data based on the user query.
4. **Function Node (Change Detection)**: Compares the fetched forecast data with the previously stored data to determine if there has been any change.
5. **Function Node (Response Formatting)**: Formats the response based on whether there was a change in the forecast and constructs the appropriate reply message.
6. **Telegram Output Node**: Sends the formatted response back to the user via the Telegram bot.
7. **File Node (Logging)**: Every minute, this node writes the count of the different types of queries received in the past minute to a local file.

The only accepted messages:
- What's the {weather/wind} forecast in {one day/two days} in {Rome/Milan}?

## Extensions Used

- **Node-RED Dashboard**: For monitoring and controlling the bot’s operation.
- **Node-RED-Contrib-Telegrambot**: For interfacing with Telegram and managing bot commands and messages.
- **Node-RED-Contrib-OpenWeatherMap**: To simplify accessing OpenWeather API.
