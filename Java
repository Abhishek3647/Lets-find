// Game variables
let player = document.getElementById('player');
let healthBar = document.getElementById('health-bar');
let enemies = document.querySelectorAll('.enemy');
let bullet = document.getElementById('bullet');
let scoreElement = document.getElementById('score');

let playerX = 380, playerY = 550; // Starting position of the player
let playerSpeed = 5;
let bulletSpeed = 10;
let playerHealth = 100;
let score = 0;

let bullets = [];
let enemySpeed = 2;

// Player movement
document.addEventListener('keydown', movePlayer);
document.addEventListener('keydown', shootBullet);

function movePlayer(event) {
    if (event.key === 'ArrowLeft' || event.key === 'a') {
        playerX -= playerSpeed;
    } else if (event.key === 'ArrowRight' || event.key === 'd') {
        playerX += playerSpeed;
    } else if (event.key === 'ArrowUp' || event.key === 'w') {
        playerY -= playerSpeed;
    } else if (event.key === 'ArrowDown' || event.key === 's') {
        playerY += playerSpeed;
    }

    // Ensure player stays within bounds
    playerX = Math.max(0, Math.min(playerX, 760));
    playerY = Math.max(0, Math.min(playerY, 560));

    player.style.left = playerX + 'px';
    player.style.top = playerY + 'px';
}

function shootBullet(event) {
    if (event.key === ' ') {
        // Create a bullet and start moving it
        let bulletX = playerX + 17; // Bullet position centered on the player
        let bulletY = playerY;

        let bulletElement = document.createElement('div');
        bulletElement.classList.add('bullet');
        bulletElement.style.left = bulletX + 'px';
        bulletElement.style.top = bulletY + 'px';

        document.getElementById('game-container').appendChild(bulletElement);

        bullets.push(bulletElement);
    }
}

// Move bullets
function moveBullets() {
    for (let bulletElement of bullets) {
        let bulletY = parseInt(bulletElement.style.top);
        bulletElement.style.top = (bulletY - bulletSpeed) + 'px';

        // Remove bullet if it goes out of bounds
        if (bulletY < 0) {
            bulletElement.remove();
            bullets.splice(bullets.indexOf(bulletElement), 1);
        }

        // Check if bullet hits enemy
        for (let enemy of enemies) {
            if (checkCollision(bulletElement, enemy)) {
                enemy.style.backgroundColor = 'green'; // Change enemy color on hit
                bulletElement.remove();
                bullets.splice(bullets.indexOf(bulletElement), 1);
                score++;
                scoreElement.textContent = 'Score: ' + score;
                break;
            }
        }
    }
}

// Check collision between bullet and enemy
function checkCollision(bulletElement, enemyElement) {
    let bulletRect = bulletElement.getBoundingClientRect();
    let enemyRect = enemyElement.getBoundingClientRect();

    return !(bulletRect.right < enemyRect.left ||
             bulletRect.left > enemyRect.right ||
             bulletRect.bottom < enemyRect.top ||
             bulletRect.top > enemyRect.bottom);
}

// Enemy movement
function moveEnemies() {
    enemies.forEach((enemy, index) => {
        let enemyX = parseInt(enemy.style.left) || Math.random() * 760;
        let enemyY = parseInt(enemy.style.top) || -40;

        // Move enemy towards player
        let dx = playerX - enemyX;
        let dy = playerY - enemyY;
        let distance = Math.sqrt(dx * dx + dy * dy);

        let moveX = (dx / distance) * enemySpeed;
        let moveY = (dy / distance) * enemySpeed;

        enemy.style.left = (enemyX + moveX) + 'px';
        enemy.style.top = (enemyY + moveY) + 'px';

        // Check if enemy hits the player
        if (checkCollision(enemy, player)) {
            playerHealth -= 1;
            healthBar.style.width = playerHealth + '%';

            if (playerHealth <= 0) {
                gameOver();
            }
        }
    });
}

// Game Over
function gameOver() {
    alert('Game Over! Final Score: ' + score);
    window.location.reload(); // Restart game
}

// Game loop
function gameLoop() {
    moveBullets();
    moveEnemies();
}

// Start the game loop
setInterval(gameLoop, 20);
