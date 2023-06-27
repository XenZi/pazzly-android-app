import { log } from "console";
import { createServer } from "http";
import { Server } from "socket.io";

const httpServer = createServer();
const io = new Server(httpServer, {
  cors: {
    origin: "*",
  },
});

io.on("connection", (socket) => {
  console.log("LOGGED TO SOCKET");
});

httpServer.listen(3001, () => {
  console.log("server is running");
});
