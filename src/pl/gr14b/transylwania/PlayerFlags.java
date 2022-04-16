package pl.gr14b.transylwania;

import java.io.Serializable;

public class PlayerFlags implements Serializable
{
	// PlayerFlags
	private boolean spacePressed;
	private int spacedPressedEnabled;
	private boolean forcingSynchronization;
	private boolean forcingLocationSynchronization;
	private boolean playerMoving;
	private int afkPenalty;
	private PlayerType playerType;
	private int nextPacket;
	private String nextSoundInQue;

	public PlayerFlags ()
	{
		this.forcingSynchronization = false;
		this.playerType = PlayerType.SURVIVOR;
		this.nextPacket = 0;
		this.nextSoundInQue = null;
		this.forcingLocationSynchronization = false;
		this.spacePressed = false;
		this.spacedPressedEnabled = 0;
		this.playerMoving = false;
	}

	void nextPacket ()
	{
		if (++this.nextPacket > Constants.PACKET_COUNT)
			this.nextPacket = 1;
	}

	void setSpacePressedDisabled(int delay)
	{
		spacePressed = false;
		spacedPressedEnabled = delay;
		forcingSynchronization = true;
	}

	void setSpacePressedEnabled()
	{
		spacePressed = (spacedPressedEnabled <= 0);
	}

	void copyFlags(Player otherPlayer)
	{
		spacePressed = otherPlayer.getPlayerFlags().spacePressed;
		spacedPressedEnabled = otherPlayer.getPlayerFlags().spacedPressedEnabled;
		nextPacket = otherPlayer.getPlayerFlags().nextPacket;
		playerMoving = otherPlayer.getPlayerFlags().playerMoving;
		nextSoundInQue = otherPlayer.getPlayerFlags().nextSoundInQue;
	}

	void updateFlagStatusCounters() {
		spacedPressedEnabled = Math.max(spacedPressedEnabled - 1, 0);
	}

	void reset ()
	{
		this.forcingSynchronization = true;
		this.spacePressed = false;
		playerType = PlayerType.SURVIVOR;
	}

	public boolean isSpacePressed() {
		return spacePressed;
	}

	public void setSpacePressed(boolean spacePressed) {
		this.spacePressed = spacePressed;
	}

	public int getSpacedPressedEnabled() {
		return spacedPressedEnabled;
	}

	public void setSpacedPressedEnabled(int spacedPressedEnabled) {
		this.spacedPressedEnabled = spacedPressedEnabled;
	}

	public boolean isForcingSynchronization() {
		return forcingSynchronization;
	}

	public void setForcingSynchronization(boolean forcingSynchronization) {
		this.forcingSynchronization = forcingSynchronization;
	}

	public boolean isForcingLocationSynchronization() {
		return forcingLocationSynchronization;
	}

	public void setForcingLocationSynchronization(boolean forcingLocationSynchronization) {
		this.forcingLocationSynchronization = forcingLocationSynchronization;
	}

	public boolean isPlayerMoving() {
		return playerMoving;
	}

	public void setPlayerMoving(boolean playerMoving) {
		this.playerMoving = playerMoving;
	}

	public int getAfkPenalty() {
		return afkPenalty;
	}

	public void setAfkPenalty(int afkPenalty) {
		this.afkPenalty = afkPenalty;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public int getNextPacket() {
		return nextPacket;
	}

	public void setNextPacket(int nextPacket) {
		this.nextPacket = nextPacket;
	}

	public String getNextSoundInQue() {
		return nextSoundInQue;
	}

	public void setNextSoundInQue(String nextSoundInQue) {
		this.nextSoundInQue = nextSoundInQue;
	}
}
