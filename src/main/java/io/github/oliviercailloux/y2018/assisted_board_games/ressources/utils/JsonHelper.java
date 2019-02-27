package io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.google.common.collect.ImmutableMap;


public class JsonHelper {

	/***
	 * Code From https://github.com/oliviercailloux/java-course/blob/master/JSON.adoc?fbclid=IwAR29CAry38B9s9OrF_-KU6wl2_8q1UOssx7bXgFRjBfmTCi02pX-LfEEoZ0
	 * @Author : Olivier Cailloux
	 */
	public String asPrettyString(JsonValue json) {
		StringWriter stringWriter = new StringWriter();
		JsonWriterFactory writerFactory = Json.createWriterFactory(ImmutableMap.of(JsonGenerator.PRETTY_PRINTING, true));
		try (JsonWriter jsonWriter = writerFactory.createWriter(stringWriter)) {
			jsonWriter.write(json);
		}
		return stringWriter.toString();
	}
}
