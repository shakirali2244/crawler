package to.us.badtgerworks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Application {
	
	/**
	 * main entry point
	 * @param args
	 */
	public static void main(String[] args){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter starting url ");
        String url = null;
		try {
			url = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URI start_uri = null;
		try {
			start_uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String url = "http://oracle.com";
		CrawlEng a = new CrawlEng(start_uri,"postgres");
		a.crawlStart();
		a.thread();
		
		
	}
}


