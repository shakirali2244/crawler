package to.us.badtgerworks;

public class Domain {
	
	private String name;
	private String page;
	//private Meta meta;
	
	public Domain(String n, String p ){
		setName(n); setPage(p); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	

}
