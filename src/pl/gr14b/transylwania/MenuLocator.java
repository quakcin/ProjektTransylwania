package pl.gr14b.transylwania;

class MenuLocator
{
	private String ip;
	private int port;

	MenuLocator(String location)
	{
		String[] tokens = location.split(":");
		ip = getIpFromTokens(tokens);
		port = getPortFromTokens(tokens);
	}

	private int getPortFromTokens (String[] tokens)
	{
		return tokens.length != Constants.LOCATOR_PORT_TOKEN_COUNT
				? Integer.parseInt(tokens[1])
				: Constants.DEFAULT_PORT
				;
	}

	private String getIpFromTokens (String[] tokens)
	{
		return tokens.length >= Constants.LOCATOR_IP_TOKEN_MIN_LENGTH
				? tokens[0]
				: Constants.DEFAULT_IP
				;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}