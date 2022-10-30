# GameCore
A nice core that manages and run Minecraft minigames in combination with HerobrinePVP-Core. Doubles as a pretty sick Minigame API!

Dependencies:
- HerobrinePVP-CORE
- A supported game plugin (ie. WallsSG, BattleClash, BlockHunt)
- NoteBlockAPI (HerobrinePVP-CORE dependency)

Arena System:
- A totally configurable arena system that will support any of the games from the Games enum.
- All configured arena instances are started when the server is loaded. It will load one instance of the Arena, Countdown, and Game class upon startup.
- The Arena class has a lot of methods used throughout each of the game plugins. With the power to manage teams, stop/start games, manage spectators, manage classes, modifiers, and just do most arena-wide operations you might need, it doubles as a pretty useful Minigame API for games to be built off of.


The arena has different states depending on what is happening in it:
- RECRUITING: The arena is open and is ready to accept players!
- COUNTDOWN:  The arena has enough players to start the game and is counting down, but more can join if it isn't full.
- LIVE: The game is live! New players can't join during this time.
- LIVE_ENDING: The game isn't exactly live, but not over yet, as it is in its ending sequence and will reset in next few seconds.

Basically, it boils down to this basic structure:

Arena Is Recruiting -> Players Join -> Minimum Player Count Reached -> Countdown Starts -> Game Starts & Is Live -> Game Plays Until It Ends Or Is Stopped -> Arena Resets & Players Are Sent To Lobby -> Arena Starts Recruiting Again 


Game Settings:
- GameCore also holds the main settings to each supported gamemode. This is stored in the Games enum.
- The settings will determine whether or not the game is ran as a team game, PVP game, has classes, has crafting, has voting for game types, etc.
- It also contains some base values for playing/winning XP and coins, and some stat keys used by the core to handle in-game stats.

**Why an enum?**
Personal preference, mainly. I'm comfortable with making things user-configurable, but with this being my own thing, it is just what I found most convienent.

Main User Commands:
- /join: Can be used to join any enabled game in it's recruiting phase.
- /shout: Used in team games to chat with everyone else in the game, as opposed to just the team chat.

Main Admin Commands:

- /spectate - Used by staff to spectate someone's game, or the game of a particular instance.
- /arena - You can check on the status of an arena instance, force start it, or force stop it -  all with this command.

![image](https://user-images.githubusercontent.com/74119793/198861292-aba16043-469a-444b-8e78-222dc678d4c3.png)

