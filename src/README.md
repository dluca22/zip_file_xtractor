## Using docker container development:

### Dockerfile
  ``
  FROM maven:3.8.3-openjdk-17
  WORKDIR ./
  ``

Building the image:
  `` docker build -t dluca22/java-dev:v1 -t dluca22/java-dev:latest .  ``

Builds the image of current Dockerfile, and tags it with both **V1** and **latest**


### docker-compose.yml

  ``services:
    java-dev:
      image: dluca22/java-dev:v1
      container_name: java-dev
      command: tail -f /dev/null # keep alive infinite loop
      volumes:
        - ./:/workspace # map current dir to workspace directory
      working_dir: /workspace # places starting dir into /workspace
  ``

### devcontainer.json
Gives Vscode context to attach and find Java SDK within the container instead of the host


  ``{
      "name": "Java Dev Container",
      "image": "dluca22/java-dev:v1",
      "workspaceFolder": "/workspace",
      "mounts": [
        "source=${localWorkspaceFolder},target=/workspace,type=bind"
      ],
      "customizations": {
        "vscode": {
          "extensions": [
            "vscjava.vscode-java-pack",
            "vscjava.vscode-maven"
          ]
        }
      }
    }
``

## Running default Java app from within the contaner

**Compile the file**:
``
    javac src/Main.java
``

This creates:

``    src/Main.class
``
**Run the class:**

from within /workdir (not directly where the .class file is generated)

  ``java -cp src Main``


**OR IF DEPLOYIN INTO bin/**

    ``javac -d bin src/Main.java
    java -cp bin Main``


    
# Create Maven app

in /workspace
``
  mvn archetype:generate -DgroupId=com.dluca22.Main \
                        -DartifactId=zip_stl_xtractor \
                        -DarchetypeArtifactId=maven-archetype-quickstart \
                        -DinteractiveMode=false
``