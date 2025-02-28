package com.hezebin.template.component.httpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import com.hezebin.template.application.dto.ResponseBody;
import com.hezebin.template.domain.entity.Example;
import com.hezebin.template.exception.ErrorException;
import com.hezebin.template.application.dto.ResponseBodyCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExampleHttpClient {

    @Value("${example.service.base-url}")
    private String baseUrl;

    @Autowired
    private WebClient webClient;

    public Example getExampleById(String id) throws ErrorException {
        try {
            return webClient.get()
                    .uri(baseUrl + "/api/example/{id}", id)
                    .retrieve()
                    .bodyToMono(ResponseBody.class)
                    .map(response -> {
                        if (response.getCode() != ResponseBodyCode.OK.getCode()) {
                            throw new ErrorException(ResponseBodyCode.BAD_REQUEST, response.getMessage());
                        }
                        return (Example) response.getData();
                    })
                    .block();
        } catch (Exception e) {
            log.error("Failed to get example by id: {}", id, e);
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public Example createExample(Example example) throws ErrorException {
        try {
            return webClient.post()
                    .uri(baseUrl + "/api/example")
                    .bodyValue(example)
                    .retrieve()
                    .bodyToMono(ResponseBody.class)
                    .map(response -> {
                        if (response.getCode() != ResponseBodyCode.OK.getCode()) {
                            throw new ErrorException(ResponseBodyCode.BAD_REQUEST, response.getMessage());
                        }
                        return (Example) response.getData();
                    })
                    .block();
        } catch (Exception e) {
            log.error("Failed to create example: {}", example, e);
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    public String getBaiduHomePage() throws ErrorException {
        try {
            return webClient.get()
                    .uri("http://www.baidu.com")
                    .exchangeToMono(response -> {
                        if (response.statusCode().is3xxRedirection()) {
                            String redirectUrl = response.headers().header("Location").get(0);
                            return webClient.get()
                                    .uri(redirectUrl)
                                    .retrieve()
                                    .bodyToMono(String.class);
                        }
                        return response.bodyToMono(String.class);
                    })
                    .map(response -> {
                        log.info("Baidu homepage response: {}", response);
                        return response;
                    })
                    .block();
        } catch (Exception e) {
            log.error("Failed to get Baidu homepage", e);
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }
}