import { createServer } from "http";
import { Server } from "socket.io";
import GameStat from "./model/GameStat.js";
const httpServer = createServer();
const io = new Server(httpServer, {
  cors: {
    origin: "*",
  },
});
let game = new GameStat();
io.on("connection", (socket) => {
  console.log("LOGGED TO SOCKET");

  io.emit("gameStat", game);
});

httpServer.listen(3001, () => {
  console.log("server is running");
});
