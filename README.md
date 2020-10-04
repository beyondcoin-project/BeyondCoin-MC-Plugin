# BeyondCoin-MC-Plugin
A simple Minecraft plugin written for Beyondcoin tipping

### Commands:
1. /tiphelp: Send's a help message to the user.
2. /balance: Send's the user's balance.
3. /deposit: Returns a BYND address to deposit BYND to.
4. /withdraw <amount> <BYND address>: Withdraw's a certain amount from the user's account to the specified address.
5. /info: Returns general information about the BeyondCoin network.
6. /tip <username> <amount>: Tip's a certain amount of BYND to an online user in the server

### How to build?
For development, I have used JetBrain's IntelliJ and have used the plugin called Minecraft Development to start a premade spigot plugin.
To build a .jar file to add to your server:
1. Clone this repository and open with IntelliJ. Allow Maven to sync and download dependencies.
2. Click File >> Project Structure >> Artifacts and click the plus sign.
3. Select JAR >> From modules with dependencies.
4. Leave Main class blank and choose your output directory (Preferably your Spigot Server's plugin folder). Ensure "extract to target JAR" is checked under JAR files from libraries.
5. Click OK. Then Build >> Build Artifacts >> Build
6. If you have your Spigot Server already running, in the console type "reload". The plugin should now be enabled :)

### Dependencies used:
* Spigot-API v1.16.3-R0.1-SNAPSHOT
* [bitcoin-rpc-client](https://github.com/Polve/bitcoin-rpc-client) v1.1.0
* Java-json

### TODO:
- [ ] Test tip functionality
- [x] Test withdraw functionality

### Screenshots:
Info:

![](https://i.imgur.com/dz8noMx.png)

Deposit:

![](https://i.imgur.com/sOFHBSB.png)

Help:

![](https://i.imgur.com/Nm7oAuT.png)

Balance:

![](https://i.imgur.com/EYSjCoW.png)

Withdraw:

![](https://i.imgur.com/UqqYakX.png)

### License:
[Licensed under the MIT license](https://github.com/Nugetzrul3/BeyondCoin-MC-Plugin/blob/master/LICENSE)

### Donations:
If you like using this plugin, please feel free to donate:

BTC: 15befWhRFQypzTJpcQR5jsg4S2ZXJjD5q5

ETH: 0x33165abBFde38781e05f9234FAc68Eb59fe1c6db

BYND: BbqgF7YHF1EVTaaF3bMpmrGxsiU7VhbHL4

SUGAR: sugar1qtl7u435t4jly2hdaa7hrcv5qkpvwa0spd9zzc7

