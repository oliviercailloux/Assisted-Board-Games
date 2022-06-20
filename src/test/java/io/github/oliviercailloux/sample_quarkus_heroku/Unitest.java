package io.github.oliviercailloux.sample_quarkus_heroku;

import static io.restassured.RestAssured.given;



import io.quarkus.test.junit.QuarkusTest;

import javax.json.JsonObject;
import javax.json.JsonWriter;
import io.restassured.http.ContentType;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.hamcrest.core.Is;
import org.hamcrest.text.MatchesPattern;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class Unitest {
        Duration reminTime = Duration.ofSeconds(1800);
	@Test
	@Order(1)
	public void testPostcreateGame() {
		given().when().post("/v0/api/v1/game/new").then().statusCode(200).body(Is.is("1"));
	}

	@Test
	@Order(2)
	public void testGetgetGame() {
		given().when().get("/v0/api/v1/game/1").then().statusCode(200).body(MatchesPattern.matchesPattern("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
	}
//	@Test
//	@Order(3)
//	public void testWilly(){
//	    	given().contentType(ContentType.JSON).body(obj)
//        .post("/v0/api/v1/game/1/move")
//        .then().statusCode(200).body(Is.is(""));
//	}	
	@Test
	@Order(3)
	public void testgetRemainingTime(){
	   given().when().get("/v0/api/v1/game/1/clock/black").then().statusCode(200).body(Is.is(reminTime));
	}
}

