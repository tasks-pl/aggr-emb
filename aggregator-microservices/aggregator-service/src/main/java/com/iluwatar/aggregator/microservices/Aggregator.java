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

import static java.util.Objects.requireNonNullElse;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * The aggregator aggregates calls on various micro-services, collects data and further publishes
 * them under a REST endpoint.
 */

@RestController
public class Aggregator {

  @Resource
  private Micro0Client micro0Client;

  @Resource
  private Micro1Client micro1Client;

  class Welcome {

    @JsonRawValue
    private final String home = "{\"Weather aggregator service\":{\"microservices\":\"http://localhost:50004/micro\",\"queries (eng)\":\"/{city_name} (ex. http://localhost:50004/new_york)\"}}";

  }

  class Micro {

    private String name;
    private Integer status;

    public String getname() {
      return name;
    }

    public Integer getstatus() {
      return status;
    }

    public void setMicro(String n, Integer s) {
      this.name = n;
      this.status = s;
    }

  }

  @RequestMapping("/")
  public Welcome welcome() {
    return new Welcome();
  }

  /**
   * Comment.
   */

  @RequestMapping(path = "/micro", method = RequestMethod.GET)
  public Micro[] micros() {
    Micro[] micros = new Micro[2];

    micros[0] = new Micro();
    Integer code0 = null;
    try {
      URL url0 = new URL("http://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric&APPID=4a0cf3632cf8b967517787b33b49ff00");
      HttpURLConnection connection0 = (HttpURLConnection) url0.openConnection();
      connection0.setRequestMethod("GET");
      connection0.connect();
      code0 = connection0.getResponseCode();
      connection0.disconnect();
    } catch (Exception e) {
      System.out.println("err0");
    }
    micros[0].setMicro("OpenWeather", code0);

    micros[1] = new Micro();
    Integer code1 = null;
    try {
      URL url1 = new URL("https://api.weatherbit.io/v2.0/current?city=moscow&key=1755f17b99674d1ebc31353debaf6330");
      HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
      connection1.setRequestMethod("GET");
      connection1.connect();
      code1 = connection1.getResponseCode();
      connection1.disconnect();
    } catch (Exception e) {
      System.out.println("err1");
    }
    micros[1].setMicro("Weatherbit.io", code1);

    return micros;
  }

  /**
   * Comment.
   */

  @RequestMapping(value = "/{query}", method = RequestMethod.GET)
  public Product weather(@PathVariable String query) {

    var product = new Product();
    var json0 = micro0Client.getWeather(query);
    var json1 = micro1Client.getWeather(query);

    //Fallback to error message
    product.setservice0(requireNonNullElse(json0, "Error: Fetching Product Title Failed"));

    //Fallback to error message
    product.setservice1(requireNonNullElse(json1, "Error: Fetching Product Title Failed"));

    return product;
  }

}
