# Maze Game

by William Chen

## What Will the Application Do?

It is a maze solving game. The goal for the player is to find the exit. At the start of the game, the entire maze will be visible for short period of time, then the screen goes black and the maze becomes invisible. The character in the game will have a regional light source that illuminates a certain area within its current position. Player then will have control the character to explore the maze and find the way out. There might be random **traps**, and **special items** that help the player.

## Who Will Use It?

Everyone!

## Why is This Project of Interest to Me?

- I have just learned about maze solving algorithm, and I want to implement it into something more interesting
- I want to also try to implement an algorithm that generates a maze

---

## User Story

### Phase 1

- As a user, I want to be able to control the character to move and turn
- As a user, I don't want to the character to be able to walk into the maze walls
- As a user, I want to be able to add in-game coin and items into the player inventory bag
- As a user, I want to be able to use the items that I have
- As a user, I want to be able to see the entire maze
- As a user, I want to be able to go the next maze if I solved the current one

### Phase 2

- As a user, I want to be able to save the game
- As a user, I want to be able to load the game that I choose
- As a user, I want to be able to be noticed when a file is corrupted
- As a user, I want to be able to be noticed when saving is unsuccessful

### Phase 3

### Phase 4

#### Task 2

```log
Thu Mar 31 12:13:28 PDT 2022
Player picked up HINT
Thu Mar 31 12:13:30 PDT 2022
Player picked up SKIP
Thu Mar 31 12:13:36 PDT 2022
Player picked up and auto applied COIN value of 12
Thu Mar 31 12:13:43 PDT 2022
Player picked up SKIP
Thu Mar 31 12:13:43 PDT 2022
Player picked up SKIP
Thu Mar 31 12:14:11 PDT 2022
Player picked up SKIP
Thu Mar 31 12:14:14 PDT 2022
Player used SKIP
Thu Mar 31 12:14:14 PDT 2022
Player used SKIP
Thu Mar 31 12:14:16 PDT 2022
Player used SKIP
Thu Mar 31 12:14:20 PDT 2022
Player picked up SKIP
Thu Mar 31 12:14:20 PDT 2022
Player picked up SKIP
Thu Mar 31 12:14:21 PDT 2022
Player picked up BREAKER range of 7
Thu Mar 31 12:14:24 PDT 2022
Player picked up HINT
Thu Mar 31 12:14:27 PDT 2022
Player picked up BREAKER range of 9
Thu Mar 31 12:14:31 PDT 2022
Player used SKIP
Thu Mar 31 12:14:33 PDT 2022
Player used SKIP
Thu Mar 31 12:14:34 PDT 2022
Player used SKIP
Thu Mar 31 12:14:37 PDT 2022
Player picked up BREAKER range of 5
Thu Mar 31 12:14:40 PDT 2022
Player picked up and auto applied COIN value of 12
Thu Mar 31 12:14:43 PDT 2022
Player picked up HINT
Thu Mar 31 12:14:46 PDT 2022
Player used HINT
```

#### Task 3

- The problem: there are too many association to the Game class
- Where is it: MazePanel, InfoPanel, KeyHandler
- How to fix it: Instead of letting those all the 3 panels to have association to the Game directly, let GamePanel, which holds all those 3 panels to have one single association to the Game. GamePanel then provide getter to let MazePanel, InfoPanel, and KeyHandler to use it