package winsome.server_app.internal.tasks.impl.test;

import winsome.connection.socket_messages.Message;
import winsome.server_app.internal.tasks.SocketTaskState;

class SocketTaskStateTest implements SocketTaskState
{
	public boolean set_user_called = false;
	public boolean unset_user_called = false;
	public Message sent_message = null;

	@Override
	public String getClientUser()
	{
		if(set_user_called)
			return "user";
		else
			return null;
	}

	@Override
	public void setClientUser(String username)
	{
		set_user_called = true;
	}

	@Override
	public void unsetClientUser()
	{
		unset_user_called = true;
	}

	@Override
	public Message getRequestMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendAnswerMessage(Message answer)
	{
		sent_message = answer;
	}

}
