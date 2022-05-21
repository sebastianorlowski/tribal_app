Load data by URL API and send it to database.

Tables:
- players
  * ID (identifier)
  * userId (identifier from url)
  * playerName (player name)
  * coords ((one to many)list of coords which user contains)
- coords
    * ID (identifier)
    * x (position X)
    * y (position Y)
    * player (related to player)
    * unitInfo ((one to one)data from js script used on view members)
- unitInfo
    * ID (identifier)
    * couple of units
    * coords (one to one)
  
-----------------------
Get data by running application.
Some data need to be add manually:
1. Create controller to paste data by 

    