# EnderdragonRespawner

A minecraft plugin API (sans commands/configurations) that will respawn the Enderdragon in **world_the_end** every **6 hours** or upon server load if no Enderdragon exists in world_the_end. Also, prevents spawning of Enderdragon portal.

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
```