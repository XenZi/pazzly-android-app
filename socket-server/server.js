import { createServer } from "http";
import { Server } from "socket.io";
import GameStat from "./model/GameStat.js";
import { v4 as uuidv4 } from "uuid";
import { log } from "console";
const httpServer = createServer();
const io = new Server(httpServer, {
  cors: {
    origin: "*",
  },
});

const calculatePointsForMojBroj = (wantedNumber, firstResult, secondResult) => {
  console.log(
    "Wanted number, result, second result" + wantedNumber,
    firstResult,
    secondResult
  );
  if (firstResult == wantedNumber && secondResult !== wantedNumber) {
    return {
      points: 20,
      player: "first",
    };
  }

  if (secondResult == wantedNumber && firstResult !== wantedNumber) {
    return {
      points: 20,
      player: "second",
    };
  }

  if (firstResult != wantedNumber && secondResult != wantedNumber) {
    let firstPlayerDivision = wantedNumber - firstResult;
    let secondPlayerDivision = wantedNumber - secondResult;

    if (firstPlayerDivision > secondPlayerDivision) {
      return {
        points: 5,
        player: "second",
      };
    }
    if (secondPlayerDivision > firstPlayerDivision) {
      return {
        points: 5,
        player: "first",
      };
    }
  }
};

const waitingPlayers = [];
const activeMatches = {}; // Čuva informacije o aktivnim mečevima i rezultatima igrača
io.on("connection", (socket) => {
  socket.on("joinQueue", (data) => {
    waitingPlayers.push({ socket, ...data });
    console.log("Player joined queue:", socket.id);
    console.log("Player with following data: ", { ...data });

    if (waitingPlayers.length >= 2) {
      const player1 = waitingPlayers.shift();
      const player2 = waitingPlayers.shift();
      const { socket, ...deconstructedPlayer1 } = player1;
      const { socket: _, ...deconstructedPlayer2 } = player2;
      const matchId = uuidv4();
      const matchDetails = {
        id: matchId,
        player1: deconstructedPlayer1,
        player2: deconstructedPlayer2,
        turn: player1.id,
        player1Points: 0,
        player2Points: 0,
        gameResults: {
          player1Result: null,
          player2Result: null,
        },
      };
      activeMatches[matchId] = matchDetails;
      player1.socket.join(matchId);
      player2.socket.join(matchId);
      io.to(matchId).emit("startMatch", matchDetails);
    }
  });

  // Event kada se igrač odspoji
  socket.on("disconnect", () => {
    // Proveri da li je igrač u redu za čekanje i ukloni ga
    const index = waitingPlayers.indexOf(socket);
    if (index !== -1) {
      waitingPlayers.splice(index, 1);
      console.log("Player removed from queue:", socket.id);
    }
  });

  socket.on(
    "mojBrojResult",
    ({ matchId, playerId, result, wantedNumber, hasMoreRounds }) => {
      console.log("Does it has more rounds? ", hasMoreRounds);
      if (!activeMatches[matchId]) {
        activeMatches[matchId] = { player1Result: null, player2Result: null };
      }

      // Ako je ovo rezultat prvog igrača
      if (playerId === activeMatches[matchId].player1.id) {
        activeMatches[matchId].gameResults.player1Result = result;
      }
      // Ako je ovo rezultat drugog igrača
      else if (playerId === activeMatches[matchId].player2.id) {
        activeMatches[matchId].gameResults.player2Result = result;
      }

      // Proveri da li su oba igrača poslala svoje rezultate
      if (
        activeMatches[matchId].gameResults.player1Result !== null &&
        activeMatches[matchId].gameResults.player2Result !== null
      ) {
        // Emituj konačne rezultate igre
        let calculatedPoints = calculatePointsForMojBroj(
          wantedNumber,
          activeMatches[matchId].gameResults.player1Result,
          activeMatches[matchId].gameResults.player2Result
        );
        if (calculatedPoints.player == "first") {
          calculatedPoints.player = activeMatches[matchId].player1.id;
        } else {
          calculatedPoints.player = activeMatches[matchId].player2.id;
        }
        const finalResults = {
          player1Result: activeMatches[matchId].gameResults.player1Result,
          player2Result: activeMatches[matchId].gameResults.player2Result,
          calculatedPoints: calculatedPoints,
        };

        if (hasMoreRounds) {
          activeMatches[matchId].turn = activeMatches[matchId].player2.id;
        } else {
          activeMatches[matchId].turn = activeMatches[matchId].player1.id;
        }

        if (finalResults.calculatedPoints.player === "first") {
          activeMatches[matchId].player1Points =
            finalResults.calculatedPoints.points;
        } else {
          activeMatches[matchId].player2Points =
            finalResults.calculatedPoints.points;
        }

        console.log({
          ...finalResults,
          ...activeMatches[matchId],
        });
        io.to(matchId).emit("gameFinished", {
          ...finalResults,
          ...activeMatches[matchId],
        });

        activeMatches[matchId].gameResults.player1Result = null;
        activeMatches[matchId].gameResults.player2Result = null;
      }
    }
  );

  socket.on("sendNumbers", ({ matchId, randomNumber, selectedNumbers }) => {
    console.log("Recieved match ID from MojBroj", {
      matchId,
      randomNumber,
      selectedNumbers,
    });
    io.to(matchId).emit("sendRandomNumber", { randomNumber, selectedNumbers });
  });
});

const PORT = 3000;
httpServer.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`);
});
