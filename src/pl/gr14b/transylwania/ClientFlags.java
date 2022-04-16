package pl.gr14b.transylwania;

public class ClientFlags
{
	private double privateLight;
	private double globalLight;
	private boolean forceHalt;
	private int roleCallTime;

	ClientFlags ()
	{
		this.forceHalt = false;
		privateLight = Constants.DEFAULT_PRIVATE_LIGHTNESS;
		roleCallTime = 0;
	}

	public double getPrivateLight() {
		return privateLight;
	}

	public void setPrivateLight(double privateLight) {
		this.privateLight = privateLight;
	}

	public double getGlobalLight() {
		return globalLight;
	}

	public void setGlobalLight(double globalLight) {
		this.globalLight = globalLight;
	}

	public boolean isForceHalt() {
		return forceHalt;
	}

	public void setForceHalt(boolean forceHalt) {
		this.forceHalt = forceHalt;
	}

	public int getRoleCallTime() {
		return roleCallTime;
	}

	public void setRoleCallTime(int roleCallTime) {
		this.roleCallTime = roleCallTime;
	}
}
