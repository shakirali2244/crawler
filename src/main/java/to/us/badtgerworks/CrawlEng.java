package to.us.badtgerworks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class CrawlEng {
	/** all the indexed links */
	private static ArrayList<String> links;
	/** all the indexed image links */
	
	
	
	public CrawlEng() {
		links = new ArrayList<String>();
	}
	
	/**
	 * returns Scanner from a given url
	 * @param url
	 * @return Scanner
	 */
	public Scanner httpCall(String url){
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("Malinformed Url!!");
			//e.printStackTrace();
			return null;
		}
		URLConnection conn = null;
		try {
			conn = u.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		InputStream stream = null;
		try {
			stream = conn.getInputStream();
		} catch (IOException e) {
			
			System.out.println("Recieved a non HTTP 200 for "+ url);
			return null;
		}
		Scanner in = new Scanner(stream);
		
		return in;
	}
	
	/**
	 * expects an html line having a tag with href attribute
	 * @param input
	 * @return url as a string
	 */
	public String extractLink(String input, String url){
		if (input == null) return "";
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Broken Url!!");
		}
		int index = input.indexOf("href");
		String newString = input.substring(index);
		String[] arr = newString.split("\"");
		if (arr.length > 1){
			String unprocessed = arr[1];
			if (unprocessed.startsWith("/")){
				if(unprocessed.indexOf("//") == 0) return unprocessed.substring(2,unprocessed.length());
				return uri.getScheme() + "://"+  uri.getHost() + unprocessed;
			}else if(unprocessed.startsWith("http")) {
				return unprocessed;
			}else {
				return uri.getScheme() + "://"+  uri.getHost() + "/" + unprocessed;
			}
		}else{
			return "";
		}
	}
	
	
	
	/**
	 * looks for new links on a page recursively 
	 * @param url
	 */
	public void crawl(String url){
		URI current_uri = null;
		try {
			current_uri = new URI(url);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			System.out.println("Broken Url!!");
		}
		Scanner in = httpCall(url);
		if (in == null) return;
		//System.out.println(links.size());
		while(in.hasNextLine()){
			String htmlDump = in.nextLine();
			if(htmlDump.contains("href") && htmlDump.contains("<a") ){
				String link = extractLink(htmlDump,url);
				URI link_uri = null;
				try {
					link_uri = new URI(link);
					if ((link_uri.toString() != null && link_uri.toString() != "")) {
						boolean added = false;
						for (String addedlink: links){
							if (addedlink.equals(link_uri.toString())) added = true;
						}
						if (!added){
							System.out.println("total of "+links.size()+" so far");
							links.add(link);
						}	
					}
				} catch (URISyntaxException e) {
					
				}
								
			}
		}
	}
	public void thread(){
		for (Object link: links){
			crawlThread p = new crawlThread(link);
		     p.start();
		}
	}
	public class crawlThread extends Thread {
		Object url;
        crawlThread(Object url) {
            this.url = url;
        }

        public void run() {
            crawl((String) url);
            
            links.remove(url);
        }
    }
	

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
		//String url = "http://oracle.com";
		CrawlEng a = new CrawlEng();
		a.crawl(url);
		a.thread();
		
		
	}
}
/*	
	 * extracts image url from img tag
	 * @param input - the html line
	 * @param url - the supposed host url for the image
	 * @return
	
	public String extractImg(String input, String url){
		if (input == null) return "";
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("Broken Url!!");
			//e.printStackTrace();
		}
		int index = input.indexOf("src");
		String newString = input.substring(index);
		String[] arr = newString.split("\"");
		if (arr.length > 1){
			String unprocessed = arr[1];
			if (unprocessed.startsWith("/")){
				if(unprocessed.indexOf("//") == 0) return uri.getScheme()+":"+ unprocessed;
				return uri.getScheme() + "://"+  uri.getHost() + unprocessed;
			}else if(unprocessed.startsWith("http")) {
				return unprocessed;
			}else {
				return uri.getScheme() + "://"+  uri.getHost() + "/" + unprocessed;
			}
		}else{
			return "";
		}
	}
	
	 * looks for image links from each link in links
	 
	public void imgCrawl(){
		for (String link: links){
			URI current_uri = null;
			try {
				current_uri = new URI(link);
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Scanner in = httpCall(link);
			if (in == null) return;
			while(in.hasNextLine()){
				String htmlDump = in.nextLine();
				if((htmlDump.contains("src") && htmlDump.contains("<img")) && 
						(htmlDump.contains(".jpg") || htmlDump.contains(".png")) ){
					String link2 = extractImg(htmlDump,link);
					boolean added = false;
					for (String addedlink: imgs){
						if (addedlink.equals(link2)) added = true;
					}
					if(!added){
						imgs.add(link2);
					}	
				}
			}
		}
	}
	 
	
	
	
	 * downloads 10 images from arraylist imgs 
	 
	public void retrieveImg(){
		if(imgs.size() == 0) return;
		for (int i = 0; i< 10; i++){
			String link = imgs.get((int)(Math.random()*(imgs.size()-1)));
			System.out.println(link);
			try {
				URL url = new URL(link);
				 InputStream in = new BufferedInputStream(url.openStream());
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
			        byte[] buf = new byte[1024];
			        int n = 0;
			        while (-1 != (n = in.read(buf))) {
			            out.write(buf, 0, n);
			        }
			        out.close();
			        in.close();
			        byte[] response1 = out.toByteArray();

			        FileOutputStream fos = new FileOutputStream(i+1+"image"+link.substring(link.length()-4));
			        fos.write(response1);
			        fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}	
*/