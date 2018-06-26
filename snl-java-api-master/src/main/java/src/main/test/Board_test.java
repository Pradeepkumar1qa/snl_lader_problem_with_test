package src.main.test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.qainfotech.tap.training.snl.api.*;

public class Board_test {
	Board board;
	@Test
	public void sucessfull_registration() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		this.board=new Board();
		JSONArray data=board.registerPlayer("pradeep kumar");
		Assert.assertTrue(data.getJSONObject(0).getString("name").equals("pradeep kumar"));
		
	}
	
	
	@Test(dependsOnMethods= {"sucessfull_registration"}, expectedExceptions=PlayerExistsException.class)
	public void unique_name_registration_test() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		
		JSONArray data=board.registerPlayer("pradeep kumar");
		System.out.println(data);
		Assert.assertTrue(data.getJSONObject(0).getString("name").equals("pradeep kumar"));
		board.registerPlayer("pradeep kumar");
		}
	
	@Test(dependsOnMethods= {"unique_name_registration_test"},expectedExceptions=MaxPlayersReachedExeption.class)
	  public void check_maximum_player_test() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, IOException, MaxPlayersReachedExeption {
		
		board.registerPlayer("amit");
		board.registerPlayer("rama");
		board.registerPlayer("shyam");
		//System.out.println(board.getData());
		board.registerPlayer("abc");
		board.registerPlayer("amitdfdsf");
		
	}
	
	@Test(dependsOnMethods= {"check_maximum_player_test"})
	public void roll_dice_test_for_positive_result() throws FileNotFoundException, UnsupportedEncodingException, JSONException, InvalidTurnException {
		int turn=board.getData().getInt("turn");
		//System.out.println("this is turn"+ turn);
		board.rollDice((UUID)board.getData().getJSONArray("players").getJSONObject(turn).get("uuid"));
		
		}
	
	@Test(dependsOnMethods= {"roll_dice_test_for_positive_result"},expectedExceptions=InvalidTurnException.class)
	public void roll_dice_test_for_negative_result() throws FileNotFoundException, UnsupportedEncodingException, JSONException, InvalidTurnException {
		int turn=board.getData().getInt("turn");
		//System.out.println("this is turn"+ turn);
		board.rollDice((UUID)board.getData().getJSONArray("players").getJSONObject(turn+1).get("uuid"));
		
		
	}
	
	@Test(dependsOnMethods= {"roll_dice_test_for_negative_result"},expectedExceptions=NoUserWithSuchUUIDException.class) //to check delete player function 
	public void do_deletion_after_removal_must_throw_exception() throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException  {
		//JSONObject data=board.getData();
		//System.out.println(data.getJSONArray("players"));
		UUID deleteuuid=UUID.fromString( board.getData().getJSONArray("players").getJSONObject(2).get("uuid").toString());
            System.out.println("here is the magic"+deleteuuid.getClass());
            board.deletePlayer(deleteuuid);
            System.out.println("deletion successfully");
            System.out.println("after deleting the player we are going to delete the same one more time ");
            System.out.println(board.getData().getJSONArray("players"));
            board.deletePlayer(deleteuuid);
		
	}
	
	@Test(dependsOnMethods= {"do_deletion_after_removal_must_throw_exception"})
	public void testingdatatypeforuuid() {
		System.out.println("here is the bug"+board.getData().getJSONArray("players").getJSONObject(0).get("uuid").getClass());
		
	}
	

	
}
