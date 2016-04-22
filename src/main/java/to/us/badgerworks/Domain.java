package to.us.badgerworks;

public class Domain {
	
	private String hostname;
	private String page;
	private String url;
	private int id = -1;
	private Domain parent;
	private int toCrawl = -1;
	//private Meta meta;
	
	public Domain(String url,String n, String p, Domain parent ){
		setUrl(url); setHostname(n); setPage(p); setParent(parent);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String name) {
		this.hostname = name;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Domain getParent() {
		return parent;
	}

	public void setParent(Domain parent) {
		this.parent = parent;
	}
	
	public String getUrl(){
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int isToCrawl() {
		return toCrawl;
	}

	public void setToCrawl(int toCrawl) {
		this.toCrawl = toCrawl;
	}
	

}
