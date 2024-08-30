# Evaluation lab - Node-RED

## Group number: 17

## Group members

- Lorenzo Corrado
- Dario d'Abate
- Filippo Ranieri Pantaleone

## Description of message flows
The only accepted messages:
- What's the {weather/wind} forecast in {one day/two days} in {Rome/Milan}?
Flow of messages:
1. "Compute answers" node is a dispatcher node: a weather forecast request is sent to "weather-counters" node, and a wind forecast request to "wind-counters" node
2. In the counter nodes we increment the queries done in a minute
3. The counter nodes activate the nodes in which we extract the city. Those nodes send the location to "openweather nodes"
4. The "openweather nodes" send the weather messages to "get" nodes, in which all the logic is performed. Those nodes send messages to a "text node" required to send messages to the telegtram bot 
5. For the counter part, we just save in a file (with overwrite) the number of queries, and then we reset them to 0.

## Extensions 
OpenWeather Api and Telegram message system

## Bot URL 
NOME BOT TELEGRAM
API Key = XXX