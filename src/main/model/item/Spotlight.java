// package model.item;

// import model.Game;
// import model.player.Player;

// // this class is not used for Phase 1
// public class Spotlight implements Item {

//     private final Player player;
//     private boolean isActive;

//     // EFFECTS: Constructs an active spotlight, with a reference to the given
//     // player
//     public Spotlight(Player player) {
//         this.player = player;
//         this.isActive = true;
//     }

//     // REQUIRES: isActive() == true
//     // MODIFIES: game, this
//     // EFFECTS: make everything visible within its range unless blocked by a wall
//     // or exceeds its range
//     @Override
//     public void apply(Game g) {
//         // TODO Auto-generated method stub
//     }

//     @Override
//     public boolean isAutoApply() {
//         return true;
//     }

//     // @Override
//     // public boolean isCoin() {
//     //     return false;
//     // }

//     // @Override
//     // public int coinValue() {
//     //     return -1;
//     // }
//     //

//     @Override
//     public String getName() {
//         return null;
//     }

//     @Override
//     public String report() {
//         // TODO Auto-generated method stub
//         return null;
//     }
// }
