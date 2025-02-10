import { Game } from './objects/game.js';
import { CreateGameDTO } from './objects/createGameDTO.js'; 
import { FieldDTO } from './objects/fieldDTO.js';
import { CellType } from './objects/cellType.js';

const playerButtons = document.querySelectorAll('.player');
const restartButton = document.getElementById('restartButton');
const statusBar = document.getElementById('statusBar');
let isPlayerSelected = false;
let currentTurn = CellType.X;
let winState = false;

let game = new Game(null, CellType.X);

playerButtons.forEach(button => { button.addEventListener('click', () => changePlayerSide(button)); });

function toCreateGameDTO(game) {
  const dto = new CreateGameDTO(game.getPlayerSide());

  for(let i = 0; i < 3; i++)
    for(let j = 0; j < 3; j++)
      dto.updateCell(i, j, game.getGameField()[i][j]);

  return dto;
}

function toFieldDTO(game) {
  const dto = new FieldDTO();

  for(let i = 0; i < 3; i++)
    for(let j = 0; j < 3; j++)
      dto.updateCell(i, j, game.getGameField()[i][j]);

  return dto;
}

function gameUpdate(field) {
  if(field == null) return;

  for(let i = 0; i < 3; i++)
    for(let j = 0; j < 3; j++)
      game.getGameField()[i][j] = field[i][j];
}

function changeStatus(){
  currentTurn = ((currentTurn == 'X') ? 'O' : 'X');
  statusBar.textContent = `Turn ${currentTurn.toUpperCase()}`;

  switchBacklight();
}

function switchBacklight() {
  let btn1 = null;
  let btn2 = null;

  for (const btn of playerButtons) {
    if (btn.id === 'playerX')
      btn1 = btn;
    else if (btn.id === 'playerO')
      btn2 = btn;
  }

  if(btn1.classList.contains('selected')){
    btn1.classList.remove('selected');
    btn2.classList.add('selected');
  }else{
    btn2.classList.remove('selected');
    btn1.classList.add('selected');
  }
}

async function changePlayerSide(button) {
  if (!isPlayerSelected && button.dataset.player != game.getPlayerSide()) {
    playerButtons.forEach(btn => btn.classList.remove('selected'));
    button.classList.add('selected');

    game.setPlayerSide(button.id == 'playerX' ? CellType.X : CellType.O);

    statusBar.textContent = `Turn ${currentTurn.toUpperCase()}`;
    switchBacklight();

    isPlayerSelected = true;

    const field = await sendRequest('api/v1/game', 'Post', toCreateGameDTO(game), requestUUIDFieldExtractor);

    console.log(field);

    await sleep(300);

    gameUpdate(field.gameField);

    drawBoard(field.gameField);

    await sleep(200);
    changeStatus();
  }
}

import { drawBoard } from './board.js';

restartButton.addEventListener('click', () => restartGame());

function restartGame() {
  drawBoard();
  isPlayerSelected = false;
  game = new Game(null, CellType.X);
  winState = false;
  statusBar.textContent = 'Start game or select player';
  playerButtons.forEach(btn => btn.id == 'playerX' ? btn.classList.add('selected') 
                                                   : btn.classList.remove('selected'));
}

import { eventManager } from './objects/eventManager.js';

eventManager.subscribe('newCellClicked', event => boardCellClicked(event));

async function boardCellClicked(event) {
  if(winState){
    restartGame();
    return;
  }   

  const { row, col } = event;

  if(!game.updateCell(row, col, game.getPlayerSide()))
    return;

  drawBoard(game.getGameField());

  await sleep(100);
  changeStatus();

  await sleep(300);

  let move = null;

  if(!isPlayerSelected){
    move = await sendRequest('api/v1/game', 'Post', toCreateGameDTO(game), requestUUIDFieldExtractor);
    isPlayerSelected = true;
  }else{
    move = await sendRequest(`api/v1/game/${game.getUUID()}`, 'Put', toFieldDTO(game), async (response) => await response.json());
  }

  gameUpdate(move.gameField);

  await drawBoard(move.gameField);

  await sleep(200);
  changeStatus();

  if(move.status == "X_WON"){
    statusBar.textContent = `Player X won`;
    winState = true;
  }else if(move.status == "O_WON"){
    statusBar.textContent = `Player O won`;
    winState = true;
  }else if(move.status == "DRAW"){
    statusBar.textContent = `Draw`;
    winState = true;
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function requestUUIDFieldExtractor(response) {
  const header = response.headers.get("Location");

  const parts = header.split('/');
  game.setUUID(parts[parts.length - 1]);
  
  const data = await response.json();

  return data; 
}

async function sendRequest(api, requestMethod, body, callback) {
  try {
    const response = await fetch(`http://localhost:8080/${api}`, { 
      method: requestMethod,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body) 
    });

    if (!response.ok){
      console.log(response);
      return Promise.reject(new Error('Network response was not ok'));
    }

    return callback(response);

  } catch (error) {
    throw error;
  }
}

drawBoard();
