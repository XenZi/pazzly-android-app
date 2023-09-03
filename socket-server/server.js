import { createServer } from "http";
import { Server } from "socket.io";
import { v4 as uuidv4 } from "uuid";
import { initializeApp } from "firebase/app";
import { getFirestore, collection, getDocs, addDoc } from "firebase/firestore";

const httpServer = createServer();
const io = new Server(httpServer, {
  cors: {
    origin: "*",
  },
});

const firebaseConfig = {
  apiKey: "AIzaSyBddmSNTkz9ZnJ3ozBj2JX2TiYp69ySExw",
  authDomain: "project-pazzly-b47a8.firebaseapp.com",
  projectId: "project-pazzly-b47a8",
  storageBucket: "project-pazzly-b47a8.appspot.com",
  messagingSenderId: "1068294650791",
  appId: "1:1068294650791:web:47aca8887903a10acb1482",
  measurementId: "G-QGD7FMB5V7",
};

const app = initializeApp(firebaseConfig);

const db = getFirestore();
const collectionRef = collection(db, "korakpokorak");

const getCollectionData = async (name) => {
  const querySnapshot = await getDocs(collection(db, name));
  return querySnapshot.docs.map((doc) => doc.data());
};

const getRandomItemForKorakPoKorak = async () => {
  const collectionData = await getCollectionData("korakpokorak");
  const randomIndex = Math.floor(Math.random() * collectionData.length);
  return collectionData[randomIndex];
};

const getItemsForKoZnaZna = async () => {
  const collectionData = await getCollectionData("koznazna");
  return collectionData;
};

const findCloserNumber = (target, number1, number2) => {
  const difference1 = Math.abs(target - number1);
  const difference2 = Math.abs(target - number2);

  if (difference1 < difference2) {
    return {
      points: 5,
      player: "first",
    };
  } else {
    return {
      points: 5,
      player: "second",
    };
  }
};

const calculatePointsForKorakPoKorak = (step) => {
  return 20 - 2 * step;
};

const calculatePointsForMojBroj = (wantedNumber, firstResult, secondResult) => {
  console.log(
    "Wanted number, result, second result " + wantedNumber,
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
    console.log(findCloserNumber(wantedNumber, firstResult, secondResult));
    return findCloserNumber(wantedNumber, firstResult, secondResult);
  }
};

const waitingPlayers = [];
const activeMatches = {}; // Čuva informacije o aktivnim mečevima i rezultatima igrača

const saveMatchHistoryInDatabase = async (matchId) => {
  const constructedObject = {
    player1: activeMatches[matchId].player1.id,
    player2: activeMatches[matchId].player2.id,
    player1Points: activeMatches[matchId].player1Points,
    player2Points: activeMatches[matchId].player2Points,
    ...activeMatches[matchId].gameDetailsResultPoints,
  };
  const matchesRef = collection(db, "matches"); // Reference to the "matches" collection
  const newMatchRef = await addDoc(matchesRef, constructedObject); // Create a new document with the specified data
  console.log("SAVED", newMatchRef);
};

io.on("connection", (socket) => {
  socket.on("joinQueue", (data) => {
    waitingPlayers.push({ socket, ...data });

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
        koZnaZnaResults: {
          player1: {
            time: null,
            isCorrect: null,
          },
          player2: {
            time: null,
            isCorrect: null,
          },
        },
        currentActiveGame: 0,
        currentActiveRound: 1,
        gameDetailsResultPoints: {
          koZnaZna: {
            player1: 0,
            player2: 0,
          },
          korakPoKorak: [
            {
              player1: 0,
              player2: 0,
            },
            {
              player1: 0,
              player2: 0,
            },
          ],
          mojBroj: [
            {
              player1: 0,
              player2: 0,
            },
            {
              player1: 0,
              player2: 0,
            },
          ],
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
      if (!activeMatches[matchId]) {
        activeMatches[matchId] = { player1Result: null, player2Result: null };
      }

      if (playerId === activeMatches[matchId].player1.id) {
        activeMatches[matchId].gameResults.player1Result = result;
      } else if (playerId === activeMatches[matchId].player2.id) {
        activeMatches[matchId].gameResults.player2Result = result;
      }

      if (
        activeMatches[matchId].gameResults.player1Result !== null &&
        activeMatches[matchId].gameResults.player2Result !== null
      ) {
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
            activeMatches[matchId].player1Points +
            finalResults.calculatedPoints.points;

          if (!hasMoreRounds) {
            activeMatches[matchId].gameDetailsResultPoints.mojBroj[1].player1 =
              finalResults.calculatedPoints.points;
          } else {
            activeMatches[matchId].gameDetailsResultPoints.mojBroj[0].player1 =
              finalResults.calculatedPoints.points;
          }
        } else {
          activeMatches[matchId].player2Points =
            activeMatches[matchId].player2Points +
            finalResults.calculatedPoints.points;
          if (!hasMoreRounds) {
            activeMatches[matchId].gameDetailsResultPoints.mojBroj[1].player2 =
              finalResults.calculatedPoints.points;
          } else {
            activeMatches[matchId].gameDetailsResultPoints.mojBroj[0].player2 =
              finalResults.calculatedPoints.points;
          }
        }
        saveMatchHistoryInDatabase(matchId);
        console.log(activeMatches[matchId].gameDetailsResultPoints);
        io.to(matchId).emit("gameFinished", {
          ...finalResults,
          ...activeMatches[matchId],
        });
      }
    }
  );

  socket.on("sendNumbers", ({ matchId, randomNumber, selectedNumbers }) => {
    io.to(matchId).emit("sendRandomNumber", { randomNumber, selectedNumbers });
  });

  socket.on("korakPoKorakInitialStart", ({ matchId }) => {
    getRandomItemForKorakPoKorak()
      .then((randomItem) => {
        const savedRandomItem = { ...randomItem };
        io.to(matchId).emit("korakPoKorakValues", randomItem);

        socket.on(
          "submitFinalAnswerForKorakPoKorakInvalidAnswer",
          ({ matchId, answer }) => {
            io.to(matchId).emit("korakPoKorakSubmitedAnswer", {
              answer,
            });
          }
        );

        socket.on(
          "submitFinalAnswerForKorakPoKorakCorrect",
          ({ matchId, answer, playerTurnId, step, hasMoreRounds }) => {
            console.log("DOES IT HAS MORE ROUNDS???", hasMoreRounds);
            if (playerTurnId == activeMatches[matchId].player1.id) {
              activeMatches[matchId].player1Points =
                activeMatches[matchId].player1Points +
                calculatePointsForKorakPoKorak(step);
              if (!hasMoreRounds) {
                activeMatches[
                  matchId
                ].gameDetailsResultPoints.korakPoKorak[1].player1 =
                  calculatePointsForKorakPoKorak(step);
              } else {
                activeMatches[
                  matchId
                ].gameDetailsResultPoints.korakPoKorak[0].player1 =
                  calculatePointsForKorakPoKorak(step);
              }
            } else {
              activeMatches[matchId].player2Points =
                activeMatches[matchId].player2Points +
                calculatePointsForKorakPoKorak(step);
              if (!hasMoreRounds) {
                activeMatches[
                  matchId
                ].gameDetailsResultPoints.korakPoKorak[1].player2 =
                  calculatePointsForKorakPoKorak(step);
              } else {
                activeMatches[
                  matchId
                ].gameDetailsResultPoints.korakPoKorak[0].player2 =
                  calculatePointsForKorakPoKorak(step);
              }
            }

            io.to(matchId).emit("endGameKorakPoKorak", {
              ...activeMatches[matchId],
            });
          }
        );
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  });

  socket.on("koZnaZnaInitialStart", ({ matchId }) => {
    getItemsForKoZnaZna().then((data) => {
      io.to(matchId).emit("koZnaZnaQuestions", { data });
      socket.on("koZnaZnaAnswer", ({ matchId, player, time, isCorrect }) => {
        const match = activeMatches[matchId];
        const player1Results = match.koZnaZnaResults.player1;
        const player2Results = match.koZnaZnaResults.player2;

        const isPlayer1 = player === match.player1.id;

        const currentPlayerResults = isPlayer1
          ? player1Results
          : player2Results;

        currentPlayerResults.time = time;
        currentPlayerResults.isCorrect = isCorrect;

        if (player1Results.time !== null && player2Results.time !== null) {
          const bothCorrect =
            player1Results.isCorrect && player2Results.isCorrect;
          const player1Faster = player1Results.time < player2Results.time;

          if (bothCorrect) {
            if (player1Faster) {
              match.player1Points += 10;
            } else {
              match.player2Points += 10;
            }
          } else {
            if (player1Results.isCorrect) {
              match.player1Points += 10;
              match.player2Points -= 5;
            } else if (player2Results.isCorrect) {
              match.player2Points += 10;
              match.player1Points -= 5;
            } else {
              match.player1Points -= 5;
              match.player2Points -= 5;
            }
          }
          io.to(matchId).emit("koZnaZnaMatchUpdate", { ...match });

          match.gameDetailsResultPoints.koZnaZna.player1 = match.player1Points;
          match.gameDetailsResultPoints.koZnaZna.player2 = match.player2Points;

          player1Results.time = null;
          player1Results.isCorrect = false;
          player2Results.time = null;
          player2Results.isCorrect = false;

          activeMatches[matchId] = match;
        }
      });
    });
  });
});

const PORT = 3000;
httpServer.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`);
});

// xiami je y
// samsung je q
