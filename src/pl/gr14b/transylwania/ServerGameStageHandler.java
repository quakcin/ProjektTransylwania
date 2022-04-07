package pl.gr14b.transylwania;

import java.util.ArrayList;

enum GameStatus
{
	LOBBY, INTRO, KILLING, SUMMARY
}

public class ServerGameStageHandler
{
	// -- Game Status (Engine) flags

	ArrayList<ServerGameStage> gameStages;

	ServerGameStageHandler (ServerBackgroundThread server)
	{
		gameStages = new ArrayList<>();
		gameStages.add(new ServerGameLobbyStage(server, GameStatus.LOBBY));
		gameStages.add(new ServerGameIntroStage(server, GameStatus.INTRO));
		gameStages.add(new ServerGameKillingStage(server, GameStatus.KILLING));
		gameStages.add(new ServerGameSummaryStage(server, GameStatus.SUMMARY));
	}

	void UpdateStage (GameStatus stageCode)
	{
		for (ServerGameStage gameStage : gameStages)
			if (gameStage.isSpecificStage(stageCode))
			{
				gameStage.UpdateStage();
				return;
			}
	}

}
