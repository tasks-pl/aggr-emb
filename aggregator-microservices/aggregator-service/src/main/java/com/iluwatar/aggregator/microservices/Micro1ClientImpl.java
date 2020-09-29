/*
 * The MIT License
 * Copyright © 2014-2019 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.iluwatar.aggregator.microservices;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * -.
 */
@Component
public class Micro1ClientImpl implements Micro1Client {

  private static final Logger LOGGER = LoggerFactory.getLogger(Micro1ClientImpl.class);

  @Override
  public String getWeather(String query) {
    var request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://api.weatherbit.io/v2.0/current?city=" + query.replaceAll("_", "%20") + "&key=1755f17b99674d1ebc31353debaf6330"))
            .build();
    var client = HttpClient.newHttpClient();
    try {
      var httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      //int code = httpResponse.statusCode();
      return httpResponse.body();
    } catch (IOException ioe) {
      LOGGER.error("IOException Occurred", ioe);
    } catch (InterruptedException ie) {
      LOGGER.error("InterruptedException Occurred", ie);
    }
    return null;
  }
}