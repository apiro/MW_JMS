package server;

public abstract class Handler {

	private String identity;
	
	public Handler(String identity) {
		this.identity = identity;
	}
	
	public void print(String s) {
		System.out.println("> " + identity + " > " + s);
	}
	
	public abstract void stopListening();
	
}
