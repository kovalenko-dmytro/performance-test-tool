package com.gmail.apachdima.ptt.execution.directory

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation1 extends Simulation {

  val httpProtocol = http
    .baseUrl(System.getProperty("baseUrl")) // Здесь находится корень для всех относительных URL

    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
    ) // Вот общие заголовки
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader(
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
    )

  val scn =
    scenario("Scenario Name") // Сценарий представляет собой цепь запросов и пауз
      .exec(
        http("request_1")
          .get("/")
      )
      .pause(7) // Обратите внимание, что Gatling записал паузы в реальном времени

  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}
