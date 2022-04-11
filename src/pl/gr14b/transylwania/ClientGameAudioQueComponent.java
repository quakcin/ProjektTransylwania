package pl.gr14b.transylwania;

class ClientGameAudioQueComponent extends ClientGameComponent
{
	ClientGameAudioQueComponent(Client client) {
		super(client);
	}


	private boolean isSoundQueEmpty()
	{
		return clientGame.getPlayer().getNextSoundInQue() == null;
	}

	private void playAndRemoveSoundFromQue()
	{
		Stuff.playSound(clientGame.getPlayer().getNextSoundInQue());
		clientGame.getPlayer().setNextSoundInQue(null);
	}

	@Override
	void updateComponent()
	{
		if (!isSoundQueEmpty())
			playAndRemoveSoundFromQue();
	}
}
