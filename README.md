# STL Zip file Extractor (early alpha)

This is a Java application that runs inside a Docker container and watches a single directory for file changes.

When new files are created or modified, the application reacts to those events, applies a debounce to avoid duplicate triggers, and processes ZIP files found in the directory.

## What it does

At a high level, the application:

* Watches one directory (non-recursive)

* Reacts to file creation and file modification events

* Applies a debounce to avoid double triggers from the filesystem

* Dispatches the event to a registered listener

* The app logic listener then:

* Scans the directory for .zip files


For each ZIP:

* inspects its contents recursively

* checks if it contains at least one .stl file

* If a .stl file is found:

* the ZIP is decompressed

* a directory with the same name as the ZIP is created

* the ZIP file is moved into that directory


## Architecture overview

The core components are:

### DirectoryWatcher

Responsible for filesystem watching

Applies debounce logic

Emits events

### DirectoryEventListener

Interface for reacting to directory events

### FileController

Implements DirectoryEventListener

Contains all ZIP / STL processing logic

The watcher and the processing logic are decoupled using a listener interface.


## Running the application

The application is designed to run inside Docker and developed in a docker development container using *maven:3.8.3-openjdk-17*


## Development workflow (VS Code)

For development and debugging:

Build and run the container using Docker Compose

In VS Code, use “Attach to Running Container”

Develop and debug directly inside the container

This keeps the development environment close to production.

## Configuration (WIP)

Configuration will be provided via environment variables when running the container.

Planned configuration options:

Variable	Description	Default
WATCH_DIR	Directory to watch	TBD
TARGET_DIR	Directory where processed files are placed	Defaults to WATCH_DIR
DEBOUNCE_INTERVAL_SEC	Debounce interval in milliseconds	3000