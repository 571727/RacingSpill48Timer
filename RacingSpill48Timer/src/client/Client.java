package client;

public interface Client {

	/**
	 * Sends whatever request to a server
	 * @return server response or END_ALL_CLIENT_STRING
	 */
	public String sendRequest(String req);
}
