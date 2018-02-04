# EnderdragonRespawner

[![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/FuzzyStatic)

A Minecraft plugin that will respawn the Enderdragon (or Enderdragons) on a time cycle.

```yaml
active: true
spawnLocation:
  x: 0.0
  y: 20.0
  z: 0.0
numberOfDragons: 1
respawnTime: 60
spawnAlert: The beast awakens from his slumber...
respawn:
  enderCrystals: true
  obsidian: false
createPortal: false
createEgg: true
nextEventStartTime: 0 // Used to maintain the event timer if/when a server shutdowns
```

Commands/Permissions

```yaml
commands:
    er:
        description: Manage Enderdragon Respawner Events
        permission: enderdragonrespawner
        usage: |
            er [restart|stop] <world>
permissions:
    enderdragonrespawner:
        description: Let's a player manage any Enderdragon Respawner events
        default: op
```

## TODO
* Alter Enderdragon Flight Zone