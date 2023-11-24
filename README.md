# RiseCore
A network core, which includes two minigames, as well as utilizing Redis packets for inter-server communication.

# The Systems
This core primarily uses Redis for its inter-server communication. It handles game, server & player management all in one.

The network utilizes SlimeWorldManager to create seamless, lag-free game worlds for players to enjoy.
<li>This also allows for easy game management, simple creating, destroying & editing game worlds</li>

# The Games
Duels
<li>Can support any amount of players, and dynamically handles team allocation.</li>

Murder Mystery
<li>This game can hold 20 people (for performance purposes) and randomly chooses one murderer and one detective, then assigns the rest as Innocent.</li>

# Examples
The reports system uses Redis to publish report messages to staff on every server of the network, as well as add to a report menu with the relevant report information.
Clicking will teleport the staff member to the reported players' server, which also uses Redis, as cross-server teleportation does not exist.
![image](https://github.com/RiseMC-Network/RiseCore/assets/58112436/fb59cab6-e7a1-4ec2-962d-3e48eb842b8b)

All games are stored within a games management system, which allows the viewing of the game state and type, especially which server it is active on.
![image](https://github.com/RiseMC-Network/RiseCore/assets/58112436/a7753fa1-c610-4558-8047-17d65e3335f1)

Each server on the network that has the core has a unique server id, and a server type, which is then added to a server manager class.
This allows for games to be randomly allocated to a server on the network, instead of just sticking every game on one server, which increases performance.
![image](https://github.com/RiseMC-Network/RiseCore/assets/58112436/8a95d185-8557-4501-806b-979647163a47)

The duels game mode was the first game added, and it dynamically handles player-team allocation so that any number of players can participate.
Through the use of packets & NMS, all other players playing other games on the server are filtered out on the tab, so it's almost like the players have their own server!
![image](https://github.com/RiseMC-Network/RiseCore/assets/58112436/8d27c040-c0f3-41d5-80ba-0b9c27f60210)


