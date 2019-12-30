package com.grmek.rso.imagemanaging;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Date;

@RequestScoped
public class DemoFaultTolerance {

    /*** User-defined interface BEGIN. ***/

    private final int MAX_CONSECUTIVE_FAILURES = 2;
    private final long OPEN_STATE_TIME = 30000;

    @Inject
    @RestClient
    private CommentingRestService commentingService;

    private Response defaultFunction(int arg1) throws Exception {
        try {
            return commentingService.userCleanUp(arg1);
        }
        catch (Exception e) {
            throw new Exception();
        }
    }

    private Response fallbackFunction(int arg1) {
        System.out.println("Fallback function was called.");
        return null;
    }

    public Response commentingServiceUserCleanUp(int arg1) {
        return circuitBreaker(arg1);
    }

    /*** User-defined interface END. ***/



    private static int circuitBreakerState = 0; /* 0 - circuit closed (normal state), 1 - open, 2 - half open. */
    private static int consecutiveFailures = 0;
    private static long openStateStartTime = 0;

    private Response circuitBreaker(int arg1) {
        Response response = null;

        if (circuitBreakerState == 0) {
            try {
                response = defaultFunction(arg1);
                consecutiveFailures = 0;
            }
            catch (Exception e) {
                response = fallbackFunction(arg1);
                consecutiveFailures++;
                if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                    circuitBreakerState = 1;
                    openStateStartTime = (new Date()).getTime();
                }
            }
        }
        else if (circuitBreakerState == 1) {
            if (((new Date()).getTime() - openStateStartTime) < OPEN_STATE_TIME) {
                response = fallbackFunction(arg1);
            }
            else {
                circuitBreakerState = 2;
                circuitBreaker(arg1);
            }
        }
        else if (circuitBreakerState == 2) {
            try {
                response = defaultFunction(arg1);
                consecutiveFailures = 0;
                circuitBreakerState = 0;
            }
            catch (Exception e) {
                response = fallbackFunction(arg1);
                circuitBreakerState = 1;
                openStateStartTime = (new Date()).getTime();
            }
        }

        return response;
    }
}
