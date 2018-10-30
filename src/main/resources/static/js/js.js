document.onkeydown = checkKey;

let canvas;
let canvas2d;
let alienCanvas;
let canvasTank;
let buletCanvas;
let boomCanvas;

let tank;
let aliens = [];
let activeBulet = true;
let bulet;
let resultTime;
let result;
let timerVar;
let totalSeconds;
let userEmail;
let bestResultTime;
let bestResult;

let basePath = "http://localhost:8080";

let resultList;

function prepareCanvas() {
    if (!$("canvas").get(0)) {
        drawCanvas();
    }
    canvas = $("#can").get(0);

    if (canvas.getContext) {
        canvas2d = canvas.getContext("2d");
    }
}

function startGame() {
    prepareCanvas();
    userEmail = $("#principal").get(0).innerText;
    totalSeconds = 0;
    result = 0;
    resultTime = 0;
    alienCanvas = $("#alien").get(0);
    canvasTank = $("#tank").get(0);
    buletCanvas = $("#bulet").get(0);
    boomCanvas = $("#boom").get(0);
    tank = new Tank(canvas.clientWidth / 2, canvas.clientHeight - 50);
    canvas2d.drawImage(canvasTank, tank.x, tank.y, 40, 40);
    timerVar = setInterval(countTimer, 1000);
    activeBulet = false;
    $("#startButton").get(0).disabled = true;
    startAliens();
}

function drawCanvas() {
    if ($("#game-over-frame")) {
        $("#game-over-frame").remove();
    }
    $("#game").append(
        `<canvas width="1420" height="800" id="can">
                    <div id="canvas-elements">
                        <img id="alien" alt="Alien" src="/img/alien.png" width="40" height="40">
                        <img id="tank" alt="Tank" src="/img/tank.png" width="40" height="40">
                        <img id="bulet" alt="Bulet" src="/img/bulet.png" width="20" height="30">
                        <img id="boom" alt="Boom" src="/img/boom.jpg " width="40" height="40">
                    </div>
                </canvas>`
    );
}

function startAliens() {
    for (let a = 0; a < 3; a++) {
        aliens["alien_" + a] = new Alien();
        aliens["alien_" + a].start();
    }
}

function gameOver() {
    for (alien in aliens) {
        aliens[alien].kill();
    }
    if (bulet) {
        bulet.kill();
    }

    resultTime = $("#timer").get(0).innerHTML;
    result = $("#result").get(0).innerHTML;

    saveGame();

    bestResultTime = getBestResultTime();
    bestResult = getBestResult();
    resultList = getResults();

    $("#startButton").get(0).disabled = false;
    $("#timer").get(0).innerHTML = "00:00:00";
    $("#result").get(0).innerHTML = 0;

    $("#principal").get(0).innerHTML = userEmail;
    $("p#user-best-time").get(0).innerHTML = bestResultTime;
    $("p#user-best-result").get(0).innerHTML = bestResult;

    clearInterval(timerVar);
    gameOverFrame();
    generateResultTable(resultList);
}


function alienInterval(alien) {
    alien.alienInter = setInterval(function () {
        canvas2d.clearRect(alien.x, alien.y, 40, 40);
        alien.img = canvas2d.drawImage(alienCanvas, alien.x, alien.y += 5, 40, 40);

        if (alien.y >= canvas.clientHeight || result >= 50) {
            gameOver();
        }
    }, 300)
}

function buletInterval(bulet) {
    bulet.buletInterv = setInterval(function () {

        if ((bulet.x == tank.x + 10) && (bulet.y == tank.y - 20)) {
            canvas2d.clearRect(bulet.x + 10, bulet.y - 20, 20, 30)
        } else {
            canvas2d.clearRect(bulet.x, bulet.y, 20, 30)
        }

        bulet.img = canvas2d.drawImage(buletCanvas, bulet.x, bulet.y -= 5, 20, 30);
        if (bulet.y <= 0) {
            activeBulet = false;
            clearInterval(bulet.buletInterv);
            canvas2d.clearRect(bulet.x, bulet.y, 20, 30)
        }

        let alienArray = [];
        let buletArray = [];

        for (alien in aliens) {
            for (g = aliens[alien].y; g <= aliens[alien].y + 10; g++) {
                for (a = aliens[alien].x; a <= aliens[alien].x + 40; a++) {
                    alienArray.push(new AlienPoint(aliens[alien], a, g))
                }
            }
        }

        for (a = bulet.x; a <= bulet.x + 20; a++) {
            buletArray.push(new Point(a, bulet.y));
        }

        for (alf of alienArray) {
            for (bul of buletArray) {
                if (alf.x == bul.x && alf.y == bul.y) {
                    result += 1;
                    $("#result").get(0).innerHTML = result;
                    activeBulet = false;

                    alf.object.kill();
                    bulet.kill();

                    canvas2d.clearRect(alf.object.x, alf.object.y, 40, 40);
                    canvas2d.clearRect(bulet.x, bulet.y, 20, 30);

                    let boom = new Point(bulet.x, bulet.y);
                    canvas2d.drawImage(boomCanvas, boom.x, boom.y, 40, 40);

                    let timeRun = 0;
                    let boomInterval = setInterval(function () {
                        timeRun += 1;
                        if (timeRun == 10) {
                            canvas2d.clearRect(boom.x, boom.y, 40, 40);
                            clearInterval(boomInterval);
                        }
                    }, 1000);

                    for (alien in aliens) {
                        if (alf.object === aliens[alien]) {
                            aliens[alien] = new Alien();
                            aliens[alien].start();
                        }
                    }

                    return;
                }
            }
        }
    }, 60)
}

function checkKey(e) {
    e = e || window.event;

    if (e.keyCode == "37") {
        // alert("left arrow");
        if (tank.x == 0) return;
        canvas2d.clearRect(tank.x, tank.y, 40, 40);
        tank.img = canvas2d.drawImage(canvasTank, tank.x -= 5, tank.y, 40, 40);

    }
    else if (e.keyCode == "39") {
        // alert("right arrow");
        if (tank.x == canvas.clientWidth) return;
        canvas2d.clearRect(tank.x, tank.y, 40, 40);
        tank.img = canvas2d.drawImage(canvasTank, tank.x += 5, tank.y, 40, 40);

    }
    else if (e.keyCode == "32") {
        if (activeBulet) return;
        activeBulet = true;
        bulet = new Bulet(tank.x + 10, tank.y - 30);
        bulet.start();
    }

}

function countTimer() {
    ++totalSeconds;
    let hour = Math.floor(totalSeconds / 3600);
    let minute = Math.floor((totalSeconds - hour * 3600) / 60);
    let seconds = totalSeconds - (hour * 3600 + minute * 60);

    if (hour.toString().length < 2) {
        hour = "0" + hour
    }
    if (minute.toString().length < 2) {
        minute = "0" + minute
    }
    if (seconds.toString().length < 2) {
        seconds = "0" + seconds
    }
    $("#timer").get(0).innerHTML = hour + ":" + minute + ":" + seconds;
}

function gameOverFrame() {
    $("canvas").replaceWith("<div id=\"game-over-frame\"></div>");
}

function randomX() {
    return Math.floor(Math.random() * (canvas.clientWidth - 40)) + 1;
}


function generateResultTable(resultList) {
    $("#game-over-frame").append(
        `<table id="result-table"><tr><th><p>User</p></th><th><p>Result Time</p></th><th><p>Result</p></th></tr></table>`
    );

    for (result of Object.keys(resultList)) {
        var res = obj[result];
        console.log("generatetable: " + res)
        $("#result-table").append(
            `<tr><td>${res.userEmail}</td><td>${res.resultTime}</td><td>${res.result}</td></tr>`
        )
    }
}

async function saveGame() {
    let gameResult = {
        userEmail: userEmail,
        resultTime: resultTime,
        result: result
    };
    try {
        await fetch(basePath + "/user/game/result", {
            method: 'POST',
            body: JSON.stringify(gameResult),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        // await $.ajax({
        //     url: basePath + "/user/game/result",
        //     type: 'POST',
        //     contentType: "application/json",
        //     data: JSON.stringify(gameResult),
        //     dataType: "json"
        // });
    } catch (e) {
        console.log("save game error =======================");
        console.log(e);
    }
}

async function getResults() {
    console.log("getResult: ================");
    try {
        const result =  await fetch(basePath + "/user/game/result").then(res => {
            var response = JSON.parse(res);
        console.log("getResult response: " + response);
            return response.text()
        })
            .then((data) => {
                resolve(data ? JSON.parse(data) : {})
            })
            .catch((error) => {
                console.log("get Result error 1 =======================");
                reject(error)
            });

        this.resultList = result;
        // const result = await $.getJSON(basePath + "/user/game/result", (data) => {
        //     console.log("getResult: " + data.toString());
        //     return data;
        // });
    } catch (e) {
        console.log("get Result error 2 =======================");
        console.log(e);
    }
}

async function getBestResult() {
    console.log("getBestResult ================");
    const bestResult = await fetch(basePath + "/user/game/bestresult").then(res => {
        var response = JSON.parse(res);
        console.log("getBestResult response: " + response);
        return response.text()
    })
        .then((data) => {
            resolve(data ? JSON.parse(data) : {})
        })
        .catch((error) => {
            console.log("getBestResult error =======================");
            reject(error)
        });
    console.log("bestResult: " + bestResult);
    this.bestResult = bestResult;

    // $.getJSON(basePath + "/user/game/bestresult", (data) => {
    //     if (data == null) {
    //         console.log("getBestResult: null ================");
    //         return '00:00:00';
    //     }
    //     console.log("getBestResult: " + data.toString());
    //     return data;
    // });
}

async function getBestResultTime() {
    console.log("getBestResultTime ================");

    const bestResultTime = await fetch("/user/game/bestresulttime").then(res => {
        var response = JSON.parse(res);
        console.log("getBestResultTime response: " + response);
        return response.text()
    })
        .then((data) => {
            resolve(data ? JSON.parse(data) : {})
        })
        .catch((error) => {
            console.log("getBestResultTime error =======================");
            reject(error)
        });

    console.log("bestResultTime: " + bestResultTime);
    this.bestResultTime = bestResultTime;

    // $.getJSON(basePath + "/user/game/bestresulttime", (data) => {
    //     if (data == null) {
    //         console.log("getBestResultTime null ================");
    //         return '0'
    //     }
    //     console.log("getBestResultTime: " + data.toString());
    //     return data;
    // });
}