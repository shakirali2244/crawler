package to.us.badgerworks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class CrawlEng {
	/** all the indexed links */
	private static ArrayList<Domain> links;
	/** db helper for crawl **/
	private dbHelper dh;
	/** limit for the crawl to stop **/
	private int limit = 5000;
	/** Starting Domain **/
	private Domain start;
	/** running threads **/
	private int threads = 0;
	/** max allowed threads **/
	private final int THREAD_LIMIT = 100;
	/** total data processed **/
	private int bytes = 0;
	
	public CrawlEng(URI url, String username){
		dh = new dbHelper(username);
		dh.DriverRegistration();
		Domain dummy = new Domain("","","",null);
		dummy.setId(1);
		start = new Domain(url.toString(),url.getHost(),url.getPath(),dummy);
		dh.addPage(start);
		links = new ArrayList<Domain>();
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
		} catch (Exception e) {
			
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
	public void crawl(Domain url){
		try {
			new URI(url.getUrl());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			System.out.println("Broken Url!!");
			return;
		}
		Scanner in = httpCall(url.getUrl());
		if (in == null) return;
		//System.out.println(links.size());
		int temp = 10; //limit to 10 links per page
		while(in.hasNextLine() && temp >= 0){
			String htmlDump = in.nextLine();
			byte[] utf8Bytes = null;
			try {
				utf8Bytes = htmlDump.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bytes+= utf8Bytes.length;
			
			if(htmlDump.contains("href=\"http") && htmlDump.contains("<a") ){
				String link = extractLink(htmlDump,url.getUrl());
				//System.out.println(link);
				URI link_uri = null;
				try {
					link_uri = new URI(link);
					if ((link_uri.toString() != null && link_uri.toString() != "")) {
												
						if (!link_uri.equals(url.getUrl())){
							//System.out.println("parent = " + url.getUrl() + "id = "+ url.getId());
							Domain d = new Domain(link,link_uri.getHost(),link_uri.getPath(),url);
							
								links.add(d);
							
							dh.addPage(d);	
							temp--;
							limit--;
						}
					}
				} catch (URISyntaxException e) {}	//output supress			
			}
		}
	}
	public void thread(){
		
		while (threads > -1 && limit >= 0){
			while(links.size() <= 0 ){
				System.out.println("queue empty" +print());
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Domain current = links.get(0);
			while(current.isToCrawl() == -1){System.out.println("waiting... ");}
			if(current.isToCrawl() == 1){
				while(threads >= THREAD_LIMIT ){
					System.out.println("thread poll full"+print());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				threads++;
				crawlThread p = new crawlThread(current);
			    p.start();
			    synchronized(links){
					links.remove(current);
				}
			}else{
				synchronized(links){
					links.remove(current);
				}
			}
		   
		}
		System.out.println("Ending "+ links.size() + " "+ limit + ".");
	}
	
	private String print(){
		return " ["+ links.size()+"] qued, ["+threads+"] running, limit = "+"["+limit+"], [" + (bytes/1000000) + "] MBs processed";
	}
	public class crawlThread extends Thread {
		Domain cur;
        crawlThread(Domain cur) {
            this.cur = cur;
        }

        public void run() {
            	crawl(cur);
            	threads--;
        		System.out.println(print());
            	
        }
    }	
	public void crawlStart() {
		crawl(start);
	}
}
