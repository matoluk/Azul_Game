package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class Game implements GameInterface{
    private BagInterface bag;
    private TableAreaInterface tableArea;
    private BoardInterface[] boards;
    private ObserverNotifyInterface observer;
    private FinishRoundResult gameStatus = FinishRoundResult.NORMAL;
    private int startPlayer = 0;
    private int currentPlayer = 0;
    Game(BagInterface bag, TableAreaInterface tableArea, BoardInterface[] boards, ObserverNotifyInterface observer){
        this.bag = bag;
        this.tableArea = tableArea;
        this.boards = boards;
        this.observer = observer;

        observer.notifyEverybody("Game started");
        observer.notifyEverybody("TableArea:\n" + tableArea.state());
        for (int i = 0; i < boards.length; i++)
            observer.notifyEverybody("Player"+i+"'s Board:\n" + boards[i].state());
        observer.notifyEverybody("Starts player " + startPlayer);
    }

    @Override
    public boolean take(int playerId, int sourceId, int idx, int destinationId) {
        if (gameStatus == FinishRoundResult.GAME_FINISHED)
            return false;
        if (playerId != currentPlayer)
            return false;
        Tile[] tiles = tableArea.take(sourceId, idx);
        if (tiles == null)
            return false;

        for (Tile tile : tiles)
            if (tile == Tile.STARTING_PLAYER)
                startPlayer = playerId;
        boards[playerId].put(destinationId, tiles);
        currentPlayer++;
        if (currentPlayer >= boards.length)
            currentPlayer = 0;

        observer.notifyEverybody("TableArea:\n" + tableArea.state());
        observer.notifyEverybody("Player"+playerId+"'s Board:\n" + boards[playerId].state());
        if(!tableArea.isRoundEnd())
            observer.notifyEverybody("Current player " + currentPlayer);
        else
            roundEnd();

        return true;
    }
    private void roundEnd(){
        for (BoardInterface board : boards)
            if (board.finishRound() == FinishRoundResult.GAME_FINISHED)
                gameStatus = FinishRoundResult.GAME_FINISHED;

        observer.notifyEverybody("Round ended");
        for (int i = 0; i < boards.length; i++)
            observer.notifyEverybody("Player"+i+"'s Board:\n" + boards[i].state());

        if (gameStatus == FinishRoundResult.NORMAL) {
            observer.notifyEverybody("New round");
            tableArea.startNewRound();
            observer.notifyEverybody("TableArea:\n" + tableArea.state());
            currentPlayer = startPlayer;
            observer.notifyEverybody("Starts player " + startPlayer);
        }else
            endGame();
    }
    private void endGame(){
        observer.notifyEverybody("Game ended");
        int maxPoints = 0;
        ArrayList<Integer> winPlayers = new ArrayList<>();
        for (int player = 0; player < boards.length; player++) {
            boards[player].endGame();
            int points = boards[player].getPoints().getValue();
            observer.notifyEverybody("Player"+player+": "+points+" points");

            if (points > maxPoints){
                maxPoints = points;
                winPlayers.clear();
            }
            if (points == maxPoints)
                winPlayers.add(player);
        }
        for (int player : winPlayers)
            observer.notifyEverybody("Player"+player+" wins");
    }
}