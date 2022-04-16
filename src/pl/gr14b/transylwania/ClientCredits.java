package pl.gr14b.transylwania;

public class ClientCredits
{
	private String nickName;
	private String ipAddress;
	private int port;

	ClientCredits (String nick, String ip, int port)
	{
		this.nickName = nick;
		this.ipAddress = ip;
		this.port = port;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
