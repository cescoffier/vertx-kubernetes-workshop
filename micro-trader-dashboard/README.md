# Micro-Trader-Dashboard

The quote generator simulates the evolution of the values of 3 companies. Every quote is sent on the event bus. It
also exposes a HTTP endpoint to retrieve the last quote of each company.


## Build

```
mvn clean package
```

## Deploy

```
mvn fabric8:deploy
```
