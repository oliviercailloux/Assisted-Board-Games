package io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import io.github.oliviercailloux.y2018.assisted_board_games.ressources.servlets.StateServlet;

/***
 * Code From https://github.com/oliviercailloux/javaee-jpa-resource-local-servlets/blob/master/src/main/java/io/github/oliviercailloux/javaee_jpa_resource_local_servlets/utils/ServletHelper.java
 * @Author : Olivier Cailloux
 */
@RequestScoped
public class ServletHelper {

	public ServletOutputStream configureAndGetOutputStream(HttpServletResponse resp) throws IOException {
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resp.setContentType(MediaType.APPLICATION_JSON);
		resp.setLocale(Locale.ENGLISH);
		return resp.getOutputStream();
	}

	public String getRedirectURL(ServletContext context) {
		Collection<String> mappings = context.getServletRegistration(StateServlet.class.getCanonicalName()).getMappings();
		assert (mappings.size() == 1);
		final String urlMapping = mappings.iterator().next();
		assert (urlMapping.charAt(0) == '/');
		return urlMapping.substring(1);
	}

}
