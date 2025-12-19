FROM maven:3.8.3-openjdk-17

ARG UID=1000
ARG GID=1000

RUN groupadd -g $GID appuser \
 && useradd -m -u $UID -g $GID -s /bin/bash appuser

# Proper home directory
ENV HOME=/home/appuser

USER appuser
WORKDIR /workspace