# Compulsive Traders

The compulsive traders projects contains several implementations of (very dumb) _traders_ that sell and buy shares. They
receive quotes from the event bus and use the portfolio service to buy and sell shared.  

## Build

```
mvn clean package
```

## Deploy

```
mvn fabric8:deploy
```
