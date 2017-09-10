#!/bin/bash
docker run -it -v $(pwd):/documents/ asciidoctor/docker-asciidoctor "./build.sh" "html"
