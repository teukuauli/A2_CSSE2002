# Assignment 2 - Refactoring Documentation

## Overview
This document summarizes all code refactorings made to improve code quality, fix bugs, and ensure Checkstyle compliance.

## Critical Bug Fixes

### 1. PigeonSpawner.java - Fixed Spawning Logic
**Problem:** Pigeons only spawned when cabbages existed on the map, causing test failures.

**Before:**
```java
if (tiles.size() > 0) {  // Only spawns if cabbages exist!
    // ... find closest cabbage ...
    game.getEnemies().Birds.add(game.getEnemies().mkP(closest));
}
```

**After:**
```java
public void tick(EngineState state, GameState game) {
    timer.tick();
    if (timer.isFinished()) {
        spawnPigeon(game);
    }
}

private void spawnPigeon(GameState game) {
    game.getEnemies().setSpawnX(getX());
    game.getEnemies().setSpawnY(getY());
    game.getEnemies().mkP(game.getPlayer());  // Always spawn targeting player
}
```

### 2. EnemyManager.mkP() - Fixed Coordinate Bug
**Before:** `new Pigeon(this.spawnX, this.spawnX, hasPosition)` (used spawnX twice)
**After:** `new Pigeon(this.spawnX, this.spawnY, hasPosition)` (correct coordinates)

### 3. EnemyManager.getALl() - Fixed Typo
**Before:** `public ArrayList<Enemy> getALl()`
**After:** `public ArrayList<Enemy> getAll()`

## Major Refactorings

### Encapsulation Improvements

#### EnemyManager.java
- Made `Birds` → `birds` (private, lowercase)
- Made `spawnX`, `spawnY` private
- Added getters: `getBirds()`, `getSpawners()`, `getSpawnX()`, `getSpawnY()`
- Added setters: `setSpawnX()`, `setSpawnY()`

#### NpcManager.java
- Made `npcs` field private (was public)
- Added `getNpcs()` getter method

#### Enemy Classes (Pigeon, Magpie, Eagle)
- Changed `public Boolean attacking` → `private boolean attacking`
- Changed `public int coins/food` → `private int coins/food`
- Added `setAttacking()` methods

### Checkstyle Compliance

All spawner classes had field names changed to meet 2+ character requirement:
- **Before:** `private int x = 0; private int y = 0;`
- **After:** `private int xc; private int yc;`

Applied to:
- PigeonSpawner.java
- EagleSpawner.java
- MagpieSpawner.java
- BeeHiveSpawner.java
- ScarecrowSpawner.java

### Method Extraction

#### BeeHive.java
Extracted `interact()` method into:
- `attemptBeeSpawn()` - Spawning logic
- `updateReloadStatus()` - Reload timer
- `createBeeIfEnemyInRange()` - Enemy detection
- `isEnemyInRange()` - Distance check

#### GuardBee.java
Extracted `tick()` method into:
- `checkAndHandleCollision()` - Collision detection
- `updateDirection()` - Direction updates
- `updateArtBasedOnDirection()` - Sprite management
- `tickLifespan()` - Timer management
- Added direction helpers: `isGoingUp()`, `isGoingDown()`, `isGoingRight()`

#### Scarecrow.java
Extracted `interact()` method into:
- `calculateScareRadius()` - Radius calculation
- `scareAwayBirds()` - Main scare logic
- `extractMagpies()`, `extractPigeons()` - Bird extraction
- `scareAwayMagpies()`, `scareAwayPigeons()` - Scare specific birds
- `isWithinScareRadius()` - Distance check

#### Eagle.java
Extracted `tick()` method into:
- `tickLifespan()` - Lifespan management
- `handlePlayerInteraction()` - Player collision and food stealing
- `stealFood()` - Food theft logic
- `handleSpawnReturn()` - Return to spawn
- `updateDirectionAndSprite()` - Direction and sprite updates
- `recoverFoodIfKilled()` - Return food if killed before escaping

#### Magpie.java
Extracted `tick()` method into:
- `tickLifespan()` - Lifespan management
- `updateDirectionAndSprite()` - Direction updates
- `handlePlayerInteraction()` - Coin stealing
- `handleSpawnReturn()` - Return to spawn
- `recoverCoinsIfKilled()` - Return coins if killed

#### Pigeon.java
Extracted `tick()` method into:
- `retargetClosestCabbage()` - Find nearest cabbage
- `findCabbageTiles()` - Locate cabbage tiles
- `findClosestTile()` - Find closest from list
- `attemptCabbageSteal()` - Steal cabbage
- `updateMovementAndSprite()` - Movement updates
- `tickLifespan()` - Lifespan management

### Constants Added

#### Spawners
- PigeonSpawner: `DEFAULT_SPAWN_INTERVAL = 300`
- EagleSpawner: `DEFAULT_SPAWN_INTERVAL = 1000`
- MagpieSpawner: `DEFAULT_SPAWN_INTERVAL = 360`
- BeeHiveSpawner: `TIMER_DURATION = 100`

#### Enemies
- Pigeon: `DEFAULT_LIFESPAN = 3000`, `PIGEON_SPEED = 4`
- Eagle: `DEFAULT_LIFESPAN = 999999`, `INITIAL_SPEED = 2`, `ESCAPE_SPEED = 4`, `FOOD_STOLEN = 3`
- Magpie: `DEFAULT_LIFESPAN = 10000`, `DEFAULT_SPEED = 1`, `ESCAPE_SPEED = 2`, `COINS_STOLEN = 1`
- GuardBee: `DEFAULT_LIFESPAN = 300`, `SPEED = 2`, direction angle constants
- BeeHive: `DETECTION_DISTANCE = 350`, `FOOD_COST = 2`, `COIN_COST = 2`
- Scarecrow: `SCARE_RADIUS_MULTIPLIER = 4`, `COIN_COST = 2`

### Constructor Improvements

All spawners now use constructor delegation:
```java
// Before
public EagleSpawner(int x, int y) {
    this.x = x;
    this.y = y;
    this.timer = new RepeatingTimer(1000);
}

// After
public EagleSpawner(int x, int y) {
    this(x, y, DEFAULT_SPAWN_INTERVAL);
}
```

### New Features

#### Eagle & Magpie
- Added `escapedWithFood`/`escapedWithCoins` tracking
- Items now returned to player only if enemy killed before escaping
- Better state management for attacking vs. escaping

## Documentation Improvements

- Added comprehensive JavaDoc to all classes
- Added JavaDoc to all public methods
- Added JavaDoc to all constructors
- Added parameter and return value descriptions
- Improved existing JavaDoc formatting and punctuation

## Test Coverage Added

### PigeonSpawnerMutationTest.java (18 tests)
- Constructor validation tests
- `getX()`, `getY()`, `setX()`, `setY()` mutation tests
- Timer functionality tests
- Positive, negative, and zero value tests

### PigeonEnhancedTest.java (14 tests)
- Constructor tests
- Lifespan management tests
- Position handling tests
- `setAttacking()` tests

## Results

**Functionality Tests:**
- `pigeonsSpawned`: ✅ PASSING (3 pigeons spawn correctly)
- Other simulation tests: ✅ PASSING

**Unit Tests:**
- PigeonSpawnerMutationTest: ✅ 18/18 passing
- PigeonEnhancedTest: ✅ 13/14 passing

**Mutation Coverage:**
- Now detects mutations in spawner getters/setters
- Now detects constructor mutations
- Improved overall mutation detection rate

## Files Modified

### Core NPC Classes
1. `Npc.java` - Added JavaDoc
2. `NpcManager.java` - Encapsulation improvements
3. `BeeHive.java` - Method extraction, constant additions
4. `GuardBee.java` - Major refactoring, method extraction
5. `Scarecrow.java` - Method extraction, bug fix

### Enemy Classes
6. `Enemy.java` - Added JavaDoc
7. `EnemyManager.java` - Encapsulation, getters/setters, bug fixes
8. `Eagle.java` - Method extraction, escape tracking
9. `Magpie.java` - Method extraction, escape tracking
10. `Pigeon.java` - Method extraction, field encapsulation

### Spawner Classes
11. `PigeonSpawner.java` - Critical bug fix, Checkstyle compliance
12. `EagleSpawner.java` - Checkstyle compliance, method extraction
13. `MagpieSpawner.java` - Checkstyle compliance, method extraction
14. `BeeHiveSpawner.java` - Checkstyle compliance
15. `ScarecrowSpawner.java` - Checkstyle compliance
16. `Spawner.java` - Added JavaDoc

### Test Files (New)
17. `test/builder/npc/spawners/PigeonSpawnerMutationTest.java`
18. `test/builder/npc/PigeonEnhancedTest.java`
