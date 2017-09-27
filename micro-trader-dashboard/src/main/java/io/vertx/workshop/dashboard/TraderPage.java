package io.vertx.workshop.dashboard;

import in.yunyul.vertx.console.base.ConsolePage;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@SuppressWarnings("unused")
public class TraderPage implements ConsolePage {


    public static TraderPage create() {
        return new TraderPage();
    }

    @Override
    public void mount(Vertx vertx, Router router, String basePath) {
        
    }

    @Override
    public String getLoaderFileName() {
        return null;
    }
}
