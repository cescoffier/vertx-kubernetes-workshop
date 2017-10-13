package io.vertx.workshop.audit.impl;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.JDBCDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A verticle storing operations in a database (hsql) and providing access to the operations.
 */
public class AuditVerticle extends AbstractVerticle {

    private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS AUDIT";
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS AUDIT (id SERIAL PRIMARY KEY, " +
        "operation varchar(250) NOT NULL)";
    private static final String INSERT_STATEMENT = "INSERT INTO AUDIT (operation) VALUES (?)";
    private static final String SELECT_STATEMENT = "SELECT * FROM AUDIT ORDER BY ID DESC LIMIT 10";

    private JDBCClient jdbc;
    private boolean ready;

    /**
     * Starts the verticle asynchronously. The the initialization is completed, it calls
     * `complete()` on the given {@link Future} object. If something wrong happens,
     * `fail` is called.
     *
     * @param future the future to indicate the completion
     */
    @Override
    public void start(Future<Void> future) {
        // creates the jdbc client.

        ServiceDiscovery.create(vertx, discovery -> {

            // Discover and configure the database.
            Single<JDBCClient> jdbc = JDBCDataSource.rxGetJDBCClient(discovery,
                svc -> svc.getName().equals("audit-database"),
                getDatabaseConfiguration()
            ).doOnSuccess(jdbcClient -> this.jdbc = jdbcClient);

            // TODO
            // ----

            Single<JDBCClient> databaseReady = jdbc
                .flatMap(client -> initializeDatabase(client, true));
            Single<HttpServer> httpServerReady = configureTheHTTPServer();
            Single<MessageConsumer<JsonObject>> messageConsumerReady = retrieveThePortfolioMessageSource();

            Single<MessageConsumer<JsonObject>> readySingle = Single.zip(databaseReady, httpServerReady,
                messageConsumerReady, (db, http, consumer) -> consumer);

            // ----

            // signal a verticle start failure
            readySingle.doOnSuccess(consumer -> {
                // on success we set the handler that will store message in the database
                consumer.handler(message -> storeInDatabase(message.body()));
            }).subscribe(consumer -> {
                // complete the verticle start with a success
                future.complete();
                ready = true;
            }, future::fail);
        });


    }

    /**
     * @return the database configuration.
     */
    private JsonObject getDatabaseConfiguration() {
        return new JsonObject().put("user", "admin")
            .put("password", "secret")
            .put("driver_class", "org.postgresql.Driver")
            .put("url", "jdbc:postgresql://audit-database:5432/audit");
    }

    @Override
    public void stop(Future<Void> future) throws Exception {
        jdbc.close();
        super.stop(future);
    }

    private void retrieveOperations(RoutingContext context) {
        // We retrieve the operation using the following process:
        // 1. Get the connection
        // 2. When done, execute the query
        // 3. When done, iterate over the result to build a list taking the `operation` value of each json object
        // 5. write this list into the response
        // 4. close the connection

        //TODO
        // ----
        jdbc.getConnection(ar -> {
            SQLConnection connection = ar.result();
            if (ar.failed()) {
                context.fail(ar.cause());
            } else {
                connection.query(SELECT_STATEMENT, result -> {
                    if (result.failed()) {
                        context.fail(result.cause());
                    } else {
                        ResultSet set = result.result();
                        List<JsonObject> operations = set.getRows().stream()
                            .map(json -> new JsonObject(json.getString("operation")))
                            .collect(Collectors.toList());
                        // 5. write this list into the response
                        context.response().setStatusCode(200).end(Json.encodePrettily(operations));
                        // 6. close the connection
                        connection.close();
                    }
                });
            }
        });
        // ----
    }

    private Single<HttpServer> configureTheHTTPServer() {

        //TODO
        //----

        Router router = Router.router(vertx);
        router.get("/").handler(this::retrieveOperations);
        router.get("/health").handler(rc -> {
            if (ready) {
                rc.response().end("Ready");
            } else {
                // Service not yet available
                rc.response().setStatusCode(503).end();
            }
        });
        return vertx.createHttpServer().requestHandler(router::accept).rxListen(8080);
        //----

//        return ready;
    }

    private Single<MessageConsumer<JsonObject>> retrieveThePortfolioMessageSource() {
        // Example of Single returning a single item already known:
        return Single.just(vertx.eventBus().consumer("portfolio"));
    }


    private void storeInDatabase(JsonObject operation) {
        // 1. need to retrieve a connection
        // 2. execute the insertion statement
        // 3. close the connection


        // Step 1 get the connection
        Single<SQLConnection> connectionRetrieved = jdbc.rxGetConnection();

        // Step 2, when the connection is retrieved (this may have failed), do the insertion (upon success)
        Single<UpdateResult> update = connectionRetrieved
            .flatMap(connection ->
                connection.rxUpdateWithParams(INSERT_STATEMENT, new JsonArray().add(operation.encode()))

                    // Step 3, when the insertion is done, close the connection.
                    .doAfterTerminate(connection::close));

        update.subscribe(result -> {
            // Ok
        }, err -> {
            System.err.println("Failed to insert operation in database: " + err);
        });
    }

    private Single<JDBCClient> initializeDatabase(JDBCClient client, boolean drop) {
        // TODO - Initialize the database and return the JDBC client
        // ----
        // The database initialization is a multi-step process:
        // 1. Retrieve the connection
        // 2. Drop the table is exist
        // 3. Create the table
        // 4. Close the connection (in any case)
        // To handle such a process, we are going to create an RxJava Single and compose it with the RxJava flatMap operation:
        // retrieve the connection -> drop table -> create table -> close the connection
        // For this we use `Func1<X, Single<R>>`that takes a parameter `X` and return a `Single<R>` object.

        // This is the starting point of our operations
        // This single will be completed when the connection with the database is established.
        // We are going to use this single as a reference on the connection to close it.
        Single<SQLConnection> connectionRetrieved = client.rxGetConnection();

        // Ok, now it's time to chain all these actions (2 to 4):
        return connectionRetrieved
            .flatMap(conn -> {
                // When the connection is retrieved

                // Prepare the batch
                List<String> batch = new ArrayList<>();
                if (drop) {
                    // When the table is dropped, we recreate it
                    batch.add(DROP_STATEMENT);
                }
                // Just create the table
                batch.add(CREATE_TABLE_STATEMENT);

                // We compose with a statement batch
                Single<List<Integer>> next = conn.rxBatch(batch);

                // Whatever the result, if the connection has been retrieved, close it
                return next.doAfterTerminate(conn::close);
            })
            .map(list -> client);
        // ----

    }
}
