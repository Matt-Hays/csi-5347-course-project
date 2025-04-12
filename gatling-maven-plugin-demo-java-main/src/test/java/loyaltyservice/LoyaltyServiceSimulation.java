package loyaltyservice;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;


import static io.gatling.javaapi.http.HttpDsl.http;

public class LoyaltyServiceSimulation extends Simulation {

    HttpProtocolBuilder resourceHttpProtocol = http
            .baseUrl("http://localhost:8072")
            .acceptHeader("application/json")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/134.0.0.0 Safari/537.36");

    ScenarioBuilder scn = scenario("Register, Login, and Create a Product and Get All Products")
            .exec(http("Login Request")
                    .post("/auth-service/login")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    // Basic Auth header for the client credentials (clientId:clientSecret)
                    .header("Authorization", "Basic cG9zLXN5c3RlbTpteS1zZWNyZXQ=")
                    // Form parameters: grant_type, and resource owner credentials.
                    .formParam("grant_type", "password")
                    .formParam("username", "matt.hays")          // Replace with the actual username
                    .formParam("password", "password1")     // Replace with the actual password
                    .check(status().is(200))
                    // Extract the access token from the JSON response and store it as "jwtToken"
                    .check(jsonPath("$.access_token").saveAs("jwtToken"))
            )
//            .exec(session -> {
//                String token = session.get("jwtToken");
//                System.out.println("Token: " + token);
//                return session;
//            })
            .exec(http("Post Product")
                    .post("/inventory/v1/product")
                    .header("Authorization", session -> "Bearer " + session.get("jwtToken"))
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"name\": \"Test Product\", \"description\": \"Test Product Description\", \"price\": 10.99, \"quantity\": 11.00}"))
                    .check(status().is(200))
            )
            .exec(http("Product Request")
                    .get("/inventory/v1/product")
                    .header("Authorization", session -> "Bearer " + session.get("jwtToken"))
                    .check(status().is(200))
            )
            .exec(http("Create Customer")
                    .post("/point-of-sale/v1/customer")
                    .header("Authorization", session -> "Bearer " + session.get("jwtToken"))
                    .header("Content-Type", "application/json")
                    .body(StringBody("{\"firstName\":\"Bob\",\"lastName\":\"Bill\",\"email\":\"BillyBob@gmail.com\",\"phoneNumber\":\"555-555-5555\",\"address\":\"123 Main St\",\"city\":\"Denver\",\"state\":\"CO\",\"zipCode\":\"99999\",\"country\":\"US\"}"
                    ))
                    .check(status().is(200))
            );

    {
        setUp(scn.injectOpen(constantUsersPerSec(4).during(60))).protocols(resourceHttpProtocol);
    }

}