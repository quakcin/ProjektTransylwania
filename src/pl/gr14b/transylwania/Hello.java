package pl.gr14b.transylwania;

import java.io.Serializable;


class Hello implements Serializable
{
	private String nickName;

	Hello (String playerNickName)
	{
		nickName = playerNickName;
	}

	String getNickName()
	{
		return nickName;
	}
}
