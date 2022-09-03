package com.hcifuture;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class CompositeFuturePOC extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(CompositeFuturePOC.class);

  @Override
  public void start(Promise<Void> promise) {

    printDateTime("Starting");

    CompositeFuture.any(
      doAsyncCall(5), //Each of this makes a vertex http request that takes 5seconds to complete,
      doAsyncCall(5),
      doAsyncCall(5),
      doAsyncCall(5),
      doAsyncCall(5)
    )
    .onSuccess(data -> {
      printDateTime("CompositeFuture.any: ALL DONE");
      logger.info("CompositeFuture.any: {}", data.toString());
    })
    .onFailure(err -> {
      logger.error("CompositeFuture.any: Something went wrong", err);
    }).onComplete(done -> {
      logger.info("CompositeFuture.any: done: {}", done.result() != null ? done.result().size() : "null");
    });


    CompositeFuture.join(
                    doAsyncCall(5), //Each of this makes a vertex http request that takes 5seconds to complete,
                    doAsyncCall(5),
                    doAsyncCall(5),
                    doAsyncCall(5),
                    doAsyncCall(5)
            )
            .onSuccess(data -> {
              printDateTime("CompositeFuture.join: ALL DONE");
              logger.info("CompositeFuture.join: {}", data.toString());
            })
            .onFailure(err -> {
              logger.error("CompositeFuture.join: Something went wrong", err);
            }).onComplete(done -> {
              logger.info("CompositeFuture.join: done: {}", done.result() != null ? done.result().size() : "null");
            });


    CompositeFuture.all(
                    doAsyncCall(5), //Each of this makes a vertex http request that takes 5seconds to complete,
                    doAsyncCall(5),
                    doAsyncCall(5),
                    doAsyncCall(5),
                    doAsyncCall(5)
            )
            .onSuccess(data -> {
              printDateTime("CompositeFuture.all: ALL DONE");
              logger.info("CompositeFuture.all: {}", data.toString());
            })
            .onFailure(err -> {
              logger.error("CompositeFuture.all: Something went wrong", err);
            }).onComplete(done -> {
              logger.info("CompositeFuture.all: done: {}", done.result() != null ? done.result().size() : "null");
            });
  }

  private AtomicInteger count = new AtomicInteger();
  private Future<JsonObject> doAsyncCall(int delay) {

    if (count.incrementAndGet() % 3 == 0)
      return Future.failedFuture("failure");
    else
      return Future.succeededFuture();
  }

  private void printDateTime(String msg) {
    LocalDateTime localDate = LocalDateTime.now();
    logger.info(msg + " at: " + localDate);
  }
}
