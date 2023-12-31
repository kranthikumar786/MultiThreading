package com.example.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

 // Use the SpringBoot application if application contains any REST .../ using 3rd party application
 // for terminal cases reading / fetching data , we cna use without springboot app .

  // using throws in main = > becuase that main can be stoped using some commands as well.
// https://medium.com/@meetgujrathi/java-concurrency-executor-service-with-a-real-world-example-866923dcc57b
@SpringBootApplication
public class StockQuoteServiceApplication {

	public static RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		List<String> symbols = new ArrayList<String>();
		symbols.add("AAPL");
		symbols.add("GOOGL");
		symbols.add("MSFT");
		long startTime = System.nanoTime();
		List<String> responses = getResponseList(symbols);
		for (String response : responses) {
			System.out.println(response);
		}
		long elapsedTime = System.nanoTime() - startTime;
		long startTimeParellel = System.nanoTime();
		List<String> responsesParellel = getResponseListParellel(symbols);
		for (String response : responsesParellel) {
			System.out.println(response);
		}
		long elapsedTimeParellel = System.nanoTime() - startTimeParellel;
		System.out.println("Sequential Execution elapsed time: " + elapsedTime/1000000);
		System.out.println("Concurrent Execution elapsed time: " + elapsedTimeParellel/1000000);
	}
	public static List<String> getResponseList(List<String> symbols) {

		List<String> responses = new ArrayList<String>();
		for (int i = 0; i < symbols.size(); i++) {
			String responseAlphavantage = alphavantageResponse(symbols.get(i));
			responses.add(responseAlphavantage);
		}
		return responses;
	}
	
	public static List<String> getResponseListParellel(List<String> symbols) throws InterruptedException, ExecutionException {
		List<Future<String>> allFutures = new ArrayList<Future<String>>();
    	ExecutorService service = Executors.newFixedThreadPool(3);
		List<String> responses = new ArrayList<String>();
		for (int i = 0; i < symbols.size(); i++) {
			
			int k = i;
			Future<String> future = service.submit(() -> {
        		return alphavantageResponse(symbols.get(k));
     		});
      		allFutures.add(future);
		}
		for (int i = 0; i < allFutures.size(); i++) {

	        Future<String> future = (Future<String>) allFutures.get(i);
		    String stockQuote;
		    stockQuote = future.get(); // fetch the value then move to next one:
	    	responses.add(stockQuote); //
			
		}
		service.shutdown();
		return responses;
	}
		
	public static String alphavantageResponse(String symbol) {
		
		String urlRequired = buildUrl(symbol);
		String responseAlphavantage = restTemplate.getForObject(urlRequired, String.class);
		return responseAlphavantage;
	}
	public static String buildUrl(String symbol) {

		String alphavantageUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" 
								+ symbol + "&apikey=YOURAPIKEY";
		return alphavantageUrl;
	}

}
