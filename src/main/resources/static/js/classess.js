class Alien {

    constructor() {
        this.x = randomX();
        this.y = 10;
        this.img = $("#alien");
        this.alienInter = null;
    }

    kill() {
        clearInterval(this.alienInter)
    }

    start() {
        alienInterval(this);
    }

}

class Bulet {

    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.img = $("#bulet");
        this.buletInterv = null;
    }

    start() {
        buletInterval(this);
    }

    kill() {
        activeBulet = false;
        clearInterval(this.buletInterv)
    }

}

class Tank {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.img = $("#tank");
    }

}

class AlienPoint {
    constructor(object, x, y) {
        this.object = object;
        this.x = x;
        this.y = y;
    }
}

class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

class GameResult {
    constructor(userName, resultTime, result) {
        this.userName = userName;
        this.resultTime = resultTime;
        this.result = result;
    }
}