package clientPackage;

interface GameCall {
    void gameCommunications(int index, int openCards, boolean toPass);
    void sendChatMessage(String message, boolean toPass);
    void sendEndOfGame(boolean win, boolean draw, boolean toPass, boolean logOut);
    void logOut(boolean toPass);
    void showLeaderBoard();
}
