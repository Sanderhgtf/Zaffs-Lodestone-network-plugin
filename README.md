# Zaffs-lodestone-network - Minecraft Spigot Plugin

Zaffs-lodestone-network is a feature-rich Minecraft Spigot plugin introducing a teleportation system using Nether Stars and Lodestones. Enhance your in-game experience by creating, managing, and teleporting to personalized locations, providing both server administrators and players with a seamless and user-friendly interface.

## Features

### BeaconClickListener

**Description:**
Handles player interactions with Nether Stars and Lodestones, allowing players to create teleportation points and managing the storage and retrieval of teleportation point data in JSON format.

**Key Functionality:**
- Teleportation Point Creation: Right-click on Lodestones with Nether Stars to create teleportation points.
- Data Persistence: Coordinates and properties of teleportation points are stored in a JSON file.
- Async Operations: Utilizes asynchronous programming for smooth gameplay.

### CustomInventoryGui

**Description:**
Provides an interactive GUI for managing teleportation points, displaying available points, and allowing players to click and teleport to their desired locations.

**Key Functionality:**
- GUI Interaction: Intuitive GUI for accessing and interacting with teleportation points.
- Teleportation: Click on a point in the GUI to teleport to the corresponding location.
- Distance Information: Displays the distance between the player's location and each teleportation point.

### GhostLodestoneCleaner

**Description:**
Automatically cleans up ghost Lodestones that might appear due to unforeseen circumstances, removing invalid or non-existent Lodestones from the stored coordinates.

**Key Functionality:**
- Ghost Lodestone Detection: Identifies Lodestones that no longer exist in the game world.
- Automatic Cleanup: Removes ghost Lodestones from the stored coordinates.
- Data Persistence: Updates the JSON file with cleaned coordinates for future use.

### LodestoneCoordinate

**Description:**
Represents the data structure for storing teleportation point information, encapsulating location, world name, Nether Star name, tier, experience requirements, and experience progression.

**Key Functionality:**
- Data Storage: Stores teleportation point information in a structured manner.
- Getter and Setter Methods: Provides methods to access and modify teleportation point properties.

## Installation

1. Place the `Test123.jar` file in the `plugins` directory of your Spigot server.
2. Start or restart your Spigot server.

## Configuration

No specific configuration is required for initial use. The plugin automatically creates a `lodestone_data.json` file in the `plugins/Test123/` directory to store teleportation point data.

## Usage

### Creating Teleportation Points:

1. Right-click on a Lodestone with a Nether Star to create a teleportation point.
2. Rename the Nether Star to set a custom name for the teleportation point.

### Accessing Teleportation Points:

1. Open the "LODESTONE NETWORK" GUI by right-clicking on a Lodestone.
2. Click on a teleportation point in the GUI to teleport to that location.

### Automatic Cleanup:

Ghost Lodestones, if any, are automatically removed during server runtime.

## Developers

**Sander (Author):**
- [GitHub](https://github.com/your-github-username)
- [Spigot Profile](https://www.spigotmc.org/members/your-spigot-profile)
