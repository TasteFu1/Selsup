package ru.taste;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;

    //переменная сохраняет время последнего сброса лимита отправки запросов
    private long lastTimeMillis = System.currentTimeMillis();
    //переменная считает кол-во отправленных запросов за (n) промежуток времени
    private int counter;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void sendRequest(Consumer<String> responseBody) {
        if (System.currentTimeMillis() > this.lastTimeMillis + this.timeUnit.toMillis(1)) { //сброс лимита отправки запросов
            this.counter = 1;
            this.lastTimeMillis = System.currentTimeMillis();
        } else if (this.counter >= this.requestLimit) { //отменяем отправку запроса если лимит превышен
            return;
        } else { //подсчет отправленных запросов
            this.counter++;
        }

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder() //
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create")) //
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "description": {
                            "participantInn": "string"
                          },
                          "doc_id": "string",
                          "doc_status": "string",
                          "doc_type": "LP_INTRODUCE_GOODS",
                          "importRequest": true,
                          "owner_inn": "string",
                          "participant_inn": "string",
                          "producer_inn": "string",
                          "production_date": "2020-01-23",
                          "production_type": "string",
                          "products": [
                            {
                              "certificate_document": "string",
                              "certificate_document_date": "2020-01-23",
                              "certificate_document_number": "string",
                              "owner_inn": "string",
                              "producer_inn": "string",
                              "production_date": "2020-01-23",
                              "tnved_code": "string",
                              "uit_code": "string",
                              "uitu_code": "string"
                            }
                          ],
                          "reg_date": "2020-01-23",
                          "reg_number": "string"
                        }
                        """)).build();

        try {
            responseBody.accept(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
