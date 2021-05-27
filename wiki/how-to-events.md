# Conditions
## Trigger Conditions
###### These conditions are what actually trigger an event to be run.

`JOIN` : Triggers on joining a defined server or the network if "network".

`LEAVE` : Triggers on leaving a defined server of the network if "network".

`MESSAGE_EXACT` : Triggers on player saying a message in chat that is exactly the same as the defined string.

`MESSAGE_CONTAINS` : Triggers on player saying a message in chat that contains the defined string.

`MESSAGE_STARTS_WITH` : Triggers on player saying a message in chat that starts with the defined string.

`MESSAGE_ENDS_WITH` : Triggers on player saying a message in chat that ends with the defined string.

`COMMAND` : Triggers on a player running a command. NOT WORKING RIGHT NOW.

Notes:
* All `MESSAGE` conditions also work with commands. (When there is a `/` in front of them)

## Soft Conditions
###### These conditions are only passed when an event is triggered.
`IN_SERVER` : Allows an event to be run when the said player is in the defined server.

`NAME_EXACT` : Allows an event to be run when the said player's name is exactly as defined.

`NAME_CONTAINS` : Allows an event to be run when the said player's name contains what is defined.

`NAME_STARTS_WITH` : Allows an event to be run when the said player's name starts with what is defined.

`NAME_ENDS_WITH` : Allows an event to be run when the said player's name ends with what is defined.

`HAS_POINTS_EXACT` : Allows an event to be run when the said player's network points are exactly as defined.

`HAS_POINTS_LESS_THAN` : Allows an event to be run when the said player's network points are less than defined.

`HAS_POINTS_LESS_THAN_EQUAL` : Allows an event to be run when the said player's network points are less than or equal to what is defined.

`HAS_POINTS_GREATER_THAN` : Allows an event to be run when the said player's network points are greater than defined.

`HAS_POINTS_GREATER_THAN_EQUAL` : Allows an event to be run when the said player's network points are greater than or equal to what is defined.

# Actions
`SEND_MESSAGE_TO` : Sends a message to the player who triggered the event.

`SEND_MESSAGE_AS` : Sends a message as the player who triggered the event. NOT WORKING RIGHT NOW.

`SEND_SERVER` : Sends the player who triggered the event to the specified server.

`KICK` : Kicks the player who triggered the event from the network.

`RUN_COMMAND_AS_OP` : Runs a specified command as the console when the event is triggered.

`RUN_COMMAND_AS_SELF` : Runs a specified command as the player who triggered the event.

`GIVE_POINTS` : Gives the player a specified amount of points.

`TAKE_POINTS` : Takes a specified amount of points from the player.

`SET_POINTS` : Sets the player's points to a specified amount.

*Notes:*
* `SEND_MESSAGE_AS` sends a color-coded message to the server the player who triggered the event is on right now. This will become a feature, but in a different action later on. What this specific action is supposed to do is send a message to the server the player is on as if the player had said it.



# Basics
## `default.yml` file.
###### This is the `default.yml` file you can toggle to be put into your `StreamLine/events/` folder.
```yml
tags:
  - "tag"
  - "list"
  - "here"
conditions:
  1:
    type: "MESSAGE_CONTAINS"
    value: "i like streamline"
  2:
    type: "NAME_EXACT"
    value: "StreamLineLover"
actions:
  1:
    type: "SEND_MESSAGE_TO"
    value: "&eI agree!"
  2:
    type: "GIVE_POINTS"
    value: "1000"
```
## Understanding the `default.yml` file.
###### Tags.
```yml
tags:
  - "tag"
  - "list"
  - "here"
```
This is a list of tags that the player must have to trigger the event.
Must be in yml format.
###### Conditions.
```yml
conditions:
  1:
    type: "MESSAGE_CONTAINS"
    value: "i like streamline"
  2:
    type: "NAME_EXACT"
    value: "StreamLineLover"
```
This is where you can put as many conditions and their values as you want.
You CAN have multiple of the same type.
Must be in yml format.
###### Actions.
```yml
actions:
  1:
    type: "SEND_MESSAGE_TO"
    value: "&eI agree!"
  2:
    type: "GIVE_POINTS"
    value: "1000"
```
This is where you can put as many actions and their values as you want.
You CAN have multiple of the same type.
Must be in yml format.

# WildCards
###### These are a way to substitute words for a sort of "wildcard" where anything can go there by using `*`.
## Examples:
```yml
tags:
  - "taco"
conditions:
  1:
    type: "MESSAGE_EXACT"
    value: "i love *"
actions:
  1:
    type: "SEND_MESSAGE_TO"
    value: "&eME TOO!"
```
This will send `&ME TOO!` (formatted) to any player saying `i love <input ANY one word here>` who has the `taco` bungee tag.
```yml
tags:
  - "joining"
conditions:
  1:
    type: "JOIN"
    value: "*"
actions:
  1:
    type: "SEND_MESSAGE_TO"
    value: "&2HELLO!"
```
This will send `&2HELLO!` (formatted) to any player joining ANY server who has the `joining` tag.

# Argument Extraction
###### This is a way to get any argument from a string of words.
"Arguments" meaning the word at a specific spot in the string of words.
(i.e. If you have "this is a string" as your string, the word "string" is the 4th argument in that string.)
## Examples:
```yml
tags:
  - "staff"
conditions:
  1:
    type: "MESSAGE_STARTS_WITH"
    value: "kick"
actions:
  1:
    type: "RUN_COMMAND_AS_OP"
    value: "kick %arg:1%"
```
This will kick whoever's name is after `kick` if a player says `kick <input ANY one word here>` who has the the `staff` bungee tag.
```yml
tags:
  - "repeat"
conditions:
  1:
    type: "MESSAGE_EXACT"
    value: "i like to repeat my messages..."
actions:
  1:
    type: "SEND_MESSAGE_TO"
    value: "%arg:0% %arg:1% %arg:2% %arg:3% %arg:4% %arg:5%"
```
This will send the exact same message you sent back to you if you have the `repeat` bungee tag.

*NOTE: %arg:0% means the first argument and %arg:5% means the sixth argument.*